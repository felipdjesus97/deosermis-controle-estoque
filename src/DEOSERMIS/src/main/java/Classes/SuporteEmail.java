
package Classes;

import java.util.Properties;
import java.util.List;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;

public class SuporteEmail {

    private static final String REMETENTE = "deosermis@gmail.com";
    private static final String SENHA = "jhqk whxt gobp jsjg"; 
    private static final String DESTINATARIO = "felipdjesus97@gmail.com"; // para onde os emails de suporte vão

    public static boolean enviarEmailSuporte(String nomeCompleto, String emailUsuario, String assunto, String descricao, List<byte[]> listaImagens, List<String> nomesArquivos) {
        // Monta a mensagem final
        String mensagem = "Novo contato de suporte:\n\n"
                + "Nome completo: " + nomeCompleto + "\n"
                + "E-mail: " + emailUsuario + "\n"
                + "Assunto: " + assunto + "\n"
                + "Descrição:\n" + descricao;

        return enviarEmail(DESTINATARIO, "Suporte: " + assunto, mensagem, listaImagens, nomesArquivos);
    }

    public static boolean enviarEmail(String destinatario, String assunto, String mensagem, List<byte[]> anexos, List<String> nomesArquivos) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMETENTE, SENHA);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(REMETENTE));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);

            // Parte do texto
            MimeBodyPart corpoMensagem = new MimeBodyPart();
            corpoMensagem.setText(mensagem);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(corpoMensagem);

            // Adiciona cada imagem da lista
            if (anexos != null && !anexos.isEmpty()) {
                for (int i = 0; i < anexos.size(); i++) {
                    byte[] anexoBytes = anexos.get(i);
                    if (anexoBytes != null && anexoBytes.length > 0) {
                        MimeBodyPart corpoAnexo = new MimeBodyPart();
                        ByteArrayDataSource dataSource = new ByteArrayDataSource(anexoBytes, "image/png");
                        corpoAnexo.setDataHandler(new DataHandler(dataSource));
                        String nomeArquivo = (nomesArquivos != null && i < nomesArquivos.size())
                                ? nomesArquivos.get(i)
                                : "anexo_" + (i + 1) + ".png";
                        corpoAnexo.setFileName(nomeArquivo);
                        multipart.addBodyPart(corpoAnexo);
                    }
                }
            }

            message.setContent(multipart);

            Transport.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

/*
// Lista com várias imagens em bytes (ex: escolhidas no JFileChooser)
List<byte[]> imagens = new ArrayList<>();
imagens.add(funcoes.carregarImagem(meuFrame, meuLabel1));
imagens.add(funcoes.carregarImagem(meuFrame, meuLabel2));

List<String> nomes = new ArrayList<>();
nomes.add("erro1.png");
nomes.add("erro2.png");

boolean sucesso = SuporteEmail.enviarEmailSuporte(
    "Felipe de Jesus",
    "felipe@email.com",
    "Problema com o sistema",
    "Estou enviando dois prints que mostram o erro.",
    imagens,
    nomes
);

if (sucesso) {
    System.out.println("Email enviado com sucesso!");
} else {
    System.out.println("Erro ao enviar email.");
}

*/