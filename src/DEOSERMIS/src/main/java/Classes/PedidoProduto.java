package Classes;

import java.sql.SQLException;
import java.sql.Statement;
import java.math.BigInteger;
import java.math.RoundingMode;
import Main.ConexaoBanco;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PedidoProduto {
    private final Funcoes funcao = new Funcoes();
    private Date data;
    private BigDecimal desconto;
    private BigDecimal valorSubTotal;
    private BigDecimal valorTotal;
    private String produtos;
    private String pagamentos;
    private BigInteger codigo;

    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger Codigo) {
        this.codigo = Codigo;
    }
    
    // Classe interna para representar um resultado da pesquisa
    public static class ResultadoPesquisa {
        public String nomeProduto;
        public String nomeFornecedor;
        public int quantidadeTotal;
        public BigDecimal valorTotal;
        public Date data;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getValorSubTotal() {
        return valorSubTotal;
    }

    public void setValorSubTotal(BigDecimal valorSubTotal) {
        this.valorSubTotal = valorSubTotal;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getProdutos() {
        return produtos;
    }

    public void setProdutos(String produtos) {
        this.produtos = produtos;
    }

    public String getPagamentos() {
        return pagamentos;
    }

    public void setPagamentos(String pagamentos) {
        this.pagamentos = pagamentos;
    }

    //INSERIR
    public boolean salvarPedido(List<Pagamento> pagamentos, java.util.Date dataPedido,Component parent) {
        Connection conn = null;
        PreparedStatement psPedido = null;
        PreparedStatement psSaida = null;
        PreparedStatement psPedidoSaida = null;
        PreparedStatement psPagamento = null;
        PreparedStatement psAtualizarProduto = null;

        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false); // Inicia a transação

            // Pega o valor do desconto em porcentagem do carrinho
            BigDecimal descontoPorcentagem = CarrinhoPedido.getDesconto();

            // Converte o percentual para um fator de multiplicação (ex: 10% -> 0.10)
            BigDecimal fatorDesconto = descontoPorcentagem.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            BigDecimal fatorMultiplicacao = BigDecimal.ONE.subtract(fatorDesconto);

            // 1. Salva o pedido
            String sqlPedido = "INSERT INTO pedidoproduto (Data, Desconto, ValorSubTotal, ValorTotal) VALUES (?, ?, ?, ?)";
            psPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS);
            psPedido.setDate(1, new java.sql.Date(dataPedido.getTime()));
            psPedido.setBigDecimal(2, descontoPorcentagem); // Salva o valor da porcentagem
            psPedido.setBigDecimal(3, CarrinhoPedido.getSubtotal());
            psPedido.setBigDecimal(4, CarrinhoPedido.getValorTotal());

            psPedido.executeUpdate();

            ResultSet rsPedido = psPedido.getGeneratedKeys();
            if (!rsPedido.next()) {
                throw new SQLException("Falha ao obter o ID do pedido.");
            }
            long pedidoId = rsPedido.getLong(1);
            rsPedido.close();

            // 2. Salva as saídas de produtos e atualiza o estoque
            String sqlSaida = "INSERT INTO saidadeproduto (ProdutoID, Quantidade, ValorDaVenda, FornecedorID) VALUES (?, ?, ?, ?)";
            String sqlPedidoSaida = "INSERT INTO pedido_saida (PedidoID, SaidaID) VALUES (?, ?)";
            String sqlAtualizarProduto = "UPDATE produto SET Quantidade = Quantidade - ? WHERE Codigo = ?";

            psSaida = conn.prepareStatement(sqlSaida, Statement.RETURN_GENERATED_KEYS);
            psPedidoSaida = conn.prepareStatement(sqlPedidoSaida);
            psAtualizarProduto = conn.prepareStatement(sqlAtualizarProduto);

            for (ItemPedido item : CarrinhoPedido.getItens()) {
                // Busca o FornecedorID antes de salvar a saída
                BigInteger fornecedorId = Produto.buscarFornecedorIdPorProdutoId(item.getCodigo(), parent);
                
                // Calcula o valor da venda do item com o desconto percentual
                BigDecimal valorVendaComDesconto = item.getTotal().multiply(fatorMultiplicacao).setScale(2, RoundingMode.HALF_UP);

                psSaida.setObject(1, item.getCodigo().longValue());
                psSaida.setLong(2, item.getQuantidade());
                psSaida.setBigDecimal(3, valorVendaComDesconto); // Salva o valor com desconto
                if (fornecedorId != null) {
                    psSaida.setObject(4, fornecedorId.longValue());
                } else {
                    psSaida.setNull(4, java.sql.Types.BIGINT);
                }
                psSaida.executeUpdate();

                ResultSet rsSaida = psSaida.getGeneratedKeys();
                if (!rsSaida.next()) {
                    throw new SQLException("Falha ao obter o ID da saída.");
                }
                // LINHA CORRIGIDA AQUI:
                long saidaId = rsSaida.getLong(1); 
                rsSaida.close();

                psPedidoSaida.setLong(1, pedidoId);
                psPedidoSaida.setLong(2, saidaId);
                psPedidoSaida.executeUpdate();

                psAtualizarProduto.setLong(1, item.getQuantidade());
                psAtualizarProduto.setObject(2, item.getCodigo().longValue());
                psAtualizarProduto.executeUpdate();
            }

            // 3. Salva as formas de pagamento
            String sqlPagamento = "INSERT INTO pedido_formadepagamento (PedidoID, FormaDePagamentoID, ValorPago) VALUES (?, ?, ?)";
            psPagamento = conn.prepareStatement(sqlPagamento);
            for (Pagamento pagamento : pagamentos) {
                int formaPagamentoId = buscarCodigoFormaPagamento(conn, pagamento.getForma());

                psPagamento.setLong(1, pedidoId);
                psPagamento.setLong(2, formaPagamentoId);
                psPagamento.setBigDecimal(3, pagamento.getValor());
                psPagamento.executeUpdate();
            }

            conn.commit(); // Confirma a transação
            return true;

        } catch (SQLException e) {
            funcao.Mensagens(parent, "Erro ao salvar pedido:" , e.getMessage(), "ERRO DE BANCO DE DADOS", "erro");
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    funcao.Mensagens(parent, "Erro ao realizar rollback:", ex.getMessage(), "ERRO CRÍTICO", "erro");
                }
            }
            return false;
        } finally {
            try { if (psPedido != null) psPedido.close(); } catch (SQLException e) { /* Ignorar */ }
            try { if (psSaida != null) psSaida.close(); } catch (SQLException e) { /* Ignorar */ }
            try { if (psPedidoSaida != null) psPedidoSaida.close(); } catch (SQLException e) { /* Ignorar */ }
            try { if (psPagamento != null) psPagamento.close(); } catch (SQLException e) { /* Ignorar */ }
            try { if (psAtualizarProduto != null) psAtualizarProduto.close(); } catch (SQLException e) { /* Ignorar */ }
            try { if (conn != null) conn.close(); } catch (SQLException e) { /* Ignorar */ }
        }
    }
    
    //CONSULTAR
    public static List<PedidoProduto> buscarPedidos(Date dataInicial, Date dataFim, Component parent) {
        List<PedidoProduto> listaPedidos = new ArrayList<>();

        String sql = "SELECT p.Codigo, p.Data, p.Desconto, p.ValorSubTotal, p.ValorTotal, "
                   + "GROUP_CONCAT(DISTINCT CONCAT(pr.Nome, ' (Qtd: ', sp.Quantidade, ' - R$ ', "
                   + "REPLACE(REPLACE(REPLACE(FORMAT(sp.ValorDaVenda / (1 - p.Desconto / 100), 2), '.', 'TEMP_CHAR'), ',', '.'), 'TEMP_CHAR', ','), ')') SEPARATOR '\n') AS Produtos, "
                   + "GROUP_CONCAT(DISTINCT CONCAT(fp.Descricao, ' - R$ ', "
                   + "REPLACE(REPLACE(REPLACE(FORMAT(pf.ValorPago, 2), '.', 'TEMP_CHAR'), ',', '.'), 'TEMP_CHAR', ',')) SEPARATOR '\n') AS Pagamentos "
                   + "FROM pedidoproduto p "
                   + "LEFT JOIN pedido_saida ps ON p.Codigo = ps.PedidoID "
                   + "LEFT JOIN saidadeproduto sp ON ps.SaidaID = sp.Codigo "
                   + "LEFT JOIN produto pr ON sp.ProdutoID = pr.Codigo "
                   + "LEFT JOIN pedido_formadepagamento pf ON p.Codigo = pf.PedidoID "
                   + "LEFT JOIN formadepagamento fp ON pf.FormaDePagamentoID = fp.Codigo "
                   + "WHERE p.Data BETWEEN ? AND ? "
                   + "GROUP BY p.Codigo "
                   + "ORDER BY p.Data DESC";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // conversão java.util.Date -> java.sql.Date
            stmt.setDate(1, new java.sql.Date(dataInicial.getTime()));
            stmt.setDate(2, new java.sql.Date(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PedidoProduto pedido = new PedidoProduto();
                    pedido.setCodigo(BigInteger.valueOf(rs.getLong("Codigo")));
                    pedido.setData(rs.getDate("Data"));
                    pedido.setDesconto(rs.getBigDecimal("Desconto"));
                    pedido.setValorSubTotal(rs.getBigDecimal("ValorSubTotal"));
                    pedido.setValorTotal(rs.getBigDecimal("ValorTotal"));
                    String produtos = rs.getString("Produtos");
                    if (produtos != null) {
                        produtos = "<html>" + produtos.replace("\n", "<br>") + "</html>";
                    }
                    pedido.setProdutos(produtos);

                    String pagamentos = rs.getString("Pagamentos");
                    if (pagamentos != null) {
                        pagamentos = "<html>" + pagamentos.replace("\n", "<br>") + "</html>";
                    }
                    pedido.setPagamentos(pagamentos);

                    listaPedidos.add(pedido);
                }
            }
        } catch (Exception e) {
            new Funcoes().Mensagens( parent,
                "Erro ao buscar pedidos:" , e.getMessage(),
                "ERRO",
                "erro");
        }

        return listaPedidos;
    }

    public List<ResultadoPesquisa> pesquisarPedido(String nomeProduto, Date dataInicio, Date dataFim) {
        String sql = "SELECT pp.Data AS Data, p.Nome AS NomeProduto, " +
                     "sdp.Quantidade AS Quantidade, " + 
                     "sdp.ValorDaVenda AS ValorDaVenda " +
                     "FROM saidadeproduto AS sdp " +
                     "JOIN pedido_saida AS ps ON sdp.Codigo = ps.SaidaID " +
                     "JOIN pedidoproduto AS pp ON ps.PedidoID = pp.Codigo " +
                     "JOIN produto AS p ON sdp.ProdutoID = p.Codigo " +
                     "WHERE 1=1 ";

        List<Object> params = new ArrayList<>();

        if (nomeProduto != null && !nomeProduto.isEmpty()) {
            sql += "AND p.Nome LIKE ? ";
            params.add("%" + nomeProduto + "%");
        }

        if (dataInicio != null && dataFim != null) {
            sql += "AND pp.Data BETWEEN ? AND ? ";
            params.add(new java.sql.Date(dataInicio.getTime()));
            params.add(new java.sql.Date(dataFim.getTime()));
        }

        sql += "ORDER BY pp.Data DESC, p.Nome";

        List<ResultadoPesquisa> resultados = new ArrayList<>();

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                Object param = params.get(i);
                if (param instanceof String) {
                    stmt.setString(i + 1, (String) param);
                } else if (param instanceof java.sql.Date) {
                    stmt.setDate(i + 1, (java.sql.Date) param);
                }
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ResultadoPesquisa resultado = new ResultadoPesquisa();
                    resultado.data = rs.getDate("Data");
                    resultado.nomeProduto = rs.getString("NomeProduto");
                    resultado.quantidadeTotal = rs.getInt("Quantidade");
                    resultado.valorTotal = rs.getBigDecimal("ValorDaVenda");

                    resultados.add(resultado);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }


    
    private int buscarCodigoFormaPagamento(Connection conn, String descricao) throws SQLException {
        String sql = "SELECT Codigo FROM formadepagamento WHERE Descricao = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, descricao);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Codigo");
                } else {
                    throw new SQLException("Forma de pagamento não encontrada: " + descricao);
                }
            }
        }
    }
    
    //EXCLUIR
    public void excluirPedidos(List<BigInteger> codigos,Component parent) {
        String sqlBuscarSaida = "SELECT ProdutoID, Quantidade FROM saidadeproduto WHERE Codigo IN (SELECT SaidaID FROM pedido_saida WHERE PedidoID = ?)";
        String sqlAtualizarEstoque = "UPDATE produto SET Quantidade = Quantidade + ? WHERE Codigo = ?";
        String sqlDeletarPagamento = "DELETE FROM pedido_formadepagamento WHERE PedidoID = ?";
        String sqlDeletarSaida = "DELETE FROM pedido_saida WHERE PedidoID = ?";
        String sqlDeletarSaidadeProduto = "DELETE FROM saidadeproduto WHERE Codigo IN (SELECT SaidaID FROM pedido_saida WHERE PedidoID = ?)";
        String sqlDeletarPedido = "DELETE FROM pedidoproduto WHERE Codigo = ?";

        try (Connection con = ConexaoBanco.getConnection()) {
            con.setAutoCommit(false);

            for (BigInteger pedidoID : codigos) {

                // 1. Reestornar produtos no estoque
                try (PreparedStatement stmtBuscar = con.prepareStatement(sqlBuscarSaida)) {
                    stmtBuscar.setLong(1, pedidoID.longValue());
                    try (ResultSet rs = stmtBuscar.executeQuery()) {
                        while (rs.next()) {
                            long produtoID = rs.getLong("ProdutoID");
                            int quantidade = rs.getInt("Quantidade");

                            try (PreparedStatement stmtAtualizar = con.prepareStatement(sqlAtualizarEstoque)) {
                                stmtAtualizar.setInt(1, quantidade);
                                stmtAtualizar.setLong(2, produtoID);
                                stmtAtualizar.executeUpdate();
                            }
                        }
                    }
                }

                // 2. Deletar dependências
                try (PreparedStatement stmt1 = con.prepareStatement(sqlDeletarPagamento)) {
                    stmt1.setLong(1, pedidoID.longValue());
                    stmt1.executeUpdate();
                }

                try (PreparedStatement stmt2 = con.prepareStatement(sqlDeletarSaida)) {
                    stmt2.setLong(1, pedidoID.longValue());
                    stmt2.executeUpdate();
                }

                try (PreparedStatement stmt3 = con.prepareStatement(sqlDeletarSaidadeProduto)) {
                    stmt3.setLong(1, pedidoID.longValue());
                    stmt3.executeUpdate();
                }

                // 3. Deletar pedido
                try (PreparedStatement stmt4 = con.prepareStatement(sqlDeletarPedido)) {
                    stmt4.setLong(1, pedidoID.longValue());
                    stmt4.executeUpdate();
                }
            }

            con.commit();
            funcao.Mensagens(parent, "Processo de exclusão realizado com sucesso!", "","SUCESSO","informacao");
        } catch (SQLException e) {
            e.printStackTrace();
            funcao.Mensagens(parent, "Erro ao excluir:", e.getMessage(),"ERRO","erro");
        }
    }

}