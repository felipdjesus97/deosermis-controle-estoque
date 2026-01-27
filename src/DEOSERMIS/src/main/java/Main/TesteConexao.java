package Main;

import java.sql.Connection;

public class TesteConexao {
    public static boolean verificarConexao() {
        try (Connection con = ConexaoBanco.getConnection()) {
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return false;
        }
    }
}
