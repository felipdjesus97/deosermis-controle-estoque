/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tela.Relatorio;

import Classes.Funcoes;
import Classes.PedidoProduto;
import Classes.UsuarioAtual;
import Tela.Inicio.MenuPrincipal;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class RelatorioResumoDePedido extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final UsuarioAtual usuarioAtual = new UsuarioAtual();    
    
    public RelatorioResumoDePedido() {
        setUndecorated(true);
        initComponents();
        
        funcao.configurarTabela(tabelaResumoPedidos, new Font("Tahoma", Font.BOLD, 14), Color.BLACK, new Color(200, 200, 200));
        funcao.aplicarEfeitoMouse(botaoExportarExcel);
        funcao.aplicarEfeitoMouse(botaoExportarPDF);
        funcao.aplicarEfeitoMouse(botaoMenu);      
        funcao.aplicarEfeitoMouse(botaoLimpar);

        adicionarAtalhosTeclado();
        adicionarLegendas();
        montarDatas();
        adicionarValidacoes();
        carregarPedidos();
        verificarPermissoes();
    }
    
    private void verificarPermissoes() {
        if (!usuarioAtual.getTipo()) {
            final int usuarioAtualID = usuarioAtual.getCodigo();
            funcao.verificarPermissao(botaoExportarExcel, usuarioAtualID, "Exportar Arquivos", this);
            funcao.verificarPermissao(botaoExportarPDF, usuarioAtualID, "Exportar Arquivos", this);
        }
    }

    private void montarDatas() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        configurarData(dataInicial, cal.getTime(), new Date(), "Selecione data inicial da pesquisa");

        Date hoje = new Date();
        configurarData(dataFinal, hoje, hoje, "Selecione data final da pesquisa");
    }

    private void configurarData(JDateChooser dateChooser, Date data, Date max, String dica) {
        dateChooser.setDate(data);
        dateChooser.setDateFormatString("dd/MM/yyyy");
        dateChooser.setMaxSelectableDate(max);

        JTextFieldDateEditor editor = (JTextFieldDateEditor) dateChooser.getDateEditor();
        editor.setToolTipText(dica);
        editor.setEditable(false);
        editor.setForeground(Color.BLACK);
        editor.setBackground(Color.WHITE);
        editor.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void adicionarValidacoes() {
        dataInicial.getDateEditor().addPropertyChangeListener("date", evt -> validarIntervaloDatas());
        dataFinal.getDateEditor().addPropertyChangeListener("date", evt -> validarIntervaloDatas());
    }

    private void validarIntervaloDatas() {
        if (dataInicial.getDate() != null && dataFinal.getDate() != null) {
            if (dataFinal.getDate().before(dataInicial.getDate())) {
                dataFinal.setDate(dataInicial.getDate());
            }
            carregarPedidos();
        }
    }

    private void carregarPedidos() {
        final java.sql.Date inicioSql = new java.sql.Date(dataInicial.getDate().getTime());
        final java.sql.Date fimSql = new java.sql.Date(dataFinal.getDate().getTime());

        List<PedidoProduto> lista = PedidoProduto.buscarPedidos(inicioSql, fimSql, this);

        DefaultTableModel modelo = (DefaultTableModel) tabelaResumoPedidos.getModel();
        modelo.setRowCount(0);

        DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");

        for (PedidoProduto p : lista) {
            modelo.addRow(new Object[]{
                formatoData.format(p.getData()),
                p.getProdutos(),
                formatoDecimal.format(p.getValorSubTotal()),
                p.getDesconto(),
                formatoDecimal.format(p.getValorTotal()),
                p.getPagamentos()
            });
        }

        alinharTabela();
        calcularTotalTabela();
    }
    
    private void calcularTotalTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaResumoPedidos.getModel();
        int colunaTotal = modelo.findColumn("Total");

        BigDecimal totalGeral = BigDecimal.ZERO;
        DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

        if (colunaTotal != -1) {
            for (int i = 0; i < modelo.getRowCount(); i++) {
                try {
                    String valorFormatado = (String) modelo.getValueAt(i, colunaTotal);
                    String valorLimpo = valorFormatado.replace(".", "").replace(",", ".");
                    totalGeral = totalGeral.add(new BigDecimal(valorLimpo));
                } catch (Exception e) {
                    System.err.println("Erro ao somar linha " + i + ": " + e.getMessage());
                }
            }
        }
        valorTotalText.setText(formatoDecimal.format(totalGeral));
    }
    
    private void alinharTabela() {
        DefaultTableCellRenderer centralizadoRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(SwingConstants.CENTER); // centraliza horizontalmente
                label.setVerticalAlignment(SwingConstants.CENTER);   // centraliza verticalmente
                label.setOpaque(true);

                if (value != null && value.toString().startsWith("<html>")) {
                    // Conta quantas linhas existem no HTML
                    String text = value.toString();
                    String[] partes = text.split("<br>");
                    int linhas = partes.length;

                    int alturaLinha = 20; // altura de cada linha em pixels
                    int alturaTotal = linhas * alturaLinha;

                    // Ajusta altura da linha se necessário
                    if (table.getRowHeight(row) < alturaTotal) {
                        table.setRowHeight(row, alturaTotal);
                    }
                }

                return label;
            }
        };

        for (int i = 0; i < tabelaResumoPedidos.getColumnCount(); i++) {
            tabelaResumoPedidos.getColumnModel().getColumn(i).setCellRenderer(centralizadoRenderer);
        }
    }
    
    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F7", botaoExportarExcel);
        funcao.adicionarAtalho(root, "F8", botaoExportarPDF);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
        funcao.adicionarAtalho(root, "F5", botaoLimpar);
    }
    
    private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoExportarExcel, "Clique para exportar tabela para Excel");
        funcao.validarBotaoComDica(botaoExportarPDF, "Clique para exportar tabela para PDF");
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
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();
        jDesktopPane3 = new javax.swing.JDesktopPane();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaResumoPedidos = new javax.swing.JTable();
        valorTotalText = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        painelDataReposicao = new javax.swing.JDesktopPane();
        jLabel22 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        dataInicial = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        dataFinal = new com.toedter.calendar.JDateChooser();
        jDesktopPane6 = new javax.swing.JDesktopPane();
        jLabel8 = new javax.swing.JLabel();
        bordaExportaPDF = new javax.swing.JPanel();
        botaoExportarPDF = new javax.swing.JButton();
        bordaSelecionar2 = new javax.swing.JPanel();
        botaoExportarExcel = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        bordaLimpar = new javax.swing.JPanel();
        botaoLimpar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(210, 164, 2));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Relatorio3.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("RESUMO DE PEDIDO");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1070, -1));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 1070, 60);

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
        botaoMenu.setBounds(1002, 630, 60, 60);

        jDesktopPane3.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PESQUISAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 28), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane3.setForeground(new java.awt.Color(0, 102, 153));
        jDesktopPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisarRegistrar.png"))); // NOI18N
        jLabel17.setOpaque(true);
        jDesktopPane3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(177, 2, 30, 30));

        tabelaResumoPedidos.setAutoCreateRowSorter(true);
        tabelaResumoPedidos.setBackground(new java.awt.Color(255, 255, 255));
        tabelaResumoPedidos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaResumoPedidos.setForeground(new java.awt.Color(51, 51, 51));
        tabelaResumoPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data ", "Produto", "Subtotal ", "Desconto ", "Total", "Pagamento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaResumoPedidos.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaResumoPedidos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaResumoPedidos.getTableHeader().setReorderingAllowed(false);
        tabelaResumoPedidos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelaResumoPedidosKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaResumoPedidos);
        if (tabelaResumoPedidos.getColumnModel().getColumnCount() > 0) {
            tabelaResumoPedidos.getColumnModel().getColumn(0).setPreferredWidth(20);
            tabelaResumoPedidos.getColumnModel().getColumn(1).setPreferredWidth(370);
            tabelaResumoPedidos.getColumnModel().getColumn(2).setPreferredWidth(5);
            tabelaResumoPedidos.getColumnModel().getColumn(3).setPreferredWidth(5);
            tabelaResumoPedidos.getColumnModel().getColumn(4).setPreferredWidth(5);
            tabelaResumoPedidos.getColumnModel().getColumn(5).setPreferredWidth(170);
        }

        jDesktopPane3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 1030, 340));

        valorTotalText.setEditable(false);
        valorTotalText.setBackground(new java.awt.Color(255, 255, 255));
        valorTotalText.setForeground(new java.awt.Color(0, 0, 0));
        valorTotalText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorTotalText.setText("0,00");
        valorTotalText.setToolTipText("");
        valorTotalText.setFocusable(false);
        valorTotalText.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        valorTotalText.setOpaque(true);
        valorTotalText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorTotalTextKeyTyped(evt);
            }
        });
        jDesktopPane3.add(valorTotalText, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 465, 190, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("VALOR TOTAL (R$) ");
        jDesktopPane3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 465, 240, 30));

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "SELECIONE PERIODO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 24), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/DATA.png"))); // NOI18N
        jLabel22.setOpaque(true);
        painelDataReposicao.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, -2, 30, 30));

        jLabel13.setFont(new java.awt.Font("Century Gothic", 1, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("De:");
        painelDataReposicao.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 36, 60, 30));

        dataInicial.setBackground(new java.awt.Color(255, 255, 255));
        dataInicial.setForeground(new java.awt.Color(0, 0, 0));
        dataInicial.setToolTipText("");
        dataInicial.setDateFormatString("");
        dataInicial.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        dataInicial.setOpaque(false);
        painelDataReposicao.add(dataInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 36, 210, 30));

        jLabel14.setFont(new java.awt.Font("Century Gothic", 1, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Até:");
        painelDataReposicao.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 36, 60, 30));

        dataFinal.setBackground(new java.awt.Color(255, 255, 255));
        dataFinal.setForeground(new java.awt.Color(0, 0, 0));
        dataFinal.setToolTipText("");
        dataFinal.setDateFormatString("");
        dataFinal.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        dataFinal.setOpaque(false);
        painelDataReposicao.add(dataFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 36, 210, 30));

        jDesktopPane3.add(painelDataReposicao, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 640, 80));

        jPanel1.add(jDesktopPane3);
        jDesktopPane3.setBounds(10, 90, 1050, 510);

        jDesktopPane6.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "EXPORTAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane6.setFocusCycleRoot(false);
        jDesktopPane6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/exportar.png"))); // NOI18N
        jLabel8.setOpaque(true);
        jDesktopPane6.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(154, 0, 30, 30));

        bordaExportaPDF.setBackground(new java.awt.Color(255, 255, 255));
        bordaExportaPDF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaExportaPDF.setForeground(new java.awt.Color(0, 0, 0));
        bordaExportaPDF.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoExportarPDF.setBackground(new java.awt.Color(204, 204, 204));
        botaoExportarPDF.setFont(new java.awt.Font("Impact", 0, 24)); // NOI18N
        botaoExportarPDF.setForeground(new java.awt.Color(0, 0, 0));
        botaoExportarPDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pdf.png"))); // NOI18N
        botaoExportarPDF.setText("PDF");
        botaoExportarPDF.setToolTipText("");
        botaoExportarPDF.setBorder(null);
        botaoExportarPDF.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoExportarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExportarPDFActionPerformed(evt);
            }
        });
        bordaExportaPDF.add(botaoExportarPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 40));

        jDesktopPane6.add(bordaExportaPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 34, 140, 40));

        bordaSelecionar2.setBackground(new java.awt.Color(255, 255, 255));
        bordaSelecionar2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSelecionar2.setForeground(new java.awt.Color(0, 0, 0));
        bordaSelecionar2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoExportarExcel.setBackground(new java.awt.Color(204, 204, 204));
        botaoExportarExcel.setFont(new java.awt.Font("Impact", 0, 24)); // NOI18N
        botaoExportarExcel.setForeground(new java.awt.Color(0, 0, 0));
        botaoExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excel.png"))); // NOI18N
        botaoExportarExcel.setText("Excel");
        botaoExportarExcel.setToolTipText("");
        botaoExportarExcel.setBorder(null);
        botaoExportarExcel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExportarExcelActionPerformed(evt);
            }
        });
        bordaSelecionar2.add(botaoExportarExcel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 40));

        jDesktopPane6.add(bordaSelecionar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 34, 140, 40));

        jPanel1.add(jDesktopPane6);
        jDesktopPane6.setBounds(10, 600, 420, 90);

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("ATALHOS: F5 = Limpar Filtros |  F7 = Exportar Dados para Excel |  F8 = Exportar Dados para PDF | F12 = Voltar ao Menu Principal");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(4, 60, 1060, 16);

        bordaLimpar.setBackground(new java.awt.Color(255, 255, 255));
        bordaLimpar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaLimpar.setForeground(new java.awt.Color(0, 0, 0));
        bordaLimpar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoLimpar.setBackground(new java.awt.Color(204, 204, 204));
        botaoLimpar.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        botaoLimpar.setForeground(new java.awt.Color(0, 0, 0));
        botaoLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        botaoLimpar.setText("LIMPAR FILTROS");
        botaoLimpar.setToolTipText("Clique para limpar os filtros");
        botaoLimpar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoLimpar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoLimpar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoLimpar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        botaoLimpar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLimparActionPerformed(evt);
            }
        });
        bordaLimpar.add(botaoLimpar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaLimpar);
        bordaLimpar.setBounds(740, 630, 91, 62);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1070, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 696, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tabelaResumoPedidosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaResumoPedidosKeyPressed

    }//GEN-LAST:event_tabelaResumoPedidosKeyPressed

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        funcao.trocarDeTela(this, new MenuPrincipal());
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void botaoExportarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExportarPDFActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tabelaResumoPedidos.getModel();
        String totalGeral = valorTotalText.getText();
        funcao.exportarTabelaComTotal(modelo, Funcoes.TipoArquivo.PDF, "ResumoDePedido", this, totalGeral);
    }//GEN-LAST:event_botaoExportarPDFActionPerformed

    private void botaoExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExportarExcelActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tabelaResumoPedidos.getModel();
        String totalGeral = valorTotalText.getText();
        funcao.exportarTabelaComTotal(modelo, Funcoes.TipoArquivo.EXCEL, "ResumoDePedido", this, totalGeral);
    }//GEN-LAST:event_botaoExportarExcelActionPerformed

    private void valorTotalTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorTotalTextKeyTyped

    }//GEN-LAST:event_valorTotalTextKeyTyped

    private void botaoLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparActionPerformed
        montarDatas();
    }//GEN-LAST:event_botaoLimparActionPerformed

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
            java.util.logging.Logger.getLogger(RelatorioResumoDePedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RelatorioResumoDePedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RelatorioResumoDePedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RelatorioResumoDePedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RelatorioResumoDePedido().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bordaExportaPDF;
    private javax.swing.JPanel bordaLimpar;
    private javax.swing.JPanel bordaSelecionar2;
    private javax.swing.JButton botaoExportarExcel;
    private javax.swing.JButton botaoExportarPDF;
    private javax.swing.JButton botaoLimpar;
    private javax.swing.JButton botaoMenu;
    private com.toedter.calendar.JDateChooser dataFinal;
    private com.toedter.calendar.JDateChooser dataInicial;
    private javax.swing.JDesktopPane jDesktopPane3;
    private javax.swing.JDesktopPane jDesktopPane6;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JDesktopPane painelDataReposicao;
    private javax.swing.JTable tabelaResumoPedidos;
    private javax.swing.JFormattedTextField valorTotalText;
    // End of variables declaration//GEN-END:variables
}
