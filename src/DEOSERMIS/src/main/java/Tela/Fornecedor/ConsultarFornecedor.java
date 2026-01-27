
package Tela.Fornecedor;

import Tela.Fornecedor.CadastrarEditarFornecedor;
import Classes.OpcaoDeTela;
import Classes.Fornecedor;
import Classes.Funcoes;
import Classes.UsuarioAtual;
import Tela.Inicio.MenuPrincipal;
import java.awt.Color;
import java.awt.Font;
import java.math.BigInteger;
import java.util.List;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ConsultarFornecedor extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();    
    private final OpcaoDeTela cadastrarEditar = new OpcaoDeTela();
    private final Fornecedor fornecedor = new Fornecedor();
    private final UsuarioAtual usuarioAtual = new UsuarioAtual();
    
    public ConsultarFornecedor() {
        setUndecorated(true);
        initComponents();
        funcao.configurarTabela(tabelaFornecedor, new Font("Tahoma", Font.BOLD, 14), Color.WHITE, new Color(0, 0, 102));
        pesquisarNome();
        pesquisarNomeFornecedorText.requestFocus();
        codigoText.getEditor().getComponent(0).setForeground(Color.BLACK);
        digitacao();
        funcao.aplicarEfeitoMouse(botaoEditar);
        funcao.aplicarEfeitoMouse(botaoExportarExcel);
        funcao.aplicarEfeitoMouse(botaoExportarPDF);
        funcao.aplicarEfeitoMouse(botaoMenu);
        adicionarAtalhosTeclado();
        adicionarLegendas();
        verificarPermissoes();
    }
    
    private void verificarPermissoes() {
        if (!usuarioAtual.getTipo()){
            int usuarioAtualID = usuarioAtual.getCodigo();
            funcao.verificarPermissao(botaoExportarExcel,usuarioAtualID, "Exportar Arquivos",this);
            funcao.verificarPermissao(botaoExportarPDF,usuarioAtualID, "Exportar Arquivos",this);
            funcao.verificarPermissao(botaoEditar,usuarioAtualID, "CadFornecedor",this);
            funcao.verificarPermissaoJSpinner(codigoText,usuarioAtualID, "CadFornecedor",this);
        } 
        adicionarAtalhosTeclado();
        adicionarLegendas();
    }
    
    private void pesquisarNome() {
        String nomeParcial = pesquisarNomeFornecedorText.getText();

        List<Fornecedor> lista = fornecedor.buscarFornecedorPorNome(nomeParcial,this);

        DefaultTableModel modelo = (DefaultTableModel) tabelaFornecedor.getModel();
        modelo.setRowCount(0);

        for (Fornecedor f : lista) {
            // Verifica se o WhatsApp é nulo antes de tentar formatá-lo
            String whatsappFormatado = (f.getWhatsApp() != null) ? funcao.formatWhatsApp(f.getWhatsApp()) : "";

            // Verifica se o CPF/CNPJ é nulo
            String cpfCnpjFormatado = (f.getCPFCNPJ() != null) ? funcao.formatarCpfCnpj(f.getCPFCNPJ()) : "";

            // Verifica se o CEP é nulo
            String cepFormatado = (f.getCEP() != null) ? funcao.formatCEP(f.getCEP()) : "";

            modelo.addRow(new Object[]{
                f.getCodigo(),
                f.getNome(),
                cpfCnpjFormatado,
                f.getEmail(),
                f.getInstagram(),
                whatsappFormatado,
                f.getSite(),
                f.getUF(),
                f.getCidade(),
                f.getBairro(),
                cepFormatado,
                f.getLogradouro(),
                f.getNumero(),
                f.getComplemento(),
                f.getObs()
            });
        }
        alinharTabelaPesquisarProdutos();
    }
    
    private void alinharTabelaPesquisarProdutos() {
        DefaultTableCellRenderer alinhamento = new DefaultTableCellRenderer();
        alinhamento.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabelaFornecedor.getColumnCount(); i++) {
            tabelaFornecedor.getColumnModel().getColumn(i).setCellRenderer(alinhamento);
        }
    }
    
    private void editar(){
        int valor = Integer.parseInt(codigoText.getValue().toString());
        fornecedor.setCodigoAlterar(BigInteger.valueOf(valor));
        if (fornecedor.consultarFornecedor(fornecedor.getCodigoAlterar(),this)) {
            cadastrarEditar.setCadastro(false);
            funcao.trocarDeTela(this, new CadastrarEditarFornecedor());
        }else{
            funcao.Mensagens(this, "Fornecedor não encontrado", "","ERRO", "erro");
        }
        
    }
    
    private void digitacao(){
        funcao.entradaNumerica((JSpinner.DefaultEditor) codigoText.getEditor());
    }
    
    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F7", botaoExportarExcel);
        funcao.adicionarAtalho(root, "F8", botaoExportarPDF);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
        funcao.adicionarAtalho(root, "F1", botaoEditar);
        funcao.adicionarAtalhoTeclado(root, "F2", () -> codigoText.requestFocus());
        funcao.adicionarAtalhoTeclado(root, "F4", () -> pesquisarNomeFornecedorText.requestFocus());
    }
    
    private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoEditar, "Clique para editar fornecedor escolhido");
        funcao.validarBotaoComDica(botaoExportarExcel, "Clique para exportar tabela para excel");
        funcao.validarBotaoComDica(botaoExportarPDF, "Clique para exportar tabela para pdf");
        funcao.validarSpinnerComDica(codigoText, "Escolha o código do fornecedor desejado");
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
        jLabel21 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();
        jDesktopPane3 = new javax.swing.JDesktopPane();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaFornecedor = new javax.swing.JTable();
        pesquisarNomeFornecedorText = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jDesktopPane6 = new javax.swing.JDesktopPane();
        jLabel8 = new javax.swing.JLabel();
        bordaExportaPDF = new javax.swing.JPanel();
        botaoExportarPDF = new javax.swing.JButton();
        bordaSelecionar2 = new javax.swing.JPanel();
        botaoExportarExcel = new javax.swing.JButton();
        jDesktopPane5 = new javax.swing.JDesktopPane();
        jLabel7 = new javax.swing.JLabel();
        codigoText = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        bordaSelecionar = new javax.swing.JPanel();
        botaoEditar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("ATALHOS: F1 = Selecionar Fornecedor para Editar | F2 = Digitar Código Fornecedor | F4 = Pesquisar Nome do Fornecedor");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(4, 60, 1060, 16);

        jPanel2.setBackground(new java.awt.Color(210, 164, 2));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Fornecedor.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("CONSULTAR FORNECEDOR");
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
        jDesktopPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PESQUISAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane3.setForeground(new java.awt.Color(0, 102, 153));
        jDesktopPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisarRegistrar.png"))); // NOI18N
        jLabel17.setOpaque(true);
        jDesktopPane3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(165, 0, 30, 30));

        tabelaFornecedor.setAutoCreateRowSorter(true);
        tabelaFornecedor.setBackground(new java.awt.Color(255, 255, 255));
        tabelaFornecedor.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaFornecedor.setForeground(new java.awt.Color(51, 51, 51));
        tabelaFornecedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "CPF/CNPJ", "E-mail", "Instagram", "WhatsApp", "Site", "UF", "Cidade", "Bairro", "CEP", "Logradouro", "Número", "Complemento", "Observação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaFornecedor.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaFornecedor.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaFornecedor.getTableHeader().setReorderingAllowed(false);
        tabelaFornecedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelaFornecedorKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaFornecedor);
        if (tabelaFornecedor.getColumnModel().getColumnCount() > 0) {
            tabelaFornecedor.getColumnModel().getColumn(0).setPreferredWidth(60);
            tabelaFornecedor.getColumnModel().getColumn(1).setPreferredWidth(150);
            tabelaFornecedor.getColumnModel().getColumn(2).setPreferredWidth(170);
            tabelaFornecedor.getColumnModel().getColumn(5).setPreferredWidth(100);
            tabelaFornecedor.getColumnModel().getColumn(7).setPreferredWidth(30);
            tabelaFornecedor.getColumnModel().getColumn(14).setPreferredWidth(50);
        }

        jDesktopPane3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 1030, 380));

        pesquisarNomeFornecedorText.setBackground(new java.awt.Color(255, 255, 255));
        pesquisarNomeFornecedorText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 18)); // NOI18N
        pesquisarNomeFornecedorText.setForeground(new java.awt.Color(0, 0, 0));
        pesquisarNomeFornecedorText.setToolTipText("Pesquise um ou mais fornecedores por nome");
        pesquisarNomeFornecedorText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pesquisarNomeFornecedorTextKeyReleased(evt);
            }
        });
        jDesktopPane3.add(pesquisarNomeFornecedorText, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 440, 590, -1));

        jLabel12.setFont(new java.awt.Font("Century Gothic", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Nome:");
        jDesktopPane3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 430, 90, 40));

        jPanel1.add(jDesktopPane3);
        jDesktopPane3.setBounds(10, 90, 1050, 480);

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("F7 = Exportar Dados para Excel |  F8 = Exportar Dados para PDF | F12 = Voltar ao Menu Principal");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(0, 66, 1060, 30);

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

        jDesktopPane5.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "EDITAR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane5.setFocusCycleRoot(false);
        jDesktopPane5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/editar.png"))); // NOI18N
        jLabel7.setOpaque(true);
        jDesktopPane5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 0, 30, 30));

        codigoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        codigoText.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(1L), Long.valueOf(1L), Long.valueOf(9223372036854775807L), Long.valueOf(1L)));
        codigoText.setToolTipText("");
        codigoText.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                codigoTextStateChanged(evt);
            }
        });
        codigoText.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                codigoTextInputMethodTextChanged(evt);
            }
        });
        codigoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                codigoTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigoTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                codigoTextKeyTyped(evt);
            }
        });
        jDesktopPane5.add(codigoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 120, -1));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Código:");
        jDesktopPane5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 100, -1));

        bordaSelecionar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSelecionar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSelecionar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSelecionar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoEditar.setBackground(new java.awt.Color(204, 204, 204));
        botaoEditar.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botaoEditar.setForeground(new java.awt.Color(0, 0, 0));
        botaoEditar.setText("SELECIONAR");
        botaoEditar.setToolTipText("");
        botaoEditar.setBorder(null);
        botaoEditar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoEditarActionPerformed(evt);
            }
        });
        bordaSelecionar.add(botaoEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 40));

        jDesktopPane5.add(bordaSelecionar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 140, 40));

        jPanel1.add(jDesktopPane5);
        jDesktopPane5.setBounds(530, 600, 420, 90);

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

    private void tabelaFornecedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaFornecedorKeyPressed

    }//GEN-LAST:event_tabelaFornecedorKeyPressed

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        funcao.trocarDeTela(this, new MenuPrincipal());
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void pesquisarNomeFornecedorTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisarNomeFornecedorTextKeyReleased
        pesquisarNome();
    }//GEN-LAST:event_pesquisarNomeFornecedorTextKeyReleased

    private void botaoEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoEditarActionPerformed
        editar();
    }//GEN-LAST:event_botaoEditarActionPerformed

    private void codigoTextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_codigoTextStateChanged
        digitacao();
    }//GEN-LAST:event_codigoTextStateChanged

    private void codigoTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_codigoTextInputMethodTextChanged

    }//GEN-LAST:event_codigoTextInputMethodTextChanged

    private void codigoTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyPressed

    }//GEN-LAST:event_codigoTextKeyPressed

    private void codigoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyReleased

    }//GEN-LAST:event_codigoTextKeyReleased

    private void codigoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyTyped

    }//GEN-LAST:event_codigoTextKeyTyped

    private void botaoExportarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExportarPDFActionPerformed
       DefaultTableModel modelo = (DefaultTableModel) tabelaFornecedor.getModel();
        funcao.exportarTabela(modelo, Funcoes.TipoArquivo.PDF,"Fornecedor", this);
    }//GEN-LAST:event_botaoExportarPDFActionPerformed

    private void botaoExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExportarExcelActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) tabelaFornecedor.getModel();
        funcao.exportarTabela(modelo, Funcoes.TipoArquivo.EXCEL,"Fornecedor", this); 
    }//GEN-LAST:event_botaoExportarExcelActionPerformed

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
            java.util.logging.Logger.getLogger(ConsultarFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConsultarFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConsultarFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConsultarFornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConsultarFornecedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bordaExportaPDF;
    private javax.swing.JPanel bordaSelecionar;
    private javax.swing.JPanel bordaSelecionar2;
    private javax.swing.JButton botaoEditar;
    private javax.swing.JButton botaoExportarExcel;
    private javax.swing.JButton botaoExportarPDF;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JSpinner codigoText;
    private javax.swing.JDesktopPane jDesktopPane3;
    private javax.swing.JDesktopPane jDesktopPane5;
    private javax.swing.JDesktopPane jDesktopPane6;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField pesquisarNomeFornecedorText;
    private javax.swing.JTable tabelaFornecedor;
    // End of variables declaration//GEN-END:variables
}
