package Classes;

import Main.ConexaoBanco;
import java.awt.Component;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Usuario {

    private long codigo;
    private String nome;
    private String email;
    private String senha; // hash
    private String salt;  // salt em Base64
    private boolean tipo; 
    private Map<Integer, Boolean> permissoes;
    private Boolean senhaTemporaria;

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    public Usuario(String nome, String email, String senha, boolean tipo) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;

        // Gera salt e hash da senha
        this.salt = gerarSalt();
        this.senha = gerarHashSenha(senha, salt);
    }

    public Usuario(String nome, String email, String senha, boolean tipo, Map<Integer, Boolean> permissoes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this(nome, email, senha, tipo);
        this.permissoes = permissoes;
    }
    
    public static Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuario WHERE Email = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(); // Construtor vazio ou método 'builder' pode ser útil
                    usuario.codigo = rs.getLong("Codigo");
                    usuario.nome = rs.getString("Nome");
                    usuario.email = rs.getString("Email");
                    usuario.senha = rs.getString("Senha");
                    usuario.salt = rs.getString("Salt");
                    usuario.tipo = rs.getBoolean("Tipo");
                    usuario.senhaTemporaria = rs.getObject("SenhaTemporaria") != null ? rs.getBoolean("SenhaTemporaria") : null;
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Usuario buscarPorNome(String nome) {
        String sql = "SELECT * FROM usuario WHERE Nome = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(); // Você precisará de um construtor vazio ou similar
                    usuario.codigo = rs.getLong("Codigo");
                    usuario.nome = rs.getString("Nome");
                    usuario.email = rs.getString("Email");
                    usuario.senha = rs.getString("Senha");
                    usuario.salt = rs.getString("Salt");
                    usuario.tipo = rs.getBoolean("Tipo");
                    usuario.senhaTemporaria = rs.getObject("SenhaTemporaria") != null ? rs.getBoolean("SenhaTemporaria") : null;
                    return usuario;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean verificarCorrespondencia(String nome, String email) {
        String sql = "SELECT 1 FROM usuario WHERE Nome = ? AND Email = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setString(2, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Map<Integer, Boolean> carregarPermissoes(long usuarioID) {
        Map<Integer, Boolean> permissoes = new HashMap<>();

        String sql = "SELECT FuncionalidadeID, Permitir FROM permissao WHERE UsuarioID = ?";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, usuarioID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int funcID = rs.getInt("FuncionalidadeID");
                boolean permitir = rs.getBoolean("Permitir");
                permissoes.put(funcID, permitir);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return permissoes;
    }

    public List<Usuario> buscarUsuarioPorNome(String emailParcial, Component parent) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT codigo, nome, email, tipo FROM usuario WHERE email LIKE ?";
        
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, "%" + emailParcial + "%");
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setCodigo(rs.getLong("codigo"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setTipo(rs.getBoolean("tipo"));
                lista.add(u);
            }
        } catch (SQLException e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar usuários: " , e.getMessage(), "DEOSÉMIS", "erro");
        }
        return lista;
    }

    // Consultar por código (para editar)
    public boolean consultarUsuario(Long codigo, Component parent) {
        String sql = "SELECT nome, email, tipo FROM usuario WHERE codigo = ?";
        
        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setLong(1, codigo.longValue());
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                this.nome = rs.getString("nome");
                this.email = rs.getString("email");
                this.tipo = rs.getBoolean("tipo");
                return true;
            }
        } catch (SQLException e) {
            new Funcoes().Mensagens(parent, "Erro ao consultar usuário: " , e.getMessage(), "DEOSÉMIS", "erro");
        }
        return false;
    }

    public Usuario() {}
     
    public boolean inserir() {
        String sqlUsuario = "INSERT INTO usuario (Nome, Senha, Salt, Tipo, Email) VALUES (?, ?, ?, ?, ?)";
        String sqlPermissao = "INSERT INTO permissao (UsuarioID, FuncionalidadeID, Permitir) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection()) {
            conn.setAutoCommit(false);

            // Inserir usuário
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUsuario, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, this.nome);
                stmtUser.setString(2, this.senha);
                stmtUser.setString(3, this.salt);
                stmtUser.setBoolean(4, this.tipo);
                stmtUser.setString(5, this.email);
                stmtUser.executeUpdate();

                // Recupera o ID gerado
                try (var rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.codigo = rs.getLong(1);
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
            }

            // Se não for admin, inserir permissões
            if (!this.tipo && this.permissoes != null) {
                try (PreparedStatement stmtPerm = conn.prepareStatement(sqlPermissao)) {
                    for (Map.Entry<Integer, Boolean> entry : this.permissoes.entrySet()) {
                        stmtPerm.setLong(1, this.codigo);
                        stmtPerm.setInt(2, entry.getKey());
                        stmtPerm.setBoolean(3, entry.getValue());
                        stmtPerm.addBatch();
                    }
                    stmtPerm.executeBatch();
                }
            }

            conn.commit();
            atualizarSenhaTemporaria(this.codigo, true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizarPermissoesETipo() {
        String sqlUsuario = "UPDATE usuario SET Tipo = ? WHERE Codigo = ?";
        String sqlDeletePermissao = "DELETE FROM permissao WHERE UsuarioID = ?";
        String sqlInsertPermissao = "INSERT INTO permissao (UsuarioID, FuncionalidadeID, Permitir) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBanco.getConnection()) {
            conn.setAutoCommit(false);

            // Atualiza apenas o tipo (Administrador ou não)
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUsuario)) {
                stmtUser.setBoolean(1, this.tipo);
                stmtUser.setLong(2, this.codigo);
                stmtUser.executeUpdate();
            }

            // Se não for admin, atualizar permissões
            if (!this.tipo && this.permissoes != null) {
                try (PreparedStatement stmtDel = conn.prepareStatement(sqlDeletePermissao)) {
                    stmtDel.setLong(1, this.codigo);
                    stmtDel.executeUpdate();
                }

                try (PreparedStatement stmtPerm = conn.prepareStatement(sqlInsertPermissao)) {
                    for (Map.Entry<Integer, Boolean> entry : this.permissoes.entrySet()) {
                        stmtPerm.setLong(1, this.codigo);
                        stmtPerm.setInt(2, entry.getKey());
                        stmtPerm.setBoolean(3, entry.getValue());
                        stmtPerm.addBatch();
                    }
                    stmtPerm.executeBatch();
                }
            } else {
                // Se for admin, só limpa permissões (não precisa salvar nada)
                try (PreparedStatement stmtDel = conn.prepareStatement(sqlDeletePermissao)) {
                    stmtDel.setLong(1, this.codigo);
                    stmtDel.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean atualizar(long codigo, String novoNome, String novoEmail, String novaSenha) {
        try (Connection conn = ConexaoBanco.getConnection()) {
            conn.setAutoCommit(false);

            StringBuilder sqlBuilder = new StringBuilder("UPDATE usuario SET ");
            boolean hasUpdate = false;
            int paramIndex = 1;

            if (novoNome != null) {
                sqlBuilder.append("Nome = ?, ");
                hasUpdate = true;
            }
            
            if (novoEmail != null) {
                sqlBuilder.append("Email = ?, ");
                hasUpdate = true;
            }
            
            String novoSalt = null;
            String novoHash = null;
            if (novaSenha != null) {
                novoSalt = gerarSalt();
                novoHash = gerarHashSenha(novaSenha, novoSalt);
                sqlBuilder.append("Senha = ?, Salt = ?, ");
                hasUpdate = true;
            }
            
            // Remove a última vírgula e espaço
            if (hasUpdate) {
                sqlBuilder.setLength(sqlBuilder.length() - 2);
            } else {
                // Não há nada para atualizar
                return true;
            }
            
            sqlBuilder.append(" WHERE Codigo = ?");
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {
                if (novoNome != null) {
                    stmt.setString(paramIndex++, novoNome);
                }
                
                if (novoEmail != null) {
                    stmt.setString(paramIndex++, novoEmail);
                }
                
                if (novaSenha != null) {
                    stmt.setString(paramIndex++, novoHash);
                    stmt.setString(paramIndex++, novoSalt);
                }
                
                stmt.setLong(paramIndex, codigo);
                
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            }
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean atualizarSenhaTemporaria(long codigo, Boolean temporaria) {
        String sql = "UPDATE usuario SET SenhaTemporaria = ? WHERE Codigo = ?";
        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (temporaria == null) {
                stmt.setNull(1, java.sql.Types.BOOLEAN);
            } else {
                stmt.setBoolean(1, temporaria);
            }
            stmt.setLong(2, codigo);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Usuario> buscarUsuarios(Long codigoUsuarioLogado, Component parent) {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT codigo, nome, email, tipo FROM usuario " +
                     "WHERE codigo <> 1 AND codigo <> ?";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setLong(1, codigoUsuarioLogado); // ignora o próprio usuário
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setCodigo(rs.getLong("codigo"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setTipo(rs.getBoolean("tipo"));
                lista.add(u);
            }
        } catch (SQLException e) {
            new Funcoes().Mensagens(parent, "Erro ao buscar usuários: ", e.getMessage(), "DEOSÉMIS", "erro");
        }
        return lista;
    }
    
    //EXCLUIR
    public void excluirUsuarios(List<BigInteger> codigos, Component parent) {
        if (codigos == null || codigos.isEmpty()) return;

        String sqlDeletarPermissao = "DELETE FROM permissao WHERE UsuarioID = ?";
        String sqlDeletarUsuario = "DELETE FROM usuario WHERE Codigo = ?";
        Connection conn = null;

        try {
            conn = ConexaoBanco.getConnection();
            conn.setAutoCommit(false);

            // 1. Excluir permissões
            try (PreparedStatement stmtPerm = conn.prepareStatement(sqlDeletarPermissao)) {
                for (BigInteger codigo : codigos) {
                    stmtPerm.setLong(1, codigo.longValue());
                    stmtPerm.addBatch();
                }
                stmtPerm.executeBatch();
            }

            // 2. Excluir usuários
            try (PreparedStatement stmtUser = conn.prepareStatement(sqlDeletarUsuario)) {
                for (BigInteger codigo : codigos) {
                    stmtUser.setLong(1, codigo.longValue());
                    stmtUser.addBatch();
                }
                stmtUser.executeBatch();
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
    
    // ==================== Hash e Salt ====================
    private static String gerarSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String gerarHashSenha(String senha, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(senha.toCharArray(), Base64.getDecoder().decode(salt), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean validarSenha(String senhaInformada, String hashBanco, String saltBanco) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String hashTentativa = gerarHashSenha(senhaInformada, saltBanco);
        return hashTentativa.equals(hashBanco);
    }

    // ================= Getters/Setters =================
    public long getCodigo() { return codigo; }
    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public String getSalt() { return salt; }
    public void setSenha(String senha) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.salt = gerarSalt();
        this.senha = gerarHashSenha(senha, salt);
    }
    public boolean isTipo() { return tipo; }
    public void setTipo(boolean tipo) { this.tipo = tipo; }
    public Map<Integer, Boolean> getPermissoes() { return permissoes; }
    public void setPermissoes(Map<Integer, Boolean> permissoes) { this.permissoes = permissoes; }
    public Boolean getSenhaTemporaria() {
        return senhaTemporaria;
    }

    public void setSenhaTemporaria(Boolean senhaTemporaria) {
        this.senhaTemporaria = senhaTemporaria;
    }


}
