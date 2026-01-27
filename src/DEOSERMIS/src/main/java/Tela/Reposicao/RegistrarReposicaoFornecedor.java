/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Tela.Reposicao;


import Classes.Fornecedor;
import Classes.FornecedorReposicao;
import Classes.Funcoes;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.math.BigInteger;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class RegistrarReposicaoFornecedor extends javax.swing.JDialog {
    private final Funcoes funcao = new Funcoes();
    private final Fornecedor fornecedor = new Fornecedor();
    private final FornecedorReposicao fornecedorReposicao = new FornecedorReposicao();
    
    public RegistrarReposicaoFornecedor(Window parent, boolean modal) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        setUndecorated(true);
        setResizable(false);
        initComponents();
        carregarFornecedores();
        montarTela();
    }

    private void montarTela() {
        funcao.configurarTabela(tabelaFornecedores, new Font("Tahoma", Font.BOLD, 14), Color.white, new Color(0, 0, 102));
        funcao.aplicarEfeitoMouse(botaoSalvar);
        funcao.aplicarEfeitoMouse(botaoSair);
        botaoSalvar.setEnabled(false);
        botaoSalvar.setOpaque(false);
    }

    private void carregarFornecedores() {
        DefaultTableModel model = new DefaultTableModel(
            new String[] {"Selecionar", "Nome", "ID"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        for (Object[] linha : fornecedor.carregarFornecedoresProduto(this)) {
            model.addRow(linha);
        }

        tabelaFornecedores.setModel(model);

        tabelaFornecedores.getColumnModel().getColumn(2).setMinWidth(0);
        tabelaFornecedores.getColumnModel().getColumn(2).setMaxWidth(0);
        tabelaFornecedores.getColumnModel().getColumn(2).setWidth(0);

        tabelaFornecedores.getColumnModel().getColumn(0).setMinWidth(80);
        tabelaFornecedores.getColumnModel().getColumn(0).setMaxWidth(80);
        tabelaFornecedores.getColumnModel().getColumn(0).setPreferredWidth(80);

        TableColumn colunaSelecionar = tabelaFornecedores.getColumnModel().getColumn(0);
        colunaSelecionar.setCellRenderer(new RadioButtonRenderer());
        colunaSelecionar.setCellEditor(new RadioButtonEditor(new JCheckBox()));
    }

    private class RadioButtonRenderer extends JRadioButton implements TableCellRenderer {
        public RadioButtonRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setSelected(value != null && (Boolean) value);
            return this;
        }
    }

    private class RadioButtonEditor extends DefaultCellEditor {
        private JRadioButton radioButton;

        public RadioButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            radioButton = new JRadioButton();
            radioButton.setHorizontalAlignment(SwingConstants.CENTER);
            radioButton.addActionListener(e -> {
                JTable table = tabelaFornecedores;
                int rowCount = table.getRowCount();

                for (int i = 0; i < rowCount; i++) {
                    if (i != table.getEditingRow()) {
                        table.setValueAt(false, i, 0);
                    }
                }

                int editingRow = table.getEditingRow();
                table.setValueAt(true, editingRow, 0);

                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            radioButton.setSelected((Boolean) value);
            return radioButton;
        }

        @Override
        public Object getCellEditorValue() {
            return radioButton.isSelected();
        }
    }
    
    private void sair(){
        funcao.MensagemConfirmarJDialog(this, "Você realmente deseja voltar sem confirmar?","","VOLTAR PARA REGISTRAR REPOSIÇÃO",() -> this.dispose());   
    }
    
    private void funcaoEnabled() {
        boolean fornecedorSelecionado = false;
        // Percorre a tabela para verificar se alguma linha está selecionada
        for (int i = 0; i < tabelaFornecedores.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tabelaFornecedores.getValueAt(i, 0);
            if (Boolean.TRUE.equals(isSelected)) {
                fornecedorSelecionado = true;
                break; // Se encontrou um, já pode sair do loop
            }
        }
        
        botaoSalvar.setOpaque(fornecedorSelecionado);
        botaoSalvar.setEnabled(fornecedorSelecionado);
        funcao.validarBotaoComDica(botaoSalvar,"Clique para salvar a seleção de fornecedor");
    }
    
    private void salvarSelecao(){
        
        int selectedRow = -1;
        for (int i = 0; i < tabelaFornecedores.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tabelaFornecedores.getValueAt(i, 0);
            if (isSelected != null && isSelected) {
                selectedRow = i;
                break; 
            }
        }

        if (selectedRow != -1) {
            Object idFornecedor = tabelaFornecedores.getValueAt(selectedRow, 2); // coluna ID
            Object nomeFornecedor = tabelaFornecedores.getValueAt(selectedRow, 1); // coluna Nome

            fornecedorReposicao.setCodigo((BigInteger) idFornecedor);
            fornecedorReposicao.setNome((String) nomeFornecedor);
            this.dispose();
        } else {
            funcao.MensagensJDialog(RegistrarReposicaoFornecedor.this, "Selecione um fornecedor!", "", "ATENÇÃO", "aviso");
            funcaoEnabled();
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
        jLabel6 = new javax.swing.JLabel();
        botaoSair = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaFornecedores = new javax.swing.JTable();
        bordaSalvar = new javax.swing.JPanel();
        botaoSalvar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(706, 475));
        setSize(new java.awt.Dimension(706, 475));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(210, 164, 2));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("SELECIONAR FORNECEDOR");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 710, -1));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(2, 3, 708, 60);

        botaoSair.setBackground(new java.awt.Color(204, 0, 0));
        botaoSair.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoSair.setForeground(new java.awt.Color(255, 255, 255));
        botaoSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/VOLTAR.png"))); // NOI18N
        botaoSair.setText("VOLTAR");
        botaoSair.setToolTipText("Clique para voltar a registrar pedido");
        botaoSair.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        botaoSair.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSairActionPerformed(evt);
            }
        });
        jPanel1.add(botaoSair);
        botaoSair.setBounds(640, 410, 60, 60);

        tabelaFornecedores.setBackground(new java.awt.Color(255, 255, 255));
        tabelaFornecedores.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaFornecedores.setForeground(new java.awt.Color(0, 0, 0));
        tabelaFornecedores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Selecionar", "Nome"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaFornecedores.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaFornecedores.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaFornecedores.getTableHeader().setReorderingAllowed(false);
        tabelaFornecedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaFornecedoresMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaFornecedores);
        if (tabelaFornecedores.getColumnModel().getColumnCount() > 0) {
            tabelaFornecedores.getColumnModel().getColumn(0).setResizable(false);
            tabelaFornecedores.getColumnModel().getColumn(1).setResizable(false);
        }

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(50, 80, 610, 310);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoSalvar.setBackground(new java.awt.Color(210, 164, 2));
        botaoSalvar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        botaoSalvar.setForeground(new java.awt.Color(0, 0, 0));
        botaoSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONFIRMAR.png"))); // NOI18N
        botaoSalvar.setText("CONFIRMAR");
        botaoSalvar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoSalvar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        botaoSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSalvarActionPerformed(evt);
            }
        });
        bordaSalvar.add(botaoSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaSalvar);
        bordaSalvar.setBounds(14, 410, 91, 62);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-3, -4, 710, 480));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
              
    private void botaoSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSairActionPerformed
         sair();
    }//GEN-LAST:event_botaoSairActionPerformed

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        salvarSelecao();
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void tabelaFornecedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaFornecedoresMouseClicked
        funcaoEnabled();
    }//GEN-LAST:event_tabelaFornecedoresMouseClicked

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
            java.util.logging.Logger.getLogger(RegistrarReposicaoFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistrarReposicaoFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistrarReposicaoFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistrarReposicaoFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                RegistrarReposicaoFornecedor dialog = new RegistrarReposicaoFornecedor(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoSair;
    private javax.swing.JButton botaoSalvar;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabelaFornecedores;
    // End of variables declaration//GEN-END:variables
}
