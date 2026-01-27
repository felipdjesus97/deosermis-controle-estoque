package Classes;

import Main.ConexaoBanco;
import java.awt.Component;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Fornecedor {
    private BigInteger Codigo;
    private String Nome, CPFCNPJ, Email, Instagram, WhatsApp, Site, UF, Cidade, Bairro, CEP, Logradouro, Numero, Complemento, Obs;
    private static BigInteger CodigoAlterar;
    
    // Getters e Setters
    public static BigInteger getCodigoAlterar() {
        return CodigoAlterar;
    }
    public static void setCodigoAlterar(BigInteger CodigoAlterar) {
        Fornecedor.CodigoAlterar = CodigoAlterar;
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

    public String getCPFCNPJ() { 
        return CPFCNPJ; 
    }
    public void setCPFCNPJ(String CPFCNPJ) { 
        this.CPFCNPJ = CPFCNPJ; 
    }

    public String getEmail() { 
        return Email; 
    }
    public void setEmail(String Email) { 
        this.Email = Email; 
    }

    public String getInstagram() { 
        return Instagram; 
    }
    public void setInstagram(String Instagram) { 
        this.Instagram = Instagram; 
    }

    public String getWhatsApp() { 
        return WhatsApp; 
    }
    public void setWhatsApp(String WhatsApp) { 
        this.WhatsApp = WhatsApp; 
    }

    public String getSite() { 
        return Site; 
    }
    public void setSite(String Site) { 
        this.Site = Site; 
    }

    public String getUF() { 
        return UF; 
    }
    public void setUF(String UF) { 
        this.UF = UF; 
    }

    public String getCidade() { 
        return Cidade; 
    }
    public void setCidade(String Cidade) { 
        this.Cidade = Cidade; 
    }

    public String getBairro() { 
        return Bairro; 
    }
    public void setBairro(String Bairro) { 
        this.Bairro = Bairro; 
    }

    public String getCEP() { 
        return CEP; 
    }
    public void setCEP(String CEP) { 
        this.CEP = CEP; 
    }

    public String getLogradouro() { 
        return Logradouro; 
    }
    public void setLogradouro(String Logradouro) { 
        this.Logradouro = Logradouro; 
    }

    public String getNumero() { 
        return Numero; 
    }
    public void setNumero(String Numero) { 
        this.Numero = Numero; 
    }

    public String getComplemento() { 
        return Complemento; 
    }
    public void setComplemento(String Complemento) { 
        this.Complemento = Complemento; 
    }

    public String getObs() { 
        return Obs; 
    }
    public void setObs(String Obs) { 
        this.Obs = Obs; 
    }

    // INSERIR
    public void inserirFornecedor(Component parent) {
        String sql = "INSERT INTO fornecedor (Nome, CPFCNPJ, Email, Instagram, WhatsApp, Site, CEP, UF, Cidade, Logradouro, Numero, Complemento, Observacao, Bairro, Inativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, false)";
        try (Connection con = ConexaoBanco.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, Nome);
            stmt.setString(2, CPFCNPJ);
            stmt.setString(3, Email);
            stmt.setString(4, Instagram);
            stmt.setString(5, WhatsApp);
            stmt.setString(6, Site);
            stmt.setString(7, CEP);
            stmt.setString(8, UF);
            stmt.setString(9, Cidade);
            stmt.setString(10, Logradouro);
            stmt.setString(11, Numero);
            stmt.setString(12, Complemento);
            stmt.setString(13, Obs);
            stmt.setString(14, Bairro);

            stmt.executeUpdate();
            new Funcoes().Mensagens(parent, "Fornecedor cadastrado com sucesso", "", "SUCESSO" ,"informacao");

        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao inserir fornecedor: ", e.getMessage(), "ERRO", "erro");
        }
    }

    // ALTERAR
    public void alterarFornecedor(Component parent) {
        String sql = "UPDATE fornecedor SET Nome=?, CPFCNPJ=?, Email=?, Instagram=?, WhatsApp=?, Site=?, CEP=?, UF=?, Cidade=?, Logradouro=?, Numero=?, Complemento=?, Observacao=?, Bairro=? " +
                     "WHERE Codigo=?";
        try (Connection con = ConexaoBanco.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, Nome);
            stmt.setString(2, CPFCNPJ);
            stmt.setString(3, Email);
            stmt.setString(4, Instagram);
            stmt.setString(5, WhatsApp);
            stmt.setString(6, Site);
            stmt.setString(7, CEP);
            stmt.setString(8, UF);
            stmt.setString(9, Cidade);
            stmt.setString(10, Logradouro);
            stmt.setString(11, Numero);
            stmt.setString(12, Complemento);
            stmt.setString(13, Obs);
            stmt.setString(14, Bairro);
            stmt.setLong(15, Codigo.longValue());

            stmt.executeUpdate();

        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao atualizar fornecedor: ",  e.getMessage(), "ERRO", "erro");
        }
    }
    
    // CONSULTAR
    public boolean consultarFornecedor(BigInteger codigoConsulta,Component parent) {
        String sql = "SELECT * FROM fornecedor WHERE Codigo = ? AND Inativo = false";
        try (Connection con = ConexaoBanco.getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setLong(1, codigoConsulta.longValue());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                this.Codigo = BigInteger.valueOf(rs.getLong("Codigo"));
                this.Nome = rs.getString("Nome");
                this.CPFCNPJ = rs.getString("CPFCNPJ");
                this.Email = rs.getString("Email");
                this.Instagram = rs.getString("Instagram");
                this.WhatsApp = rs.getString("WhatsApp");
                this.Site = rs.getString("Site");
                this.CEP = rs.getString("CEP");
                this.UF = rs.getString("UF");
                this.Bairro = rs.getString("Bairro");
                this.Cidade = rs.getString("Cidade");
                this.Logradouro = rs.getString("Logradouro");
                this.Numero = rs.getString("Numero");
                this.Complemento = rs.getString("Complemento");
                this.Obs = rs.getString("Observacao");
                return true;
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao consultar fornecedor: ", e.getMessage(), "ERRO", "erro");
        }
        return false;
    }
    
    public boolean fornecedorExisteCampo(String valor, String campoBanco,Component parent) {
       if (!campoBanco.equalsIgnoreCase("cpfcnpj") && !campoBanco.equalsIgnoreCase("nome")) {
           return false;
       }

       String sql = "SELECT COUNT(*) FROM fornecedor WHERE " + campoBanco + " = ? AND Inativo = false";

       if (this.CodigoAlterar != null) {
           sql += " AND Codigo <> ?";
       }

       try (Connection conn = ConexaoBanco.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

           stmt.setString(1, valor);

           if (this.CodigoAlterar != null) {
               stmt.setLong(2, this.CodigoAlterar.longValue());
           }

           try (ResultSet rs = stmt.executeQuery()) {
               if (rs.next()) {
                   return rs.getInt(1) > 0;
               }
           }

       } catch (Exception e) {
           new Funcoes().Mensagens(parent, "Erro no banco: ", e.getMessage(), "ERRO", "erro");
       }
       return false;
   }
    
    public static List<Object[]> carregarFornecedoresProduto(Component parent) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT Codigo, Nome FROM fornecedor WHERE Inativo = false ORDER BY Nome";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                BigInteger codigo = BigInteger.valueOf(rs.getLong("Codigo"));
                String nome = rs.getString("Nome");
                lista.add(new Object[] {false, nome, codigo}); // checkbox desmarcado
            }

        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao obter fornecedores: ", e.getMessage(), "ERRO", "erro");
        }

        return lista;
    }
    
    public static List<Fornecedor> buscarFornecedorPorNome(String nomeBusca,Component parent) {
        List<Fornecedor> fornecedoresEncontrados = new ArrayList<>();
        String sql = "SELECT * FROM fornecedor WHERE Nome LIKE ? AND Inativo = false ORDER BY Codigo";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeBusca + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setCodigo(BigInteger.valueOf(rs.getLong("Codigo")));
                    fornecedor.setNome(rs.getString("Nome"));
                    fornecedor.setCPFCNPJ(rs.getString("CPFCNPJ"));
                    fornecedor.setEmail(rs.getString("Email"));
                    fornecedor.setInstagram(rs.getString("Instagram"));
                    fornecedor.setWhatsApp(rs.getString("WhatsApp"));
                    fornecedor.setSite(rs.getString("Site"));
                    fornecedor.setCEP(rs.getString("CEP"));
                    fornecedor.setUF(rs.getString("UF"));
                    fornecedor.setCidade(rs.getString("Cidade"));
                    fornecedor.setBairro(rs.getString("Bairro"));
                    fornecedor.setLogradouro(rs.getString("Logradouro"));
                    fornecedor.setNumero(rs.getString("Numero"));
                    fornecedor.setComplemento(rs.getString("Complemento"));
                    fornecedor.setObs(rs.getString("Observacao"));
                    fornecedoresEncontrados.add(fornecedor);
                }
            }
        } catch (Exception e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar fornecedores por nome: ", e.getMessage(), "ERRO", "erro");
        }
        return fornecedoresEncontrados;
    }    

    //EXCLUIR
    public void excluirFornecedores(List<BigInteger> codigos, Component parent) {
        if (codigos == null || codigos.isEmpty()) return;

        String sqlInativo = "UPDATE fornecedor SET Inativo = true WHERE Codigo = ?";
        Connection conn = null;

        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtInativo = conn.prepareStatement(sqlInativo)) {
                for (BigInteger codigo : codigos) {
                    stmtInativo.setLong(1, codigo.longValue());
                    stmtInativo.addBatch();
                }
                stmtInativo.executeBatch();
            }

            conn.commit();
            new Funcoes().Mensagens(parent, "Processo de exclusão realizado com sucesso!", "", "SUCESSO", "informacao");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            new Funcoes().Mensagens(parent, "Erro ao excluir:", e.getMessage(), "ERRO", "erro");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
