/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Tela.Usuario;

import Classes.Funcoes;
import Classes.Usuario;
import java.awt.Window;


@SuppressWarnings("serial")
public class RecuperarSenhaEmail extends javax.swing.JDialog {
    private final Funcoes funcao = new Funcoes();
    
    public RecuperarSenhaEmail(Window parent, boolean modal) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        setUndecorated(true);
        setResizable(false);
        initComponents();
        emailText.requestFocus();
        funcao.aplicarEfeitoMouse(botaoMenu);  
        funcao.aplicarEfeitoMouse(botaoConfirmar); 
        digitacao();
        qualEsqueceu();
    }
    
    private void digitacao(){
        funcao.limitarCaracteres(nomeText, 50); 
        funcao.limitarCaracteres(emailText, 100); 
        funcaoEnabled();
    }
    
    private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoConfirmar, "Clique para confirmar");
    }
    
    private boolean elegivelSalvar(){
        if(radioSenha.isSelected()){
            return funcao.validarCampoVazio(nomeText) && emailValido();
        }
        return emailValido();
    }
    
    private void funcaoEnabled(){
        botaoConfirmar.setEnabled(elegivelSalvar());
        botaoConfirmar.setOpaque(elegivelSalvar());
        labelEmailValido.setVisible(!emailValido());
        adicionarLegendas();
    }
    
    private boolean emailValido(){
        return funcao.validarEmail(emailText);
    }

    private void confirmar() {
        // 1. Validação inicial do email
        if (!emailValido()) {
            funcao.MensagensJDialog(this,
                "Por favor, insira um e-mail válido.",
                "",
                "ATENÇÃO", "aviso");
            return;
        }

        String email = emailText.getText().trim();
        String usuarioNome = nomeText.getText().trim();

        if (radioSenha.isSelected()) {
            // Opção: Esqueceu a senha
            if (usuarioNome.isEmpty()) {
                funcao.MensagensJDialog(this,
                    "Por favor, insira o seu nome de usuário.",
                    "",
                    "ATENÇÃO", "aviso");
                return;
            }

            Usuario user = Usuario.buscarPorNome(usuarioNome);

            if (user != null && user.getEmail().equalsIgnoreCase(email)) {
                try {
                    // 2. Gera nova senha temporária
                    String senhaTemporaria = funcao.gerarSenhaAleatoria();

                    // 3. Atualiza no banco (hash + salt já é tratado em setSenha)
                    user.setSenha(senhaTemporaria);
                    user.atualizar(user.getCodigo(), null, null, senhaTemporaria);
                    
                    user.atualizarSenhaTemporaria(user.getCodigo(), true);
                    // 4. Envia por e-mail
                    if (funcao.enviarCredenciais(email, usuarioNome, senhaTemporaria)) {
                        funcao.MensagensJDialog(this,
                            "Uma nova senha temporária foi enviada para o seu e-mail.",
                            "Use-a para acessar e altere a senha assim que possível.",
                            "SUCESSO", "simples");
                        dispose();
                    } else {
                        funcao.MensagensJDialog(this,
                            "Erro ao enviar o e-mail.",
                            "Tente novamente mais tarde.",
                            "ERRO", "erro");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    funcao.MensagensJDialog(this,
                        "Erro interno ao redefinir a senha.",
                        "Tente novamente mais tarde.",
                        "ERRO", "erro");
                }
            } else {
                funcao.MensagensJDialog(this,
                    "Usuário ou e-mail inválido.",
                    "",
                    "ERRO DE DIGITAÇÃO", "erro");
            }

        } else {
            // Opção: Esqueceu o nome do usuário
            Usuario user = Usuario.buscarPorEmail(email);

            if (user != null) {
                String nomeParaEnvio = user.getNome();
                if (!Funcoes.verificarConexaoInternet(this)) {
                    funcao.MensagensJDialog(this,
                        "Erro: Não foi possível conectar à internet.",
                        "Verifique sua conexão e tente novamente.",
                        "ERRO DE CONEXÃO", "erro");
                    return;
                }
                
                if (funcao.enviarCredenciais(email, nomeParaEnvio, null)) {
                    funcao.MensagensJDialog(this,
                        "O seu nome de usuário foi enviado para o e-mail.",
                        "Por favor, verifique sua caixa de entrada.",
                        "SUCESSO", "simples");
                    dispose();
                } else {
                    funcao.MensagensJDialog(this,
                        "Erro ao enviar o e-mail.",
                        "Tente novamente mais tarde.",
                        "ERRO", "erro");
                }
            } else {
                funcao.MensagensJDialog(this,
                    "E-mail não cadastrado.",
                    "",
                    "ERRO DE DIGITAÇÃO", "erro");
            }
        }
        this.dispose();
    }

    private void qualEsqueceu(){
        if(radioSenha.isSelected()){
            labelUsuario.setVisible(true);
            nomeText.setVisible(true);
            LBinfo.setText("Para gerar uma senha temporária,");
            LBinfo2.setText("informe seu nome de usuário e o e-mail cadastrado.");
        }else{
            funcao.limparComponente(nomeText);
            labelUsuario.setVisible(false);
            nomeText.setVisible(false);
            LBinfo.setText("Para recuperar seu nome de usuário,");
            LBinfo2.setText("informe seu e-mail cadastrado.");
        }
        funcaoEnabled();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoConfirmar = new javax.swing.JButton();
        botaoMenu = new javax.swing.JButton();
        emailText = new javax.swing.JTextField();
        labelEmail = new javax.swing.JLabel();
        LBinfo = new javax.swing.JLabel();
        labelUsuario = new javax.swing.JLabel();
        nomeText = new javax.swing.JTextField();
        labelEmailValido = new javax.swing.JLabel();
        LBinfo2 = new javax.swing.JLabel();
        painelDataReposicao = new javax.swing.JDesktopPane();
        radioUsuario = new javax.swing.JRadioButton();
        radioSenha = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(210, 164, 2));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Arial", 1, 25)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoTipoRECUPERAR.png"))); // NOI18N
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 430, 60));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 0, 430, 60);

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
        bordaSalvar.setBounds(10, 320, 91, 62);

        botaoMenu.setBackground(new java.awt.Color(204, 0, 0));
        botaoMenu.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoMenu.setForeground(new java.awt.Color(255, 255, 255));
        botaoMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/VOLTAR.png"))); // NOI18N
        botaoMenu.setText("VOLTAR");
        botaoMenu.setToolTipText("Clique para voltar");
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
        botaoMenu.setBounds(360, 320, 60, 60);

        emailText.setBackground(new java.awt.Color(255, 255, 255));
        emailText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        emailText.setForeground(new java.awt.Color(0, 0, 0));
        emailText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        emailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextActionPerformed(evt);
            }
        });
        emailText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailTextKeyReleased(evt);
            }
        });
        jPanel1.add(emailText);
        emailText.setBounds(65, 210, 300, 23);

        labelEmail.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelEmail.setForeground(new java.awt.Color(0, 0, 0));
        labelEmail.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelEmail.setText("E-mail:");
        jPanel1.add(labelEmail);
        labelEmail.setBounds(65, 190, 300, 20);

        LBinfo.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 22)); // NOI18N
        LBinfo.setForeground(new java.awt.Color(0, 0, 102));
        LBinfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LBinfo.setText("para enviar sua senha");
        LBinfo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(LBinfo);
        LBinfo.setBounds(0, 140, 430, 30);

        labelUsuario.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        labelUsuario.setForeground(new java.awt.Color(0, 0, 0));
        labelUsuario.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelUsuario.setText("Usuário:");
        jPanel1.add(labelUsuario);
        labelUsuario.setBounds(65, 260, 300, 20);

        nomeText.setBackground(new java.awt.Color(255, 255, 255));
        nomeText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nomeText.setForeground(new java.awt.Color(0, 0, 0));
        nomeText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        nomeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeTextActionPerformed(evt);
            }
        });
        nomeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomeTextKeyReleased(evt);
            }
        });
        jPanel1.add(nomeText);
        nomeText.setBounds(65, 280, 300, 23);

        labelEmailValido.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelEmailValido.setForeground(new java.awt.Color(204, 0, 0));
        labelEmailValido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelEmailValido.setText("*DIGITE E-MAIL VÁLIDO");
        jPanel1.add(labelEmailValido);
        labelEmailValido.setBounds(65, 230, 300, 20);

        LBinfo2.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 22)); // NOI18N
        LBinfo2.setForeground(new java.awt.Color(0, 0, 102));
        LBinfo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LBinfo2.setText("informe seu nome de usuário e o e-mail cadastrado.");
        LBinfo2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(LBinfo2);
        LBinfo2.setBounds(0, 162, 430, 20);

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "SELECIONE", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 24), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        radioUsuario.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioUsuario);
        radioUsuario.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        radioUsuario.setForeground(new java.awt.Color(0, 0, 0));
        radioUsuario.setText("Esqueci meu usuário");
        radioUsuario.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                radioUsuarioStateChanged(evt);
            }
        });
        radioUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioUsuarioActionPerformed(evt);
            }
        });
        painelDataReposicao.add(radioUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 160, -1));

        radioSenha.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioSenha);
        radioSenha.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        radioSenha.setForeground(new java.awt.Color(0, 0, 0));
        radioSenha.setSelected(true);
        radioSenha.setText("Esqueci minha senha");
        radioSenha.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                radioSenhaStateChanged(evt);
            }
        });
        radioSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioSenhaActionPerformed(evt);
            }
        });
        painelDataReposicao.add(radioSenha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 160, -1));

        jPanel1.add(painelDataReposicao);
        painelDataReposicao.setBounds(40, 70, 350, 70);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConfirmarActionPerformed
        confirmar();            
    }//GEN-LAST:event_botaoConfirmarActionPerformed

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        this.dispose();
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void nomeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeTextKeyReleased
        digitacao();
    }//GEN-LAST:event_nomeTextKeyReleased

    private void nomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeTextActionPerformed
       confirmar(); 
    }//GEN-LAST:event_nomeTextActionPerformed

    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextActionPerformed

    private void emailTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextKeyReleased
        digitacao();
    }//GEN-LAST:event_emailTextKeyReleased

    private void radioSenhaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_radioSenhaStateChanged

    }//GEN-LAST:event_radioSenhaStateChanged

    private void radioSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioSenhaActionPerformed
        qualEsqueceu();
    }//GEN-LAST:event_radioSenhaActionPerformed

    private void radioUsuarioStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_radioUsuarioStateChanged

    }//GEN-LAST:event_radioUsuarioStateChanged

    private void radioUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioUsuarioActionPerformed
        qualEsqueceu();
    }//GEN-LAST:event_radioUsuarioActionPerformed

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
            java.util.logging.Logger.getLogger(RecuperarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RecuperarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RecuperarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RecuperarSenhaEmail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RecuperarSenhaEmail dialog = new RecuperarSenhaEmail(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel LBinfo;
    private javax.swing.JLabel LBinfo2;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoConfirmar;
    private javax.swing.JButton botaoMenu;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JTextField emailText;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel labelEmail;
    private javax.swing.JLabel labelEmailValido;
    private javax.swing.JLabel labelUsuario;
    private javax.swing.JTextField nomeText;
    private javax.swing.JDesktopPane painelDataReposicao;
    private javax.swing.JRadioButton radioSenha;
    private javax.swing.JRadioButton radioUsuario;
    // End of variables declaration//GEN-END:variables
}
