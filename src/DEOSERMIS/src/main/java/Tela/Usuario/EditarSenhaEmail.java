/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Tela.Usuario;

import Classes.Funcoes;
import Classes.OpcaoDeTela;
import Classes.Usuario;
import Classes.UsuarioAtual;
import java.awt.Window;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JPasswordField;


@SuppressWarnings("serial")
public class EditarSenhaEmail extends javax.swing.JDialog {
    private final Funcoes funcao = new Funcoes();
    private final UsuarioAtual usuarioAtual = new UsuarioAtual();
    private final OpcaoDeTela SenhaEmail = new OpcaoDeTela();
    
    public EditarSenhaEmail(Window parent, boolean modal) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        setUndecorated(true);
        setResizable(false);
        initComponents();
        SenhaEmail();
        antigaText.requestFocus();
        funcao.aplicarEfeitoMouse(botaoMenu);  
        funcao.aplicarEfeitoMouse(botaoConfirmar);
        botaoMenu.setVisible(!usuarioAtual.getSenhaTemporaria());
        digitacao();
    }
    
    private void digitacao(){
        if (SenhaEmail.getCadastro()) {
            digitacaoSenha();
        }else{
            digitacaoEmail();
        }
        funcaoEnabled();
    }
    
    private void digitacaoSenha(){
        funcao.limitarCaracteres(antigaText, 20); 
        funcao.limitarCaracteres(novaText, 20);
        funcao.limitarCaracteres(confirmarNovaText, 20);
    }
    
    private void digitacaoEmail(){
        funcao.limitarCaracteres(antigaText, 100); 
        funcao.limitarCaracteres(novaText, 100);
        funcao.limitarCaracteres(confirmarNovaText, 100);
    }
    
    private void SenhaEmail() {
        if (SenhaEmail.getCadastro()) {
            labelTitulo.setText("ALTERAR SENHA");
            
            lableAntiga.setText("Senha Atual:");
            labelNova.setText("Nova Senha:");
            labelConfirmarNova.setText("Confirme Nova Senha:");
            
            labelAntigaVazio.setText("*SENHA ATUAL INCORRETA");
            labelNovaVazio.setText("*DIGITE A NOVA SENHA");
            labelConfirmaNovaVazio.setText("*SENHAS DIGITADAS SÃO DIFERENTES");
            
            antigaText.setEchoChar('*');
            novaText.setEchoChar('*');
            confirmarNovaText.setEchoChar('*');
            
            labelNovoIgualAntigo.setText("*A NOVA SENHA DEVE SER DIFERENTE DA ATUAL");
        }else{
            labelTitulo.setText("ALTERAR E-MAIL");
            
            lableAntiga.setText("E-mail Atual:");
            labelNova.setText("Novo E-mail:");
            labelConfirmarNova.setText("Confirme Novo E-mail:");
            
            labelAntigaVazio.setText("*E-MAIL ATUAL INCORRETO");
            labelNovaVazio.setText("*DIGITE O NOVO E-MAIL VÁLIDO");
            labelConfirmaNovaVazio.setText("*E-MAILS DIGITADOS SÃO DIFERENTES");
            
            antigaText.setEchoChar((char) 0);
            novaText.setEchoChar((char) 0);
            confirmarNovaText.setEchoChar((char) 0);
            
            labelNovoIgualAntigo.setText("*O NOVO E-MAIL DEVE SER DIFERENTE DO ATUAL");
        }
    }
    
    private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoConfirmar, "Clique para confirmar");
    }
    
    private boolean campoAtualCorreto(){
        if (SenhaEmail.getCadastro()) {
            // MODO SENHA
            String senhaDigitada = new String(antigaText.getPassword()).trim();
            try {
                // Instancie a classe Usuario para usar o método validarSenha
                Usuario usuario = new Usuario(usuarioAtual.getNome(), usuarioAtual.getEmail(), usuarioAtual.getSenhaHash(), usuarioAtual.getTipo());
                return usuario.validarSenha(senhaDigitada, usuarioAtual.getSenhaHash(), usuarioAtual.getSalt());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // MODO EMAIL
            String emailDigitado = new String(antigaText.getPassword()).trim();
            return emailDigitado.equalsIgnoreCase(usuarioAtual.getEmail());
        }
    }
    
    private boolean elegivelSalvar(){
        boolean senhaValida = funcao.senhaValida(new String(novaText.getPassword()).trim());
        boolean emailValido = validarEmail(novaText);
        
        boolean camposValidos;
        if (SenhaEmail.getCadastro()) {
            camposValidos = senhaValida;
        } else {
            camposValidos = emailValido;
        }
        
       return funcao.validarCamposPreenchidos(antigaText, novaText, confirmarNovaText)
                && funcao.camposIguais(novaText,confirmarNovaText)
                && !novoIgualAntigo()
                && camposValidos;
    }
    
    private void funcaoEnabled(){
        componentesEnabled();
        confimarVazio();
        adicionarLegendas();
    }
    
    private void componentesEnabled(){
        boolean novoMesmoAntigo = novoIgualAntigo();
        String novaSenha = new String(novaText.getPassword()).trim();
        boolean senhaValida = funcao.senhaValida(novaSenha);
        
        if (SenhaEmail.getCadastro()) {
            // MODO SENHA
            botaoConfirmar.setEnabled(elegivelSalvar());
            botaoConfirmar.setOpaque(elegivelSalvar());
            
            labelAntigaVazio.setVisible(!campoAtualCorreto());
            labelNovaVazio.setVisible(!funcao.validarCampoVazio(novaText));
            labelConfirmaNovaVazio.setVisible(!funcao.camposIguais(novaText,confirmarNovaText));
            
            if (novoMesmoAntigo) {
                labelNovoIgualAntigo.setText("*A NOVA SENHA DEVE SER DIFERENTE DA ATUAL");
            } else if (!senhaValida) {
                labelNovoIgualAntigo.setText("*A NOVA SENHA DEVE TER 8-20 CARACTERES, UMA LETRA E UM NÚMERO");
            }
            
            labelNovoIgualAntigo.setVisible(
                    !senhaValida && funcao.validarCampoVazio(novaText) 
                    || novoMesmoAntigo && funcao.validarCampoVazio(novaText));
        }else{
            // MODO EMAIL
            botaoConfirmar.setEnabled(elegivelSalvar() && emailValido());
            botaoConfirmar.setOpaque(elegivelSalvar() && emailValido());
            
            labelAntigaVazio.setVisible(!campoAtualCorreto());
            labelNovaVazio.setVisible(!validarEmail(novaText));
            labelConfirmaNovaVazio.setVisible(!funcao.camposIguais(novaText,confirmarNovaText));
            labelNovoIgualAntigo.setVisible(novoMesmoAntigo);
        }        
    }
    private void confimarVazio(){
         if (funcao.validarCampoVazio(confirmarNovaText)){
             if (SenhaEmail.getCadastro()) {
                labelConfirmaNovaVazio.setText("*SENHAS DIGITADAS SÃO DIFERENTES");
            }else{
                labelConfirmaNovaVazio.setText("*E-MAILS DIGITADOS SÃO DIFERENTES");
            }
        }else{
             if (SenhaEmail.getCadastro()) {
                labelConfirmaNovaVazio.setText("*DIGITE NOVAMENTE A NOVA SENHA");
            }else{
                labelConfirmaNovaVazio.setText("*DIGITE NOVAMENTE O NOVO E-MAIL");
            }
         }
         
         if (funcao.validarCampoVazio(antigaText)){
             if (SenhaEmail.getCadastro()) {
                labelAntigaVazio.setText("*SENHA ATUAL INCORRETA");
            }else{
                labelAntigaVazio.setText("*E-MAIL ATUAL INCORRETO");
            }
        }else{
             if (SenhaEmail.getCadastro()) {
                labelAntigaVazio.setText("*DIGITE A SENHA ATUAL");
            }else{
                labelAntigaVazio.setText("*DIGITE O E-MAIL ATUAL");
            }
         }
    }
    private boolean emailValido(){
        return validarEmail(antigaText) && validarEmail(novaText) && funcao.camposIguais(novaText,confirmarNovaText);
    }
    
    private boolean validarEmail(JPasswordField campo){
        return funcao.validarEmailJPasswordField(campo);
    }
    
    private boolean novoIgualAntigo() {
        if (SenhaEmail.getCadastro()) {
            // Modo senha: valida se o hash da nova senha é igual ao hash salvo
            String novaSenha = new String(novaText.getPassword()).trim();
            try {
                String novoHash = Usuario.gerarHashSenha(novaSenha, usuarioAtual.getSalt());
                return novoHash.equals(usuarioAtual.getSenhaHash());
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                e.printStackTrace();
                // Retorna true para bloquear a alteração em caso de erro.
                return true; 
            }
        } else {
            // Modo e-mail: valida se o novo e-mail é igual ao e-mail salvo
            String novoEmail = new String(novaText.getPassword()).trim();
            return novoEmail.equalsIgnoreCase(usuarioAtual.getEmail());
        }
    }
    
    private void confirmar(){
        // Verifica se todas as condições para salvar são atendidas
        if (!elegivelSalvar() && !campoAtualCorreto()) {
            funcao.MensagensJDialog(this, "Preencha todos os campos corretamente!","", "ATENÇÃO", "aviso");
            funcaoEnabled();
            return;
        }
        if (!SenhaEmail.getCadastro()) {
             if (!emailValido()) {
                 funcao.MensagensJDialog(this, "Preencha todos os campos corretamente!","", "ATENÇÃO", "aviso");
                 funcaoEnabled();
                 return;
             }
        }
        
        if (novoIgualAntigo()) {
            if (SenhaEmail.getCadastro()) {
                funcao.MensagensJDialog(this, "A nova senha não pode ser","a mesma que a anterior", "ATENÇÃO", "aviso");
                funcaoEnabled();
                return;
            }else{
                funcao.MensagensJDialog(this, "O novo e-mail não pode ser","o mesmo que o anterior", "ATENÇÃO", "aviso");
                funcaoEnabled();
                return;
            }
        }
        boolean senhaValida = funcao.senhaValida(new String(novaText.getPassword()).trim());

        if (!senhaValida && SenhaEmail.getCadastro()) {
            funcao.MensagensJDialog(this, 
                "A senha deve ter 8-20 caracteres,", 
                "uma letra e um número", 
                "ATENÇÃO", "aviso");
            funcaoEnabled();
            return;
        }
         
        
        try {
            Usuario usuario = new Usuario(usuarioAtual.getNome(), usuarioAtual.getEmail(), usuarioAtual.getSenhaHash(), usuarioAtual.getTipo());
            boolean sucesso;
            String novoSenhaEmail = new String(novaText.getPassword()).trim();
            
            if (SenhaEmail.getCadastro()) {
                sucesso = usuario.atualizar(usuarioAtual.getCodigo(), null, null, novoSenhaEmail);
            } else {
                sucesso = usuario.atualizar(usuarioAtual.getCodigo(), null, novoSenhaEmail, null);
            }

            if (sucesso) {
                if (SenhaEmail.getCadastro()) {
                    try (Connection conexao = Main.ConexaoBanco.getConnection()) {
                        // Busca o usuário pelo nome
                        String sql = "SELECT * FROM usuario WHERE BINARY Nome = ?";
                        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                            stmt.setString(1, usuarioAtual.getNome());
                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    String hashBanco = rs.getString("Senha").trim();
                                    String saltBanco = rs.getString("Salt").trim();

                                    // Valida a senha usando hash e salt
                                    boolean senhaCorreta = Usuario.validarSenha(novoSenhaEmail, hashBanco, saltBanco);
                                     if (senhaCorreta) {
                                        usuarioAtual.setSenhaHash(hashBanco);
                                        usuarioAtual.setSalt(saltBanco);
                                     }else{
                                         return;
                                     }
                                } 
                            }
                        }
                     } catch (Exception ex) {
                        ex.printStackTrace();
                        return;
                    }
                }else{
                    usuarioAtual.setEmail(novoSenhaEmail);
                }
                usuario.atualizarSenhaTemporaria(usuarioAtual.getCodigo(), false);
                funcao.MensagensJDialog(this, "Dados alterados com sucesso!", "","SUCESSO", "informacao");
                this.dispose();
            } else {
                funcao.MensagensJDialog(this, "Não foi possível alterar os dados,","Tente novamente.", "ERRO", "erro");
            }

        } catch (Exception e) {
            e.printStackTrace();
            funcao.MensagensJDialog(this, "Erro ao tentar alterar os dados.","Verifique a conexão com o banco de dados.", "ERRO", "erro");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        labelTitulo = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoConfirmar = new javax.swing.JButton();
        painelDataReposicao = new javax.swing.JDesktopPane();
        lableAntiga = new javax.swing.JLabel();
        novaText = new javax.swing.JPasswordField();
        labelNova = new javax.swing.JLabel();
        confirmarNovaText = new javax.swing.JPasswordField();
        labelConfirmarNova = new javax.swing.JLabel();
        antigaText = new javax.swing.JPasswordField();
        labelAntigaVazio = new javax.swing.JLabel();
        labelNovaVazio = new javax.swing.JLabel();
        labelConfirmaNovaVazio = new javax.swing.JLabel();
        labelNovoIgualAntigo = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(210, 164, 2));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        labelTitulo.setFont(new java.awt.Font("Arial", 1, 36)); // NOI18N
        labelTitulo.setForeground(new java.awt.Color(0, 0, 0));
        labelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitulo.setText("ALTERAR SENHA");
        jPanel3.add(labelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 470, 60));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 0, 471, 60);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoConfirmar.setBackground(new java.awt.Color(210, 164, 2));
        botaoConfirmar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        botaoConfirmar.setForeground(new java.awt.Color(0, 0, 0));
        botaoConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONFIRMAR.png"))); // NOI18N
        botaoConfirmar.setText("CONFIRMAR");
        botaoConfirmar.setToolTipText("");
        botaoConfirmar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoConfirmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoConfirmar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoConfirmar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        botaoConfirmar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConfirmarActionPerformed(evt);
            }
        });
        bordaSalvar.add(botaoConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaSalvar);
        bordaSalvar.setBounds(10, 380, 91, 62);

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 24), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lableAntiga.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        lableAntiga.setForeground(new java.awt.Color(0, 0, 0));
        lableAntiga.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lableAntiga.setText("Antigo");
        painelDataReposicao.add(lableAntiga, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 230, -1));

        novaText.setBackground(new java.awt.Color(255, 255, 255));
        novaText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        novaText.setForeground(new java.awt.Color(0, 0, 0));
        novaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                novaTextActionPerformed(evt);
            }
        });
        novaText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                novaTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(novaText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 230, -1));

        labelNova.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelNova.setForeground(new java.awt.Color(0, 0, 0));
        labelNova.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNova.setText("Nova");
        painelDataReposicao.add(labelNova, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 230, -1));

        confirmarNovaText.setBackground(new java.awt.Color(255, 255, 255));
        confirmarNovaText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        confirmarNovaText.setForeground(new java.awt.Color(0, 0, 0));
        confirmarNovaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmarNovaTextActionPerformed(evt);
            }
        });
        confirmarNovaText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                confirmarNovaTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(confirmarNovaText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 230, -1));

        labelConfirmarNova.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelConfirmarNova.setForeground(new java.awt.Color(0, 0, 0));
        labelConfirmarNova.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelConfirmarNova.setText("Confirmar nova");
        painelDataReposicao.add(labelConfirmarNova, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, 230, -1));

        antigaText.setBackground(new java.awt.Color(255, 255, 255));
        antigaText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        antigaText.setForeground(new java.awt.Color(0, 0, 0));
        antigaText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                antigaTextFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                antigaTextFocusLost(evt);
            }
        });
        antigaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                antigaTextActionPerformed(evt);
            }
        });
        antigaText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                antigaTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(antigaText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 230, -1));

        labelAntigaVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelAntigaVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelAntigaVazio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelAntigaVazio.setText("*DIGITE A SENHA ATUAL");
        painelDataReposicao.add(labelAntigaVazio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 230, 20));

        labelNovaVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelNovaVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelNovaVazio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNovaVazio.setText("*DIGITE A NOVA SENHA");
        painelDataReposicao.add(labelNovaVazio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 230, 20));

        labelConfirmaNovaVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelConfirmaNovaVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelConfirmaNovaVazio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelConfirmaNovaVazio.setText("*E-MAILS DIGITADOS SÃO DIFERENTES ");
        painelDataReposicao.add(labelConfirmaNovaVazio, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 210, 230, 20));

        jPanel1.add(painelDataReposicao);
        painelDataReposicao.setBounds(80, 90, 310, 270);

        labelNovoIgualAntigo.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelNovoIgualAntigo.setForeground(new java.awt.Color(204, 0, 0));
        labelNovoIgualAntigo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelNovoIgualAntigo.setText("*ANTIGO IGUAL ATUAL");
        jPanel1.add(labelNovoIgualAntigo);
        labelNovoIgualAntigo.setBounds(0, 360, 470, 13);

        botaoMenu.setBackground(new java.awt.Color(204, 0, 0));
        botaoMenu.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoMenu.setForeground(new java.awt.Color(255, 255, 255));
        botaoMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/home.png"))); // NOI18N
        botaoMenu.setText("MENU");
        botaoMenu.setToolTipText("Clique para voltar ao menu principal");
        botaoMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        botaoMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoMenu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMenuActionPerformed(evt);
            }
        });
        jPanel1.add(botaoMenu);
        botaoMenu.setBounds(403, 380, 60, 60);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConfirmarActionPerformed
        confirmar();            
    }//GEN-LAST:event_botaoConfirmarActionPerformed

    private void novaTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_novaTextActionPerformed
        confirmar();
    }//GEN-LAST:event_novaTextActionPerformed

    private void novaTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_novaTextKeyReleased
        digitacao();
    }//GEN-LAST:event_novaTextKeyReleased

    private void confirmarNovaTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmarNovaTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmarNovaTextActionPerformed

    private void confirmarNovaTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmarNovaTextKeyReleased
        digitacao();
    }//GEN-LAST:event_confirmarNovaTextKeyReleased

    private void antigaTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_antigaTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_antigaTextActionPerformed

    private void antigaTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_antigaTextKeyReleased
        digitacao();
    }//GEN-LAST:event_antigaTextKeyReleased

    private void antigaTextFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_antigaTextFocusGained
        digitacao();
    }//GEN-LAST:event_antigaTextFocusGained

    private void antigaTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_antigaTextFocusLost
        digitacao();
    }//GEN-LAST:event_antigaTextFocusLost

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        this.dispose();
    }//GEN-LAST:event_botaoMenuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EditarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EditarSenhaEmail dialog = new EditarSenhaEmail(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPasswordField antigaText;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoConfirmar;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JPasswordField confirmarNovaText;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labelAntigaVazio;
    private javax.swing.JLabel labelConfirmaNovaVazio;
    private javax.swing.JLabel labelConfirmarNova;
    private javax.swing.JLabel labelNova;
    private javax.swing.JLabel labelNovaVazio;
    private javax.swing.JLabel labelNovoIgualAntigo;
    private javax.swing.JLabel labelTitulo;
    private javax.swing.JLabel lableAntiga;
    private javax.swing.JPasswordField novaText;
    private javax.swing.JDesktopPane painelDataReposicao;
    // End of variables declaration//GEN-END:variables
}
