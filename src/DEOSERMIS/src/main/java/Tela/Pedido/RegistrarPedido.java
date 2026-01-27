
package Tela.Pedido;

import Classes.CarrinhoPedido;
import Classes.Funcoes;
import Classes.ItemPedido;
import Classes.ItensCarrinho;
import Classes.OverlayUtil;
import Classes.Produto;
import Tela.Inicio.MenuPrincipal;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.AbstractTableModel;
import javax.swing.border.SoftBevelBorder;

@SuppressWarnings("serial")
public class RegistrarPedido extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final Produto produto = new Produto();
    public RegistrarPedido() {
        setUndecorated(true);
        initComponents();
        montarTela();
        funcao.bloquearArrastar(tabelaCarrinhoDePedido);
        codigoText.requestFocus();
    }
    
    private void montarTela() {
        JTableHeader header = tabelaCarrinhoDePedido.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 16));
        tabelaCarrinhoDePedido.getTableHeader().setReorderingAllowed(false);
        tabelaCarrinhoDePedido.setRowSorter(null);

        // Aplica azul escuro nas colunas de "CÓD" até "TOTAL (R$)"
        TableColumnModel columnModel = tabelaCarrinhoDePedido.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            String columnName = tabelaCarrinhoDePedido.getColumnName(i);
            TableColumn col = columnModel.getColumn(i);

            if (!columnName.equals("AÇÃO")) {
                col.setHeaderRenderer(new HeaderRenderer(new Color(0, 0, 102))); // azul escuro
            } else {
                col.setHeaderRenderer(new HeaderRenderer(new Color(70, 130, 180))); // azul claro para AÇÃO
            }
        }

        limparCarrinho(); //limpa itens
        
        // Adiciona botão EXCLUIR na coluna "AÇÃO"
        TableColumn colunaItem = tabelaCarrinhoDePedido.getColumn("AÇÃO");
        colunaItem.setCellRenderer(new BotaoRenderer());
        colunaItem.setCellEditor(new BotaoEditor(new JCheckBox(), tabelaCarrinhoDePedido));
        colunaItem.setMinWidth(100);
        colunaItem.setMaxWidth(100);

        funcao.configurarTabela(tabelaPesquisarProdutos, new Font("Tahoma", Font.BOLD, 12), Color.BLACK, new Color(200, 200, 200));

        quantidadeText.getEditor().getComponent(0).setForeground(Color.BLACK);
        codigoText.getEditor().getComponent(0).setForeground(Color.BLACK);
        descontoText.getEditor().getComponent(0).setForeground(Color.BLACK);
        funcao.aplicarEfeitoMouse(botaoMenu);
        funcao.aplicarEfeitoMouse(botaoAdicionarCarrinho);
        funcao.aplicarEfeitoMouse(botaoFormaDePagamento);
        funcao.aplicarEfeitoMouse(botaoLimparCarrinho);
        pesquisarNomeProduto();
        pesquisarCodigoProduto();
        configurarEntradaDeDados();
        atualizarEstadoComponentes();
        adicionarAtalhosTeclado();
    }
    
    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoFormaDePagamento);
        funcao.adicionarAtalho(root, "F5", botaoLimparCarrinho);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
        funcao.adicionarAtalho(root, "F8", botaoAdicionarCarrinho);
        
        funcao.adicionarAtalhoTeclado(root, "F2", () -> descontoText.requestFocus());
        funcao.adicionarAtalhoTeclado(root, "F4", () -> pesquisarNomeProdutoText.requestFocus());
        funcao.adicionarAtalhoTeclado(root, "F10", () ->  codigoText.requestFocus());
    }
    
    private void trocarTela(){
        funcao.trocarDeTela(this, new MenuPrincipal());
    }
    
    private void configurarEntradaDeDados(){
        funcao.entradaNumerica((JSpinner.DefaultEditor) quantidadeText.getEditor());
        funcao.entradaNumerica((JSpinner.DefaultEditor) codigoText.getEditor());
        funcao.entradaNumerica((JSpinner.DefaultEditor) descontoText.getEditor());
    }
    
    private void addFormaDePagamento() {
        if (carrinhoVazio()){
            // Coleta os dados do pedido do carrinho e passa para a tela de pagamento
            CarrinhoPedido carrinhoSalvar = new CarrinhoPedido(
                ItensCarrinho.getItens(),
                new BigDecimal(subtotalText.getText().replace(",", ".")),
                new BigDecimal(descontoText.getValue().toString()),
                new BigDecimal(valorTotalText.getText().replace(",", "."))
            );

            // Navega para a tela de pagamento, passando o objeto de carrinho
          FormaDePagamento dialog = new FormaDePagamento(this, false);

            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    if (carrinhoSalvar.getFinalizouPedido()){
                        limparTudo();
                        carrinhoSalvar.finalizarPedido();
                    }          
                }
            });

            OverlayUtil.abrirTela(this, dialog);
        }else{
            funcao.Mensagens(this, "Adicione pelo menos um produto ao carrinho","", "ATENÇÃO", "aviso");
        }
    }

    private void limparCarrinho() {
        ItensCarrinho.getItens().clear(); 
        atualizarTabelaCarrinho();
        calcularTotal();
        funcao.limparComponente(descontoText);
    }

    private void calcularTotal() {
            BigDecimal subtotal = BigDecimal.ZERO;
            for (ItemPedido item : ItensCarrinho.getItens()) {
                subtotal = subtotal.add(item.getTotal());
            }

            subtotalText.setText(String.format("%.2f", subtotal));
            BigDecimal descontoPorcentagem = new BigDecimal(descontoText.getValue().toString());
            BigDecimal fatorDesconto = descontoPorcentagem.divide(new BigDecimal(100), MathContext.DECIMAL32);
            BigDecimal totalComDesconto = subtotal.subtract(subtotal.multiply(fatorDesconto));

            valorTotalText.setText(String.format("%.2f", totalComDesconto));
            atualizarEstadoComponentes();
            pesquisarCodigoProduto();
            pesquisarNomeProduto();
        }

    private void pesquisarNomeProduto() {
        String nomeParcial = pesquisarNomeProdutoText.getText();

        List<Produto> lista = produto.buscarProduto(nomeParcial,this);

        DefaultTableModel modelo = (DefaultTableModel) tabelaPesquisarProdutos.getModel();
        modelo.setRowCount(0);
        DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");

        for (Produto p : lista) {
            // Obtém a quantidade do produto no carrinho (se existir)
            ItemPedido itemNoCarrinho = ItensCarrinho.buscarItemPorCodigo(p.getCodigo());
            int quantidadeNoCarrinho = (itemNoCarrinho != null) ? itemNoCarrinho.getQuantidade() : 0;
            
            // Calcula a quantidade disponível
            int quantidadeDisponivel = p.getQuantidade().intValue() - quantidadeNoCarrinho;

            String valorFormatado = formatoDecimal.format(p.getValorUnitario());

            modelo.addRow(new Object[]{
                p.getCodigo(),
                p.getNome(),
                quantidadeDisponivel,
                valorFormatado
            });
            
        }
        
        if (!lista.isEmpty()) {
            byte[] imagem = produto.buscarImagemProduto(lista.get(0).getCodigo(),this);
            funcao.exibirImagem(labelPesquisar, imagem, "/imagens/imgPedidoReposicaoPesquisarProduto.png");
        } else {
            funcao.exibirImagem(labelPesquisar, null, "/imagens/imgPedidoReposicaoPesquisarProduto.png");
        }
        alinharTabelaPesquisarProdutos();
    }

    private void atualizarTotalProduto(){
        try {
            int quantidade = Integer.parseInt(quantidadeText.getValue().toString());
            double precoUnitario = Double.parseDouble(valorUnitarioText.getText().replace(",", "."));
            double total = quantidade * precoUnitario;
            totalText.setText(String.format("%.2f", total));
        } catch (NumberFormatException ex) {
            totalText.setText("0,00");
        }
    }

    private void pesquisarCodigoProduto(){
        try {
            BigInteger codigo = new BigInteger(codigoText.getValue().toString());
            Produto produto = Produto.buscarProdutoPorCodigo(codigo,this);

            if (produto != null) {
                // Obtém a quantidade do produto no carrinho (se existir)
                ItemPedido itemNoCarrinho = ItensCarrinho.buscarItemPorCodigo(codigo);
                int quantidadeNoCarrinho = (itemNoCarrinho != null) ? itemNoCarrinho.getQuantidade() : 0;
                
                // Calcula a quantidade disponível
                int quantidadeDisponivel = produto.getQuantidade().intValue() - quantidadeNoCarrinho;

                BigDecimal valor = produto.getValorUnitario();
                nomeText.setText(produto.getNome());
                valorUnitarioText.setText(String.format("%.2f", valor));

                int valorInicial = (quantidadeDisponivel > 0) ? 1 : 0;
                int valorMinimo = (quantidadeDisponivel > 0) ? 1 : 0;

                SpinnerNumberModel modelo = new SpinnerNumberModel(valorInicial, valorMinimo, quantidadeDisponivel, 1);
                quantidadeText.setModel(modelo);
                quantidadeText.setEnabled(quantidadeDisponivel > 0);
                quantidadeText.setValue(valorInicial);
                atualizarTotalProduto();
                byte[] imagem = produto.buscarImagemProduto(codigo,this);
                funcao.exibirImagem(labelSelecionarProduto, imagem, "/imagens/imgPedidoProduto.png");
            } else {
                funcao.limparComponente(nomeText);
                funcao.limparComponenteFormatadoNumerico(valorUnitarioText);
                SpinnerNumberModel modelo = new SpinnerNumberModel(0, 0, 0, 1);
                quantidadeText.setModel(modelo);
                quantidadeText.setEnabled(false);
                funcao.exibirImagem(labelSelecionarProduto, null, "/imagens/imgPedidoProduto.png");
                quantidadeText.setValue(0);
                atualizarTotalProduto();
            }
        } catch (NumberFormatException e) {
            funcao.limparComponente(nomeText);
            funcao.limparComponenteFormatadoNumerico(valorUnitarioText);
            SpinnerNumberModel modelo = new SpinnerNumberModel(0, 0, 0, 1);
            quantidadeText.setModel(modelo);
            quantidadeText.setEnabled(false);
            funcao.exibirImagem(labelSelecionarProduto, null, "/imagens/imgPedidoProduto.png");
            quantidadeText.setValue(0);
            atualizarTotalProduto();
        }
        atualizarEstadoComponentes();
    }

    private void alinharTabelaPesquisarProdutos() {
        DefaultTableCellRenderer alinhamentoEsquerda = new DefaultTableCellRenderer();
        alinhamentoEsquerda.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabelaPesquisarProdutos.getColumnCount(); i++) {
            tabelaPesquisarProdutos.getColumnModel().getColumn(i).setCellRenderer(alinhamentoEsquerda);
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
                boolean isSelected, boolean hasFocus,
                int row, int column) {
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
            setHorizontalAlignment(SwingConstants.CENTER);
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
        private JTable tabela;
        private int linhaSelecionada;

        public BotaoEditor(JCheckBox checkBox, JTable tabela) {
            super(checkBox);
            this.tabela = tabela;

            button = new JButton("EXCLUIR");

            button.setBackground(new java.awt.Color(204, 0, 0));
            button.setForeground(new java.awt.Color(255, 255, 255));
            button.setFont(new java.awt.Font("Arial", 1, 14));
            button.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            button.setOpaque(true);
            Icon iconExcluir = new ImageIcon(getClass().getResource("/imagens/botaoExcluir.png"));
            button.setIcon(iconExcluir);
            button.addActionListener(e -> {
                if (tabela.isEditing()) {
                    tabela.getCellEditor().stopCellEditing();
                }
                if (linhaSelecionada >= 0 && linhaSelecionada < ItensCarrinho.getItens().size()) {
                    ItensCarrinho.removerItem(linhaSelecionada);
                    atualizarTabelaCarrinho();
                    calcularTotal();
                    funcao.limparComponente(descontoText);
                }
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

    private void atualizarEstadoComponentes() {
        botaoAdicionarCarrinho.setEnabled(elegivelAddCarrinho());
        botaoFormaDePagamento.setEnabled(carrinhoVazio());
        botaoLimparCarrinho.setEnabled(carrinhoVazio());
        botaoAdicionarCarrinho.setOpaque(elegivelAddCarrinho());
        botaoFormaDePagamento.setOpaque(carrinhoVazio());
        botaoLimparCarrinho.setOpaque(carrinhoVazio());
        labelCarrinhoVazio.setVisible(!carrinhoVazio());
        descontoText.setEnabled(carrinhoVazio());
        funcao.validarSpinnerComDica(quantidadeText, "Escolha a quantidade desejada");
        funcao.validarSpinnerComDica(descontoText, "Escolha o desconto desejado");
        adicionarAtalhosTeclado();
        adicionarLegendasBotoes();
    }

    private boolean carrinhoVazio() {
        return !ItensCarrinho.getItens().isEmpty();
    }

    private boolean elegivelAddCarrinho(){
        return funcao.validarCampoVazio(nomeText) && (Integer) quantidadeText.getValue() > 0;
    }

    private void limparCampos(){
        funcao.limparComponente(codigoText);
        atualizarEstadoComponentes();
    }

    private void addCarrinho(){
        try {
            BigInteger codigo = new BigInteger(codigoText.getValue().toString());
            String nome = nomeText.getText();
            int quantidade = (int) quantidadeText.getValue();
            BigDecimal precoUnitario = new BigDecimal(valorUnitarioText.getText().replace(",", "."));

            ItemPedido itemExistente = ItensCarrinho.buscarItemPorCodigo(codigo);

            if (itemExistente != null) {
                int novaQuantidade = itemExistente.getQuantidade() + quantidade;
                itemExistente.setQuantidade(novaQuantidade);
            } else {
                ItemPedido novoItem = new ItemPedido(codigo, nome, quantidade, precoUnitario);
                ItensCarrinho.adicionarItem(novoItem);
            }

            atualizarTabelaCarrinho();
            calcularTotal();
            limparCampos();

        } catch (NumberFormatException e) {
            funcao.Mensagens(null, "Erro ao adicionar produto: ", e.getMessage(), "ATENÇÃO!", "erro");
        }
        codigoText.requestFocus();
    }

    private void atualizarTabelaCarrinho() {
        DefaultTableModel modeloCarrinho = (DefaultTableModel) tabelaCarrinhoDePedido.getModel();
        modeloCarrinho.setRowCount(0);

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);

        for (ItemPedido item : ItensCarrinho.getItens()) {
            modeloCarrinho.addRow(new Object[]{
                item.getCodigo(),
                item.getNome(),
                item.getQuantidade(),
                String.format("%.2f", item.getPrecoUnitario()),
                String.format("%.2f", item.getTotal()),
                "EXCLUIR"
            });
        }
        
        for (int i = 0; i < tabelaCarrinhoDePedido.getColumnCount() - 1; i++) {
            tabelaCarrinhoDePedido.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }
        
        ((AbstractTableModel) tabelaCarrinhoDePedido.getModel()).fireTableDataChanged();
    }
    
    private void adicionarLegendasBotoes() {
        funcao.validarBotaoComDica(botaoFormaDePagamento, "Clique para ir para a tela de forma de pagamento");
        funcao.validarBotaoComDica(botaoAdicionarCarrinho, "Clique para adicionar o produto selecionado ao carrinho");
        funcao.validarBotaoComDica(botaoLimparCarrinho, "Clique para remover todos os produtos do carrinho");
        funcao.validarBotaoComDica(botaoMenu, "Clique para voltar ao menu principal");
    }
    
    private void limparTudo() {
        funcao.limparComponente(codigoText);
        funcao.limparComponente(nomeText);
        funcao.limparComponenteFormatadoNumerico(valorUnitarioText);
        funcao.limparComponente(quantidadeText);
        funcao.limparComponente(descontoText);
        funcao.limparComponente(pesquisarNomeProdutoText);
        funcao.limparComponenteFormatadoNumerico(subtotalText);
        funcao.limparComponenteFormatadoNumerico(totalText);
        funcao.limparComponenteFormatadoNumerico(valorTotalText);
        limparCarrinho();
        pesquisarCodigoProduto();
        pesquisarNomeProduto();
        codigoText.requestFocus();
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
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel5 = new javax.swing.JLabel();
        quantidadeText = new javax.swing.JSpinner();
        codigoText = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        valorUnitarioText = new javax.swing.JFormattedTextField();
        nomeText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        totalText = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        labelSelecionarProduto = new javax.swing.JLabel();
        bordaAdicionarCarrinho = new javax.swing.JPanel();
        botaoAdicionarCarrinho = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaCarrinhoDePedido = new javax.swing.JTable();
        descontoText = new javax.swing.JSpinner();
        subtotalText = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        valorTotalText = new javax.swing.JFormattedTextField();
        bordaLimparCarrinho = new javax.swing.JPanel();
        botaoLimparCarrinho = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jDesktopPane3 = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaPesquisarProdutos = new javax.swing.JTable();
        pesquisarNomeProdutoText = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        labelPesquisar = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();
        labelCarrinhoVazio = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoFormaDePagamento = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1070, 696));
        setSize(new java.awt.Dimension(1070, 696));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(210, 164, 2));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Pedido.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("RESGISTRO DE PEDIDO");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1070, -1));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 1070, 60);

        jDesktopPane1.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*SELECIONAR PRODUTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Quantidade:");
        jDesktopPane1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, 130, -1));

        quantidadeText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        quantidadeText.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));
        quantidadeText.setToolTipText("");
        quantidadeText.setEnabled(false);
        quantidadeText.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                quantidadeTextStateChanged(evt);
            }
        });
        quantidadeText.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                quantidadeTextInputMethodTextChanged(evt);
            }
        });
        quantidadeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                quantidadeTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                quantidadeTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                quantidadeTextKeyTyped(evt);
            }
        });
        jDesktopPane1.add(quantidadeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 40, 80, -1));

        codigoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        codigoText.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        codigoText.setToolTipText("Escolha o código do produto");
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
        jDesktopPane1.add(codigoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 80, -1));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Código:");
        jDesktopPane1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 140, -1));

        valorUnitarioText.setEditable(false);
        valorUnitarioText.setBackground(new java.awt.Color(255, 255, 255));
        valorUnitarioText.setForeground(new java.awt.Color(51, 51, 51));
        valorUnitarioText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        valorUnitarioText.setText("0,00");
        valorUnitarioText.setToolTipText("");
        valorUnitarioText.setFocusable(false);
        valorUnitarioText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        valorUnitarioText.setOpaque(true);
        valorUnitarioText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorUnitarioTextKeyTyped(evt);
            }
        });
        jDesktopPane1.add(valorUnitarioText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 100, 150, -1));

        nomeText.setEditable(false);
        nomeText.setBackground(new java.awt.Color(255, 255, 255));
        nomeText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        nomeText.setForeground(new java.awt.Color(51, 51, 51));
        nomeText.setFocusable(false);
        nomeText.setOpaque(true);
        nomeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeTextActionPerformed(evt);
            }
        });
        jDesktopPane1.add(nomeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 300, -1));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nome:");
        jDesktopPane1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 140, -1));

        jLabel8.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Valor Unitário (R$):");
        jDesktopPane1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        totalText.setEditable(false);
        totalText.setBackground(new java.awt.Color(255, 255, 255));
        totalText.setForeground(new java.awt.Color(51, 51, 51));
        totalText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        totalText.setText("0,00");
        totalText.setToolTipText("");
        totalText.setFocusable(false);
        totalText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        totalText.setOpaque(true);
        totalText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalTextActionPerformed(evt);
            }
        });
        totalText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                totalTextKeyTyped(evt);
            }
        });
        jDesktopPane1.add(totalText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 150, -1));

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Total (R$):");
        jDesktopPane1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 140, -1));

        labelSelecionarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imgPedidoProduto.png"))); // NOI18N
        jDesktopPane1.add(labelSelecionarProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 100, 140, 140));

        bordaAdicionarCarrinho.setBackground(new java.awt.Color(255, 255, 255));
        bordaAdicionarCarrinho.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaAdicionarCarrinho.setForeground(new java.awt.Color(0, 0, 0));
        bordaAdicionarCarrinho.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoAdicionarCarrinho.setBackground(new java.awt.Color(204, 204, 204));
        botaoAdicionarCarrinho.setFont(new java.awt.Font("Impact", 0, 20)); // NOI18N
        botaoAdicionarCarrinho.setForeground(new java.awt.Color(0, 0, 0));
        botaoAdicionarCarrinho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoADDcarrinho.png"))); // NOI18N
        botaoAdicionarCarrinho.setText("ADICIONAR AO CARRINHO");
        botaoAdicionarCarrinho.setBorder(null);
        botaoAdicionarCarrinho.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAdicionarCarrinho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAdicionarCarrinhoActionPerformed(evt);
            }
        });
        bordaAdicionarCarrinho.add(botaoAdicionarCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 70));

        jDesktopPane1.add(bordaAdicionarCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 300, 70));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/produtoRegistrar.png"))); // NOI18N
        jLabel9.setOpaque(true);
        jDesktopPane1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 0, 30, 30));

        jPanel1.add(jDesktopPane1);
        jDesktopPane1.setBounds(10, 330, 470, 250);

        jLabel18.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText(" F8 = Adicionar Produto ao Carrinho | F10 = Digitar Código do Produto | F12 = Voltar ao Menu Principal");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(0, 74, 1070, 16);

        jDesktopPane2.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*CARRINHO DE PEDIDO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelaCarrinhoDePedido.setAutoCreateRowSorter(true);
        tabelaCarrinhoDePedido.setBackground(new java.awt.Color(255, 255, 255));
        tabelaCarrinhoDePedido.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaCarrinhoDePedido.setForeground(new java.awt.Color(51, 51, 51));
        tabelaCarrinhoDePedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÓD", "PRODUTO", "QTD", "VL UN (R$)", "TOTAL (R$)", "AÇÃO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaCarrinhoDePedido.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaCarrinhoDePedido.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaCarrinhoDePedido.getTableHeader().setReorderingAllowed(false);
        tabelaCarrinhoDePedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelaCarrinhoDePedidoKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaCarrinhoDePedido);
        if (tabelaCarrinhoDePedido.getColumnModel().getColumnCount() > 0) {
            tabelaCarrinhoDePedido.getColumnModel().getColumn(0).setPreferredWidth(10);
            tabelaCarrinhoDePedido.getColumnModel().getColumn(2).setPreferredWidth(10);
        }

        jDesktopPane2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 550, 240));

        descontoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        descontoText.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 99.0d, 1.0d));
        descontoText.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                descontoTextStateChanged(evt);
            }
        });
        descontoText.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                descontoTextMouseClicked(evt);
            }
        });
        descontoText.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                descontoTextInputMethodTextChanged(evt);
            }
        });
        descontoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                descontoTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                descontoTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descontoTextKeyTyped(evt);
            }
        });
        jDesktopPane2.add(descontoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, -1, -1));

        subtotalText.setEditable(false);
        subtotalText.setBackground(new java.awt.Color(255, 255, 255));
        subtotalText.setForeground(new java.awt.Color(0, 0, 0));
        subtotalText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        subtotalText.setText("0,00");
        subtotalText.setToolTipText("");
        subtotalText.setFocusable(false);
        subtotalText.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        subtotalText.setOpaque(true);
        subtotalText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                subtotalTextKeyTyped(evt);
            }
        });
        jDesktopPane2.add(subtotalText, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 370, 310, -1));

        jLabel15.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Desconto (%):");
        jDesktopPane2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 190, 20));
        jLabel15.getAccessibleContext().setAccessibleName("");

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("SUBTOTAL (R$)");
        jDesktopPane2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 190, 30));

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 26)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("TOTAL (R$)");
        jDesktopPane2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 410, 190, -1));

        valorTotalText.setEditable(false);
        valorTotalText.setBackground(new java.awt.Color(255, 255, 255));
        valorTotalText.setForeground(new java.awt.Color(0, 0, 0));
        valorTotalText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        valorTotalText.setText("0,00");
        valorTotalText.setToolTipText("");
        valorTotalText.setFocusable(false);
        valorTotalText.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        valorTotalText.setOpaque(true);
        valorTotalText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorTotalTextKeyTyped(evt);
            }
        });
        jDesktopPane2.add(valorTotalText, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 410, 310, -1));

        bordaLimparCarrinho.setBackground(new java.awt.Color(255, 255, 255));
        bordaLimparCarrinho.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaLimparCarrinho.setForeground(new java.awt.Color(0, 0, 0));
        bordaLimparCarrinho.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoLimparCarrinho.setBackground(new java.awt.Color(204, 204, 204));
        botaoLimparCarrinho.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botaoLimparCarrinho.setForeground(new java.awt.Color(0, 0, 0));
        botaoLimparCarrinho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoLimparCarrinho.png"))); // NOI18N
        botaoLimparCarrinho.setText("EXCLUIR TODOS PRODUTOS DO CARRINHO");
        botaoLimparCarrinho.setBorder(null);
        botaoLimparCarrinho.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoLimparCarrinho.setHideActionText(true);
        botaoLimparCarrinho.setMaximumSize(new java.awt.Dimension(173, 25));
        botaoLimparCarrinho.setMinimumSize(new java.awt.Dimension(173, 25));
        botaoLimparCarrinho.setPreferredSize(new java.awt.Dimension(173, 25));
        botaoLimparCarrinho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLimparCarrinhoActionPerformed(evt);
            }
        });
        bordaLimparCarrinho.add(botaoLimparCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 552, 40));

        jDesktopPane2.add(bordaLimparCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 550, 40));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/carrinho.png"))); // NOI18N
        jLabel6.setOpaque(true);
        jDesktopPane2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 0, 30, 30));

        jPanel1.add(jDesktopPane2);
        jDesktopPane2.setBounds(490, 120, 570, 460);

        jDesktopPane3.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PESQUISAR PRODUTOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane3.setForeground(new java.awt.Color(0, 102, 153));
        jDesktopPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabelaPesquisarProdutos.setBackground(new java.awt.Color(255, 255, 255));
        tabelaPesquisarProdutos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 12)); // NOI18N
        tabelaPesquisarProdutos.setForeground(new java.awt.Color(51, 51, 51));
        tabelaPesquisarProdutos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Quantidade", "Valor Unitário"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaPesquisarProdutos.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaPesquisarProdutos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaPesquisarProdutos.getTableHeader().setReorderingAllowed(false);
        tabelaPesquisarProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaPesquisarProdutosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaPesquisarProdutos);
        if (tabelaPesquisarProdutos.getColumnModel().getColumnCount() > 0) {
            tabelaPesquisarProdutos.getColumnModel().getColumn(0).setPreferredWidth(30);
        }

        jDesktopPane3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(9, 30, 340, 110));

        pesquisarNomeProdutoText.setBackground(new java.awt.Color(255, 255, 255));
        pesquisarNomeProdutoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        pesquisarNomeProdutoText.setForeground(new java.awt.Color(0, 0, 0));
        pesquisarNomeProdutoText.setToolTipText("Pesquise produto(s) por nome");
        pesquisarNomeProdutoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pesquisarNomeProdutoTextKeyReleased(evt);
            }
        });
        jDesktopPane3.add(pesquisarNomeProdutoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 380, -1));

        jLabel12.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Nome:");
        jDesktopPane3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 60, -1));

        labelPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imgPedidoReposicaoPesquisarProduto.png"))); // NOI18N
        jDesktopPane3.add(labelPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(351, 30, 110, 110));

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisarRegistrar.png"))); // NOI18N
        jLabel17.setOpaque(true);
        jDesktopPane3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 0, 30, 30));

        jPanel1.add(jDesktopPane3);
        jDesktopPane3.setBounds(10, 120, 470, 180);

        botaoMenu.setBackground(new java.awt.Color(204, 0, 0));
        botaoMenu.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoMenu.setForeground(new java.awt.Color(255, 255, 255));
        botaoMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/home.png"))); // NOI18N
        botaoMenu.setText("MENU");
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

        labelCarrinhoVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelCarrinhoVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelCarrinhoVazio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCarrinhoVazio.setText("*ADICIONE PELO MENOS UM PRODUTO AO CARRINHO.");
        jPanel1.add(labelCarrinhoVazio);
        labelCarrinhoVazio.setBounds(10, 580, 470, 20);

        jLabel20.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 102, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("ATALHOS: F1 = Selecionar Forma de Pagamento | F2 = Digitar Desconto| F4 = Pesquisar Nome do Produto | F5 = Limpar Carrinho");
        jPanel1.add(jLabel20);
        jLabel20.setBounds(4, 60, 1060, 16);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoFormaDePagamento.setBackground(new java.awt.Color(210, 164, 2));
        botaoFormaDePagamento.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        botaoFormaDePagamento.setForeground(new java.awt.Color(0, 0, 0));
        botaoFormaDePagamento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoPag.png"))); // NOI18N
        botaoFormaDePagamento.setText("FORMA DE PAGAMENTO");
        botaoFormaDePagamento.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoFormaDePagamento.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoFormaDePagamento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoFormaDePagamento.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        botaoFormaDePagamento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoFormaDePagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoFormaDePagamentoActionPerformed(evt);
            }
        });
        bordaSalvar.add(botaoFormaDePagamento, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaSalvar);
        bordaSalvar.setBounds(10, 630, 91, 62);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("*campo(s) obrigatório(s)");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(2, 670, 1070, 17);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1070, 696);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tabelaCarrinhoDePedidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaCarrinhoDePedidoKeyPressed

    }//GEN-LAST:event_tabelaCarrinhoDePedidoKeyPressed

    private void subtotalTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_subtotalTextKeyTyped

    }//GEN-LAST:event_subtotalTextKeyTyped

    private void valorTotalTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorTotalTextKeyTyped

    }//GEN-LAST:event_valorTotalTextKeyTyped

    private void descontoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descontoTextKeyTyped
        calcularTotal();
    }//GEN-LAST:event_descontoTextKeyTyped

    private void descontoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descontoTextKeyReleased

    }//GEN-LAST:event_descontoTextKeyReleased

    private void descontoTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descontoTextKeyPressed

    }//GEN-LAST:event_descontoTextKeyPressed

    private void descontoTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_descontoTextInputMethodTextChanged

    }//GEN-LAST:event_descontoTextInputMethodTextChanged

    private void descontoTextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_descontoTextStateChanged
        calcularTotal();
    }//GEN-LAST:event_descontoTextStateChanged

    private void codigoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyTyped

    }//GEN-LAST:event_codigoTextKeyTyped

    private void codigoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyReleased
        
    }//GEN-LAST:event_codigoTextKeyReleased

    private void codigoTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyPressed

    }//GEN-LAST:event_codigoTextKeyPressed

    private void codigoTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_codigoTextInputMethodTextChanged

    }//GEN-LAST:event_codigoTextInputMethodTextChanged

    private void codigoTextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_codigoTextStateChanged
        pesquisarCodigoProduto();
    }//GEN-LAST:event_codigoTextStateChanged

    private void totalTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_totalTextKeyTyped

    }//GEN-LAST:event_totalTextKeyTyped

    private void valorUnitarioTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorUnitarioTextKeyTyped

    }//GEN-LAST:event_valorUnitarioTextKeyTyped

    private void quantidadeTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeTextKeyTyped
 
    }//GEN-LAST:event_quantidadeTextKeyTyped

    private void quantidadeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeTextKeyReleased

    }//GEN-LAST:event_quantidadeTextKeyReleased

    private void quantidadeTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeTextKeyPressed

    }//GEN-LAST:event_quantidadeTextKeyPressed

    private void quantidadeTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_quantidadeTextInputMethodTextChanged

    }//GEN-LAST:event_quantidadeTextInputMethodTextChanged

    private void quantidadeTextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_quantidadeTextStateChanged
        atualizarTotalProduto();
    }//GEN-LAST:event_quantidadeTextStateChanged

    private void nomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeTextActionPerformed

    }//GEN-LAST:event_nomeTextActionPerformed

    private void totalTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalTextActionPerformed

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        trocarTela();
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void descontoTextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_descontoTextMouseClicked
       calcularTotal();
    }//GEN-LAST:event_descontoTextMouseClicked

    private void pesquisarNomeProdutoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pesquisarNomeProdutoTextKeyReleased
        pesquisarNomeProduto();
    }//GEN-LAST:event_pesquisarNomeProdutoTextKeyReleased

    private void tabelaPesquisarProdutosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaPesquisarProdutosMouseClicked
        int linha = tabelaPesquisarProdutos.getSelectedRow();
        if (linha != -1) {
            try {
                BigInteger codigo = new BigInteger(tabelaPesquisarProdutos.getValueAt(linha, 0).toString());
                byte[] imagem = produto.buscarImagemProduto(codigo,this); // método que busca a imagem no banco
                funcao.exibirImagem(labelPesquisar,imagem,"/imagens/imgPedidoReposicaoPesquisarProduto.png"); // método que mostra a imagem no JLabel
            } catch (Exception ex) {
                funcao.exibirImagem(labelPesquisar,null,"/imagens/imgPedidoReposicaoPesquisarProduto.png");
            }
        }
    }//GEN-LAST:event_tabelaPesquisarProdutosMouseClicked

    private void botaoFormaDePagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoFormaDePagamentoActionPerformed
        addFormaDePagamento();
    }//GEN-LAST:event_botaoFormaDePagamentoActionPerformed

    private void botaoLimparCarrinhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparCarrinhoActionPerformed
        funcao.MensagemConfirmar(this, "Você realmente deseja excluir","todos produtos do Carrinho?", "LIMPAR CARRINHO", () -> limparCarrinho());
    }//GEN-LAST:event_botaoLimparCarrinhoActionPerformed

    private void botaoAdicionarCarrinhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarCarrinhoActionPerformed
        addCarrinho();
    }//GEN-LAST:event_botaoAdicionarCarrinhoActionPerformed

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
                java.util.logging.Logger.getLogger(RegistrarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(RegistrarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(RegistrarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(RegistrarPedido.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>
            //</editor-fold>
            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new RegistrarPedido().setVisible(true);
                }
            });
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bordaAdicionarCarrinho;
    private javax.swing.JPanel bordaLimparCarrinho;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoAdicionarCarrinho;
    private javax.swing.JButton botaoFormaDePagamento;
    private javax.swing.JButton botaoLimparCarrinho;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JSpinner codigoText;
    private javax.swing.JSpinner descontoText;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JDesktopPane jDesktopPane3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelCarrinhoVazio;
    private javax.swing.JLabel labelPesquisar;
    private javax.swing.JLabel labelSelecionarProduto;
    private javax.swing.JTextField nomeText;
    private javax.swing.JTextField pesquisarNomeProdutoText;
    private javax.swing.JSpinner quantidadeText;
    private javax.swing.JFormattedTextField subtotalText;
    private javax.swing.JTable tabelaCarrinhoDePedido;
    private javax.swing.JTable tabelaPesquisarProdutos;
    private javax.swing.JFormattedTextField totalText;
    private javax.swing.JFormattedTextField valorTotalText;
    private javax.swing.JFormattedTextField valorUnitarioText;
    // End of variables declaration//GEN-END:variables
}
