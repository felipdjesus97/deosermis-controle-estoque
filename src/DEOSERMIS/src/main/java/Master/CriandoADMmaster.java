
package Master;
import Classes.Funcoes;
import Classes.Usuario;
import java.awt.Window;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
//na hora de chamar:
//UsuarioMaster.criarMaster("adm", "adm@dominio.com", "123", this);
public class CriandoADMmaster {
    public static void criarMaster(String nome, String email, String senha, Window parent) {
        try {
            // Tenta criar o usuário admin
            boolean tipoAdmin = true; // sempre admin

            // Cria o objeto Usuario com hash e salt
            Usuario novoUsuario = new Usuario(nome, email, senha, tipoAdmin);

            // Insere no banco
            boolean sucesso = novoUsuario.inserir();

            if (sucesso) {
                System.out.println("Usuário administrador criado com sucesso!");
                System.out.println("ID: " + novoUsuario.getCodigo());
                System.out.println("Nome: " + novoUsuario.getNome());
                System.out.println("Senha: " + senha);
            } else {
                System.out.println("Erro ao criar o usuário administrador!");
            }
            
            String corpo = "Olá!\n\n"
                + "Seus dados de acesso ao sistema DEOSÉRMIS:\n"
                + "Usuário: " + novoUsuario.getNome() + "\n"
                + "Senha: " + senha + "\n\n"
                + "Recomenda-se trocar a senha após o primeiro acesso.";

        // Envia email
        if (Funcoes.verificarConexaoInternet(parent)) {
           Funcoes.enviarEmail(email, "Acesso ao Sistema", corpo);
        }    

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            System.out.println("Falha ao gerar hash/salt da senha!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
