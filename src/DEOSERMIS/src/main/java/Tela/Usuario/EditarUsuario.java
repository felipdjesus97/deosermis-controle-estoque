
package Tela.Usuario;

import Classes.Funcoes;
import Classes.OverlayUtil;
import Classes.Usuario;
import Classes.UsuarioAtual;
import Classes.UsuarioNovo;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JRootPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class EditarUsuario extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final UsuarioNovo usuarioNovo = new UsuarioNovo();
    private final Usuario usuario = new Usuario();
    
     public EditarUsuario() {
        setUndecorated(true);
        initComponents();
        nomeText.setText(" "+usuarioNovo.getNome());
        emailText.setText(" "+usuarioNovo.getEmail());
        funcao.configurarTabela(tabelaPermissoes, new Font("Tahoma", Font.BOLD, 14), Color.white, new Color(0, 0, 102));
        funcao.aplicarEfeitoMouse(botaoMenu);  
        funcao.aplicarEfeitoMouse(botaoConfirmar); 
        carregarTabelaPermissoes();
        tabelaPermissoes.setEnabled(!getAdministrador());
        tabelaPermissoes.setOpaque(!getAdministrador());
        adicionarAtalhosTeclado();
        adicionarLegendas();
    }
    
     private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoConfirmar, "Clique para confirmar edição");
    }

     private void funcaoEnabled(){
        tabelaPermissoes.setEnabled(!getAdministrador());
        tabelaPermissoes.setOpaque(!getAdministrador());
        verirficarTipo();
        adicionarAtalhosTeclado();
        adicionarLegendas();
    }
     
     private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoConfirmar);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
    }
     
     private void confirmar(){
         confirmarPermissoes();
         UsuarioAtual.setAcao(() ->  salvarEdicao());
         ConfirmarUsuarioAtual dialog = new ConfirmarUsuarioAtual(this, true);
         OverlayUtil.abrirTela(this, dialog);
         funcao.trocarDeTela(this, new ConsultarUsuario());
     }
     
     private String formatarNomeFuncionalidade(String nomeOriginal) {
        if (nomeOriginal.startsWith("Cad")) {
            return " Cadastrar " + nomeOriginal.substring(3);
        } else if (nomeOriginal.startsWith("Con")) {
            return " Consultar " + nomeOriginal.substring(3);
        } else if (nomeOriginal.startsWith("Reg")) {
            return " Registrar " + nomeOriginal.substring(3);
        } else {
            // padrão para nomes que não seguem o prefixo
            return " "+nomeOriginal;
        }
    }
     
     private void carregarTabelaPermissoes() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Permitir", "Funcionalidade", "Código"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0; // só o checkbox é editável
            }
        };

        try {
            // Lista todas as funcionalidades cadastradas
            List<UsuarioNovo.Funcionalidade> funcionalidades = usuarioNovo.listarFuncionalidades();

            // Adiciona as linhas ao modelo sem marcar ainda
            for (UsuarioNovo.Funcionalidade f : funcionalidades) {
                String nomeFormatado = formatarNomeFuncionalidade(f.getNome());
                model.addRow(new Object[]{false, nomeFormatado, f.getCodigo()});
            }

            // Seta o modelo no JTable
            tabelaPermissoes.setModel(model);

            // Renderizador e editor da coluna checkbox
            tabelaPermissoes.getColumnModel().getColumn(0)
                    .setCellRenderer(tabelaPermissoes.getDefaultRenderer(Boolean.class));
            tabelaPermissoes.getColumnModel().getColumn(0)
                    .setCellEditor(tabelaPermissoes.getDefaultEditor(Boolean.class));

            // Esconder coluna código
            tabelaPermissoes.getColumnModel().getColumn(2).setMinWidth(0);
            tabelaPermissoes.getColumnModel().getColumn(2).setMaxWidth(0);
            tabelaPermissoes.getColumnModel().getColumn(2).setWidth(0);

            tabelaPermissoes.getColumnModel().getColumn(0).setPreferredWidth(80);

            // Seleciona tipo no combo box
            tipoText.setSelectedItem(usuarioNovo.getTipo() ? "Administrador" : "Comum");

            aplicarPermissoesUsuario();

        } catch (Exception ex) {
            ex.printStackTrace();
            funcao.Mensagens(this, "Erro ao carregar permissões!", "", "ERRO", "erro");
        }

        ajustarLarguraColunaPermitir();
     }

     private void marcarCheckbox(int row, boolean marcar) {
        if (row < 0 || row >= tabelaPermissoes.getRowCount()) return;
        tabelaPermissoes.setValueAt(Boolean.valueOf(marcar), row, 0);
     }

     private void aplicarPermissoesUsuario() {
        Map<Integer, Boolean> permissoesDoUsuario = usuarioNovo.getPermissoes();
        DefaultTableModel model = (DefaultTableModel) tabelaPermissoes.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            int codigoFunc = (Integer) model.getValueAt(i, 2);
            boolean permitir = usuarioNovo.getTipo() ? true : permissoesDoUsuario.getOrDefault(codigoFunc, false);
            marcarCheckbox(i, permitir);
        }
     }

     private void selecionarTodosCheckboxes() {
        DefaultTableModel model = (DefaultTableModel) tabelaPermissoes.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(true, i, 0); // Coluna 0 é a de checkbox
        }
    }
     
     private void desmarcarTodosCheckboxes() {
        DefaultTableModel model = (DefaultTableModel) tabelaPermissoes.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 0);
        }
    }

     private void verirficarTipo(){
         if(getAdministrador()){
             selecionarTodosCheckboxes();
         }else{
             desmarcarTodosCheckboxes();
         }
         tabelaPermissoes.clearSelection();
     }
     
     private void ajustarLarguraColunaPermitir() {
        JTableHeader header = tabelaPermissoes.getTableHeader();
        TableColumn coluna = tabelaPermissoes.getColumnModel().getColumn(0); // coluna "Permitir"

        // Usa o FontMetrics para calcular a largura da palavra "Permitir"
        FontMetrics fm = header.getFontMetrics(header.getFont());
        int larguraTexto = fm.stringWidth("Permitir");

        
        int larguraFinal = larguraTexto + 12;

        // Define a largura fixa da coluna
        coluna.setMinWidth(larguraFinal);
        coluna.setMaxWidth(larguraFinal);
        coluna.setPreferredWidth(larguraFinal);
    }
     
     private void confirmarPermissoes() {
        DefaultTableModel model = (DefaultTableModel) tabelaPermissoes.getModel();
        Map<Integer, Boolean> permissoes = new HashMap<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean permitir = (Boolean) model.getValueAt(i, 0); // checkbox
            int funcionalidadeID = (Integer) model.getValueAt(i, 2); // código da funcionalidade

            permissoes.put(funcionalidadeID, permitir);
        }

        usuarioNovo.setPermissoes(permissoes);
    }
    
     private boolean getAdministrador(){
        return "Administrador".equalsIgnoreCase((String) tipoText.getSelectedItem());
    }

    private void salvarEdicao() {
        usuario.setCodigo(usuarioNovo.getCodigo());   // código recebido da outra tela
        usuario.setTipo(getAdministrador());          // pega se é admin ou não
        usuario.setPermissoes(usuarioNovo.getPermissoes());

        if (usuario.atualizarPermissoesETipo()) {
            funcao.Mensagens(this, "Usuário atualizado com sucesso!", "", "SUCESSO", "informacao");
        } else {
            funcao.Mensagens(this, "Erro ao atualizar usuário.", "", "ERRO", "erro");
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
        IconeTitulo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoConfirmar = new javax.swing.JButton();
        painelDataReposicao = new javax.swing.JDesktopPane();
        jLabel21 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nomeText = new javax.swing.JTextField();
        tipoText = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaPermissoes = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        emailText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        labelAtalhos = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(710, 570));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(210, 164, 2));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        IconeTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/UsuarioEditar.png"))); // NOI18N
        jPanel3.add(IconeTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 50, 50));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("EDITAR USUÁRIO");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 710, -1));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoConfirmar.setBackground(new java.awt.Color(210, 164, 2));
        botaoConfirmar.setFont(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        botaoConfirmar.setForeground(new java.awt.Color(0, 0, 0));
        botaoConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONFIRMAR.png"))); // NOI18N
        botaoConfirmar.setText("CONFIRMAR EDIÇÃO");
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

        jPanel1.add(bordaSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(14, 500, 91, 62));

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DADOS DO USUÁRIO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuarioDados.png"))); // NOI18N
        jLabel21.setOpaque(true);
        painelDataReposicao.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 0, 30, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Permissão:");
        painelDataReposicao.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, 360, 30));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Usuário:");
        painelDataReposicao.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 80, 30));

        nomeText.setEditable(false);
        nomeText.setBackground(new java.awt.Color(255, 255, 255));
        nomeText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        nomeText.setForeground(new java.awt.Color(51, 51, 51));
        nomeText.setFocusable(false);
        nomeText.setOpaque(true);
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
        painelDataReposicao.add(nomeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 360, -1));

        tipoText.setBackground(new java.awt.Color(255, 255, 255));
        tipoText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tipoText.setForeground(new java.awt.Color(0, 0, 0));
        tipoText.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Comum", "Administrador" }));
        tipoText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoTextActionPerformed(evt);
            }
        });
        painelDataReposicao.add(tipoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, 120, 30));

        tabelaPermissoes.setBackground(new java.awt.Color(255, 255, 255));
        tabelaPermissoes.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaPermissoes.setForeground(new java.awt.Color(0, 0, 0));
        tabelaPermissoes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Permitir", "Funcionalidade"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaPermissoes.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaPermissoes.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaPermissoes.getTableHeader().setReorderingAllowed(false);
        tabelaPermissoes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaPermissoesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaPermissoes);

        painelDataReposicao.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 190, 360, 200));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Tipo:");
        painelDataReposicao.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 150, 80, 30));

        emailText.setEditable(false);
        emailText.setBackground(new java.awt.Color(255, 255, 255));
        emailText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        emailText.setForeground(new java.awt.Color(51, 51, 51));
        emailText.setFocusable(false);
        emailText.setOpaque(true);
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
        painelDataReposicao.add(emailText, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 360, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("E-mail:");
        painelDataReposicao.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 80, 30));

        jPanel1.add(painelDataReposicao, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 100, 490, 400));

        labelAtalhos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        labelAtalhos.setForeground(new java.awt.Color(0, 102, 0));
        labelAtalhos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAtalhos.setText("ATALHOS: F1 = Confirmar Edição de Permissão do Usuário | F12 = Voltar Para Tela Consultar Usuário");
        jPanel1.add(labelAtalhos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 710, -1));

        botaoMenu.setBackground(new java.awt.Color(204, 0, 0));
        botaoMenu.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoMenu.setForeground(new java.awt.Color(255, 255, 255));
        botaoMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/VOLTAR.png"))); // NOI18N
        botaoMenu.setText("VOLTAR");
        botaoMenu.setToolTipText("Clique para voltar a consultar usuário");
        botaoMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        botaoMenu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoMenu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoMenu.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoMenuActionPerformed(evt);
            }
        });
        jPanel1.add(botaoMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 500, 60, 60));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 710, 570);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConfirmarActionPerformed
        confirmar();
    }//GEN-LAST:event_botaoConfirmarActionPerformed

    private void nomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeTextActionPerformed

    private void nomeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeTextKeyReleased

    }//GEN-LAST:event_nomeTextKeyReleased

    private void tipoTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoTextActionPerformed
        funcaoEnabled();
    }//GEN-LAST:event_tipoTextActionPerformed

    private void tabelaPermissoesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaPermissoesMouseClicked

    }//GEN-LAST:event_tabelaPermissoesMouseClicked

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        funcao.trocarDeTela(this, new ConsultarUsuario());
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextActionPerformed

    private void emailTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextKeyReleased

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
            java.util.logging.Logger.getLogger(EditarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EditarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EditarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EditarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditarUsuario().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconeTitulo;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoConfirmar;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JTextField emailText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelAtalhos;
    private javax.swing.JTextField nomeText;
    private javax.swing.JDesktopPane painelDataReposicao;
    private javax.swing.JTable tabelaPermissoes;
    private javax.swing.JComboBox<String> tipoText;
    // End of variables declaration//GEN-END:variables
}
