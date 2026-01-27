
package Tela.Reposicao;

import Classes.FornecedorReposicao;
import Classes.Funcoes;
import Classes.OverlayUtil;
import Classes.Produto;
import Classes.ReposicaoProduto;
import Tela.Inicio.MenuPrincipal;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.*;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.border.SoftBevelBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

@SuppressWarnings("serial")
public class RegistrarReposicao extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final  OverlayUtil OverlayUtil = new OverlayUtil();    
    private final  FornecedorReposicao fornecedorReposicao = new FornecedorReposicao();
    private final  Produto produto = new Produto();
    
    public RegistrarReposicao() {
        setUndecorated(true);
        initComponents();        
        montarTela();
        adicionarAtalhosTeclado();        
        funcao.bloquearArrastar(tabelaCarrinhoDeReposicao);
        montarData();
    }    
    
    private void montarData() {
        dataPedido.setDate(new Date());
        dataPedido.setDateFormatString("dd/MM/yyyy");
        dataPedido.setMaxSelectableDate(new Date());
        JTextFieldDateEditor editor = (JTextFieldDateEditor) dataPedido.getDateEditor();
        editor.setToolTipText(null);
        editor.setEditable(false);
        editor.setForeground(Color.BLACK);
        editor.setBackground(Color.WHITE);
        editor.setHorizontalAlignment(CENTER);
    }
    
    private void limparTudo(){
        fornecedorReposicao.setCodigo(null);
        fornecedorReposicao.setNome(null);
        limparCarrinho();
        limparCampos();
        atualizarEstadoComponentes();
        atualizarSelecaoFornecedor();
    }
    
    private void montarTela() {
        configurarTabelaCarrinho();
        funcao.configurarTabela(tabelaPesquisarProdutos, new Font("Tahoma", Font.BOLD, 12), Color.BLACK, new Color(200, 200, 200));
        
        quantidadeText.getEditor().getComponent(0).setForeground(Color.BLACK);
        codigoText.getEditor().getComponent(0).setForeground(Color.BLACK);
        botaoSelecionarFornecedor.requestFocus();
        labelChaveInvalida.setVisible(false);
        labelCarrinhoVazio.setVisible(false);
        funcao.aplicarEfeitoMouse(botaoMenu);
        funcao.aplicarEfeitoMouse(botaoAdicionarCarrinho);  
        funcao.aplicarEfeitoMouse(botaoSalvar);
        funcao.aplicarEfeitoMouse(botaoLimparCarrinho);  
        funcao.aplicarEfeitoMouse(botaoSelecionarFornecedor);
        configurarEntradaDeDados();
        atualizarEstadoComponentes();
        
        atualizarComponentes();
        configurarListeners();
    }
    
    private void configurarTabelaCarrinho() {
        JTableHeader header = tabelaCarrinhoDeReposicao.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 16));
        tabelaCarrinhoDeReposicao.getTableHeader().setReorderingAllowed(false);
        tabelaCarrinhoDeReposicao.setRowSorter(null);

        // Aplica azul escuro nas colunas de "CÓD" até "TOTAL (R$)"
        TableColumnModel columnModel = tabelaCarrinhoDeReposicao.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            String columnName = tabelaCarrinhoDeReposicao.getColumnName(i);
            TableColumn col = columnModel.getColumn(i);

            if (!columnName.equals("AÇÃO")) {
                col.setHeaderRenderer(new HeaderRenderer(new Color(0, 0, 102))); // azul escuro
            } else {
                col.setHeaderRenderer(new HeaderRenderer(new Color(70, 130, 180))); // azul claro para AÇÃO
            }
        }
        limparCarrinho();

        TableColumn colunaItem = tabelaCarrinhoDeReposicao.getColumn("AÇÃO");
        colunaItem.setCellRenderer(new BotaoRenderer());
        colunaItem.setCellEditor(new BotaoEditor(new JCheckBox(), tabelaCarrinhoDeReposicao));
        colunaItem.setMinWidth(100);
        colunaItem.setMaxWidth(100);
    }
    
    private void atualizarEstadoComponentes() {
        boolean fornecedorSelecionado = fornecedorReposicao.getCodigo() != null;
        boolean carrinhoTemItens = carrinhoTemItens();  

        pesquisarNomeProdutoText.setEnabled(fornecedorSelecionado);
        codigoText.setEnabled(fornecedorSelecionado);
        quantidadeText.setEnabled(fornecedorSelecionado);
        valorPagoText.setEnabled(fornecedorSelecionado);
        dataPedido.setEnabled(fornecedorSelecionado);
        
        radioSim.setEnabled(fornecedorSelecionado && carrinhoTemItens);
        radioNao.setEnabled(fornecedorSelecionado && carrinhoTemItens);

        botaoAdicionarCarrinho.setEnabled(funcao.validarCampoVazio(nomeText));
        botaoSalvar.setEnabled(reposicaoValidaParaSalvar());
        botaoLimparCarrinho.setEnabled(carrinhoTemItens);
        botaoAdicionarCarrinho.setOpaque(funcao.validarCampoVazio(nomeText));
        botaoSalvar.setOpaque(reposicaoValidaParaSalvar());
        botaoLimparCarrinho.setOpaque(carrinhoTemItens);
        labelFornecedorSelecionado.setVisible(!fornecedorSelecionado);
        labelCarrinhoVazio.setVisible(fornecedorSelecionado && !carrinhoTemItens);

        funcao.validarSpinnerComDica(codigoText, "Escolha o código do produto");
        funcao.validarSpinnerComDica(quantidadeText, "Escolha a quantidade desejada");
        funcao.validarTextFieldComDica(numeroChaveNFeText, "Digite o número da chave de acesso da NFe");
        atualizarChaveNFe();

        adicionarAtalhosTeclado();
        adicionarLegendasBotoes();
    }
    
    private void configurarEntradaDeDados(){
        funcao.entradaNumerica((JSpinner.DefaultEditor) quantidadeText.getEditor());
        funcao.entradaNumerica((JSpinner.DefaultEditor) codigoText.getEditor());
        funcao.entradaNumericaMonetaria(valorPagoText);
        funcao.aplicarMascaraChaveNFe(numeroChaveNFeText);
        funcao.limitarCaracteres(numeroChaveNFeText, 54);
        funcao.limitarCaracteres(valorPagoText, 13);
        funcao.limitarCaracteresMonetarios(valorPagoText, 13, 2);
    }
    
    private void limparCarrinho() {
        DefaultTableModel modeloCarrinho = (DefaultTableModel) tabelaCarrinhoDeReposicao.getModel();
        modeloCarrinho.setRowCount(0);
        calcularTotal();
        atualizarEstadoComponentes();
    }
    
    private void pesquisarNomeProduto() {
        BigInteger idFornecedor = fornecedorReposicao.getCodigo();
        String nomeParcial = pesquisarNomeProdutoText.getText();

        List<Produto> lista = produto.buscarProdutoPorFornecedor(idFornecedor, nomeParcial,this);

        DefaultTableModel modelo = (DefaultTableModel) tabelaPesquisarProdutos.getModel();
        modelo.setRowCount(0);
        DecimalFormat formatoDecimal = new DecimalFormat("#,##0.00");

        for (Produto p : lista) {
            String valorFormatado = formatoDecimal.format(p.getValorUnitario());

            modelo.addRow(new Object[]{
                p.getCodigo(),
                p.getNome(),
                p.getQuantidade(),
                valorFormatado
            });
        }

        
        if (!lista.isEmpty()) {
        byte[] imagem = produto.buscarImagemProduto(lista.get(0).getCodigo(),this);
            funcao.exibirImagem(labelPesquisar,imagem,"/imagens/imgPedidoReposicaoPesquisarProduto.png");
        } else {
            funcao.exibirImagem(labelPesquisar,null,"/imagens/imgPedidoReposicaoPesquisarProduto.png");
        }
        alinharTabelaPesquisarProdutos();
    }
    
    private void pesquisarCodigoProduto(){
        try {
            BigInteger codigo = new BigInteger(codigoText.getValue().toString());
            Produto produto = Produto.buscarProdutoPorCodigoEFornecedor(
                fornecedorReposicao.getCodigo(),
                codigo,
                this
            );

            if (produto != null) {
                nomeText.setText(produto.getNome());
                byte[] imagem = produto.buscarImagemProduto(codigo,this);
                funcao.exibirImagem(labelSelecionarProduto,imagem,"/imagens/imgPedidoReposicaoPesquisarProduto.png");
            } else {
                nomeText.setText(null);
                funcao.exibirImagem(labelSelecionarProduto,null,"/imagens/imgPedidoReposicaoPesquisarProduto.png");
            }
            
        } catch (NumberFormatException e) {
            nomeText.setText(null);
        }
        atualizarEstadoComponentes();
    }
    
    private void atualizarSelecaoFornecedor(){
        limparCarrinho();
        limparCampos();
        fornecedorTxt.setText(fornecedorReposicao.getNome());
        pesquisarNomeProduto();
        alinharTabelaPesquisarProdutos();
        pesquisarCodigoProduto();
        
        funcao.limparComponentes(pesquisarNomeProdutoText,numeroChaveNFeText);
        atualizarComponentes();
        
    }
    
    private void atualizarComponentes(){
        boolean fornecedorSelecionado = fornecedorReposicao.getCodigo() != null;
        if (fornecedorSelecionado){
            labelCarrinhoVazio.setVisible(true);
            codigoText.requestFocus();
            radioSim.setSelected(true);
        }else{
            radioNao.setSelected(true);
            labelCarrinhoVazio.setVisible(false);
            botaoSelecionarFornecedor.requestFocus();
        }  
    }
    
    private void limparCampos(){
        
        funcao.limparComponentes(nomeText, codigoText);
        quantidadeText.setValue(1);
        funcao.limparComponenteFormatadoNumerico(valorPagoText);
        atualizarEstadoComponentes();
    }
    
    private void alinharTabelaPesquisarProdutos() {
        DefaultTableCellRenderer alinhamentoEsquerda = new DefaultTableCellRenderer();
        alinhamentoEsquerda.setHorizontalAlignment(SwingConstants.CENTER);

        for (int i = 0; i < tabelaPesquisarProdutos.getColumnCount(); i++) {
            tabelaPesquisarProdutos.getColumnModel().getColumn(i).setCellRenderer(alinhamentoEsquerda);
        }
    }

    private void calcularTotal() {
        BigDecimal total = BigDecimal.ZERO;
        DefaultTableModel modeloCarrinho = (DefaultTableModel) tabelaCarrinhoDeReposicao.getModel();

        for (int i = 0; i < modeloCarrinho.getRowCount(); i++) {
            Object valorCelula = modeloCarrinho.getValueAt(i, 3);
            if (valorCelula != null && !valorCelula.toString().isEmpty()) {
                try {
                    // Remove a formatação de moeda antes de criar o BigDecimal
                    String valorStr = valorCelula.toString().replace(",", ".");
                    BigDecimal valor = new BigDecimal(valorStr);
                    total = total.add(valor);
                } catch (NumberFormatException e) {
                    // Ignora valores que não podem ser convertidos para números
                }
            }
        }

        // Formata o valor total para exibição
        valorTotalText.setText(String.format("%.2f", total));
        atualizarEstadoComponentes();
    }
  
    private boolean validarChaveNFe() {
        String chave = numeroChaveNFeText.getText().replaceAll("\\D", "");

        // A validação só é necessária se "Sim" estiver selecionado e o campo habilitado
        if (radioSim.isSelected() && numeroChaveNFeText.isEnabled()) {
            if (chave.length() != 44) {
                labelChaveInvalida.setVisible(true);
                return false;
            }
        }

        labelChaveInvalida.setVisible(false);
        return true;
    }

    private class HeaderRenderer extends DefaultTableCellRenderer {
        private final Color bgColor;

        public HeaderRenderer(Color bgColor) {
            this.bgColor = bgColor;
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(bgColor);
            setForeground(Color.WHITE);
                        
            setFont(new Font("Tahoma", Font.BOLD, 16));
            setHorizontalAlignment(CENTER);
            setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            return this;
        }
    }

    private class BotaoRenderer extends JButton implements TableCellRenderer {

        public BotaoRenderer() {
            setBackground(new Color(204, 0, 0));
            setForeground(new Color(255, 255, 255));
            setFont(new Font("Arial", 1, 14));
            setBorder(new SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
            setText("EXCLUIR");
            Icon  iconExcluir = new ImageIcon(getClass().getResource("/imagens/botaoExcluir.png"));
            setIcon(iconExcluir);
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
            
            Icon  iconExcluir = new ImageIcon(getClass().getResource("/imagens/botaoExcluir.png"));
            button.setIcon(iconExcluir);
            
            button.addActionListener(e -> {
                if (tabela.isEditing()) {
                    tabela.getCellEditor().stopCellEditing();
                }

                if (linhaSelecionada >= 0 && linhaSelecionada < tabela.getRowCount()) {
                    DefaultTableModel model = (DefaultTableModel) tabela.getModel();
                    model.removeRow(linhaSelecionada);
                    calcularTotal();
                    atualizarEstadoComponentes(); // Chama o método principal de atualização
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
    
    private void selecionarFornecedor() {
        labelChaveInvalida.setVisible(false);
        labelCarrinhoVazio.setVisible(false);
        RegistrarReposicaoFornecedor dialog = new RegistrarReposicaoFornecedor(this, false);
        
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                boolean fornecedorSelecionado = fornecedorReposicao.getCodigo() != null;
                if (fornecedorSelecionado){
                    
                    atualizarSelecaoFornecedor();
                    
                }            
            }
        });
        
        OverlayUtil.abrirTela(this, dialog);      
    }
    
    private void addCarrinho(){
        DefaultTableModel modeloCarrinho = (DefaultTableModel) tabelaCarrinhoDeReposicao.getModel();

        DefaultTableCellRenderer centralizado = new DefaultTableCellRenderer();
        centralizado.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tabelaCarrinhoDeReposicao.getColumnCount() - 1; i++) {
            tabelaCarrinhoDeReposicao.getColumnModel().getColumn(i).setCellRenderer(centralizado);
        }

        String codigo = codigoText.getValue().toString();
        String nome = nomeText.getText();

        int novaQuantidade = Integer.parseInt(quantidadeText.getValue().toString());
        double novoValor = Double.parseDouble(valorPagoText.getText().replace(",", "."));

        for (int i = 0; i < modeloCarrinho.getRowCount(); i++) {
            String codigoExistente = modeloCarrinho.getValueAt(i, 0).toString();
            if (codigoExistente.equals(codigo)) {
                int quantidadeExistente = Integer.parseInt(modeloCarrinho.getValueAt(i, 2).toString());
                double valorExistente = Double.parseDouble(modeloCarrinho.getValueAt(i, 3).toString().replace(",", "."));

                int novaQuantidadeTotal = quantidadeExistente + novaQuantidade;
                double novoValorTotal = valorExistente + novoValor;

                modeloCarrinho.setValueAt(novaQuantidadeTotal, i, 2);
                modeloCarrinho.setValueAt(String.format("%.2f", novoValorTotal), i, 3);

                calcularTotal();
                limparCampos();
                return;
            }
        }

        modeloCarrinho.addRow(new Object[]{
            codigo,
            nome,
            novaQuantidade,
            String.format("%.2f", novoValor),
            "EXCLUIR"
        });

        int colunaItem = tabelaCarrinhoDeReposicao.getColumnCount() - 1;
        TableColumn coluna = tabelaCarrinhoDeReposicao.getColumnModel().getColumn(colunaItem);
        coluna.setCellRenderer(new BotaoRenderer());
        coluna.setCellEditor(new BotaoEditor(new JCheckBox(), tabelaCarrinhoDeReposicao));

        ((AbstractTableModel) tabelaCarrinhoDeReposicao.getModel()).fireTableDataChanged();
        calcularTotal();
        limparCampos();
        atualizarEstadoComponentes();
        codigoText.requestFocus();
    }

    private boolean reposicaoValidaParaSalvar() {
        boolean fornecedorSelecionado = fornecedorReposicao.getCodigo() != null;
        
        return fornecedorSelecionado && carrinhoTemItens() && validarChaveNFe();
    }

    private boolean carrinhoTemItens() {
        return tabelaCarrinhoDeReposicao.getModel().getRowCount() > 0;
    }
    
    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoSalvar);
        funcao.adicionarAtalho(root, "F5", botaoLimparCarrinho);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
        funcao.adicionarAtalho(root, "F8", botaoAdicionarCarrinho);
        funcao.adicionarAtalho(root, "F2", botaoSelecionarFornecedor);
        funcao.adicionarAtalhoTeclado(root, "F4", () -> pesquisarNomeProdutoText.requestFocus());
    }
    
    private void salvarReposicao() {
        if(reposicaoValidaParaSalvar()){
            if (!validarChaveNFe()) {
                funcao.Mensagens(this, "Chave de Acesso NFe inválida!"," Verifique o número digitado.", "ATENÇÃO", "aviso");
                return; 
            }

            ReposicaoProduto reposicao = new ReposicaoProduto();
            reposicao.setData(dataPedido.getDate());
            reposicao.setFornecedorID(fornecedorReposicao.getCodigo());
            reposicao.setValorTotal(new BigDecimal(valorTotalText.getText().replace(",", ".")));

            String chave = numeroChaveNFeText.getText();
            if (chave != null && !chave.trim().isEmpty()) {
                chave = chave.replaceAll("\\D", "");
                if (chave.length() > 44) {
                    chave = chave.substring(0, 44);
                }
            } else {
                chave = null;
            }
            reposicao.setChaveAcessoNFe(chave);

            List<ReposicaoProduto.ItemReposicao> itens = new ArrayList<>();
            DefaultTableModel modelo = (DefaultTableModel) tabelaCarrinhoDeReposicao.getModel();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                ReposicaoProduto.ItemReposicao item = new ReposicaoProduto.ItemReposicao();
                item.produtoID = new BigInteger(modelo.getValueAt(i, 0).toString());
                item.quantidade = Integer.parseInt(modelo.getValueAt(i, 2).toString());
                item.valorCompra = new BigDecimal(modelo.getValueAt(i, 3).toString().replace(",", "."));
                itens.add(item);
            }

            if (reposicao.salvarReposicao(itens)) {
                funcao.Mensagens(this, "Reposição salva com sucesso","", "SUCESSO", "informacao");
                limparTudo();
            } else {
                funcao.Mensagens(this, "Erro ao salvar reposição!","", "ERRO", "erro");
            }
        }else{
            funcao.Mensagens(this, "Preencha os campos Corretamente","", "ATENÇÃO", "aviso");
        }
    }
    
    private void atualizarChaveNFe() {
        boolean carrinhoTemItens = carrinhoTemItens();

        if (carrinhoTemItens && radioSim.isSelected()) {
            numeroChaveNFeText.setEnabled(true);
            labelDigiteChave.setEnabled(true);
            numeroChaveNFeText.requestFocus();

            // Exibe a label de invalidez se o campo estiver vazio
            if (numeroChaveNFeText.getText().isEmpty()) {
                labelChaveInvalida.setVisible(true);
            } else {
                validarChaveNFe(); // Valida se já há texto
            }
        } else {
            numeroChaveNFeText.setEnabled(false);
            labelDigiteChave.setEnabled(false);
            funcao.limparComponente(numeroChaveNFeText);
            labelChaveInvalida.setVisible(false); // Sempre esconde quando "Não" está selecionado
        }
    }

    private void trocarTela(){
        limparTudo();
        funcao.trocarDeTela(this, new MenuPrincipal());
    }
    
    private void configurarListeners() {
        numeroChaveNFeText.getDocument().addDocumentListener(new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            validarChaveNFe(); // Continua validando a label
            atualizarEstadoComponentes(); // Adicionado para atualizar o botão Salvar
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validarChaveNFe(); // Continua validando a label
            atualizarEstadoComponentes(); // Adicionado para atualizar o botão Salvar
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validarChaveNFe(); // Continua validando a label
            atualizarEstadoComponentes(); // Adicionado para atualizar o botão Salvar
        }
    });
    }
    
    private void adicionarLegendasBotoes() {
        funcao.validarBotaoComDica(botaoSalvar, "Clique para salvar o registro da reposição de produto(s)");
        funcao.validarBotaoComDica(botaoAdicionarCarrinho, "Clique para adicionar o produto selecionado ao carrinho");
        funcao.validarBotaoComDica(botaoLimparCarrinho, "Clique para remover todos os produtos do carrinho");
        funcao.validarBotaoComDica(botaoSelecionarFornecedor, "Clique para selecionar um fornecedor");
        funcao.validarBotaoComDica(botaoMenu, "Clique para voltar para o menu principal");
        funcao.validarDataComDica(dataPedido, "Selecione data da reposição");      
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
        botaoMenu = new javax.swing.JButton();
        jDesktopPane5 = new javax.swing.JDesktopPane();
        fornecedorTxt = new javax.swing.JTextField();
        botaoSelecionarFornecedor = new javax.swing.JButton();
        labelFornecedorSelecionado = new javax.swing.JLabel();
        painelPesquisar = new javax.swing.JDesktopPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaPesquisarProdutos = new javax.swing.JTable();
        pesquisarNomeProdutoText = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        labelPesquisar = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        labelChaveInvalida = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        painelSelecionarProduto = new javax.swing.JDesktopPane();
        jLabel5 = new javax.swing.JLabel();
        quantidadeText = new javax.swing.JSpinner();
        codigoText = new javax.swing.JSpinner();
        jLabel11 = new javax.swing.JLabel();
        nomeText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        valorPagoText = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        labelSelecionarProduto = new javax.swing.JLabel();
        bordaAdicionarCarrinho = new javax.swing.JPanel();
        botaoAdicionarCarrinho = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jDesktopPane2 = new javax.swing.JDesktopPane();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaCarrinhoDeReposicao = new javax.swing.JTable();
        valorTotalText = new javax.swing.JFormattedTextField();
        jLabel17 = new javax.swing.JLabel();
        bordaLimparCarrinho = new javax.swing.JPanel();
        botaoLimparCarrinho = new javax.swing.JButton();
        painelCompra = new javax.swing.JDesktopPane();
        radioSim = new javax.swing.JRadioButton();
        radioNao = new javax.swing.JRadioButton();
        jLabel15 = new javax.swing.JLabel();
        labelDigiteChave = new javax.swing.JLabel();
        numeroChaveNFeText = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        labelCarrinhoVazio = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoSalvar = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        painelDataReposicao = new javax.swing.JDesktopPane();
        jLabel21 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        dataPedido = new com.toedter.calendar.JDateChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1070, 696));
        setSize(new java.awt.Dimension(1070, 696));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

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

        jDesktopPane5.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*FORNECEDOR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane5.setFocusCycleRoot(false);
        jDesktopPane5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fornecedorTxt.setEditable(false);
        fornecedorTxt.setBackground(new java.awt.Color(255, 255, 255));
        fornecedorTxt.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 18)); // NOI18N
        fornecedorTxt.setForeground(new java.awt.Color(51, 51, 51));
        fornecedorTxt.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fornecedorTxt.setFocusable(false);
        fornecedorTxt.setOpaque(true);
        fornecedorTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fornecedorTxtActionPerformed(evt);
            }
        });
        jDesktopPane5.add(fornecedorTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 300, 30));

        botaoSelecionarFornecedor.setBackground(new java.awt.Color(204, 204, 204));
        botaoSelecionarFornecedor.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botaoSelecionarFornecedor.setForeground(new java.awt.Color(0, 0, 0));
        botaoSelecionarFornecedor.setText("SELECIONAR");
        botaoSelecionarFornecedor.setToolTipText("Clique para selecionar um fornecedor");
        botaoSelecionarFornecedor.setBorder(null);
        botaoSelecionarFornecedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoSelecionarFornecedor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoSelecionarFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSelecionarFornecedorActionPerformed(evt);
            }
        });
        jDesktopPane5.add(botaoSelecionarFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 30, 140, 30));

        labelFornecedorSelecionado.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelFornecedorSelecionado.setForeground(new java.awt.Color(204, 0, 0));
        labelFornecedorSelecionado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFornecedorSelecionado.setText("*SELECIONE UM FORNECEDOR.");
        jDesktopPane5.add(labelFornecedorSelecionado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 450, 20));

        painelPesquisar.setBackground(new java.awt.Color(255, 255, 255));
        painelPesquisar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PESQUISAR PRODUTOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 22), new java.awt.Color(0, 0, 102))); // NOI18N
        painelPesquisar.setForeground(new java.awt.Color(0, 102, 153));
        painelPesquisar.setFocusCycleRoot(false);
        painelPesquisar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        painelPesquisar.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 30, 324, 110));

        pesquisarNomeProdutoText.setBackground(new java.awt.Color(255, 255, 255));
        pesquisarNomeProdutoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        pesquisarNomeProdutoText.setForeground(new java.awt.Color(0, 0, 0));
        pesquisarNomeProdutoText.setToolTipText("Pesquise produto(s) do fornecedor por nome");
        pesquisarNomeProdutoText.setEnabled(false);
        pesquisarNomeProdutoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pesquisarNomeProdutoTextKeyReleased(evt);
            }
        });
        painelPesquisar.add(pesquisarNomeProdutoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 360, -1));

        jLabel12.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Nome:");
        painelPesquisar.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 60, -1));

        labelPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imgPedidoReposicaoPesquisarProduto.png"))); // NOI18N
        painelPesquisar.add(labelPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 30, 110, 110));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/pesquisarRegistrar.png"))); // NOI18N
        jLabel20.setOpaque(true);
        painelPesquisar.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, -2, 30, 30));

        jDesktopPane5.add(painelPesquisar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 450, 180));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/fornecedorRegistrar.png"))); // NOI18N
        jLabel7.setOpaque(true);
        jDesktopPane5.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(209, 0, 30, 30));

        jPanel1.add(jDesktopPane5);
        jDesktopPane5.setBounds(10, 80, 470, 270);

        labelChaveInvalida.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelChaveInvalida.setForeground(new java.awt.Color(204, 0, 0));
        labelChaveInvalida.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelChaveInvalida.setText("*A CHAVE DA NFE DEVE CONTER EXATAMENTE 44 NÚMEROS.");
        jPanel1.add(labelChaveInvalida);
        labelChaveInvalida.setBounds(490, 590, 570, 20);

        jPanel2.setBackground(new java.awt.Color(210, 164, 2));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/Reposicao.png"))); // NOI18N
        jLabel4.setText("Chave da NFe inválida! Ela deve conter exatamente 44 números.");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        jLabel3.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("RESGISTRO DE REPOSIÇÃO");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1070, -1));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 1070, 60);

        painelSelecionarProduto.setBackground(new java.awt.Color(255, 255, 255));
        painelSelecionarProduto.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*SELECIONAR PRODUTO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelSelecionarProduto.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Quantidade:");
        painelSelecionarProduto.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 130, -1));

        quantidadeText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        quantidadeText.setModel(new javax.swing.SpinnerNumberModel(1, 1, 999999999, 1));
        quantidadeText.setToolTipText("");
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
        painelSelecionarProduto.add(quantidadeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, 80, -1));

        codigoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        codigoText.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), Long.valueOf(9223372036854775807L), Long.valueOf(1L)));
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
        painelSelecionarProduto.add(codigoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, 80, -1));

        jLabel11.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Código:");
        painelSelecionarProduto.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 140, -1));

        nomeText.setEditable(false);
        nomeText.setBackground(new java.awt.Color(255, 255, 255));
        nomeText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 18)); // NOI18N
        nomeText.setForeground(new java.awt.Color(51, 51, 51));
        nomeText.setFocusable(false);
        nomeText.setOpaque(true);
        nomeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeTextActionPerformed(evt);
            }
        });
        painelSelecionarProduto.add(nomeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 290, -1));

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Nome:");
        painelSelecionarProduto.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 140, -1));

        valorPagoText.setBackground(new java.awt.Color(255, 255, 255));
        valorPagoText.setForeground(new java.awt.Color(0, 0, 0));
        valorPagoText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        valorPagoText.setText("0,00");
        valorPagoText.setToolTipText("Digite valor pago no produto");
        valorPagoText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        valorPagoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorPagoTextKeyTyped(evt);
            }
        });
        painelSelecionarProduto.add(valorPagoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 105, 160, -1));

        jLabel10.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Valor Pago (R$):");
        painelSelecionarProduto.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 105, 140, -1));

        labelSelecionarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imgPedidoReposicaoPesquisarProduto.png"))); // NOI18N
        painelSelecionarProduto.add(labelSelecionarProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, -1, -1));

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
        botaoAdicionarCarrinho.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        botaoAdicionarCarrinho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAdicionarCarrinhoActionPerformed(evt);
            }
        });
        bordaAdicionarCarrinho.add(botaoAdicionarCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 70));

        painelSelecionarProduto.add(bordaAdicionarCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 142, 300, 70));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/produtoRegistrar.png"))); // NOI18N
        jLabel8.setOpaque(true);
        painelSelecionarProduto.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(335, 0, 30, 30));

        jPanel1.add(painelSelecionarProduto);
        painelSelecionarProduto.setBounds(10, 450, 470, 220);

        jDesktopPane2.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*CARRINHO DE REPOSIÇÃO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        jDesktopPane2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/carrinho.png"))); // NOI18N
        jLabel6.setOpaque(true);
        jDesktopPane2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 30, 30));

        tabelaCarrinhoDeReposicao.setAutoCreateRowSorter(true);
        tabelaCarrinhoDeReposicao.setBackground(new java.awt.Color(255, 255, 255));
        tabelaCarrinhoDeReposicao.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaCarrinhoDeReposicao.setForeground(new java.awt.Color(51, 51, 51));
        tabelaCarrinhoDeReposicao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CÓD", "PRODUTO", "QTD", "TOTAL (R$)", "AÇÃO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaCarrinhoDeReposicao.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaCarrinhoDeReposicao.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaCarrinhoDeReposicao.getTableHeader().setReorderingAllowed(false);
        tabelaCarrinhoDeReposicao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelaCarrinhoDeReposicaoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tabelaCarrinhoDeReposicaoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tabelaCarrinhoDeReposicaoKeyTyped(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaCarrinhoDeReposicao);
        if (tabelaCarrinhoDeReposicao.getColumnModel().getColumnCount() > 0) {
            tabelaCarrinhoDeReposicao.getColumnModel().getColumn(0).setPreferredWidth(10);
            tabelaCarrinhoDeReposicao.getColumnModel().getColumn(2).setPreferredWidth(10);
            tabelaCarrinhoDeReposicao.getColumnModel().getColumn(4).setResizable(false);
        }

        jDesktopPane2.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 550, 220));

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
        jDesktopPane2.add(valorTotalText, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 310, 230, -1));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("VALOR DA COMPRA (R$)");
        jDesktopPane2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 320, -1));

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

        jDesktopPane2.add(bordaLimparCarrinho, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, 550, 40));

        jPanel1.add(jDesktopPane2);
        jDesktopPane2.setBounds(490, 80, 570, 360);

        painelCompra.setBackground(new java.awt.Color(255, 255, 255));
        painelCompra.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "*COMPRA", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelCompra.setFocusCycleRoot(false);
        painelCompra.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        radioSim.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioSim);
        radioSim.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioSim.setForeground(new java.awt.Color(0, 0, 0));
        radioSim.setText("Sim, vou inserir");
        radioSim.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                radioSimStateChanged(evt);
            }
        });
        radioSim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioSimActionPerformed(evt);
            }
        });
        painelCompra.add(radioSim, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 54, -1, -1));

        radioNao.setBackground(new java.awt.Color(255, 255, 255));
        buttonGroup1.add(radioNao);
        radioNao.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        radioNao.setForeground(new java.awt.Color(0, 0, 0));
        radioNao.setSelected(true);
        radioNao.setText("Não, sem Nota Fiscal");
        radioNao.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                radioNaoStateChanged(evt);
            }
        });
        radioNao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioNaoActionPerformed(evt);
            }
        });
        painelCompra.add(radioNao, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 54, -1, -1));

        jLabel15.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Selecione se deseja inserir o número da Nota Fiscal:");
        painelCompra.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 32, 550, 20));

        labelDigiteChave.setFont(new java.awt.Font("Century Gothic", 1, 18)); // NOI18N
        labelDigiteChave.setForeground(new java.awt.Color(0, 0, 0));
        labelDigiteChave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDigiteChave.setText("Digite Nº da Chave de Acesso da NFe:");
        painelCompra.add(labelDigiteChave, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 77, 550, 30));

        numeroChaveNFeText.setBackground(new java.awt.Color(255, 255, 255));
        numeroChaveNFeText.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        numeroChaveNFeText.setForeground(new java.awt.Color(0, 0, 0));
        numeroChaveNFeText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        numeroChaveNFeText.setToolTipText("");
        numeroChaveNFeText.setEnabled(false);
        numeroChaveNFeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numeroChaveNFeTextActionPerformed(evt);
            }
        });
        numeroChaveNFeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numeroChaveNFeTextKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                numeroChaveNFeTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                numeroChaveNFeTextKeyTyped(evt);
            }
        });
        painelCompra.add(numeroChaveNFeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 104, 510, -1));

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/compra.png"))); // NOI18N
        jLabel9.setOpaque(true);
        painelCompra.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 30, 30));

        jPanel1.add(painelCompra);
        painelCompra.setBounds(490, 450, 570, 140);
        painelCompra.getAccessibleContext().setAccessibleName("SELECIONAR FORNECEDOR");

        labelCarrinhoVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelCarrinhoVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelCarrinhoVazio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCarrinhoVazio.setText("*ADICIONE PELO MENOS UM PRODUTO AO CARRINHO.");
        jPanel1.add(labelCarrinhoVazio);
        labelCarrinhoVazio.setBounds(10, 670, 470, 20);

        jLabel18.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 102, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("ATALHOS: F1 = Salvar Registro da Reposição | F2 = Selecionar Fornecedor | F4 = Pesquisar Nome do Produto | F5 = Limpar Carrinho |  F8 = Adicionar Produto ao Carrinho | F12 = Voltar ao Menu Principal");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(0, 60, 1070, 16);

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
        bordaSalvar.setBounds(490, 630, 91, 62);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("*campo(s) obrigatório(s)");
        jPanel1.add(jLabel19);
        jLabel19.setBounds(480, 660, 590, 17);

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DATA DA REPOSIÇÃO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/DATA.png"))); // NOI18N
        jLabel21.setOpaque(true);
        painelDataReposicao.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 30, 30));

        jLabel13.setFont(new java.awt.Font("Century Gothic", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Selecione:");
        painelDataReposicao.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 160, -1));

        dataPedido.setBackground(new java.awt.Color(255, 255, 255));
        dataPedido.setForeground(new java.awt.Color(0, 0, 0));
        dataPedido.setToolTipText("");
        dataPedido.setDateFormatString("");
        dataPedido.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        dataPedido.setOpaque(false);
        painelDataReposicao.add(dataPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 36, 240, 30));

        jPanel1.add(painelDataReposicao);
        painelDataReposicao.setBounds(10, 360, 470, 80);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1070, 696);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void tabelaCarrinhoDeReposicaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaCarrinhoDeReposicaoKeyPressed
        
    }//GEN-LAST:event_tabelaCarrinhoDeReposicaoKeyPressed

    private void botaoLimparCarrinhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparCarrinhoActionPerformed
       funcao.MensagemConfirmar(this, "Você realmente deseja excluir","todos produtos do Carrinho?", "LIMPAR CARRINHO", () -> limparCarrinho());
    }//GEN-LAST:event_botaoLimparCarrinhoActionPerformed

    private void valorTotalTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorTotalTextKeyTyped

    }//GEN-LAST:event_valorTotalTextKeyTyped

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
        codigoText.requestFocus();
    }//GEN-LAST:event_codigoTextStateChanged

    private void valorPagoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorPagoTextKeyTyped
        configurarEntradaDeDados();
    }//GEN-LAST:event_valorPagoTextKeyTyped

    private void botaoAdicionarCarrinhoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarCarrinhoActionPerformed
        addCarrinho();
    }//GEN-LAST:event_botaoAdicionarCarrinhoActionPerformed

    private void quantidadeTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeTextKeyTyped
 
    }//GEN-LAST:event_quantidadeTextKeyTyped

    private void quantidadeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeTextKeyReleased

    }//GEN-LAST:event_quantidadeTextKeyReleased

    private void quantidadeTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeTextKeyPressed

    }//GEN-LAST:event_quantidadeTextKeyPressed

    private void quantidadeTextInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_quantidadeTextInputMethodTextChanged

    }//GEN-LAST:event_quantidadeTextInputMethodTextChanged

    private void quantidadeTextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_quantidadeTextStateChanged

    }//GEN-LAST:event_quantidadeTextStateChanged

    private void nomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeTextActionPerformed

    }//GEN-LAST:event_nomeTextActionPerformed

    private void fornecedorTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fornecedorTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fornecedorTxtActionPerformed

    private void botaoSelecionarFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSelecionarFornecedorActionPerformed
        selecionarFornecedor();
    }//GEN-LAST:event_botaoSelecionarFornecedorActionPerformed

    private void tabelaCarrinhoDeReposicaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaCarrinhoDeReposicaoKeyReleased
       
    }//GEN-LAST:event_tabelaCarrinhoDeReposicaoKeyReleased

    private void tabelaCarrinhoDeReposicaoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaCarrinhoDeReposicaoKeyTyped
        
    }//GEN-LAST:event_tabelaCarrinhoDeReposicaoKeyTyped

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
       trocarTela();
    }//GEN-LAST:event_botaoMenuActionPerformed

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

    private void radioSimStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_radioSimStateChanged
       
    }//GEN-LAST:event_radioSimStateChanged

    private void radioNaoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_radioNaoStateChanged
       
    }//GEN-LAST:event_radioNaoStateChanged

    private void radioSimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioSimActionPerformed
         atualizarChaveNFe();
         atualizarEstadoComponentes();
    }//GEN-LAST:event_radioSimActionPerformed

    private void radioNaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioNaoActionPerformed
         atualizarChaveNFe();
         atualizarEstadoComponentes();
    }//GEN-LAST:event_radioNaoActionPerformed

    private void numeroChaveNFeTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numeroChaveNFeTextKeyTyped
        configurarEntradaDeDados();
        atualizarEstadoComponentes();
    }//GEN-LAST:event_numeroChaveNFeTextKeyTyped

    private void numeroChaveNFeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numeroChaveNFeTextKeyReleased
        configurarEntradaDeDados();
        atualizarEstadoComponentes();
    }//GEN-LAST:event_numeroChaveNFeTextKeyReleased

    private void numeroChaveNFeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numeroChaveNFeTextActionPerformed
        configurarEntradaDeDados();
        atualizarEstadoComponentes();
    }//GEN-LAST:event_numeroChaveNFeTextActionPerformed

    private void numeroChaveNFeTextKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numeroChaveNFeTextKeyPressed
        configurarEntradaDeDados();
        atualizarEstadoComponentes();
    }//GEN-LAST:event_numeroChaveNFeTextKeyPressed

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        salvarReposicao();
    }//GEN-LAST:event_botaoSalvarActionPerformed

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
                java.util.logging.Logger.getLogger(RegistrarReposicao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(RegistrarReposicao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(RegistrarReposicao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(RegistrarReposicao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new RegistrarReposicao().setVisible(true);
                }
            });
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bordaAdicionarCarrinho;
    private javax.swing.JPanel bordaLimparCarrinho;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoAdicionarCarrinho;
    private javax.swing.JButton botaoLimparCarrinho;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JButton botaoSalvar;
    private javax.swing.JButton botaoSelecionarFornecedor;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JSpinner codigoText;
    private com.toedter.calendar.JDateChooser dataPedido;
    private javax.swing.JTextField fornecedorTxt;
    private javax.swing.JDesktopPane jDesktopPane2;
    private javax.swing.JDesktopPane jDesktopPane5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelCarrinhoVazio;
    private javax.swing.JLabel labelChaveInvalida;
    private javax.swing.JLabel labelDigiteChave;
    private javax.swing.JLabel labelFornecedorSelecionado;
    private javax.swing.JLabel labelPesquisar;
    private javax.swing.JLabel labelSelecionarProduto;
    private javax.swing.JTextField nomeText;
    private javax.swing.JTextField numeroChaveNFeText;
    private javax.swing.JDesktopPane painelCompra;
    private javax.swing.JDesktopPane painelDataReposicao;
    private javax.swing.JDesktopPane painelPesquisar;
    private javax.swing.JDesktopPane painelSelecionarProduto;
    private javax.swing.JTextField pesquisarNomeProdutoText;
    private javax.swing.JSpinner quantidadeText;
    private javax.swing.JRadioButton radioNao;
    private javax.swing.JRadioButton radioSim;
    private javax.swing.JTable tabelaCarrinhoDeReposicao;
    private javax.swing.JTable tabelaPesquisarProdutos;
    private javax.swing.JFormattedTextField valorPagoText;
    private javax.swing.JFormattedTextField valorTotalText;
    // End of variables declaration//GEN-END:variables
}
