/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Tela.Pedido;

import Classes.Pagamento;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import Classes.CarrinhoPedido;
import Classes.Funcoes;
import Classes.BuscarFormaDePagamento;
import Classes.PedidoProduto;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.DefaultCellEditor;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.math.RoundingMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class FormaDePagamento extends javax.swing.JDialog {
    private final Funcoes funcao = new Funcoes();
    private final BuscarFormaDePagamento buscarFormaDePagamento = new BuscarFormaDePagamento();
    private final DecimalFormat df = new DecimalFormat("0.00");
    private final DefaultTableModel modeloTabela;
    private final List<Pagamento> listaPagamentos = new ArrayList<>();
    
    
    public FormaDePagamento(Window parent, boolean modal) {
         super(parent, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setResizable(false);
        initComponents();
        
        modeloTabela = new DefaultTableModel(new Object[]{"TIPO", "VALOR PAGO", "AÇÃO"}, 0);
        tabelaFormaDePagamento1.setModel(modeloTabela);
        
        ajustarTabela();
        buscarFormaDePagamento.addFP(fpText);
        funcao.aplicarEfeitoMouse(botaoSalvar);
        funcao.aplicarEfeitoMouse(botaoSair);
        funcao.aplicarEfeitoMouse(botaoConfirmarFP);
        montarData();
        atualizarCampos();
    }
    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoSalvar);
        funcao.adicionarAtalho(root, "F8", botaoConfirmarFP);
        funcao.adicionarAtalho(root, "F12", botaoSair);
        funcao.adicionarAtalhoTeclado(root, "F2", () -> valorPagoText.requestFocus());
        funcao.adicionarAtalhoTeclado(root, "F3", () -> atalhoFP("PIX"));      
        funcao.adicionarAtalhoTeclado(root, "F4", () -> atalhoFP("DÉBITO"));
        funcao.adicionarAtalhoTeclado(root, "F5", () -> atalhoFP("CRÉDITO"));  
        funcao.adicionarAtalhoTeclado(root, "F6", () -> atalhoFP("DINHEIRO"));
    }
    
    private void atalhoFP(String nome){
        fpText.setSelectedItem(nome);
    }
    private void valorMax(BigDecimal valorMaximo) {
        try {
            BigDecimal valor = new BigDecimal(valorPagoText.getText().replace(",", "."));
            valor = valor.setScale(2, RoundingMode.HALF_EVEN);

            BigDecimal minimo = new BigDecimal("0.01");

            if (valor.compareTo(minimo) < 0) {
                valorPagoText.setText(String.format("%.2f", minimo));
            } else if (valor.compareTo(valorMaximo) > 0) {
                valorPagoText.setText(String.format("%.2f", valorMaximo));
            } else {
                valorPagoText.setText(String.format("%.2f", valor));
            }
        } catch (NumberFormatException ex) {
            valorPagoText.setText(String.format("%.2f", valorMaximo));
        }
    }

    private void montarData() {
        dataPedido.setDate(new Date());
        dataPedido.setDateFormatString("dd/MM/yyyy");
        dataPedido.setMaxSelectableDate(new Date());
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dataPedido.getDateEditor();
        editor.setToolTipText("Clique para selecionar data do pedido");
        editor.setEditable(false);
        editor.setForeground(Color.BLACK);
        editor.setBackground(Color.WHITE);
        editor.setHorizontalAlignment(CENTER);
        valorTotalDaCompraText.setText(df.format(CarrinhoPedido.getValorTotal()));
        valorPagoText.setText(df.format(CarrinhoPedido.getValorTotal()));
        atualizarEstadoComponentes();
        funcao.entradaNumericaMonetaria(valorPagoText);
    }
    
    private boolean valorMenorQueDigitado(){
        String pg =  valorPagoText.getText();
        if(pg != null && !pg.isEmpty()){
            BigDecimal valorPago = new BigDecimal(valorPagoText.getText().replace(",", "."));
            BigDecimal valorRestante = new BigDecimal(valorRestatanteText.getText().replace(",", "."));
            if (valorPago.compareTo(BigDecimal.ZERO) == 0) {
                return false;
            }
            return valorPago.compareTo(valorRestante) <= 0;
        }
        return false;
    }
    
    private boolean valorPagoIgualValorCompra() {
        String valorTotalPagoStr = valorTotalPagoText.getText().trim().replace(",", ".");
        String valorTotalCompraStr = valorTotalDaCompraText.getText().trim().replace(",", ".");

        BigDecimal valorTotalPago = new BigDecimal(valorTotalPagoStr);
        BigDecimal valorTotalCompra = new BigDecimal(valorTotalCompraStr);

        return valorTotalPago.compareTo(valorTotalCompra) == 0;
    }
    
    private void validarValorDigitado(){
        String valorTotalCompraStr = valorTotalDaCompraText.getText().trim().replace(",", ".");
        BigDecimal totalPago = listaPagamentos.stream().map(Pagamento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal valorTotalCompra = new BigDecimal(valorTotalCompraStr);
        BigDecimal valorRestante = valorTotalCompra.subtract(totalPago);
        valorMax(valorRestante);
        digitacao();
    }
    
    private void atualizarEstadoComponentes() {
        botaoConfirmarFP.setEnabled(!valorPagoIgualValorCompra());
        botaoConfirmarFP.setOpaque(!valorPagoIgualValorCompra());
        botaoSalvar.setEnabled(valorPagoIgualValorCompra());
        botaoSalvar.setOpaque(valorPagoIgualValorCompra());
        fpText.setEnabled(!valorPagoIgualValorCompra());
        valorPagoText.setEnabled(!valorPagoIgualValorCompra());
        labelFormaDePagamentoInsuficiente.setVisible(!valorPagoIgualValorCompra());
        adicionarLegendas();
    }
    
    private void sair() {
        funcao.MensagemConfirmarJDialog(this, "Você realmente deseja voltar sem confirmar?","", "VOLTAR PARA REGISTRAR PEDIDO", () -> this.dispose());
    }
        
    private void adicionarLegendas() {
        funcao.validarComboboxComDica(fpText, "Selecione a forma de pagamento usado");
        funcao.validarTextFieldComDica(valorPagoText, "Digite valor pago");
        funcao.validarBotaoComDica(botaoConfirmarFP, "Clique para confirmar forma de pagamento");
        funcao.validarBotaoComDica(botaoSalvar, "Clique para salvar o registro do pedido de produto(s)");
    }
    
    private void ajustarTabela() {
        JTableHeader header = tabelaFormaDePagamento1.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 16));
        tabelaFormaDePagamento1.getTableHeader().setReorderingAllowed(false);
        tabelaFormaDePagamento1.setRowSorter(null);

        // Aplica a formatação do cabeçalho
        TableColumnModel columnModel = tabelaFormaDePagamento1.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            String columnName = tabelaFormaDePagamento1.getColumnName(i);
            TableColumn col = columnModel.getColumn(i);
            col.setHeaderRenderer(new HeaderRenderer(new Color(0, 0, 102)));

            // Aplica o renderizador centralizado a todas as colunas que não são de ação
            if (!columnName.equals("AÇÃO")) {
                col.setCellRenderer(new CentralizadoRenderer());
            }
        }

        // Configura o renderizador e o editor para a coluna de ação
        TableColumn colunaAcao = tabelaFormaDePagamento1.getColumn("AÇÃO");
        colunaAcao.setCellRenderer(new BotaoRenderer());
        colunaAcao.setCellEditor(new BotaoEditor(new JCheckBox(), this));
        colunaAcao.setMinWidth(100);
        colunaAcao.setMaxWidth(100);
    }
    
    private void atualizarCampos() {
        BigDecimal totalPago = listaPagamentos.stream().map(Pagamento::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        valorTotalPagoText.setText(df.format(totalPago));
        
        BigDecimal valorTotalCompra = new BigDecimal(valorTotalDaCompraText.getText().replace(",", "."));
        BigDecimal valorRestante = valorTotalCompra.subtract(totalPago);
        valorPagoText.setText(df.format(valorRestante.max(BigDecimal.ZERO)));
        valorRestatanteText.setText(df.format(valorRestante.max(BigDecimal.ZERO)));
        atualizarEstadoComponentes();
        adicionarAtalhosTeclado();
    }
    
    private void digitacao() {
        funcao.limitarCaracteres(valorPagoText, 13);
        funcao.limitarCaracteresMonetarios(valorPagoText, 13, 2);    
        botaoConfirmarFP.setEnabled(valorMenorQueDigitado());
        botaoConfirmarFP.setOpaque(valorMenorQueDigitado());
        adicionarAtalhosTeclado();
    }
    
    private void adicionarPagamento() {
        if (fpText.getSelectedIndex() == -1) {
            funcao.MensagensJDialog(FormaDePagamento.this, "Selecione uma forma de pagamento","","ATENÇÃO", "aviso");
            return;
        }
        if(!valorMenorQueDigitado()){
            return;
        }
        
        String formaPagamento = fpText.getSelectedItem().toString();

        BigDecimal valorPago;
        try {
            valorPago = new BigDecimal(valorPagoText.getText().replace(",", "."));
        } catch (NumberFormatException e) {
            funcao.MensagensJDialog(FormaDePagamento.this, "O valor pago deve ser um número válido","","ATENÇÃO", "aviso");
            return;
        }

        if (valorPago.compareTo(BigDecimal.ZERO) <= 0) {
            funcao.MensagensJDialog(FormaDePagamento.this, "O valor pago deve ser maior que 0 (zero)","","ATENÇÃO", "aviso");
            return;
        }

        // cria o pagamento
        Pagamento novoPagamento = new Pagamento(formaPagamento, valorPago);
        listaPagamentos.add(novoPagamento);

        // adiciona na tabela
        Object[] rowData = {formaPagamento, df.format(valorPago), "Excluir"};
        modeloTabela.addRow(rowData);

        // 🔹 remove do combo se for PIX ou DINHEIRO
        if (formaPagamento.equals("PIX") || formaPagamento.equals("DINHEIRO")) {
            fpText.removeItem(formaPagamento);
        }

        atualizarCampos();
        
    }
    
    private void removerPagamento(int row) {
        Pagamento pagamentoRemovido = listaPagamentos.get(row);
        String formaRemovida = pagamentoRemovido.getForma();

        // remove da lista e da tabela
        listaPagamentos.remove(row);
        modeloTabela.removeRow(row);

        // 🔹 devolve ao combo se for PIX ou DINHEIRO
        if (formaRemovida.equals("PIX") || formaRemovida.equals("DINHEIRO")) {
            fpText.addItem(formaRemovida);
        }

        atualizarCampos();
    }

    private void salvarPedido() {
        if (valorPagoIgualValorCompra()){
            PedidoProduto pedido = new PedidoProduto();

            // Pega a data selecionada no JDateChooser
            java.util.Date dataDoPedido = dataPedido.getDate();

            // Chama a função de salvar no banco de dados, passando os pagamentos e a data
            boolean sucesso = pedido.salvarPedido(listaPagamentos, dataDoPedido,this);

            // Se salvou com sucesso, finaliza a tela e limpa os dados estáticos do carrinho
            if (sucesso) {
                CarrinhoPedido.setFinalizouPedido(true);
                funcao.MensagensJDialog(FormaDePagamento.this, "Pedido salvo com sucesso", "","SUCESSO", "informacao");
                this.dispose();
            }else{
                funcao.MensagensJDialog(FormaDePagamento.this, "Erro ao salvar pedido", "","ERRO", "erro");
            }
        }else{
            funcao.MensagensJDialog(FormaDePagamento.this, "Preencha os campos corretamente!", "", "ATENÇÃO", "aviso");
        }
    }
    
    private class HeaderRenderer implements TableCellRenderer {
        private final TableCellRenderer defaultRenderer;
        private final Color bgColor;

        public HeaderRenderer(Color bgColor) {
            this.defaultRenderer = new JTableHeader().getDefaultRenderer();
            this.bgColor = bgColor;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = defaultRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(bgColor);
            c.setForeground(Color.WHITE);
            c.setFont(new Font("Tahoma", Font.BOLD, 16));
            return c;
        }
    }

    private class BotaoRenderer extends JButton implements TableCellRenderer {
        public BotaoRenderer() {
            setBackground(new Color(204, 0, 0));
            setForeground(new Color(255, 255, 255));
            setFont(new Font("Arial", 1, 14));
            Icon iconExcluir = new ImageIcon(getClass().getResource("/imagens/botaoExcluir.png"));
            setIcon(iconExcluir);
            setBorder(new SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            setText("EXCLUIR");
            setHorizontalAlignment(CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class BotaoEditor extends DefaultCellEditor {
        private JButton button;
        private final FormaDePagamento parent;
        private int linhaSelecionada;

        public BotaoEditor(JCheckBox checkBox, FormaDePagamento parent) {
            super(checkBox);
            this.parent = parent;

            this.button = new JButton("EXCLUIR");
            this.button.setBackground(new java.awt.Color(204, 0, 0));
            this.button.setForeground(new java.awt.Color(255, 255, 255));
            this.button.setFont(new java.awt.Font("Arial", 1, 14));
            this.button.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            this.button.setOpaque(true);
            Icon iconExcluir = new ImageIcon(getClass().getResource("/imagens/botaoExcluir.png"));
            this.button.setIcon(iconExcluir);
            
            this.button.addActionListener(e -> {
                if (parent.tabelaFormaDePagamento1.isEditing()) {
                    parent.tabelaFormaDePagamento1.getCellEditor().stopCellEditing();
                }
                parent.removerPagamento(linhaSelecionada);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            this.linhaSelecionada = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "EXCLUIR";
        }
    }
    
    private class CentralizadoRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(CENTER);
            return this;
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
        jLabel24 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        botaoSair = new javax.swing.JButton();
        bordaSalvar = new javax.swing.JPanel();
        botaoSalvar = new javax.swing.JButton();
        painelFormaDePagamento = new javax.swing.JDesktopPane();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaFormaDePagamento1 = new javax.swing.JTable();
        valorTotalPagoText = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        valorTotalDaCompraText = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        valorRestatanteText = new javax.swing.JFormattedTextField();
        jLabel23 = new javax.swing.JLabel();
        painelPesquisar = new javax.swing.JDesktopPane();
        valorPagoText = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        fpText = new javax.swing.JComboBox<>();
        bordaAdicionarCarrinho = new javax.swing.JPanel();
        botaoConfirmarFP = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        painelDataPedido = new javax.swing.JDesktopPane();
        jLabel20 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        dataPedido = new com.toedter.calendar.JDateChooser();
        labelFormaDePagamentoInsuficiente = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jLabel24.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 102, 0));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("ATALHOS: F1 = Salvar Registro de Pedido | F2 = Digitar Valor Pago| F3 = Selecionar PIX | F4 = Selecionar Débito");
        jPanel1.add(jLabel24);
        jLabel24.setBounds(-6, 60, 1000, 16);

        jPanel3.setBackground(new java.awt.Color(210, 164, 2));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pagamento.png"))); // NOI18N
        jLabel4.setText("Chave da NFe inválida! Ela deve conter exatamente 44 números.");
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, 50, 50));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("SELECIONAR FORMA DE PAGAMENTO");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 990, -1));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 0, 989, 60);

        jLabel22.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 102, 0));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText(" F5 = Selecionar Crédito | F6 = Selecionar Dinheiro | F8 = Confirmar Forma de Pagamento | F12 = Voltar ao Menu Principal");
        jPanel1.add(jLabel22);
        jLabel22.setBounds(0, 66, 990, 30);

        botaoSair.setBackground(new java.awt.Color(204, 0, 0));
        botaoSair.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoSair.setForeground(new java.awt.Color(255, 255, 255));
        botaoSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/VOLTAR.png"))); // NOI18N
        botaoSair.setText("VOLTAR");
        botaoSair.setToolTipText("Clique para voltar a registro de pedido");
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
        botaoSair.setBounds(920, 410, 60, 60);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoSalvar.setBackground(new java.awt.Color(210, 164, 2));
        botaoSalvar.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        botaoSalvar.setForeground(new java.awt.Color(0, 0, 0));
        botaoSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/SALVARcad.png"))); // NOI18N
        botaoSalvar.setText("SALVAR REGISTRO");
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

        painelFormaDePagamento.setBackground(new java.awt.Color(255, 255, 255));
        painelFormaDePagamento.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*FORMA DE PAGAMENTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelFormaDePagamento.setForeground(new java.awt.Color(0, 102, 153));
        painelFormaDePagamento.setFocusCycleRoot(false);
        painelFormaDePagamento.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/iconePag.png"))); // NOI18N
        jLabel21.setOpaque(true);
        painelFormaDePagamento.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(342, 0, 30, 30));

        tabelaFormaDePagamento1.setAutoCreateRowSorter(true);
        tabelaFormaDePagamento1.setBackground(new java.awt.Color(255, 255, 255));
        tabelaFormaDePagamento1.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaFormaDePagamento1.setForeground(new java.awt.Color(51, 51, 51));
        tabelaFormaDePagamento1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TIPO", "VALOR PAGO", "AÇÃO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaFormaDePagamento1.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaFormaDePagamento1.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaFormaDePagamento1.getTableHeader().setReorderingAllowed(false);
        tabelaFormaDePagamento1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelaFormaDePagamento1KeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tabelaFormaDePagamento1);
        if (tabelaFormaDePagamento1.getColumnModel().getColumnCount() > 0) {
            tabelaFormaDePagamento1.getColumnModel().getColumn(1).setPreferredWidth(5);
            tabelaFormaDePagamento1.getColumnModel().getColumn(2).setResizable(false);
        }

        painelFormaDePagamento.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 480, 140));

        valorTotalPagoText.setEditable(false);
        valorTotalPagoText.setBackground(new java.awt.Color(255, 255, 255));
        valorTotalPagoText.setForeground(new java.awt.Color(0, 0, 102));
        valorTotalPagoText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorTotalPagoText.setText("0,00");
        valorTotalPagoText.setToolTipText("");
        valorTotalPagoText.setFocusable(false);
        valorTotalPagoText.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        valorTotalPagoText.setOpaque(true);
        valorTotalPagoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorTotalPagoTextKeyTyped(evt);
            }
        });
        painelFormaDePagamento.add(valorTotalPagoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 190, 170, 30));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 102));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("VALOR PAGO (R$)");
        painelFormaDePagamento.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 290, 30));

        valorTotalDaCompraText.setEditable(false);
        valorTotalDaCompraText.setBackground(new java.awt.Color(255, 255, 255));
        valorTotalDaCompraText.setForeground(new java.awt.Color(0, 0, 0));
        valorTotalDaCompraText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorTotalDaCompraText.setText("0,00");
        valorTotalDaCompraText.setToolTipText("");
        valorTotalDaCompraText.setFocusable(false);
        valorTotalDaCompraText.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        valorTotalDaCompraText.setOpaque(true);
        valorTotalDaCompraText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorTotalDaCompraTextKeyTyped(evt);
            }
        });
        painelFormaDePagamento.add(valorTotalDaCompraText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 270, 170, 30));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("VALOR DA COMPRA (R$)");
        painelFormaDePagamento.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 270, 290, 30));

        valorRestatanteText.setEditable(false);
        valorRestatanteText.setBackground(new java.awt.Color(255, 255, 255));
        valorRestatanteText.setForeground(new java.awt.Color(204, 0, 0));
        valorRestatanteText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorRestatanteText.setText("0,00");
        valorRestatanteText.setToolTipText("");
        valorRestatanteText.setFocusable(false);
        valorRestatanteText.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        valorRestatanteText.setOpaque(true);
        valorRestatanteText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorRestatanteTextKeyTyped(evt);
            }
        });
        painelFormaDePagamento.add(valorRestatanteText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 230, 170, 30));

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 17)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(204, 0, 0));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("VALOR RESTANTE (R$)");
        painelFormaDePagamento.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 290, 30));

        jPanel1.add(painelFormaDePagamento);
        painelFormaDePagamento.setBounds(420, 90, 520, 310);

        painelPesquisar.setBackground(new java.awt.Color(255, 255, 255));
        painelPesquisar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*SELECIONE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelPesquisar.setForeground(new java.awt.Color(0, 102, 153));
        painelPesquisar.setFocusCycleRoot(false);
        painelPesquisar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        valorPagoText.setBackground(new java.awt.Color(255, 255, 255));
        valorPagoText.setForeground(new java.awt.Color(0, 0, 0));
        valorPagoText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        valorPagoText.setText("0,00");
        valorPagoText.setToolTipText("");
        valorPagoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        valorPagoText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valorPagoTextFocusLost(evt);
            }
        });
        valorPagoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                valorPagoTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorPagoTextKeyTyped(evt);
            }
        });
        painelPesquisar.add(valorPagoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 80, 150, -1));

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Forma de Pagamento:");
        painelPesquisar.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 190, -1));

        fpText.setBackground(new java.awt.Color(255, 255, 255));
        fpText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        fpText.setForeground(new java.awt.Color(0, 0, 0));
        fpText.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        fpText.setToolTipText("");
        fpText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fpTextActionPerformed(evt);
            }
        });
        painelPesquisar.add(fpText, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 150, -1));

        bordaAdicionarCarrinho.setBackground(new java.awt.Color(255, 255, 255));
        bordaAdicionarCarrinho.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaAdicionarCarrinho.setForeground(new java.awt.Color(0, 0, 0));
        bordaAdicionarCarrinho.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoConfirmarFP.setBackground(new java.awt.Color(204, 204, 204));
        botaoConfirmarFP.setFont(new java.awt.Font("Impact", 0, 24)); // NOI18N
        botaoConfirmarFP.setForeground(new java.awt.Color(0, 0, 0));
        botaoConfirmarFP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONFIRMAR.png"))); // NOI18N
        botaoConfirmarFP.setText("CONFIRMAR");
        botaoConfirmarFP.setToolTipText("");
        botaoConfirmarFP.setBorder(null);
        botaoConfirmarFP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoConfirmarFP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConfirmarFPActionPerformed(evt);
            }
        });
        bordaAdicionarCarrinho.add(botaoConfirmarFP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 322, 50));

        painelPesquisar.add(bordaAdicionarCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 122, 322, 50));

        jLabel12.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Valor Pago (R$):");
        painelPesquisar.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 80, 190, -1));

        jPanel1.add(painelPesquisar);
        painelPesquisar.setBounds(40, 90, 370, 190);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("*campo(s) obrigatório(s)");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(0, 450, 990, 17);

        painelDataPedido.setBackground(new java.awt.Color(255, 255, 255));
        painelDataPedido.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA DO PEDIDO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataPedido.setForeground(new java.awt.Color(0, 102, 153));
        painelDataPedido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/DATA.png"))); // NOI18N
        jLabel20.setOpaque(true);
        painelDataPedido.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(243, 0, 30, 30));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Selecione:");
        painelDataPedido.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 120, -1));

        dataPedido.setBackground(new java.awt.Color(255, 255, 255));
        dataPedido.setForeground(new java.awt.Color(0, 0, 0));
        dataPedido.setToolTipText("Selecione Data do Pedido");
        dataPedido.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        dataPedido.setOpaque(false);
        painelDataPedido.add(dataPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 45, 200, 30));

        jPanel1.add(painelDataPedido);
        painelDataPedido.setBounds(40, 300, 370, 100);

        labelFormaDePagamentoInsuficiente.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelFormaDePagamentoInsuficiente.setForeground(new java.awt.Color(204, 0, 0));
        labelFormaDePagamentoInsuficiente.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFormaDePagamentoInsuficiente.setText("*O VALOR TOTAL PAGO É MENOR QUE O VALOR DA COMPRA. ADICIONE MAIS PAGAMENTOS PARA PROSSEGUIR.");
        jPanel1.add(labelFormaDePagamentoInsuficiente);
        labelFormaDePagamentoInsuficiente.setBounds(40, 400, 900, 20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 988, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSairActionPerformed
        sair();
    }//GEN-LAST:event_botaoSairActionPerformed

    private void valorPagoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorPagoTextKeyTyped

    }//GEN-LAST:event_valorPagoTextKeyTyped

    private void fpTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fpTextActionPerformed

    }//GEN-LAST:event_fpTextActionPerformed

    private void tabelaFormaDePagamento1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaFormaDePagamento1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tabelaFormaDePagamento1KeyPressed

    private void botaoConfirmarFPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConfirmarFPActionPerformed
        adicionarPagamento();
    }//GEN-LAST:event_botaoConfirmarFPActionPerformed

    private void valorTotalDaCompraTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorTotalDaCompraTextKeyTyped

    }//GEN-LAST:event_valorTotalDaCompraTextKeyTyped

    private void valorTotalPagoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorTotalPagoTextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_valorTotalPagoTextKeyTyped

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        salvarPedido();
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void valorPagoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorPagoTextKeyReleased
        digitacao();
    }//GEN-LAST:event_valorPagoTextKeyReleased

    private void valorPagoTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valorPagoTextFocusLost
        validarValorDigitado();
    }//GEN-LAST:event_valorPagoTextFocusLost

    private void valorRestatanteTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorRestatanteTextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_valorRestatanteTextKeyTyped

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
            java.util.logging.Logger.getLogger(FormaDePagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FormaDePagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FormaDePagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FormaDePagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FormaDePagamento dialog = new FormaDePagamento(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel bordaAdicionarCarrinho;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoConfirmarFP;
    private javax.swing.JButton botaoSair;
    private javax.swing.JButton botaoSalvar;
    private com.toedter.calendar.JDateChooser dataPedido;
    private javax.swing.JComboBox<String> fpText;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelFormaDePagamentoInsuficiente;
    private javax.swing.JDesktopPane painelDataPedido;
    private javax.swing.JDesktopPane painelFormaDePagamento;
    private javax.swing.JDesktopPane painelPesquisar;
    private javax.swing.JTable tabelaFormaDePagamento1;
    private javax.swing.JFormattedTextField valorPagoText;
    private javax.swing.JFormattedTextField valorRestatanteText;
    private javax.swing.JFormattedTextField valorTotalDaCompraText;
    private javax.swing.JFormattedTextField valorTotalPagoText;
    // End of variables declaration//GEN-END:variables
}
