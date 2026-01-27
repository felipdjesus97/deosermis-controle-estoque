
package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/loja"; // nome do banco
        String usuario = "root"; // usuário do MySQL
        String senha = "";       // senha
        return DriverManager.getConnection(url, usuario, senha);
    }
}

