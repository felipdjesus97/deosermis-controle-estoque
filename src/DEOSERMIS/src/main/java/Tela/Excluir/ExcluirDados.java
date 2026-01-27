package Tela.Excluir;

import Classes.Excluir;
import Classes.Fornecedor;
import Classes.Funcoes;
import Classes.OverlayUtil;
import Classes.PedidoProduto;
import Classes.Produto;
import Classes.ReposicaoProduto;
import Classes.Usuario;
import Classes.UsuarioAtual;
import Tela.Inicio.MenuPrincipal;
import Tela.Usuario.ConfirmarUsuarioAtual;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

@SuppressWarnings("serial")
public class ExcluirDados extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();   
    private final Excluir excluir = new Excluir();
    private final UsuarioAtual usuarioAtual = new UsuarioAtual();
    private final Usuario usuario = new Usuario();

    public ExcluirDados() {
        setUndecorated(true);
        initComponents();
        reordenar();
        funcao.aplicarEfeitoMouse(botaoConfirmarExcluir);
        funcao.aplicarEfeitoMouse(botaoMenu);
        funcao.configurarTabela(tabela, new Font("Tahoma", Font.BOLD, 14), Color.white, new Color(0, 0, 102));       
        adicionarAtalhosTeclado();
        montarDatas();
        adicionarValidacoes();
        qualTabelaExibir();
    }
    
    private void configurarComponentes(boolean visivel, String texto, String img, String textoAtencao){
        painelData.setVisible(visivel);
        iconeData.setVisible(visivel);
        labelData.setVisible(visivel);
        data.setVisible(visivel);
        labelTitulo.setText(texto);
        iconeTitulo.setIcon(new ImageIcon(getClass().getResource("/imagens/"+img)));
        labelAtencao.setText(textoAtencao);
    }
    
    private void reordenar(){
        tabela.setAutoCreateRowSorter(false); // desativa o RowSorter automático
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            if (i == 0) {
                tabela.getColumnModel().getColumn(i).setResizable(false); // coluna 0 bloqueada
            } else {
                tabela.getColumnModel().getColumn(i).setResizable(true);  // outras liberadas
            }
        }
    }
    
    private void atualizarTabela(DefaultTableModel modelo) {
        tabela.setModel(modelo);
        reordenar();
    }

    private void montarDatas() {
        Date hoje = new Date();
        configurarData(data, hoje, hoje, "Selecione data final da pesquisa");
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
        data.getDateEditor().addPropertyChangeListener("date", evt -> qualTabelaExibir());
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

        for (int i = 1; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(centralizadoRenderer);
        }
        
    }

    private void qualTabelaExibir() {
        if (null == excluir.getTela()) {
            funcao.trocarDeTela(this, new MenuPrincipal());
            return;
        }

        switch (excluir.getTela()) {
            case "pedido":
                pesquisaPedido();
                configurarComponentes(true, "EXCLUIR PEDIDO","EXCLUIRpe.png",
                        "<html><body style='width: 400px; text-align: center;'>"
                        + "<b>Atenção:</b> Exclusão <b>permanente.</b><br>"
                        + "Esta ação não pode ser desfeita, "
                        + "pode gerar divergências no estoque e relatórios inconsistentes."
                        + "</body></html>");
                ajustarColunasPedido();
                break;

            case "reposicao":
                pesquisaReposicao();
                configurarComponentes(true,"EXCLUIR REPOSIÇÃO","EXCLUIRr.png",
                        "<html><body style='width: 400px; text-align: center;'>"
                        + "<b>Atenção:</b> Exclusão <b>permanente.</b><br>"
                        + "Esta ação não pode ser desfeita, "
                        + "pode gerar divergências no estoque e relatórios inconsistentes."
                        + "</body></html>");
                ajustarColunasReposicao();
                break;

            case "produto":
                pesquisarProduto();
                configurarComponentes(false,"EXCLUIR PRODUTO","EXCLUIRpr.png",
                        "<html><body style='width: 400px; text-align: center;'>"
                        + "<b>Atenção:</b> Exclusão <b>permanente.</b><br>"
                        + "Esta ação não pode ser desfeita."
                        + "</body></html>");
                ajustarColunasProduto();
                break;

            case "fornecedor":
                pesquisarFornecedor();
                configurarComponentes(false,"EXCLUIR FORNECEDOR","EXCLUIRf.png",
                        "<html><body style='width: 400px; text-align: center;'>"
                        + "<b>Atenção:</b> Exclusão <b>permanente.</b><br>"
                        + "Esta ação não pode ser desfeita, pode impedir novas reposições de produtos associados e "
                        + "gerar relatórios inconsistentes."
                        + "</body></html>");
                ajustarColunasFornecedor();
                break;

            case "usuario":
                pesquisarUsuario();
                configurarComponentes(false,"EXCLUIR USUÁRIO","excluirUsu.png",
                        "<html><body style='width: 400px; text-align: center;'>"
                        + "<b>Atenção:</b> Exclusão <b>permanente.</b><br>"
                        + "Esta ação não pode ser desfeita."
                        + "</body></html>");
                ajustarColunasUsuario();
                break;

            default:
                funcao.trocarDeTela(this, new MenuPrincipal());
                return;
        }

        alinharTabela();
        configurarCheckbox(tabela);
        ocultarColuna(1);
        funcaoEnabled();
    }
    
    private void ajustarColunasPedido() {
        int colunas = tabela.getColumnModel().getColumnCount();
        if (colunas > 0) {
            if (colunas > 0) tabela.getColumnModel().getColumn(0).setPreferredWidth(25);
            if (colunas > 2) tabela.getColumnModel().getColumn(2).setPreferredWidth(30);
            if (colunas > 3) tabela.getColumnModel().getColumn(3).setPreferredWidth(370);
            if (colunas > 4) tabela.getColumnModel().getColumn(4).setPreferredWidth(5);
            if (colunas > 5) tabela.getColumnModel().getColumn(5).setPreferredWidth(5);
            if (colunas > 6) tabela.getColumnModel().getColumn(6).setPreferredWidth(5);
            if (colunas > 7) tabela.getColumnModel().getColumn(7).setPreferredWidth(170);
        }
    }

    private void ajustarColunasReposicao() {
        int colunas = tabela.getColumnModel().getColumnCount();
        if (colunas > 0) {
            if (colunas > 0) tabela.getColumnModel().getColumn(0).setPreferredWidth(30);
            if (colunas > 2) tabela.getColumnModel().getColumn(2).setPreferredWidth(35);
            if (colunas > 3) tabela.getColumnModel().getColumn(3).setPreferredWidth(65);
            if (colunas > 4) tabela.getColumnModel().getColumn(4).setPreferredWidth(290);
            if (colunas > 5) tabela.getColumnModel().getColumn(5).setPreferredWidth(10);
            if (colunas > 6) tabela.getColumnModel().getColumn(6).setPreferredWidth(275);
        }
    }

    private void ajustarColunasProduto() {
        int colunas = tabela.getColumnModel().getColumnCount();
        if (colunas > 0) {
            if (colunas > 0) tabela.getColumnModel().getColumn(0).setPreferredWidth(6);
            if (colunas > 2) tabela.getColumnModel().getColumn(2).setPreferredWidth(180);
            if (colunas > 3) tabela.getColumnModel().getColumn(3).setPreferredWidth(250);
            if (colunas > 4) tabela.getColumnModel().getColumn(4).setPreferredWidth(50);
            if (colunas > 5) tabela.getColumnModel().getColumn(5).setPreferredWidth(60);
            if (colunas > 6) tabela.getColumnModel().getColumn(6).setPreferredWidth(50);
            if (colunas > 7) tabela.getColumnModel().getColumn(7).setPreferredWidth(50);
        }
    }

    private void ajustarColunasFornecedor() {
        int colunas = tabela.getColumnModel().getColumnCount();
        if (colunas > 0) {
            if (colunas > 0) tabela.getColumnModel().getColumn(0).setPreferredWidth(80);
            if (colunas > 2) tabela.getColumnModel().getColumn(2).setPreferredWidth(130);
            if (colunas > 3) tabela.getColumnModel().getColumn(3).setPreferredWidth(148);
            if (colunas > 4) tabela.getColumnModel().getColumn(4).setPreferredWidth(50);
            if (colunas > 7) tabela.getColumnModel().getColumn(7).setPreferredWidth(40);
            if (colunas > 8) tabela.getColumnModel().getColumn(8).setPreferredWidth(30);
            if (colunas > 13) tabela.getColumnModel().getColumn(13).setPreferredWidth(35);
            if (colunas > 15) tabela.getColumnModel().getColumn(15).setPreferredWidth(42);
        }
    }
    
    private void ajustarColunasUsuario() {
        int colunas = tabela.getColumnModel().getColumnCount();
        if (colunas > 0) {
            if (colunas > 0) tabela.getColumnModel().getColumn(0).setPreferredWidth(25);
            if (colunas > 2) tabela.getColumnModel().getColumn(2).setPreferredWidth(200);
            if (colunas > 3) tabela.getColumnModel().getColumn(3).setPreferredWidth(370);
            if (colunas > 4) tabela.getColumnModel().getColumn(4).setPreferredWidth(70);
}
    }

    private void configurarCheckbox(JTable tabela){
        if (tabela.getColumnCount() == 0) return; // evita erro
        TableColumn colunaCheckbox = tabela.getColumnModel().getColumn(0);
        colunaCheckbox.setCellRenderer(tabela.getDefaultRenderer(Boolean.class));
        colunaCheckbox.setCellEditor(tabela.getDefaultEditor(Boolean.class));
    }

    private void pesquisaPedido() {
        List<PedidoProduto> lista = PedidoProduto.buscarPedidos(data.getDate(), data.getDate(), this);

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Selecionar","Código","Data","Produtos","Subtotal","Desconto","Total","Pagamentos"}, 0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex){
                return columnIndex == 0 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column){
                return column == 0;
            }
        };

        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt","BR")));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(PedidoProduto p : lista){
            modelo.addRow(new Object[]{ Boolean.FALSE,
                p.getCodigo(),
                sdf.format(p.getData()),
                p.getProdutos(),
                df.format(p.getValorSubTotal()),
                p.getDesconto(),
                df.format(p.getValorTotal()),
                p.getPagamentos()
            });
        }
        atualizarTabela(modelo);
    }

    private void pesquisaReposicao() {
        List<ReposicaoProduto> lista = ReposicaoProduto.buscarReposicao(null,null,data.getDate(),data.getDate());

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Selecionar","Código","Data","Fornecedor","Produtos","Total","Chave NFe"},0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex){
                return columnIndex == 0 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row,int column){
                return column == 0;
            }
        };

        DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt","BR")));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for(ReposicaoProduto r : lista){
            modelo.addRow(new Object[]{ Boolean.FALSE,
                r.getCodigo(),
                sdf.format(r.getData()),
                r.getFornecedorNome(),
                funcao.formatarProdutosParaHtml(r.getProdutos()),
                df.format(r.getValorTotal()),
                r.getChaveAcessoNFe() != null ? funcao.formatarChaveNFe(r.getChaveAcessoNFe()) : ""
            });
        }
        atualizarTabela(modelo);
    }

    private void pesquisarProduto() {
        List<Produto> lista = Produto.buscarProdutosComFornecedores("", this);

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Selecionar","Código","Nome","Fornecedor","Valor Unitário","Quantidade","Qtd Mínima"},0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex){
                return columnIndex == 0 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row,int column){
                return column == 0;
            }
        };

        DecimalFormat df = new DecimalFormat("#,##0.00");
        for(Produto p : lista){
            modelo.addRow(new Object[]{ Boolean.FALSE,
                p.getCodigo(),
                p.getNome(),
                p.getFornecedor(),
                df.format(p.getValorUnitario()),
                p.getQuantidade(),
                p.getQuantidadeMinima()
            });
        }
        atualizarTabela(modelo);
    }

    private void pesquisarFornecedor() {
        List<Fornecedor> lista = Fornecedor.buscarFornecedorPorNome("", this);

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Selecionar","Código","Nome","CPF/CNPJ","E-mail","Instagram","WhatsApp","Site","UF","Cidade","Bairro","CEP","Logradouro","Número","Complemento","Observação"},0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex){
                return columnIndex == 0 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row,int column){
                return column == 0;
            }
        };

        for(Fornecedor f : lista){
            modelo.addRow(new Object[]{ Boolean.FALSE,
                f.getCodigo(),
                f.getNome(),
                f.getCPFCNPJ() != null ? funcao.formatarCpfCnpj(f.getCPFCNPJ()) : "",
                f.getEmail(),
                f.getInstagram(),
                f.getWhatsApp() != null ? funcao.formatWhatsApp(f.getWhatsApp()) : "",
                f.getSite(),
                f.getUF(),
                f.getCidade(),
                f.getBairro(),
                f.getCEP() != null ? funcao.formatCEP(f.getCEP()) : "",
                f.getLogradouro(),
                f.getNumero(),
                f.getComplemento(),
                f.getObs()
            });
        }
        atualizarTabela(modelo);
    }
    
    private void pesquisarUsuario() {
        long cod = usuarioAtual.getCodigo();
        List<Usuario> lista = usuario.buscarUsuarios(cod, this);
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Selecionar","Código","Nome","E-mail","Tipo"},0
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex){
                return columnIndex == 0 ? Boolean.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row,int column){
                return column == 0;
            }
        };

        for (Usuario u : lista) {
            String tipo = (u.isTipo()) ? "Administrador" : "Comum";
            
            modelo.addRow(new Object[]{
                Boolean.FALSE,
                u.getCodigo(),
                u.getNome(),
                u.getEmail(),
                tipo
            });
        }
        atualizarTabela(modelo);
    }

    private void ocultarColuna(int index){
        if (index >= tabela.getColumnCount()) return; 
        tabela.getColumnModel().getColumn(index).setMinWidth(0);
        tabela.getColumnModel().getColumn(index).setMaxWidth(0);
        tabela.getColumnModel().getColumn(index).setPreferredWidth(0);
        tabela.getColumnModel().getColumn(index).setWidth(0);
    }

    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F12", botaoMenu);
        funcao.adicionarAtalho(root, "F1", botaoConfirmarExcluir);
    }
    
    private List<BigInteger> getCodigosSelecionados() {
        List<BigInteger> codigos = new ArrayList<>();
        for (int i = 0; i < tabela.getRowCount(); i++) {
            Boolean selecionado = (Boolean) tabela.getValueAt(i, 0);
            if (selecionado != null && selecionado) {
                String valor = tabela.getValueAt(i, 1).toString();
                codigos.add(new BigInteger(valor));
            }
        }
        return codigos;
    }

    private void confirmarExclusao() {        
        List<BigInteger> codigosSelecionados = getCodigosSelecionados();

        if (codigosSelecionados.isEmpty()) {
            funcao.Mensagens(this, "Nenhum dado selecionado para exclusão!", "", "ATENÇÃO", "aviso");
            return;
        }
       funcao.MensagemConfirmar(this,"Você realmente deseja excluir","permanentemente o(s) dado(s) selecionado(s)?", "CONFIRMAR EXCLUSÃO", 
               () -> qualExcluir(codigosSelecionados));
    }
    
    private void qualExcluir(List codigosSelecionados){
        if (null == excluir.getTela()) {
            funcao.Mensagens(this, "Erro interno, tente mais tarde!", "", "ERRO", "erro");
            funcao.trocarDeTela(this, new MenuPrincipal());
            return;
        }switch (excluir.getTela()) {
            case "pedido":
                UsuarioAtual.setAcao(() ->excluirPedido(codigosSelecionados));
                break;

            case "reposicao":
                UsuarioAtual.setAcao(() ->excluirReposicao(codigosSelecionados));
                break;
                
            case "produto":
                UsuarioAtual.setAcao(() ->excluirProduto(codigosSelecionados));
                break;

            case "fornecedor":
                UsuarioAtual.setAcao(() ->excluirFornecedor(codigosSelecionados));
                break;
                
            case "usuario":
                UsuarioAtual.setAcao(() ->excluirUsuario(codigosSelecionados));
                break;

            default:
                funcao.Mensagens(this, "Nenhum dado selecionado para exclusão!", "", "ATENÇÃO", "aviso");
                return;
        }
        
       ConfirmarUsuarioAtual dialog = new ConfirmarUsuarioAtual(this, true);
       OverlayUtil.abrirTela(this, dialog);
       qualTabelaExibir();
    }
    
    private void excluirPedido(List codigosSelecionados){
        PedidoProduto pedidoProduto = new PedidoProduto();
        pedidoProduto.excluirPedidos(codigosSelecionados, this);
    }
    
    private void excluirReposicao(List codigosSelecionados){
        ReposicaoProduto reposicaoProduto = new ReposicaoProduto();
        reposicaoProduto.excluirReposicoes(codigosSelecionados, this);
    }
    
    private void excluirUsuario(List codigosSelecionados){
        usuario.excluirUsuarios(codigosSelecionados, this);
    }
    
    private void excluirProduto(List codigosSelecionados){
        Produto produto = new Produto();
        produto.excluirProdutos(codigosSelecionados, this);
    }
    
    private void excluirFornecedor(List codigosSelecionados){
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.excluirFornecedores(codigosSelecionados, this);
    }
    
    private void funcaoEnabled(){
        boolean Selecionado = false;
        // Percorre a tabela para verificar se alguma linha está selecionada
        for (int i = 0; i < tabela.getRowCount(); i++) {
            Boolean isSelected = (Boolean) tabela.getValueAt(i, 0);
            if (Boolean.TRUE.equals(isSelected)) {
                Selecionado = true;
                break;
            }
        }
        botaoConfirmarExcluir.setOpaque(Selecionado);
        botaoConfirmarExcluir.setEnabled(Selecionado);
        funcao.validarBotaoComDica(botaoConfirmarExcluir, "Clique para confirmar exclusão");
        adicionarAtalhosTeclado();
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
        iconeTitulo = new javax.swing.JLabel();
        labelTitulo = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();
        jLabel21 = new javax.swing.JLabel();
        painelData = new javax.swing.JDesktopPane();
        iconeData = new javax.swing.JLabel();
        labelData = new javax.swing.JLabel();
        data = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabela = new javax.swing.JTable();
        bordaSalvar = new javax.swing.JPanel();
        botaoConfirmarExcluir = new javax.swing.JButton();
        labelAtencao = new javax.swing.JLabel();

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

        iconeTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/EXCLUIRpr.png"))); // NOI18N
        jPanel2.add(iconeTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        labelTitulo.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        labelTitulo.setForeground(new java.awt.Color(0, 0, 0));
        labelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTitulo.setText("EXCLUIR DADOS");
        jPanel2.add(labelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1070, -1));

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

        jLabel21.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 102, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("ATALHOS: F1 = Confirmar Exclusão dos Dados | F12 = Voltar ao Menu Principal");
        jPanel1.add(jLabel21);
        jLabel21.setBounds(-6, 60, 1080, 16);

        painelData.setBackground(new java.awt.Color(255, 255, 255));
        painelData.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "SELECIONE", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 24), new java.awt.Color(0, 0, 102))); // NOI18N
        painelData.setForeground(new java.awt.Color(0, 102, 153));
        painelData.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconeData.setBackground(new java.awt.Color(255, 255, 255));
        iconeData.setForeground(new java.awt.Color(0, 0, 0));
        iconeData.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/DATA.png"))); // NOI18N
        iconeData.setOpaque(true);
        painelData.add(iconeData, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 30, 30));

        labelData.setFont(new java.awt.Font("Century Gothic", 1, 22)); // NOI18N
        labelData.setForeground(new java.awt.Color(0, 0, 0));
        labelData.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labelData.setText("Data:");
        painelData.add(labelData, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 60, 30));

        data.setBackground(new java.awt.Color(255, 255, 255));
        data.setForeground(new java.awt.Color(0, 0, 0));
        data.setToolTipText("");
        data.setDateFormatString("");
        data.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 20)); // NOI18N
        data.setOpaque(false);
        painelData.add(data, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 210, 30));

        jPanel1.add(painelData);
        painelData.setBounds(365, 610, 340, 80);

        tabela.setAutoCreateRowSorter(true);
        tabela.setBackground(new java.awt.Color(255, 255, 255));
        tabela.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabela.setForeground(new java.awt.Color(51, 51, 51));
        tabela.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabela.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabela.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabela.getTableHeader().setReorderingAllowed(false);
        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaMouseClicked(evt);
            }
        });
        tabela.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelaKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(tabela);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(10, 160, 1050, 440);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoConfirmarExcluir.setBackground(new java.awt.Color(210, 164, 2));
        botaoConfirmarExcluir.setFont(new java.awt.Font("Tahoma", 1, 7)); // NOI18N
        botaoConfirmarExcluir.setForeground(new java.awt.Color(0, 0, 0));
        botaoConfirmarExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/botaoExcluirConfirmar.png"))); // NOI18N
        botaoConfirmarExcluir.setText("CONFIRMAR EXCLUSÃO");
        botaoConfirmarExcluir.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoConfirmarExcluir.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoConfirmarExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoConfirmarExcluir.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        botaoConfirmarExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoConfirmarExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConfirmarExcluirActionPerformed(evt);
            }
        });
        bordaSalvar.add(botaoConfirmarExcluir, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaSalvar);
        bordaSalvar.setBounds(10, 630, 91, 62);

        labelAtencao.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        labelAtencao.setForeground(new java.awt.Color(204, 0, 0));
        labelAtencao.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAtencao.setText("texto");
        labelAtencao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelAtencao.setInheritsPopupMenu(false);
        labelAtencao.setName(""); // NOI18N
        jPanel1.add(labelAtencao);
        labelAtencao.setBounds(0, 80, 1070, 70);

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

    private void tabelaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelaKeyPressed

    }//GEN-LAST:event_tabelaKeyPressed

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        funcao.trocarDeTela(this, new MenuPrincipal());
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void botaoConfirmarExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConfirmarExcluirActionPerformed
        confirmarExclusao();
    }//GEN-LAST:event_botaoConfirmarExcluirActionPerformed

    private void tabelaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaMouseClicked
        funcaoEnabled();
    }//GEN-LAST:event_tabelaMouseClicked

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
            java.util.logging.Logger.getLogger(ExcluirDados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExcluirDados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExcluirDados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExcluirDados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ExcluirDados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoConfirmarExcluir;
    private javax.swing.JButton botaoMenu;
    private com.toedter.calendar.JDateChooser data;
    private javax.swing.JLabel iconeData;
    private javax.swing.JLabel iconeTitulo;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelAtencao;
    private javax.swing.JLabel labelData;
    private javax.swing.JLabel labelTitulo;
    private javax.swing.JDesktopPane painelData;
    private javax.swing.JTable tabela;
    // End of variables declaration//GEN-END:variables
}
