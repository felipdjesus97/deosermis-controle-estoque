
package Tela.Produto;

import Classes.OpcaoDeTela;
import Classes.Fornecedor;
import Classes.Funcoes;
import Classes.Produto;
import Tela.Inicio.MenuPrincipal;
import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JRootPane;
import javax.swing.JSpinner;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

@SuppressWarnings("serial")
public class CadastrarEditarProduto extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final Fornecedor fornecedor = new Fornecedor();
    private final Produto produto = new Produto();
    private final OpcaoDeTela cadastrarEditar = new OpcaoDeTela();
    private byte[] imagemBytes = null;
    private byte[] imagemOriginalBytes = null;

    public CadastrarEditarProduto() {
        setUndecorated(true);
        initComponents();
        carregarFornecedores();
        montarTela();
        adicionarAtalhosTeclado();
        funcao.bloquearArrastar(tabelaFornecedores);
    }

    private void montarTela() {
        JTableHeader header = tabelaFornecedores.getTableHeader();
        header.setFont(new Font("Tahoma", Font.BOLD, 14));
        header.setForeground(Color.white);
        header.setBackground(new Color(0, 0, 102));
        tabelaFornecedores.getTableHeader().setReorderingAllowed(false);
        tabelaFornecedores.setRowSorter(null);

        funcao.entradaNumericaMonetaria(valorUnitarioText);
        funcao.entradaNumerica((JSpinner.DefaultEditor) quantidadeMinimaText.getEditor());
        quantidadeMinimaText.getEditor().getComponent(0).setForeground(Color.BLACK);

        nomeText.requestFocus();
       
        funcao.aplicarEfeitoMouse(botaoLimpar);
        funcao.aplicarEfeitoMouse(botaoMenu);
        funcao.aplicarEfeitoMouse(botaoSalvar);
        funcao.aplicarEfeitoMouse(botaoCarregarImagem);
        funcao.aplicarEfeitoMouse(boataoImagemPadrao);
        funcao.aplicarEfeitoMouse(boataoRestaurarIMG);
        cadastroEdicao();
        digitacao();
        validarCampos();
    }

    private void cadastroEdicao() {
        if (cadastrarEditar.getCadastro()) {
            limparCampos();
            labelTextoTitulo.setText("CADASTRAR PRODUTO");
            labelAtalhos.setText("ATALHOS: F1 = Salvar Cadastro do Produto | F5 = Limpar Campos | F8 = Sem Imagem | F9 = Carregar Imagem | F12 = Menu Principal");
            botaoMenu.setText("MENU");
            botaoMenu.setIcon(new ImageIcon(getClass().getResource("/imagens/home.png")));
            botaoSalvar.setText("SALVAR CADASTRO");
            botaoSalvar.setIcon(new ImageIcon(getClass().getResource("/imagens/SALVARcad.png")));
            botaoLimpar.setText("LIMPAR CAMPOS");
            IconeTitulo.setIcon(new ImageIcon(getClass().getResource("/imagens/ADDproduto.png")));
            botaoMenu.setToolTipText("Clique para voltar ao menu principal");
            boataoRestaurarIMG.setVisible(false);
            boataoRestaurarIMG.setEnabled(false);
            bordaRestaurarIMG.setVisible(false);
        } else {
            preencherCampos();
            labelTextoTitulo.setText("EDITAR PRODUTO");
            labelAtalhos.setText("ATALHOS: F1 = Salvar Edição do Produto | F5 = Desfazer Alterações | F6 = Restaurar Imagem Salva | F8 = Sem Imagem | F9 = Carregar Imagem | F12 = Voltar Para Consultar Produto");
            botaoMenu.setText("VOLTAR");
            botaoMenu.setIcon(new ImageIcon(getClass().getResource("/imagens/VOLTAR.png")));
            botaoSalvar.setText("SALVAR EDIÇÃO");
            botaoSalvar.setIcon(new ImageIcon(getClass().getResource("/imagens/SALVARedit.png")));
            botaoLimpar.setText("DESFAZER ALTERAÇÕES");
            IconeTitulo.setIcon(new ImageIcon(getClass().getResource("/imagens/produto.png")));
            botaoMenu.setToolTipText("Clique para voltar a consultar produto");
            boataoRestaurarIMG.setVisible(true);
            boataoRestaurarIMG.setEnabled(true);
            bordaRestaurarIMG.setVisible(true);
            boataoRestaurarIMG.setToolTipText("Clique para restaurar imagem salva");
            boataoRestaurarIMG.setText("RESTAURAR IMAGEM");
        }
        boataoImagemPadrao.setToolTipText("Clique para deixar sem imagem");
        boataoImagemPadrao.setText("SEM IMAGEM");
        codigoText.setVisible(!cadastrarEditar.getCadastro());
        labelCodigoFornecedor.setVisible(!cadastrarEditar.getCadastro());

        digitacao();
    }
    
    private void preencherCampos() {
        if (produto.consultarProduto(produto.getCodigoAlterar(),this)) {
            codigoText.setText(produto.getCodigo().toString());
            nomeText.setText(produto.getNome());
            quantidadeMinimaText.setValue(produto.getQuantidadeMinima().intValue());
            valorUnitarioText.setText(funcao.formatarValor(produto.getValorUnitario()));

            // Carregar fornecedores selecionados
            marcarFornecedores(produto.buscarFornecedoresDoProduto(produto.getCodigoAlterar(),this));

            // Carregar imagem
            imagemOriginalBytes = produto.buscarImagemProduto(produto.getCodigoAlterar(),this);
            imagemBytes = imagemOriginalBytes;
            if (imagemBytes != null) {
                funcao.byteToImage(imagemProduto, imagemBytes);
            } else {
                limparImagem();
            }
        } else {
            funcao.Mensagens(this, "Produto não encontrado", "","ERRO", "erro");
            trocarTela();
        }
    }
    
    private void marcarFornecedores(List<BigInteger> fornecedoresDoProduto) {
        DefaultTableModel model = (DefaultTableModel) tabelaFornecedores.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            BigInteger idFornecedor = new BigInteger(model.getValueAt(i, 2).toString());
            if (fornecedoresDoProduto.contains(idFornecedor)) {
                model.setValueAt(true, i, 0);
            } else {
                model.setValueAt(false, i, 0);
            }
        }
    }

    private void carregarFornecedores() {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Selecionar", "Nome", "ID"}, 0
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
    }

    private List<BigInteger> getFornecedoresSelecionados() {
        List<BigInteger> fornecedoresSelecionados = new ArrayList<>();
        DefaultTableModel model = (DefaultTableModel) tabelaFornecedores.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean selecionado = (Boolean) model.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selecionado)) {
                String idStr = model.getValueAt(i, 2).toString();
                fornecedoresSelecionados.add(new BigInteger(idStr));
            }
        }
        return fornecedoresSelecionados;
    }

    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoSalvar);
        funcao.adicionarAtalho(root, "F5", botaoLimpar);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
        funcao.adicionarAtalho(root, "F8", boataoImagemPadrao);
        funcao.adicionarAtalho(root, "F9", botaoCarregarImagem);
        funcao.adicionarAtalho(root, "F6", boataoRestaurarIMG);
    }

    private void escolherIMG() {
        imagemBytes = funcao.carregarImagem(this, imagemProduto);
        funcaoEnabled();
    }

    private void funcaoEnabled() {
        labelNomeVazio.setVisible(!funcao.validarCampoVazio(nomeText));
        labelFornecedorVazio.setVisible(!fornecedorSelecionado());
        botaoSalvar.setEnabled(enabledSalvar());
        botaoSalvar.setOpaque(enabledSalvar());
        botaoLimpar.setEnabled(enabledLimpar());
        botaoLimpar.setOpaque(enabledLimpar());
    }

    private boolean elegivelSalvar() {
        return funcao.validarCampoVazio(nomeText) && fornecedorSelecionado();
    }

    private boolean fornecedorSelecionado() {
        return !getFornecedoresSelecionados().isEmpty();
    }

    private boolean enabledSalvar() {
        if (cadastrarEditar.getCadastro()) {
            funcao.validarBotaoComDica(botaoSalvar, "Clique para salvar o cadastro do produto");
            return elegivelSalvar();
        } else {
            funcao.validarBotaoComDica(botaoSalvar, "Clique para salvar edição do produto");
            return validarAlteracoes() && elegivelSalvar();
        }
    }

    private boolean enabledLimpar() {
        if (cadastrarEditar.getCadastro()) {
            funcao.validarBotaoComDica(botaoLimpar, "Clique para limpar os campos");
            return !camposVazio();
        } else {
            funcao.validarBotaoComDica(botaoLimpar, "Clique para desfazer alterações");
            return validarAlteracoes();
        }
    }

    private boolean camposVazio() {
        return !funcao.validarCampoVazio(nomeText)
                && getFornecedoresSelecionados().isEmpty()
                && imagemBytes == null;
    }

    private boolean validarAlteracoes() {
        Produto produtoOriginal = new Produto();
        if (produto.getCodigoAlterar() == null || !produtoOriginal.consultarProduto(produto.getCodigoAlterar(),this)) {
            return false;
        }

        boolean nomeAlterado = !nomeText.getText().trim().equalsIgnoreCase(produtoOriginal.getNome());
        boolean qtdMinimaAlterada = !BigInteger.valueOf(((Number) quantidadeMinimaText.getValue()).longValue()).equals(produtoOriginal.getQuantidadeMinima());
        boolean valorAlterado = !new BigDecimal(valorUnitarioText.getText().trim().replace(",", ".")).equals(produtoOriginal.getValorUnitario());
        boolean fornecedoresAlterados = !getFornecedoresSelecionados().equals(produtoOriginal.buscarFornecedoresDoProduto(produtoOriginal.getCodigoAlterar(),this));
        boolean imagemAlterada = !Arrays.equals(imagemBytes, imagemOriginalBytes);

        return nomeAlterado || qtdMinimaAlterada || valorAlterado || fornecedoresAlterados || imagemAlterada;
    }

    private void digitacao() {
        funcao.limitarCaracteres(nomeText, 50);
        funcao.limitarCaracteres(valorUnitarioText, 13);
        funcao.limitarCaracteresMonetarios(valorUnitarioText, 13, 2);
        funcao.limitarCaracteresJSpinner(quantidadeMinimaText, 9);
        validarCampos();
        funcaoEnabled();
    }

    private void salvarCadastrarProduto() {
       try {
            String nomeProduto = nomeText.getText().trim();
            String valorStr = valorUnitarioText.getText().trim().replace(",", ".");
            Number qtdMin = (Number) quantidadeMinimaText.getValue();
            List<BigInteger> fornecedoresSelecionados = getFornecedoresSelecionados();

            if (produto.produtoExiste(nomeProduto)) {
                funcao.Mensagens(this, "Já existe um produto com esse nome.", "","ATENÇÃO", "aviso");
                nomeText.requestFocus();
                return;
            }

            produto.setNome(nomeProduto);
            produto.setQuantidade(BigInteger.ZERO);
            produto.setQuantidadeMinima(BigInteger.valueOf(qtdMin.longValue()));
            produto.setValorUnitario(new BigDecimal(valorStr));
            produto.setFornecedoresSelecionados(fornecedoresSelecionados);

            if (produto.inserirProduto(imagemBytes,this)) {
                funcao.Mensagens(this, "Produto cadastrado com sucesso", "","SUCESSO", "informacao");
                limparCampos();
            } else {
                funcao.Mensagens(this, "Erro ao cadastrar produto.", "", "ERRO", "erro");
            }
        } catch (Exception e) {
            funcao.Mensagens(this, "Erro: " , e.getMessage(), "ERRO", "erro");
        }
    }
    
    private void salvarEditarProduto() {
        try {
            String nomeProduto = nomeText.getText().trim();
            if (produto.produtoExiste(nomeProduto, produto.getCodigoAlterar())) {
                funcao.Mensagens(this, "Já existe um produto com este nome.","", "ATENÇÃO", "aviso");
                nomeText.requestFocus();
                return;
            }

            produto.setCodigo(produto.getCodigoAlterar());
            produto.setNome(nomeProduto);
            produto.setQuantidadeMinima(BigInteger.valueOf(((Number) quantidadeMinimaText.getValue()).longValue()));
            produto.setValorUnitario(new BigDecimal(valorUnitarioText.getText().trim().replace(",", ".")));
            produto.setFornecedoresSelecionados(getFornecedoresSelecionados());
            
            produto.alterarProduto(imagemBytes,this);
            funcao.Mensagens(this,  "Produto atualizado com sucesso","", "SUCESSO", "informacao");
            trocarTela();
        
        } catch (Exception e) {
            funcao.Mensagens(this, "Erro ao salvar edição do produto: " , e.getMessage(), "ERRO", "erro");
            return;
        }finally{ 
            produto.setCodigoAlterar(null);
        }
    }

    private void limparCampos() {
        funcao.limparComponentes(nomeText, quantidadeMinimaText);
        funcao.limparComponenteFormatadoNumerico(valorUnitarioText);

        if (tabelaFornecedores.isEditing()) {
            tabelaFornecedores.getCellEditor().stopCellEditing();
        }

        tabelaFornecedores.clearSelection();

        DefaultTableModel model = (DefaultTableModel) tabelaFornecedores.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 0);
        }

        model.fireTableRowsUpdated(0, model.getRowCount() - 1);
        limparImagem();
        funcaoEnabled();
    }
    
    private void limparImagem() {
        imagemPadrao();
        funcaoEnabled();
    }
             
    private void restaurarImagem() {
        imagemBytes = produto.buscarImagemProduto(produto.getCodigoAlterar(),this);
        if (imagemBytes != null) {
            funcao.byteToImage(imagemProduto, imagemBytes);
        } else {
            imagemPadrao();
        }
        funcaoEnabled();
    }
    
    private void imagemPadrao(){
        imagemProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imgProduto.png")));
        imagemBytes = null;
    }

    private void trocarTela() {
        if (cadastrarEditar.getCadastro()) {
            funcao.trocarDeTela(this, new MenuPrincipal());
        } else {
            funcao.trocarDeTela(this, new ConsultarProduto());
        }
    }

    private void salvar() {
        validarCampos();
        if(funcao.validarCampoVazio(nomeText) && funcao.validarCampoVazio(valorUnitarioText) && fornecedorSelecionado()){
            if (cadastrarEditar.getCadastro()) {
                salvarCadastrarProduto();
            } else {
                salvarEditarProduto();
            }
            nomeText.requestFocus();
        }else{
            funcao.Mensagens(this, "Preencha os campos corretamente!", "","ATENÇÃO", "aviso");
             funcaoEnabled();
        }
    }

    private void limpar() {
        if (cadastrarEditar.getCadastro()) {
            limparCampos();
        } else {
            preencherCampos();
        }
        nomeText.requestFocus();
        funcaoEnabled();
    }
    
    private void validarCampos(){
        funcao.validarTexto(nomeText, 50);
        funcao.validarNumero(valorUnitarioText, 13, 2);
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
        IconeTitulo = new javax.swing.JLabel();
        codigoText = new javax.swing.JTextField();
        labelCodigoFornecedor = new javax.swing.JLabel();
        labelTextoTitulo = new javax.swing.JLabel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        nomeText = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaFornecedores = new javax.swing.JTable();
        quantidadeMinimaText = new javax.swing.JSpinner();
        imagemProduto = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        botaoCarregarImagem = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        labelFornecedorVazio = new javax.swing.JLabel();
        labelNomeVazio = new javax.swing.JLabel();
        boataoImagemPadrao = new javax.swing.JButton();
        valorUnitarioText = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();
        labelAtalhos = new javax.swing.JLabel();
        bordaLimpar = new javax.swing.JPanel();
        botaoLimpar = new javax.swing.JButton();
        bordaSalvar = new javax.swing.JPanel();
        botaoSalvar = new javax.swing.JButton();
        bordaRestaurarIMG = new javax.swing.JPanel();
        boataoRestaurarIMG = new javax.swing.JButton();

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

        IconeTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ADDproduto.png"))); // NOI18N
        jPanel2.add(IconeTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        codigoText.setEditable(false);
        codigoText.setBackground(new java.awt.Color(255, 255, 255));
        codigoText.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        codigoText.setForeground(new java.awt.Color(0, 0, 0));
        codigoText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        codigoText.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        codigoText.setFocusable(false);
        codigoText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                codigoTextFocusLost(evt);
            }
        });
        codigoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                codigoTextKeyReleased(evt);
            }
        });
        jPanel2.add(codigoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 25, 240, 30));

        labelCodigoFornecedor.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelCodigoFornecedor.setForeground(new java.awt.Color(0, 0, 0));
        labelCodigoFornecedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCodigoFornecedor.setText("CÓDIGO:");
        jPanel2.add(labelCodigoFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 0, 240, 30));

        labelTextoTitulo.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        labelTextoTitulo.setForeground(new java.awt.Color(0, 0, 0));
        labelTextoTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTextoTitulo.setText("EDITAR PRODUTO");
        jPanel2.add(labelTextoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1070, -1));

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 1070, 60);

        jDesktopPane1.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jDesktopPane1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("*Fornecedor:");
        jDesktopPane1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 130, 160, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Imagem:");
        jDesktopPane1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 0, 260, 30));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Quantidade Mínima em Estoque:");
        jDesktopPane1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 80, 270, -1));

        nomeText.setBackground(new java.awt.Color(255, 255, 255));
        nomeText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nomeText.setForeground(new java.awt.Color(0, 0, 0));
        nomeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomeTextKeyReleased(evt);
            }
        });
        jDesktopPane1.add(nomeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 530, -1));

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

        jDesktopPane1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 530, 230));

        quantidadeMinimaText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        quantidadeMinimaText.setModel(new javax.swing.SpinnerNumberModel(0, 0, 999999999, 1));
        quantidadeMinimaText.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                quantidadeMinimaTextStateChanged(evt);
            }
        });
        quantidadeMinimaText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                quantidadeMinimaTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                quantidadeMinimaTextKeyTyped(evt);
            }
        });
        jDesktopPane1.add(quantidadeMinimaText, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 80, 100, -1));

        imagemProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/imgProduto.png"))); // NOI18N
        jDesktopPane1.add(imagemProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 30, 260, 260));

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("*Valor Unitário:");
        jDesktopPane1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 80, 160, -1));

        botaoCarregarImagem.setBackground(new java.awt.Color(204, 204, 204));
        botaoCarregarImagem.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        botaoCarregarImagem.setForeground(new java.awt.Color(0, 0, 0));
        botaoCarregarImagem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/carregarIMG.png"))); // NOI18N
        botaoCarregarImagem.setText("CARREGAR IMAGEM");
        botaoCarregarImagem.setToolTipText("Clique para carregar imagem desejada");
        botaoCarregarImagem.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botaoCarregarImagem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoCarregarImagem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCarregarImagemActionPerformed(evt);
            }
        });
        jDesktopPane1.add(botaoCarregarImagem, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 310, 260, 40));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Formato ideal: imagem quadrada (ex: 260x260 px)");
        jDesktopPane1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 290, 260, -1));

        labelFornecedorVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelFornecedorVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelFornecedorVazio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFornecedorVazio.setText("*SELECIONE PELO MENOS UM FORNECEDOR");
        jDesktopPane1.add(labelFornecedorVazio, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 363, 530, 20));

        labelNomeVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelNomeVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelNomeVazio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNomeVazio.setText("*DIGITE O NOME DO PRODUTO");
        jDesktopPane1.add(labelNomeVazio, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 53, 530, 20));

        boataoImagemPadrao.setBackground(new java.awt.Color(102, 102, 102));
        boataoImagemPadrao.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        boataoImagemPadrao.setForeground(new java.awt.Color(255, 255, 255));
        boataoImagemPadrao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/restaurarIMG.png"))); // NOI18N
        boataoImagemPadrao.setText("IMAGEM PADRÃO");
        boataoImagemPadrao.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        boataoImagemPadrao.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boataoImagemPadrao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boataoImagemPadraoActionPerformed(evt);
            }
        });
        jDesktopPane1.add(boataoImagemPadrao, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 360, 150, 20));

        valorUnitarioText.setBackground(new java.awt.Color(255, 255, 255));
        valorUnitarioText.setForeground(new java.awt.Color(0, 0, 0));
        valorUnitarioText.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0.00"))));
        valorUnitarioText.setText("0,00");
        valorUnitarioText.setToolTipText("");
        valorUnitarioText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        valorUnitarioText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valorUnitarioTextFocusLost(evt);
            }
        });
        valorUnitarioText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                valorUnitarioTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                valorUnitarioTextKeyTyped(evt);
            }
        });
        jDesktopPane1.add(valorUnitarioText, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 80, 130, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("*Nome:");
        jDesktopPane1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, 30, 160, -1));

        jPanel1.add(jDesktopPane1);
        jDesktopPane1.setBounds(40, 130, 990, 390);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("*Preenchimento obrigatório de campo(s)");
        jPanel1.add(jLabel18);
        jLabel18.setBounds(40, 520, 990, 15);

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

        labelAtalhos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        labelAtalhos.setForeground(new java.awt.Color(0, 102, 0));
        labelAtalhos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAtalhos.setText("ATALHOS: F1 = Salvar Cadastro do Produto | F5 = Limpar Campos | F8 = Imagem Padrão | F9 = Carregar Imagem | F12 = Menu Principal");
        jPanel1.add(labelAtalhos);
        labelAtalhos.setBounds(0, 60, 1070, 16);

        bordaLimpar.setBackground(new java.awt.Color(255, 255, 255));
        bordaLimpar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaLimpar.setForeground(new java.awt.Color(0, 0, 0));
        bordaLimpar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoLimpar.setBackground(new java.awt.Color(204, 204, 204));
        botaoLimpar.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        botaoLimpar.setForeground(new java.awt.Color(0, 0, 0));
        botaoLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/limpar.png"))); // NOI18N
        botaoLimpar.setText("LIMPAR CAMPOS");
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
        bordaLimpar.setBounds(740, 560, 91, 62);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoSalvar.setBackground(new java.awt.Color(210, 164, 2));
        botaoSalvar.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        botaoSalvar.setForeground(new java.awt.Color(0, 0, 0));
        botaoSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/SALVARcad.png"))); // NOI18N
        botaoSalvar.setText("SALVAR CADASTRO");
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
        bordaSalvar.setBounds(240, 560, 91, 62);

        bordaRestaurarIMG.setBackground(new java.awt.Color(255, 255, 255));
        bordaRestaurarIMG.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaRestaurarIMG.setForeground(new java.awt.Color(0, 0, 0));
        bordaRestaurarIMG.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        boataoRestaurarIMG.setBackground(new java.awt.Color(204, 204, 204));
        boataoRestaurarIMG.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        boataoRestaurarIMG.setForeground(new java.awt.Color(0, 0, 0));
        boataoRestaurarIMG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/RedefinirIMG.png"))); // NOI18N
        boataoRestaurarIMG.setText("RESTAURAR IMAGEM");
        boataoRestaurarIMG.setBorder(null);
        boataoRestaurarIMG.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boataoRestaurarIMG.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        boataoRestaurarIMG.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        boataoRestaurarIMG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boataoRestaurarIMGActionPerformed(evt);
            }
        });
        bordaRestaurarIMG.add(boataoRestaurarIMG, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaRestaurarIMG);
        bordaRestaurarIMG.setBounds(930, 560, 91, 62);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1070, 696);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoCarregarImagemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCarregarImagemActionPerformed
        escolherIMG();
    }//GEN-LAST:event_botaoCarregarImagemActionPerformed

    private void quantidadeMinimaTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeMinimaTextKeyTyped
        
    }//GEN-LAST:event_quantidadeMinimaTextKeyTyped

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        trocarTela();
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void nomeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeTextKeyReleased
        digitacao();
    }//GEN-LAST:event_nomeTextKeyReleased

    private void tabelaFornecedoresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaFornecedoresMouseClicked
        digitacao();
    }//GEN-LAST:event_tabelaFornecedoresMouseClicked

    private void boataoImagemPadraoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boataoImagemPadraoActionPerformed
        limparImagem();
    }//GEN-LAST:event_boataoImagemPadraoActionPerformed

    private void botaoLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparActionPerformed
        limpar(); 
    }//GEN-LAST:event_botaoLimparActionPerformed

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void codigoTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codigoTextFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoTextFocusLost

    private void codigoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoTextKeyReleased

    private void valorUnitarioTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valorUnitarioTextFocusLost
        
    }//GEN-LAST:event_valorUnitarioTextFocusLost

    private void valorUnitarioTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorUnitarioTextKeyReleased
        digitacao();
    }//GEN-LAST:event_valorUnitarioTextKeyReleased

    private void valorUnitarioTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_valorUnitarioTextKeyTyped

    }//GEN-LAST:event_valorUnitarioTextKeyTyped

    private void quantidadeMinimaTextStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_quantidadeMinimaTextStateChanged
        digitacao();
    }//GEN-LAST:event_quantidadeMinimaTextStateChanged

    private void quantidadeMinimaTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_quantidadeMinimaTextKeyReleased
       digitacao();
    }//GEN-LAST:event_quantidadeMinimaTextKeyReleased

    private void boataoRestaurarIMGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boataoRestaurarIMGActionPerformed
       restaurarImagem();
    }//GEN-LAST:event_boataoRestaurarIMGActionPerformed

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
            java.util.logging.Logger.getLogger(CadastrarEditarProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastrarEditarProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastrarEditarProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastrarEditarProduto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                new CadastrarEditarProduto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconeTitulo;
    private javax.swing.JButton boataoImagemPadrao;
    private javax.swing.JButton boataoRestaurarIMG;
    private javax.swing.JPanel bordaLimpar;
    private javax.swing.JPanel bordaRestaurarIMG;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoCarregarImagem;
    private javax.swing.JButton botaoLimpar;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JButton botaoSalvar;
    private javax.swing.JTextField codigoText;
    private javax.swing.JLabel imagemProduto;
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelAtalhos;
    private javax.swing.JLabel labelCodigoFornecedor;
    private javax.swing.JLabel labelFornecedorVazio;
    private javax.swing.JLabel labelNomeVazio;
    private javax.swing.JLabel labelTextoTitulo;
    private javax.swing.JTextField nomeText;
    private javax.swing.JSpinner quantidadeMinimaText;
    private javax.swing.JTable tabelaFornecedores;
    private javax.swing.JFormattedTextField valorUnitarioText;
    // End of variables declaration//GEN-END:variables
}
