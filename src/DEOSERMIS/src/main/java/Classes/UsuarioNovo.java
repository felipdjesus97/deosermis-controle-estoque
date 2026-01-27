/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import Main.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioNovo {
    private static String email, nome;
    private static boolean tipo;
    private static long codigo;

    public static long getCodigo() {
        return codigo;
    }

    public static void setCodigo(long codigo) {
        UsuarioNovo.codigo = codigo;
    }
    private static Map<Integer, Boolean> permissoes = new HashMap<>();

    public static boolean getTipo() {
        return tipo;
    }

    public static void setTipo(boolean tipo) {
        UsuarioNovo.tipo = tipo;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UsuarioNovo.email = email;
    }

    public static String getNome() {
        return nome;
    }

    public static void setNome(String nome) {
        UsuarioNovo.nome = nome;
    }

    public static Map<Integer, Boolean> getPermissoes() {
        return permissoes;
    }

    public static void setPermissoes(Map<Integer, Boolean> permissoes) {
        UsuarioNovo.permissoes = permissoes;
    }
    
    public static boolean emailExiste(String email) {
        String sql = "SELECT 1 FROM usuario WHERE Email = ? LIMIT 1";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); 

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Retorna lista de funcionalidades cadastradas
    public static List<Funcionalidade> listarFuncionalidades() {
        List<Funcionalidade> funcionalidades = new ArrayList<>();

        String sql = "SELECT Codigo, Nome FROM funcionalidades ORDER BY Codigo";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int codigo = rs.getInt("Codigo");
                String nome = rs.getString("Nome");
                funcionalidades.add(new Funcionalidade(codigo, nome));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return funcionalidades;
    }

    public static class Funcionalidade {
        private int codigo;
        private String nome;

        public Funcionalidade(int codigo, String nome) {
            this.codigo = codigo;
            this.nome = nome;
        }

        public int getCodigo() {
            return codigo;
        }

        public String getNome() {
            return nome;
        }
    }
}
