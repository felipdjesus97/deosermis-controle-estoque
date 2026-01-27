package Classes;

public class UsuarioAtual {
    private static int codigo;
    private static String nome;
    private static String senhaHash; // hash
    private static String salt;      // salt da senha
    private static boolean tipo;
    private static String email;
    private static Boolean senhaTemporaria; // flag para senha temporária
    private static Runnable acao;

    public static Runnable getAcao() { return acao; }
    public static void setAcao(Runnable acao) { UsuarioAtual.acao = acao; }

    // Getters e Setters
    public static int getCodigo() { return codigo; }
    public static void setCodigo(int codigo) { UsuarioAtual.codigo = codigo; }

    public static String getNome() { return nome; }
    public static void setNome(String nome) { UsuarioAtual.nome = nome; }

    public static String getSenhaHash() { return senhaHash; }
    public static void setSenhaHash(String senhaHash) { UsuarioAtual.senhaHash = senhaHash; }

    public static String getSalt() { return salt; }
    public static void setSalt(String salt) { UsuarioAtual.salt = salt; }

    public static boolean getTipo() { return tipo; }
    public static void setTipo(boolean tipo) { UsuarioAtual.tipo = tipo; }

    public static String getEmail() { return email; }
    public static void setEmail(String email) { UsuarioAtual.email = email; }

    public static Boolean getSenhaTemporaria() { return senhaTemporaria; }
    public static void setSenhaTemporaria(Boolean temporaria) { UsuarioAtual.senhaTemporaria = temporaria; }
}
