/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tela.Relatorio;

import Classes.BalançoFinanceiro;
import Classes.Funcoes;
import Classes.Funcoes.TipoArquivo;
import Classes.UsuarioAtual;
import Tela.Inicio.MenuPrincipal;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import javax.swing.JRootPane;
import static javax.swing.SwingConstants.CENTER;

@SuppressWarnings("serial")
public class RelatorioBalancoFinanceiro extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes(); 
    private final UsuarioAtual usuarioAtual = new UsuarioAtual();   
    
    public RelatorioBalancoFinanceiro() {
        setUndecorated(true);
        initComponents();
        funcao.aplicarEfeitoMouse(botaoExportarExcel);
        funcao.aplicarEfeitoMouse(botaoExportarPDF);
        funcao.aplicarEfeitoMouse(botaoMenu);      
        funcao.aplicarEfeitoMouse(botaoLimpar);
        adicionarAtalhosTeclado();
        adicionarLegendas();
        montarDataInicial();
        montarDataFinal();
        adicionarValidacoes();
        atualizarBalanço();
        verificarPermissoes();
    }
    
    private void verificarPermissoes() {
        if (!usuarioAtual.getTipo()){
            int usuarioAtualID = usuarioAtual.getCodigo();
            funcao.verificarPermissao(botaoExportarExcel,usuarioAtualID, "Exportar Arquivos",this);
            funcao.verificarPermissao(botaoExportarPDF,usuarioAtualID, "Exportar Arquivos",this);           
        } 
        adicionarAtalhosTeclado();
        adicionarLegendas();
    }
    
    private void montarDataInicial() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1); // 1 mês antes de hoje
        dataInicial.setDate(cal.getTime());
        dataInicial.setDateFormatString("dd/MM/yyyy");
        dataInicial.setMaxSelectableDate(new Date());

        JTextFieldDateEditor editor = (JTextFieldDateEditor) dataInicial.getDateEditor();
        editor.setToolTipText("Selecione data inicial da pesquisa");
        editor.setEditable(false);
        editor.setForeground(Color.BLACK);
        editor.setBackground(Color.WHITE);
        editor.setHorizontalAlignment(CENTER);
    }

    private void montarDataFinal() {
        Date hoje = new Date();
        dataFinal.setDate(hoje);
        dataFinal.setDateFormatString("dd/MM/yyyy");
        dataFinal.setMaxSelectableDate(hoje);

        JTextFieldDateEditor editor = (JTextFieldDateEditor) dataFinal.getDateEditor();
        editor.setToolTipText("Selecione data final da pesquisa");
        editor.setEditable(false);
        editor.setForeground(Color.BLACK);
        editor.setBackground(Color.WHITE);
        editor.setHorizontalAlignment(CENTER);
    }

    private void adicionarValidacoes() {
        dataInicial.getDateEditor().addPropertyChangeListener("date", evt -> {
            if (dataFinal.getDate() != null && dataInicial.getDate() != null) {
                if (dataFinal.getDate().before(dataInicial.getDate())) {
                    dataFinal.setDate(dataInicial.getDate()); // força final >= inicial
                }
                atualizarBalanço();
            }
        });

        dataFinal.getDateEditor().addPropertyChangeListener("date", evt -> {
            if (dataFinal.getDate() != null && dataInicial.getDate() != null) {
                if (dataFinal.getDate().before(dataInicial.getDate())) {
                    dataInicial.setDate(dataFinal.getDate()); // ajusta inicial se final < inicial
                }
                atualizarBalanço();
            }
        });
    }

    
    private void atualizarBalanço() {
        Date dataInicio = dataInicial.getDate();
        Date dataFim = dataFinal.getDate();

        if (dataInicial == null || dataFim == null) {
            // Exiba uma mensagem de erro ou retorne se as datas não estiverem selecionadas
            return;
        }

        BigDecimal totalVendas = BalançoFinanceiro.calcularTotalVendas(dataInicio, dataFim);
        BigDecimal totalGastos = BalançoFinanceiro.calcularTotalReposicoes(dataInicio, dataFim);

        BigDecimal balanco = totalVendas.subtract(totalGastos);
        DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));

        totalVendasText.setText(formatoDecimal.format(totalVendas));
        totalGastosText.setText(formatoDecimal.format(totalGastos));
        balancoTotalText.setText(formatoDecimal.format(balanco));

        if (balanco.compareTo(BigDecimal.ZERO) > 0) {
            balancoTotalText.setForeground(new Color(0, 102, 0));
        } else if (balanco.compareTo(BigDecimal.ZERO) < 0) {
            balancoTotalText.setForeground(new Color(204, 0, 0));
        } else {
            balancoTotalText.setForeground(Color.BLACK);
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
        funcao.validarBotaoComDica(botaoExportarExcel, "Clique para exportar dados para excel");
        funcao.validarBotaoComDica(botaoExportarPDF, "Clique para exportar dados para pdf");
    }
    
    private void exportar(TipoArquivo tipo) {
        Date dataInicio = dataInicial.getDate();
        Date dataFim = dataFinal.getDate();

        if (dataInicio == null || dataFim == null) {
            return;
        }

        BigDecimal totalVendas = BalançoFinanceiro.calcularTotalVendas(dataInicio, dataFim);
        BigDecimal totalGastos = BalançoFinanceiro.calcularTotalReposicoes(dataInicio, dataFim);
        BigDecimal balanco = totalVendas.subtract(totalGastos);

        funcao.exportarBalançoFinanceiro(totalVendas, totalGastos, balanco, tipo, this);
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
        totalGastosText = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        balancoTotalText = new javax.swing.JFormattedTextField();
        totalVendasText = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jDesktopPane6 = new javax.swing.JDesktopPane();
        jLabel8 = new javax.swing.JLabel();
        bordaExportaPDF = new javax.swing.JPanel();
        botaoExportarPDF = new javax.swing.JButton();
        bordaSelecionar2 = new javax.swing.JPanel();
        botaoExportarExcel = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        bordaLimpar = new javax.swing.JPanel();
        botaoLimpar = new javax.swing.JButton();
        painelDataReposicao = new javax.swing.JDesktopPane();
        jLabel22 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        dataInicial = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        dataFinal = new com.toedter.calendar.JDateChooser();

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

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Relatorio1.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("BALANÇO FINANCEIRO");
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
        jDesktopPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "VALORES", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 28), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane3.setForeground(new java.awt.Color(0, 102, 153));
        jDesktopPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/valores.png"))); // NOI18N
        jLabel17.setOpaque(true);
        jDesktopPane3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(142, 3, 30, 30));

        totalGastosText.setEditable(false);
        totalGastosText.setBackground(new java.awt.Color(255, 255, 255));
        totalGastosText.setForeground(new java.awt.Color(204, 0, 0));
        totalGastosText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalGastosText.setText("0,00");
        totalGastosText.setToolTipText("");
        totalGastosText.setFocusable(false);
        totalGastosText.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        totalGastosText.setOpaque(true);
        totalGastosText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                totalGastosTextKeyTyped(evt);
            }
        });
        jDesktopPane3.add(totalGastosText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 140, 260, -1));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Custo de Reposição (R$)");
        jDesktopPane3.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 310, 30));

        balancoTotalText.setEditable(false);
        balancoTotalText.setBackground(new java.awt.Color(255, 255, 255));
        balancoTotalText.setForeground(new java.awt.Color(0, 0, 0));
        balancoTotalText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        balancoTotalText.setText("0,00");
        balancoTotalText.setToolTipText("");
        balancoTotalText.setFocusable(false);
        balancoTotalText.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        balancoTotalText.setOpaque(true);
        balancoTotalText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                balancoTotalTextKeyTyped(evt);
            }
        });
        jDesktopPane3.add(balancoTotalText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 210, 260, -1));

        totalVendasText.setEditable(false);
        totalVendasText.setBackground(new java.awt.Color(255, 255, 255));
        totalVendasText.setForeground(new java.awt.Color(0, 102, 0));
        totalVendasText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        totalVendasText.setText("0,00");
        totalVendasText.setToolTipText("");
        totalVendasText.setFocusable(false);
        totalVendasText.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        totalVendasText.setOpaque(true);
        totalVendasText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                totalVendasTextKeyTyped(evt);
            }
        });
        jDesktopPane3.add(totalVendasText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 260, -1));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Balanço do Período (R$)");
        jDesktopPane3.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, 310, 30));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Receita de Pedidos (R$)");
        jDesktopPane3.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 310, 30));

        jPanel1.add(jDesktopPane3);
        jDesktopPane3.setBounds(216, 250, 640, 300);

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

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "SELECIONE PERIODO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel22.setBackground(new java.awt.Color(255, 255, 255));
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/DATA.png"))); // NOI18N
        jLabel22.setOpaque(true);
        painelDataReposicao.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 0, 30, 30));

        jLabel13.setFont(new java.awt.Font("Century Gothic", 1, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("De:");
        painelDataReposicao.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 50, 60, 30));

        dataInicial.setBackground(new java.awt.Color(255, 255, 255));
        dataInicial.setForeground(new java.awt.Color(0, 0, 0));
        dataInicial.setToolTipText("");
        dataInicial.setDateFormatString("");
        dataInicial.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        dataInicial.setOpaque(false);
        painelDataReposicao.add(dataInicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 210, 30));

        jLabel14.setFont(new java.awt.Font("Century Gothic", 1, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Até:");
        painelDataReposicao.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 50, 60, 30));

        dataFinal.setBackground(new java.awt.Color(255, 255, 255));
        dataFinal.setForeground(new java.awt.Color(0, 0, 0));
        dataFinal.setToolTipText("");
        dataFinal.setDateFormatString("");
        dataFinal.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        dataFinal.setOpaque(false);
        painelDataReposicao.add(dataFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50, 210, 30));

        jPanel1.add(painelDataReposicao);
        painelDataReposicao.setBounds(216, 110, 640, 110);

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

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        funcao.trocarDeTela(this, new MenuPrincipal());
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void botaoExportarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExportarPDFActionPerformed
        exportar(TipoArquivo.PDF);
    }//GEN-LAST:event_botaoExportarPDFActionPerformed

    private void botaoExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExportarExcelActionPerformed
        exportar(TipoArquivo.EXCEL);
    }//GEN-LAST:event_botaoExportarExcelActionPerformed

    private void totalGastosTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalGastosTextKeyTyped

    }//GEN-LAST:event_totalGastosTextKeyTyped

    private void botaoLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparActionPerformed
        montarDataInicial();
        montarDataFinal();
    }//GEN-LAST:event_botaoLimparActionPerformed

    private void balancoTotalTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_balancoTotalTextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_balancoTotalTextKeyTyped

    private void totalVendasTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalVendasTextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_totalVendasTextKeyTyped

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
            java.util.logging.Logger.getLogger(RelatorioBalancoFinanceiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RelatorioBalancoFinanceiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RelatorioBalancoFinanceiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RelatorioBalancoFinanceiro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RelatorioBalancoFinanceiro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField balancoTotalText;
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
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JDesktopPane painelDataReposicao;
    private javax.swing.JFormattedTextField totalGastosText;
    private javax.swing.JFormattedTextField totalVendasText;
    // End of variables declaration//GEN-END:variables
}
