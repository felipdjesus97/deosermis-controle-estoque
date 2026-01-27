package Classes;

import Main.ConexaoBanco;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Produto {

    private BigInteger Codigo, FornecedorID, Quantidade, QuantidadeMinima;
    private String Nome, Fornecedor;

    public String getFornecedor() {
        return Fornecedor;
    }

    public void setFornecedor(String Fornecedor) {
        this.Fornecedor = Fornecedor;
    }
    private BigDecimal ValorUnitario;
    private static BigInteger CodigoAlterar;
    private List<BigInteger> fornecedoresSelecionados = new ArrayList<>();

    public static BigInteger getCodigoAlterar() {
        return CodigoAlterar;
    }

    public static void setCodigoAlterar(BigInteger CodigoAlterar) {
        Produto.CodigoAlterar = CodigoAlterar;
    }
    
    public BigInteger getFornecedorID() {
        return FornecedorID;
    }

    public void setFornecedorID(BigInteger FornecedorID) {
        this.FornecedorID = FornecedorID;
    }

    public BigInteger getCodigo() {
        return Codigo;
    }

    public void setCodigo(BigInteger Codigo) {
        this.Codigo = Codigo;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public BigInteger getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(BigInteger Quantidade) {
        this.Quantidade = Quantidade;
    }

    public BigInteger getQuantidadeMinima() {
        return QuantidadeMinima;
    }

    public void setQuantidadeMinima(BigInteger QuantidadeMinima) {
        this.QuantidadeMinima = QuantidadeMinima;
    }

    public BigDecimal getValorUnitario() {
        return ValorUnitario;
    }

    public void setValorUnitario(BigDecimal ValorUnitario) {
        this.ValorUnitario = ValorUnitario;
    }

    public List<BigInteger> getFornecedoresSelecionados() {
        return fornecedoresSelecionados;
    }

    public void setFornecedoresSelecionados(List<BigInteger> fornecedoresSelecionados) {
        this.fornecedoresSelecionados = fornecedoresSelecionados;
    }
    
    // Funções para salvar, alterar e suas relações
    public boolean inserirProduto(byte[] imagemBytes,Component parent) {
        String sqlProduto = "INSERT INTO produto (Nome, Quantidade, QuantidadeMinima, ValorUnitario, Inativo) VALUES (?, 0, ?, ?, false)";
        String sqlImagem = "INSERT INTO fotodoproduto (ProdutoID, Imagem) VALUES (?, ?)";

        try (Connection con = ConexaoBanco.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement stmtProduto = con.prepareStatement(sqlProduto, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtProduto.setString(1, Nome);
                stmtProduto.setLong(2, QuantidadeMinima.longValue());
                stmtProduto.setBigDecimal(3, ValorUnitario);
                
                int rows = stmtProduto.executeUpdate();
                if (rows > 0) {
                    try (ResultSet generatedKeys = stmtProduto.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            Codigo = BigInteger.valueOf(generatedKeys.getLong(1));
                        }
                    }

                    inserirFornecedoresRelacionados(con);
                    try (PreparedStatement stmtImagem = con.prepareStatement(sqlImagem)) {
                        stmtImagem.setLong(1, Codigo.longValue());
                        if (imagemBytes != null) {
                            stmtImagem.setBytes(2, imagemBytes);
                        } else {
                            stmtImagem.setNull(2, Types.BLOB);
                        }
                        stmtImagem.executeUpdate();
                    }

                    con.commit();
                    return true;
                } else {
                    con.rollback();
                }
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao inserir produto: " , e.getMessage(), "ERRO", "erro");
        }
        return false;
    }
    
    public boolean alterarProduto(byte[] imagemBytes, Component parent) {
        String sqlProduto = "UPDATE produto SET Nome=?, QuantidadeMinima=?, ValorUnitario=? WHERE Codigo=?";
        String sqlImagem = "UPDATE fotodoproduto SET Imagem=? WHERE ProdutoID=?";
        
        try (Connection con = ConexaoBanco.getConnection()) {
            con.setAutoCommit(false);
            
            try (PreparedStatement stmtProduto = con.prepareStatement(sqlProduto)) {
                stmtProduto.setString(1, Nome);
                stmtProduto.setLong(2, QuantidadeMinima.longValue());
                stmtProduto.setBigDecimal(3, ValorUnitario);
                stmtProduto.setLong(4, Codigo.longValue());
                stmtProduto.executeUpdate();
                
                excluirFornecedoresRelacionados();
                inserirFornecedoresRelacionados(con);
                
                try (PreparedStatement stmtImagem = con.prepareStatement(sqlImagem)) {
                    if (imagemBytes != null) {
                        stmtImagem.setBytes(1, imagemBytes);
                    } else {
                        stmtImagem.setNull(1, Types.BLOB);
                    }
                    stmtImagem.setLong(2, Codigo.longValue());
                    stmtImagem.executeUpdate();
                }
                
                con.commit();
                return true;
                
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao alterar produto: " , e.getMessage(), "ERRO", "erro");
        }
        return false;
    }
    
    private void inserirFornecedoresRelacionados(Connection con) throws Exception {
        String sql = "INSERT INTO produto_fornecedor (ProdutoID, FornecedorID) VALUES (?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            for (BigInteger fornecedorID : fornecedoresSelecionados) {
                stmt.setLong(1, Codigo.longValue());
                stmt.setLong(2, fornecedorID.longValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
    
    // Funções de consulta
    public boolean consultarProduto(BigInteger codigoConsulta,Component parent) {
        String sql = "SELECT Codigo, Nome, QuantidadeMinima, ValorUnitario "
                + "FROM produto WHERE Codigo = ? AND Inativo = false";
        
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setLong(1, codigoConsulta.longValue());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    this.setCodigo(BigInteger.valueOf(rs.getLong("Codigo")));
                    this.setNome(rs.getString("Nome"));
                    this.setQuantidadeMinima(BigInteger.valueOf(rs.getLong("QuantidadeMinima")));
                    this.setValorUnitario(rs.getBigDecimal("ValorUnitario"));
                    return true;
                }
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao consultar produto: " , e.getMessage(), "ERRO", "erro");
            e.printStackTrace();
        }
        return false;
    }
    
    public List<BigInteger> buscarFornecedoresDoProduto(BigInteger codigoProduto, Component parent) {
        List<BigInteger> fornecedores = new ArrayList<>();
        String sql = "SELECT FornecedorID FROM produto_fornecedor WHERE ProdutoID = ?";
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, codigoProduto.longValue());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fornecedores.add(BigInteger.valueOf(rs.getLong("FornecedorID")));
                }
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar fornecedores do produto: " , e.getMessage(), "ERRO", "erro");
        }
        return fornecedores;
    }
    
    public boolean produtoExiste(String nome) {
        return produtoExiste(nome, null);
    }
    
    public boolean produtoExiste(String nome, BigInteger codigoExclusao) {
        boolean existe = false;
        String sql = "SELECT COUNT(*) FROM produto WHERE Nome = ? AND Inativo = false";
        if (codigoExclusao != null) {
            sql += " AND Codigo <> ?";
        }
        
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nome);
            if (codigoExclusao != null) {
                ps.setLong(2, codigoExclusao.longValue());
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    existe = rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return existe;
    }
    
    public static Produto buscarProdutoPorCodigo(BigInteger codigoProduto, Component parent) {
        Produto produto = null;
        String sql = "SELECT Nome, Quantidade, ValorUnitario, QuantidadeMinima "
                + "FROM produto WHERE Codigo = ? AND Inativo = false";
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, codigoProduto.longValue());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                produto = new Produto();
                produto.setNome(rs.getString("Nome"));
                produto.setQuantidade(BigInteger.valueOf(rs.getLong("Quantidade")));
                produto.setQuantidadeMinima(BigInteger.valueOf(rs.getLong("QuantidadeMinima")));
                produto.setValorUnitario(rs.getBigDecimal("ValorUnitario"));
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar produto por código: " , e.getMessage(), "ERRO", "erro");
        }
        return produto;
    }
    
    public static List<Produto> buscarProduto(String nomeParcial, Component parent) {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT Codigo, Nome, Quantidade, ValorUnitario, QuantidadeMinima "
                + "FROM produto WHERE Nome LIKE ? AND Inativo = false";
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeParcial + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Produto p = new Produto();
                p.setCodigo(BigInteger.valueOf(rs.getLong("Codigo")));
                p.setNome(rs.getString("Nome"));
                p.setQuantidade(BigInteger.valueOf(rs.getLong("Quantidade")));
                p.setQuantidadeMinima(BigInteger.valueOf(rs.getLong("QuantidadeMinima")));
                p.setValorUnitario(rs.getBigDecimal("ValorUnitario"));
                lista.add(p);
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar produtos: " , e.getMessage(), "ERRO", "erro");
        }
        return lista;
    }
    
    public static List<Produto> buscarProdutoPorFornecedor(BigInteger fornecedorID, String nomeParcial, Component parent) {
        List<Produto> lista = new ArrayList<>();
        if (fornecedorID != null) {
            String sql = "SELECT p.Codigo, p.Nome, p.Quantidade, p.ValorUnitario, p.QuantidadeMinima "
                    + "FROM produto p "
                    + "JOIN produto_fornecedor pf ON p.Codigo = pf.ProdutoID "
                    + "WHERE pf.FornecedorID = ? AND p.Nome LIKE ? AND p.Inativo = false";
            try (Connection con = ConexaoBanco.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setLong(1, fornecedorID.longValue());
                stmt.setString(2, "%" + nomeParcial + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setCodigo(BigInteger.valueOf(rs.getLong("Codigo")));
                    p.setNome(rs.getString("Nome"));
                    p.setQuantidade(BigInteger.valueOf(rs.getLong("Quantidade")));
                    p.setQuantidadeMinima(BigInteger.valueOf(rs.getLong("QuantidadeMinima")));
                    p.setValorUnitario(rs.getBigDecimal("ValorUnitario"));
                    lista.add(p);
                }
            } catch (Exception e) {
                new Funcoes().Mensagens(parent, "Erro ao buscar produtos:" , e.getMessage(), "ERRO", "erro");
            }
        }
        return lista;
    }
    
    public static Produto buscarProdutoPorCodigoEFornecedor(BigInteger fornecedorID, BigInteger codigoProduto, Component parent) {
        Produto produto = null;
        if (fornecedorID != null) {
            String sql = "SELECT p.Nome, p.Quantidade, p.QuantidadeMinima "
                    + "FROM produto p "
                    + "JOIN produto_fornecedor pf ON p.Codigo = pf.ProdutoID "
                    + "WHERE pf.FornecedorID = ? AND p.Codigo = ? AND p.Inativo = false";
            try (Connection con = ConexaoBanco.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setLong(1, fornecedorID.longValue());
                stmt.setLong(2, codigoProduto.longValue());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    produto = new Produto();
                    produto.setNome(rs.getString("Nome"));
                    produto.setQuantidade(BigInteger.valueOf(rs.getLong("Quantidade")));
                    produto.setQuantidadeMinima(BigInteger.valueOf(rs.getLong("QuantidadeMinima")));
                }
            } catch (Exception e) {
                new Funcoes().Mensagens(parent, "Erro ao buscar produto por código: " , e.getMessage(), "ERRO", "erro");
            }
        }
        return produto;
    }
    
    public byte[] buscarImagemProduto(BigInteger codigoProduto, Component parent) {
        String sql = "SELECT Imagem FROM fotodoproduto WHERE ProdutoID = ?";
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, codigoProduto.longValue());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("Imagem");
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar imagem do produto: " , e.getMessage(), "ERRO", "erro");
        }
        return null;
    }
    
    public static BigInteger buscarFornecedorIdPorProdutoId(BigInteger produtoId, Component parent) {
        String sql = "SELECT FornecedorID FROM produto_fornecedor WHERE ProdutoID = ?";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setLong(1, produtoId.longValue());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return BigInteger.valueOf(rs.getLong("FornecedorID"));
                }
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar FornecedorID: " , e.getMessage(), "ERRO", "erro");
        }
        return null;
    }
    
    public static List<Produto> buscarProdutosComFornecedores(String nomeParcial,Component parent) {
        List<Produto> listaProdutos = new ArrayList<>();
        String sql = "SELECT p.Codigo, p.Nome, p.Quantidade, p.ValorUnitario, p.QuantidadeMinima, "
                   + "GROUP_CONCAT(f.Nome SEPARATOR ', ') AS Fornecedor "
                   + "FROM produto p "
                   + "LEFT JOIN produto_fornecedor pf ON p.Codigo = pf.ProdutoID "
                   + "LEFT JOIN fornecedor f ON pf.FornecedorID = f.Codigo "
                   + "WHERE p.Nome LIKE ? AND p.Inativo = false "
                   + "GROUP BY p.Codigo";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeParcial + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setCodigo(BigInteger.valueOf(rs.getLong("Codigo")));
                    p.setNome(rs.getString("Nome"));
                    p.setFornecedor(rs.getString("Fornecedor"));
                    p.setValorUnitario(rs.getBigDecimal("ValorUnitario"));
                    p.setQuantidade(BigInteger.valueOf(rs.getLong("Quantidade")));
                    p.setQuantidadeMinima(BigInteger.valueOf(rs.getLong("QuantidadeMinima")));
                    listaProdutos.add(p);
                }
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar produtos com fornecedores: " , e.getMessage(), "ERRO", "erro");
        }
        return listaProdutos;
    }
    
    public static List<Produto> buscarProdutosEmFalta() {
        List<Produto> listaProdutos = new ArrayList<>();
        String sql = "SELECT p.Codigo, p.Nome, p.Quantidade, p.ValorUnitario, p.QuantidadeMinima, "
                   + "GROUP_CONCAT(f.Nome SEPARATOR ', ') AS Fornecedor "
                   + "FROM produto p "
                   + "LEFT JOIN produto_fornecedor pf ON p.Codigo = pf.ProdutoID "
                   + "LEFT JOIN fornecedor f ON pf.FornecedorID = f.Codigo "
                   + "WHERE p.Quantidade <= p.QuantidadeMinima AND p.Inativo = false "
                   + "GROUP BY p.Codigo";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto p = new Produto();
                    p.setCodigo(BigInteger.valueOf(rs.getLong("Codigo")));
                    p.setNome(rs.getString("Nome"));
                    p.setFornecedor(rs.getString("Fornecedor"));
                    p.setValorUnitario(rs.getBigDecimal("ValorUnitario"));
                    p.setQuantidade(BigInteger.valueOf(rs.getLong("Quantidade")));
                    p.setQuantidadeMinima(BigInteger.valueOf(rs.getLong("QuantidadeMinima")));
                    listaProdutos.add(p);
                }
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(null, 
                "Erro ao buscar produtos em falta com fornecedores: " , e.getMessage(), 
                "ERRO", 
                "erro");
        }
        return listaProdutos;
    }    
    
    public static boolean existeProdutoEmFalta() {
        String sql = "SELECT 1 "
                   + "FROM produto p "
                   + "WHERE p.Quantidade <= p.QuantidadeMinima AND p.Inativo = false "
                   + "LIMIT 1"; // Limita a 1 resultado, só precisamos saber se existe

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            // Se houver ao menos 1 linha, significa que existe produto em falta
            return rs.next();

        } catch (Exception e) {
            new Funcoes().Mensagens(null,
                "Erro ao verificar produtos em falta: ", e.getMessage(),
                "ERRO",
                "erro");
        }
        return false; // Se houver erro, retornamos false
    }

    // Funções de exclusão    
    private void excluirFornecedoresRelacionados() throws Exception {
        String sql = "DELETE FROM produto_fornecedor WHERE ProdutoID = ?";
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, Codigo.longValue());
            stmt.executeUpdate();
        }
    }
    
    public void excluirProdutos(List<BigInteger> codigos, Component parent) {
        if (codigos == null || codigos.isEmpty()) return;

        String sqlFornecedor = "DELETE FROM produto_fornecedor WHERE ProdutoID = ?";
        Connection conn = null;

        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtFornecedor = conn.prepareStatement(sqlFornecedor)) {
                for (BigInteger codigo : codigos) {
                    stmtFornecedor.setLong(1, codigo.longValue());
                    stmtFornecedor.addBatch();
                }
                stmtFornecedor.executeBatch();
            }

            String sqlFoto = "DELETE FROM fotodoproduto WHERE ProdutoID = ?";
            try (PreparedStatement stmtFoto = conn.prepareStatement(sqlFoto)) {
                for (BigInteger codigo : codigos) {
                    stmtFoto.setLong(1, codigo.longValue());
                    stmtFoto.addBatch();
                }
                stmtFoto.executeBatch();
            }
                
            String sqlInativo = "UPDATE produto SET Inativo = true WHERE Codigo = ?";
            try (PreparedStatement stmtInativo = conn.prepareStatement(sqlInativo)) {
                for (BigInteger codigo : codigos) {
                    stmtInativo.setLong(1, codigo.longValue());
                    stmtInativo.addBatch();
                }
                stmtInativo.executeBatch();
            }

            conn.commit();
            new Funcoes().Mensagens(parent, "Processo de exclusão realizado com sucesso!", "", "SUCESSO", "informacao");

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            new Funcoes().Mensagens(parent, "Erro ao excluir:", e.getMessage(), "ERRO", "erro");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
