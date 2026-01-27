package Classes;
import Main.ConexaoBanco;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReposicaoProduto {

    private BigInteger codigo;
    private Date data;
    private BigInteger fornecedorID;
    private BigDecimal valorTotal;
    private String chaveAcessoNFe;
    private String produtos; 
    private String fornecedorNome;
    
    public static class ResultadoPesquisaReposicao {
        public Date data;
        public String nomeProduto;
        public String nomeFornecedor;
        public int quantidadeTotal;
        public BigDecimal valorTotal;
    }

    public static class ItemReposicao {
        public BigInteger produtoID;
        public int quantidade;
        public BigDecimal valorCompra;
    }
    
    // Getters e Setters
    public BigInteger getCodigo() { return codigo; }
    public void setCodigo(BigInteger codigo) { this.codigo = codigo; }
    public void setData(Date data) { this.data = data; }
    public void setFornecedorID(BigInteger fornecedorID) { this.fornecedorID = fornecedorID; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }
    public void setChaveAcessoNFe(String chaveAcessoNFe) { this.chaveAcessoNFe = chaveAcessoNFe; }
    public void setProdutos(String produtos) { this.produtos = produtos; }
    public String getProdutos() { return produtos; }
    public String getFornecedorNome() {return fornecedorNome; }
    public Date getData() {
            return data;
        }
    public BigDecimal getValorTotal() {
            return valorTotal;
        }
    public String getChaveAcessoNFe() {
            return chaveAcessoNFe;
        }
    public void setFornecedorNome(String fornecedorNome) {this.fornecedorNome = fornecedorNome; }
    
    //INSERIR
    public boolean salvarReposicao(List<ItemReposicao> itens) {
        String sqlReposicao = "INSERT INTO reposicaoproduto (Data, FornecedorID, ValorDaCompra, ChaveAcessoNFe) VALUES (?, ?, ?, ?)";
        String sqlEntrada = "INSERT INTO entradadeproduto (ProdutoID, Quantidade, ValorDaCompra) VALUES (?, ?, ?)";
        String sqlVinculo = "INSERT INTO reposicao_entrada (ReposicaoID, EntradaID) VALUES (?, ?)";
        String sqlAtualizarEstoque = "UPDATE produto SET Quantidade = Quantidade + ? WHERE Codigo = ?";

        try (Connection con = ConexaoBanco.getConnection()) {
            con.setAutoCommit(false);

            // 1. Inserir reposição
            try (PreparedStatement stmtRepo = con.prepareStatement(sqlReposicao, Statement.RETURN_GENERATED_KEYS)) {
                stmtRepo.setDate(1, new java.sql.Date(data.getTime()));
                stmtRepo.setLong(2, fornecedorID.longValue());
                stmtRepo.setBigDecimal(3, valorTotal);
                if (chaveAcessoNFe != null && !chaveAcessoNFe.trim().isEmpty()) {
                    stmtRepo.setString(4, chaveAcessoNFe);
                } else {
                    stmtRepo.setNull(4, Types.CHAR);
                }
                stmtRepo.executeUpdate();

                ResultSet rsRepo = stmtRepo.getGeneratedKeys();
                if (rsRepo.next()) {
                    codigo = BigInteger.valueOf(rsRepo.getLong(1));
                } else {
                    con.rollback();
                    return false;
                }
            }

            // 2. Inserir cada entrada de produto e atualizar estoque
            for (ItemReposicao item : itens) {
                BigInteger entradaID = null;

                // 2.1 Inserir entrada
                try (PreparedStatement stmtEntrada = con.prepareStatement(sqlEntrada, Statement.RETURN_GENERATED_KEYS)) {
                    stmtEntrada.setLong(1, item.produtoID.longValue());
                    stmtEntrada.setInt(2, item.quantidade);
                    stmtEntrada.setBigDecimal(3, item.valorCompra);
                    stmtEntrada.executeUpdate();

                    ResultSet rsEnt = stmtEntrada.getGeneratedKeys();
                    if (rsEnt.next()) {
                        entradaID = BigInteger.valueOf(rsEnt.getLong(1));
                    } else {
                        con.rollback();
                        return false;
                    }
                }

                // 2.2 Vincular entrada à reposição
                try (PreparedStatement stmtVinculo = con.prepareStatement(sqlVinculo)) {
                    stmtVinculo.setLong(1, codigo.longValue());
                    stmtVinculo.setLong(2, entradaID.longValue());
                    stmtVinculo.executeUpdate();
                }

                // 2.3 Atualizar estoque do produto
                try (PreparedStatement stmtEstoque = con.prepareStatement(sqlAtualizarEstoque)) {
                    stmtEstoque.setInt(1, item.quantidade);
                    stmtEstoque.setLong(2, item.produtoID.longValue());
                    stmtEstoque.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //CONSULTAR
    public static List<ReposicaoProduto> buscarReposicao(String nomeFornecedor, String chaveAcesso, Date dataInicial, Date dataFinal) {
        List<ReposicaoProduto> listaReposicoes = new ArrayList<>();

        String sql = "SELECT rp.Codigo, rp.Data, rp.ValorDaCompra AS ValorTotal, rp.ChaveAcessoNFe, f.Nome AS FornecedorNome, "
                   + "GROUP_CONCAT(DISTINCT CONCAT(p.Nome, ' (Qtd: ', edp.Quantidade, ' - R$ ', "
                   + "REPLACE(REPLACE(REPLACE(FORMAT(edp.ValorDaCompra, 2), '.', 'TEMP_CHAR'), ',', '.'), 'TEMP_CHAR', ','), ')') SEPARATOR ' | ') AS Produtos "
                   + "FROM reposicaoproduto rp "
                   + "LEFT JOIN fornecedor f ON rp.FornecedorID = f.Codigo " // JOIN com a tabela de fornecedor
                   + "LEFT JOIN reposicao_entrada re ON rp.Codigo = re.ReposicaoID "
                   + "LEFT JOIN entradadeproduto edp ON re.EntradaID = edp.Codigo "
                   + "LEFT JOIN produto p ON edp.ProdutoID = p.Codigo "
                   + "WHERE rp.Data BETWEEN ? AND ? ";

        List<Object> params = new ArrayList<>();
        params.add(new java.sql.Date(dataInicial.getTime()));
        params.add(new java.sql.Date(dataFinal.getTime()));

        // Adiciona o filtro por nome de fornecedor, se existir
        if (nomeFornecedor != null && !nomeFornecedor.trim().isEmpty()) {
            sql += "AND f.Nome LIKE ? ";
            params.add("%" + nomeFornecedor + "%");
        }

        // Adiciona o filtro por chave de acesso, se existir
        if (chaveAcesso != null && !chaveAcesso.trim().isEmpty()) {
            sql += "AND rp.ChaveAcessoNFe LIKE ? ";
            params.add("%" + chaveAcesso + "%");
        }

        sql += "GROUP BY rp.Codigo ORDER BY rp.Data DESC";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            // Define os parâmetros dinamicamente
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
                    ReposicaoProduto reposicao = new ReposicaoProduto();
                    reposicao.data = rs.getDate("Data");
                    reposicao.codigo = BigInteger.valueOf(rs.getLong("Codigo"));
                    reposicao.fornecedorNome = rs.getString("FornecedorNome"); 
                    reposicao.valorTotal = rs.getBigDecimal("ValorTotal");
                    reposicao.chaveAcessoNFe = rs.getString("ChaveAcessoNFe");
                    reposicao.produtos = rs.getString("Produtos");
                    listaReposicoes.add(reposicao);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaReposicoes;
    }

    public static List<ResultadoPesquisaReposicao> pesquisarReposicao(String nomeProduto, String nomeFornecedor, Date dataInicio, Date dataFim) {
        String sql = "SELECT rp.Data AS Data, p.Nome AS NomeProduto, f.Nome AS NomeFornecedor, " +
                     "SUM(edp.Quantidade) AS QuantidadeTotal, " +
                     "SUM(edp.ValorDaCompra) AS ValorCompraTotal " +
                     "FROM entradadeproduto AS edp " +
                     "JOIN reposicao_entrada AS re ON edp.Codigo = re.EntradaID " +
                     "JOIN reposicaoproduto AS rp ON re.ReposicaoID = rp.Codigo " +
                     "JOIN produto AS p ON edp.ProdutoID = p.Codigo " +
                     "JOIN fornecedor AS f ON rp.FornecedorID = f.Codigo " +
                     "WHERE 1=1 ";

        List<Object> params = new ArrayList<>();

        if (nomeProduto != null && !nomeProduto.isEmpty()) {
            sql += "AND p.Nome LIKE ? ";
            params.add("%" + nomeProduto + "%");
        }
        if (nomeFornecedor != null && !nomeFornecedor.isEmpty()) {
            sql += "AND f.Nome LIKE ? ";
            params.add("%" + nomeFornecedor + "%");
        }
        if (dataInicio != null && dataFim != null) {
            sql += "AND rp.Data BETWEEN ? AND ? ";
            params.add(new java.sql.Date(dataInicio.getTime()));
            params.add(new java.sql.Date(dataFim.getTime()));
        }

        sql += "GROUP BY p.Nome, f.Nome, rp.Data ORDER BY rp.Data DESC";

        List<ResultadoPesquisaReposicao> resultados = new ArrayList<>();

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
                    ResultadoPesquisaReposicao resultado = new ResultadoPesquisaReposicao();
                    resultado.data = rs.getDate("Data");
                    resultado.nomeProduto = rs.getString("NomeProduto");
                    resultado.nomeFornecedor = rs.getString("NomeFornecedor");
                    resultado.quantidadeTotal = rs.getInt("QuantidadeTotal");
                    resultado.valorTotal = rs.getBigDecimal("ValorCompraTotal");
                    resultados.add(resultado);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultados;
    }
    
    //EXCLUIR
    public void excluirReposicoes(List<BigInteger> codigos, Component parent) {
        String sqlBuscarEntrada = "SELECT ProdutoID, Quantidade FROM entradadeproduto WHERE Codigo IN (SELECT EntradaID FROM reposicao_entrada WHERE ReposicaoID = ?)";
        String sqlAtualizarEstoque = "UPDATE produto SET Quantidade = Quantidade - ? WHERE Codigo = ?";
        String sqlDeletarEntradaVinculo = "DELETE FROM reposicao_entrada WHERE ReposicaoID = ?";
        String sqlDeletarEntrada = "DELETE FROM entradadeproduto WHERE Codigo IN (SELECT EntradaID FROM reposicao_entrada WHERE ReposicaoID = ?)";
        String sqlDeletarReposicao = "DELETE FROM reposicaoproduto WHERE Codigo = ?";

        try (Connection con = ConexaoBanco.getConnection()) {
            con.setAutoCommit(false);

            for (BigInteger reposicaoID : codigos) {

                // 1. Reverter produtos no estoque
                try (PreparedStatement stmtBuscar = con.prepareStatement(sqlBuscarEntrada)) {
                    stmtBuscar.setLong(1, reposicaoID.longValue());
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
                try (PreparedStatement stmt1 = con.prepareStatement(sqlDeletarEntradaVinculo)) {
                    stmt1.setLong(1, reposicaoID.longValue());
                    stmt1.executeUpdate();
                }

                try (PreparedStatement stmt2 = con.prepareStatement(sqlDeletarEntrada)) {
                    stmt2.setLong(1, reposicaoID.longValue());
                    stmt2.executeUpdate();
                }

                // 3. Deletar reposição principal
                try (PreparedStatement stmt3 = con.prepareStatement(sqlDeletarReposicao)) {
                    stmt3.setLong(1, reposicaoID.longValue());
                    stmt3.executeUpdate();
                }
            }

            con.commit();
            new Funcoes().Mensagens(parent, "Processo de exclusão realizado com sucesso!", "", "SUCESSO", "informacao");

        } catch (SQLException e) {
            e.printStackTrace();
            new Funcoes().Mensagens(parent, "Erro ao excluir:", e.getMessage(), "ERRO", "erro");
        }
    }
}