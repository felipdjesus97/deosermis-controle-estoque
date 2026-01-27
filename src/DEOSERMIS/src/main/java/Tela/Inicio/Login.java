/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tela.Inicio;

import Classes.Funcoes;
import Classes.OpcaoDeTela;
import Classes.OverlayUtil;
import Classes.Usuario;
import Classes.UsuarioAtual;
import Tela.Usuario.EditarSenhaEmail;
import Tela.Usuario.RecuperarSenhaEmail;
import java.awt.Frame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JRootPane;

@SuppressWarnings("serial")
public class Login extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final UsuarioAtual usuarioAtual = new UsuarioAtual();    
    private final OpcaoDeTela OpcaoDeTela = new OpcaoDeTela();
    
    public Login() {
        setUndecorated(true);
        initComponents();
        adicionarAtalhosTeclado();
        nomeText.requestFocus();
        funcao.aplicarEfeitoMouse(botaoEntrar);  
        funcao.aplicarEfeitoMouse(botaoRedefinir);
        funcao.aplicarEfeitoMouse(botaoSair);
        funcao.aplicarEfeitoMouse(botaoSuporte);
    }

    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "ENTER", botaoEntrar);
        funcao.adicionarAtalho(root, "F12", botaoSair);       
        funcao.adicionarAtalhoTeclado(root, "F1", () -> check());
    }
    
    private void entrar() {
        String login = nomeText.getText().trim();
        String senhaDigitada = new String(senhaText.getPassword()).trim();

        if (login.isEmpty() || senhaDigitada.isEmpty()) {
            funcao.Mensagens(this, "Preencha todos os campos corretamente!", "", "ATENÇÃO", "aviso");
            return;
        }

        try (Connection conexao = Main.ConexaoBanco.getConnection()) {
            // Busca o usuário pelo nome
            String sql = "SELECT * FROM usuario WHERE BINARY Nome = ?";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setString(1, login);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String hashBanco = rs.getString("Senha").trim();
                        String saltBanco = rs.getString("Salt").trim();
                        Boolean senhaTemp = rs.getObject("SenhaTemporaria") != null ? rs.getBoolean("SenhaTemporaria") : null;

                        // Valida a senha usando hash e salt
                        boolean senhaCorreta = Usuario.validarSenha(senhaDigitada, hashBanco, saltBanco);

                        if (senhaCorreta) {
                            // Armazena os dados do usuário atual
                            usuarioAtual.setCodigo(rs.getInt("Codigo"));
                            usuarioAtual.setNome(rs.getString("Nome"));
                            usuarioAtual.setSenhaHash(hashBanco);
                            usuarioAtual.setSalt(saltBanco);
                            usuarioAtual.setTipo(rs.getBoolean("Tipo"));
                            usuarioAtual.setEmail(rs.getString("Email"));
                            usuarioAtual.setSenhaTemporaria(senhaTemp);

                            // Se a senha for temporária, força a alteração antes de entrar
                            if (usuarioAtual.getSenhaTemporaria() != null && usuarioAtual.getSenhaTemporaria()) {
                                funcao.Mensagens(this,
                                        "Por motivos de segurança,",
                                        "será necessário a alteração da senha.",
                                        "ALTERAÇÃO NECESSÁRIA", "simples");
                                OpcaoDeTela.setCadastro(true);
                                EditarSenhaEmail dialog = new EditarSenhaEmail((Frame) this, true);
                                OverlayUtil.abrirTela((JFrame) this, dialog);

                                // Atualiza a flag de senha temporária após fechar a tela
                                Usuario usuarioAtualizado = Usuario.buscarPorNome(login);
                                usuarioAtual.setSenhaTemporaria(usuarioAtualizado.getSenhaTemporaria());

                                if (usuarioAtual.getSenhaTemporaria() != null && usuarioAtual.getSenhaTemporaria()) {
                                    funcao.Mensagens(this,
                                        "Você precisa alterar a senha temporária antes de entrar no sistema!",
                                        "",
                                        "ATENÇÃO", "aviso");
                                    return;
                                }
                            }

                            // Troca para a tela principal
                            funcao.trocarDeTela(this, new MenuPrincipal());
                        } else {
                            funcao.Mensagens(this, "Usuário ou Senha inválidos!", "", "ERRO DE LOGIN", "erro");
                        }
                    } else {
                        funcao.Mensagens(this, "Usuário ou Senha inválidos!", "", "ERRO DE LOGIN", "erro");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            funcao.Mensagens(this, "Erro ao validar a senha!", "", "ERRO", "erro");
        }
    }
    
    private void mostrarSenha(){
        if (checkMostrarSenha.isSelected()) {
            senhaText.setEchoChar((char) 0);
        } else {
            senhaText.setEchoChar('*');
        }
    }
    
    private void check(){
        if (checkMostrarSenha.isSelected()) {
            checkMostrarSenha.setSelected(false);
        } else {
            checkMostrarSenha.setSelected(true);
        }
        mostrarSenha();
    }
    
    private void sair(){
         funcao.MensagemConfirmar(this, "Você realmente deseja sair do sistema?","","SAIR DO SISTEMA",() -> System.exit(0));
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
        jLabel5 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        botaoSuporte = new javax.swing.JButton();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        checkMostrarSenha = new javax.swing.JCheckBox();
        nomeText = new javax.swing.JTextField();
        botaoRedefinir = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        senhaText = new javax.swing.JPasswordField();
        bordaSalvar = new javax.swing.JPanel();
        botaoEntrar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        botaoSair = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(750, 419));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(210, 164, 2));
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), null));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("CONTROLE DE ESTOQUE");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 230, 360, 20));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoTipoLogin.png"))); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 360, 90));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Fundo.png"))); // NOI18N
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 360, 420));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 360, 420);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEtchedBorder()));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoSuporte.setBackground(new java.awt.Color(204, 204, 204));
        botaoSuporte.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        botaoSuporte.setForeground(new java.awt.Color(0, 0, 0));
        botaoSuporte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoSuporte.png"))); // NOI18N
        botaoSuporte.setText("SUPORTE");
        botaoSuporte.setToolTipText("Clique para entrar em contato com o suporte");
        botaoSuporte.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botaoSuporte.setBorderPainted(false);
        botaoSuporte.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoSuporte.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoSuporte.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoSuporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSuporteActionPerformed(evt);
            }
        });
        jPanel2.add(botaoSuporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, 40, 40));

        jDesktopPane1.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane1.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        jDesktopPane1.setToolTipText("");

        checkMostrarSenha.setBackground(new java.awt.Color(255, 255, 255));
        checkMostrarSenha.setFont(new java.awt.Font("Lucida Console", 0, 10)); // NOI18N
        checkMostrarSenha.setForeground(new java.awt.Color(0, 0, 0));
        checkMostrarSenha.setText("MOSTRAR SENHA");
        checkMostrarSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkMostrarSenhaActionPerformed(evt);
            }
        });
        jDesktopPane1.add(checkMostrarSenha);
        checkMostrarSenha.setBounds(160, 130, 110, 19);

        nomeText.setBackground(new java.awt.Color(255, 255, 255));
        nomeText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nomeText.setForeground(new java.awt.Color(0, 0, 0));
        nomeText.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jDesktopPane1.add(nomeText);
        nomeText.setBounds(50, 40, 210, 20);

        botaoRedefinir.setBackground(new java.awt.Color(204, 204, 204));
        botaoRedefinir.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        botaoRedefinir.setForeground(new java.awt.Color(0, 0, 0));
        botaoRedefinir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/REDEFINIRsenha.png"))); // NOI18N
        botaoRedefinir.setText("Clique Aqui");
        botaoRedefinir.setToolTipText("Clique para ajuda com senha ou usuário");
        botaoRedefinir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botaoRedefinir.setBorderPainted(false);
        botaoRedefinir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoRedefinir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRedefinirActionPerformed(evt);
            }
        });
        jDesktopPane1.add(botaoRedefinir);
        botaoRedefinir.setBounds(180, 210, 110, 30);
        botaoRedefinir.getAccessibleContext().setAccessibleDescription("");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Esqueceu Senha ou Usuário?");
        jDesktopPane1.add(jLabel6);
        jLabel6.setBounds(18, 220, 160, 15);

        senhaText.setBackground(new java.awt.Color(255, 255, 255));
        senhaText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        senhaText.setForeground(new java.awt.Color(0, 0, 0));
        senhaText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                senhaTextActionPerformed(evt);
            }
        });
        jDesktopPane1.add(senhaText);
        senhaText.setBounds(50, 100, 210, 23);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoEntrar.setBackground(new java.awt.Color(210, 164, 2));
        botaoEntrar.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        botaoEntrar.setForeground(new java.awt.Color(0, 0, 0));
        botaoEntrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ENTRAR.png"))); // NOI18N
        botaoEntrar.setText("ENTRAR");
        botaoEntrar.setToolTipText("Clique para entrar na aplicação");
        botaoEntrar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoEntrar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoEntrar.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botaoEntrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoEntrarActionPerformed(evt);
            }
        });
        bordaSalvar.add(botaoEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 230, 40));

        jDesktopPane1.add(bordaSalvar);
        bordaSalvar.setBounds(40, 160, 230, 40);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel7.setText("SENHA:");
        jDesktopPane1.add(jLabel7);
        jLabel7.setBounds(50, 78, 210, 20);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel8.setText("USUÁRIO:");
        jDesktopPane1.add(jLabel8);
        jLabel8.setBounds(50, 18, 210, 20);

        jPanel2.add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 310, 250));
        jDesktopPane1.getAccessibleContext().setAccessibleName("");

        botaoSair.setBackground(new java.awt.Color(204, 0, 0));
        botaoSair.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoSair.setForeground(new java.awt.Color(255, 255, 255));
        botaoSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/FECHAR.png"))); // NOI18N
        botaoSair.setText("SAIR");
        botaoSair.setToolTipText("Clique para sair da aplicação");
        botaoSair.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        botaoSair.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSairActionPerformed(evt);
            }
        });
        jPanel2.add(botaoSair, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 350, 60, 60));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuarioLogin.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 50));

        jLabel2.setFont(new java.awt.Font("Arial", 1, 26)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("LOGIN");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 390, -1));

        getContentPane().add(jPanel2);
        jPanel2.setBounds(360, 0, 390, 420);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void senhaTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_senhaTextActionPerformed
        entrar();
    }//GEN-LAST:event_senhaTextActionPerformed
 
    private void botaoSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSairActionPerformed
        sair();
    }//GEN-LAST:event_botaoSairActionPerformed

    private void checkMostrarSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkMostrarSenhaActionPerformed
         mostrarSenha();
    }//GEN-LAST:event_checkMostrarSenhaActionPerformed

    private void botaoEntrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoEntrarActionPerformed
        entrar();
    }//GEN-LAST:event_botaoEntrarActionPerformed

    private void botaoRedefinirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRedefinirActionPerformed
        RecuperarSenhaEmail dialog = new RecuperarSenhaEmail((Frame) this, true);
        OverlayUtil.abrirTela((JFrame) this, dialog);
    }//GEN-LAST:event_botaoRedefinirActionPerformed

    private void botaoSuporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSuporteActionPerformed
       Suporte dialog = new Suporte((Frame) this, true);
       OverlayUtil.abrirTela((JFrame) this, dialog);
    }//GEN-LAST:event_botaoSuporteActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoEntrar;
    private javax.swing.JButton botaoRedefinir;
    private javax.swing.JButton botaoSair;
    private javax.swing.JButton botaoSuporte;
    private javax.swing.JCheckBox checkMostrarSenha;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField nomeText;
    private javax.swing.JPasswordField senhaText;
    // End of variables declaration//GEN-END:variables
}
