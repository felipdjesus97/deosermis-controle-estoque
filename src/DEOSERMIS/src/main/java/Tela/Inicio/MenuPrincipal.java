
package Tela.Inicio;

import Classes.Backup;
import Classes.Excluir;
import Tela.Reposicao.RegistrarReposicao;
import Tela.Pedido.RegistrarPedido;
import Tela.Produto.ConsultarProduto;
import Tela.Produto.CadastrarEditarProduto;
import Tela.Relatorio.RelatorioResumoDePedido;
import Tela.Relatorio.RelatorioProdutoEmFalta;
import Tela.Relatorio.RelatorioBalancoFinanceiro;
import Tela.Relatorio.RelatorioResumoDeReposicao;
import Tela.Relatorio.RelatorioConsultarPedido;
import Tela.Relatorio.RelatorioConsultarReposicao;
import Tela.Fornecedor.ConsultarFornecedor;
import Tela.Fornecedor.CadastrarEditarFornecedor;
import Classes.OpcaoDeTela;
import Classes.Funcoes;
import Classes.OverlayUtil;
import Classes.Produto;
import Classes.UsuarioAtual;
import Tela.Excluir.ExcluirDados;
import Tela.Usuario.CadastrarUsuario;
import Tela.Usuario.ConsultarUsuario;
import Tela.Usuario.EditarSenhaEmail;
import java.awt.Frame;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JRootPane;

@SuppressWarnings("serial")
public class MenuPrincipal extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final UsuarioAtual usuarioAtual = new UsuarioAtual();
    private final OpcaoDeTela cadastrarEditar = new OpcaoDeTela();
    private final Excluir excluir = new Excluir();
    
    public MenuPrincipal() {
        setUndecorated(true);
        initComponents();
        funcao.bloquearArrastar(this);
        verificarPermissoes();
        labalLOGO.requestFocus();
        funcao.aplicarEfeitoMouse(botaoCadastrarFornecedor);
        funcao.aplicarEfeitoMouse(botaoCadastrarProduto);  
        funcao.aplicarEfeitoMouse(botaoRegistrarPedido);
        funcao.aplicarEfeitoMouse(botaoRegistrarReposicao);
        funcao.aplicarEfeitoMouse(botaoConsultarProduto);  
        funcao.aplicarEfeitoMouse(botaoConsultarFornecedor);
        funcao.aplicarEfeitoMouse(botaoLogout);
        labelProdutoEmFalta.setVisible(Produto.existeProdutoEmFalta());
    }    
    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoCadastrarFornecedor);
        funcao.adicionarAtalho(root, "F2", botaoConsultarFornecedor);
        funcao.adicionarAtalho(root, "F3", botaoRegistrarPedido);
        funcao.adicionarAtalho(root, "F4", botaoRegistrarReposicao);
        funcao.adicionarAtalho(root, "F5", botaoCadastrarProduto);
        funcao.adicionarAtalho(root, "F6", botaoConsultarProduto);
        funcao.adicionarAtalho(root, "F12", botaoLogout);
    }
    private void verificarPermissoes() {
        if (!usuarioAtual.getTipo()){
            int usuarioAtualID = usuarioAtual.getCodigo();
            funcao.verificarPermissao(botaoCadastrarProduto,usuarioAtualID, "CadProduto",this);
            funcao.verificarPermissao(botaoCadastrarFornecedor,usuarioAtualID, "CadFornecedor",this);
            funcao.verificarPermissao(botaoRegistrarPedido,usuarioAtualID, "RegPedido",this);
            funcao.verificarPermissao(botaoRegistrarReposicao,usuarioAtualID, "RegReposição",this);
            funcao.verificarPermissao(botaoConsultarProduto,usuarioAtualID, "ConProduto",this);
            funcao.verificarPermissao(botaoConsultarFornecedor,usuarioAtualID, "ConFornecedor",this);
            funcao.verificarPermissao(botaoGerenciarUsuario,usuarioAtualID, "Gerenciar Usuários",this);
            funcao.verificarPermissao(botaoRelatorios,usuarioAtualID, "Relatórios",this);
            funcao.verificarPermissao(botaoExclusoes,usuarioAtualID, "Exclusões",this);
            funcao.verificarPermissao(botaoBackup,usuarioAtualID, "Backup",this);
            funcao.verificarPermissao(botaoAuditoria,usuarioAtualID, "Auditoria",this);           
        } 
        adicionarAtalhosTeclado();
        adicionarLegendas();
    }
    private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoCadastrarFornecedor, "Clique para cadastrar fornecedor");
        funcao.validarBotaoComDica(botaoConsultarFornecedor, "Clique para consultar fornecedor");
        funcao.validarBotaoComDica(botaoRegistrarPedido, "Clique para registrar pedido");
        funcao.validarBotaoComDica(botaoRegistrarReposicao, "Clique para registrar reposição");
        funcao.validarBotaoComDica(botaoCadastrarProduto, "Clique para cadastrar produto");
        funcao.validarBotaoComDica(botaoConsultarProduto, "Clique para consultar produto");
    }
    
    private void novoBackup(){
        try {
            Backup.criarBackup(this);
        } catch (Exception e) {
            e.printStackTrace();
            funcao.Mensagens(this, "Erro ao criar backup:", e.getMessage(), "ERRO", "erro");
        }
    }
    
    private void restaurarBackup(){
        try {
            String ultimoBackup = Backup.getUltimoBackup();
            if (ultimoBackup != null) {
                Backup.restaurarBackup(ultimoBackup);
                funcao.Mensagens(this, "Backup restaurado com sucesso!", "", "SUCESSO" ,"informacao");
            } else {
                funcao.Mensagens(this, "Nenhum backup encontrado", "", "ERRO" ,"erro");
            }
        } catch (Exception e) {
            e.printStackTrace();
            funcao.Mensagens(this, "Erro ao restaurar backup:", e.getMessage(), "ERRO", "erro");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        labelAtalhos = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        labalLOGO = new javax.swing.JLabel();
        botaoLogout = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        bordaSalvarP = new javax.swing.JPanel();
        botaoCadastrarProduto = new javax.swing.JButton();
        bordaConsultarP = new javax.swing.JPanel();
        botaoConsultarProduto = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        bordaSalvarF = new javax.swing.JPanel();
        botaoCadastrarFornecedor = new javax.swing.JButton();
        bordaConsultarF = new javax.swing.JPanel();
        botaoConsultarFornecedor = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        bordaResgistrarP = new javax.swing.JPanel();
        botaoRegistrarPedido = new javax.swing.JButton();
        bordaResgistrarR = new javax.swing.JPanel();
        botaoRegistrarReposicao = new javax.swing.JButton();
        labelAtalhos1 = new javax.swing.JLabel();
        labelProdutoEmFalta = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        botaoMeuUsuario = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        botaoGerenciarUsuario = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        botaoAuditoria = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        botaoBackup = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        botaoRelatorios = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        botaoExclusoes = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1070, 696));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelAtalhos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        labelAtalhos.setForeground(new java.awt.Color(0, 102, 0));
        labelAtalhos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAtalhos.setText("F5 = Cadastrar Produto | F6 = Consultar Produto | F12 = Fazer Logout no Sistema");
        jPanel1.add(labelAtalhos, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, 1070, 20));

        jPanel2.setBackground(new java.awt.Color(210, 164, 2));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("CONTROLE DE ESTOQUE");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 1070, 30));

        labalLOGO.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labalLOGO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoTipoHome.png"))); // NOI18N
        jPanel2.add(labalLOGO, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1070, 200));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1070, 200));

        botaoLogout.setBackground(new java.awt.Color(204, 0, 0));
        botaoLogout.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoLogout.setForeground(new java.awt.Color(255, 255, 255));
        botaoLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/logout.png"))); // NOI18N
        botaoLogout.setText("SAIR");
        botaoLogout.setToolTipText("Clique para fazer logout no sistema");
        botaoLogout.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        botaoLogout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoLogout.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoLogout.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoLogoutActionPerformed(evt);
            }
        });
        jPanel1.add(botaoLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1002, 605, 60, 60));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PRODUTO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 28), new java.awt.Color(0, 0, 102))); // NOI18N
        jPanel3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bordaSalvarP.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvarP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvarP.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvarP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoCadastrarProduto.setBackground(new java.awt.Color(228, 228, 228));
        botaoCadastrarProduto.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        botaoCadastrarProduto.setForeground(new java.awt.Color(0, 0, 0));
        botaoCadastrarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ADDprodutoHome.png"))); // NOI18N
        botaoCadastrarProduto.setText("CADASTRAR");
        botaoCadastrarProduto.setBorder(null);
        botaoCadastrarProduto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoCadastrarProduto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoCadastrarProduto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoCadastrarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCadastrarProdutoActionPerformed(evt);
            }
        });
        bordaSalvarP.add(botaoCadastrarProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 155));

        jPanel3.add(bordaSalvarP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 115, 155));

        bordaConsultarP.setBackground(new java.awt.Color(255, 255, 255));
        bordaConsultarP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaConsultarP.setForeground(new java.awt.Color(0, 0, 0));
        bordaConsultarP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoConsultarProduto.setBackground(new java.awt.Color(184, 184, 184));
        botaoConsultarProduto.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        botaoConsultarProduto.setForeground(new java.awt.Color(0, 0, 0));
        botaoConsultarProduto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONSULTARprodutoHome.png"))); // NOI18N
        botaoConsultarProduto.setText("CONSULTAR");
        botaoConsultarProduto.setBorder(null);
        botaoConsultarProduto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoConsultarProduto.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoConsultarProduto.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoConsultarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConsultarProdutoActionPerformed(evt);
            }
        });
        bordaConsultarP.add(botaoConsultarProduto, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 155));

        jPanel3.add(bordaConsultarP, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 115, 155));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, 330, 240));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "FORNECEDOR", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 28), new java.awt.Color(0, 0, 102))); // NOI18N
        jPanel4.setForeground(new java.awt.Color(0, 0, 102));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bordaSalvarF.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvarF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvarF.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvarF.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoCadastrarFornecedor.setBackground(new java.awt.Color(228, 228, 228));
        botaoCadastrarFornecedor.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        botaoCadastrarFornecedor.setForeground(new java.awt.Color(0, 0, 0));
        botaoCadastrarFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ADDfornecedorHome.png"))); // NOI18N
        botaoCadastrarFornecedor.setText("CADASTRAR");
        botaoCadastrarFornecedor.setBorder(null);
        botaoCadastrarFornecedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoCadastrarFornecedor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoCadastrarFornecedor.setRequestFocusEnabled(false);
        botaoCadastrarFornecedor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoCadastrarFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoCadastrarFornecedorActionPerformed(evt);
            }
        });
        bordaSalvarF.add(botaoCadastrarFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 155));

        jPanel4.add(bordaSalvarF, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 115, 155));

        bordaConsultarF.setBackground(new java.awt.Color(255, 255, 255));
        bordaConsultarF.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaConsultarF.setForeground(new java.awt.Color(0, 0, 0));
        bordaConsultarF.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoConsultarFornecedor.setBackground(new java.awt.Color(184, 184, 184));
        botaoConsultarFornecedor.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        botaoConsultarFornecedor.setForeground(new java.awt.Color(0, 0, 0));
        botaoConsultarFornecedor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONSULTAfornecedorHome.png"))); // NOI18N
        botaoConsultarFornecedor.setText("CONSULTAR");
        botaoConsultarFornecedor.setBorder(null);
        botaoConsultarFornecedor.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoConsultarFornecedor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoConsultarFornecedor.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoConsultarFornecedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConsultarFornecedorActionPerformed(evt);
            }
        });
        bordaConsultarF.add(botaoConsultarFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 155));

        jPanel4.add(bordaConsultarF, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 115, 155));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 290, 330, 240));

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "REGISTRAR", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 28), new java.awt.Color(0, 0, 102))); // NOI18N
        jPanel5.setForeground(new java.awt.Color(0, 0, 0));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bordaResgistrarP.setBackground(new java.awt.Color(255, 255, 255));
        bordaResgistrarP.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaResgistrarP.setForeground(new java.awt.Color(0, 0, 0));
        bordaResgistrarP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoRegistrarPedido.setBackground(new java.awt.Color(228, 228, 228));
        botaoRegistrarPedido.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        botaoRegistrarPedido.setForeground(new java.awt.Color(0, 0, 0));
        botaoRegistrarPedido.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/REGISTRARpedido.png"))); // NOI18N
        botaoRegistrarPedido.setText("PEDIDO");
        botaoRegistrarPedido.setBorder(null);
        botaoRegistrarPedido.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoRegistrarPedido.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoRegistrarPedido.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoRegistrarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRegistrarPedidoActionPerformed(evt);
            }
        });
        bordaResgistrarP.add(botaoRegistrarPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 155));

        jPanel5.add(bordaResgistrarP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 115, 155));

        bordaResgistrarR.setBackground(new java.awt.Color(255, 255, 255));
        bordaResgistrarR.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaResgistrarR.setForeground(new java.awt.Color(0, 0, 0));
        bordaResgistrarR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoRegistrarReposicao.setBackground(new java.awt.Color(184, 184, 184));
        botaoRegistrarReposicao.setFont(new java.awt.Font("Arial", 0, 17)); // NOI18N
        botaoRegistrarReposicao.setForeground(new java.awt.Color(0, 0, 0));
        botaoRegistrarReposicao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/REGISTRARreposicao.png"))); // NOI18N
        botaoRegistrarReposicao.setText("REPOSIÇÃO");
        botaoRegistrarReposicao.setBorder(null);
        botaoRegistrarReposicao.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoRegistrarReposicao.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoRegistrarReposicao.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoRegistrarReposicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoRegistrarReposicaoActionPerformed(evt);
            }
        });
        bordaResgistrarR.add(botaoRegistrarReposicao, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 155));

        jPanel5.add(bordaResgistrarR, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 50, 115, 155));

        jPanel1.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, 330, 240));

        labelAtalhos1.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        labelAtalhos1.setForeground(new java.awt.Color(0, 102, 0));
        labelAtalhos1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAtalhos1.setText("ATALHOS: F1 = Cadastrar Fornecedor | F2 = Consultar Fornecedor | F3 = Registrar Pedido | F4 = Registrar Reposição");
        jPanel1.add(labelAtalhos1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 1070, -1));

        labelProdutoEmFalta.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        labelProdutoEmFalta.setForeground(new java.awt.Color(204, 0, 0));
        labelProdutoEmFalta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelProdutoEmFalta.setText("Atenção: existem produtos em falta no estoque. Recomenda-se verificar os itens e realizar a reposição o quanto antes para evitar indisponibilidade.");
        jPanel1.add(labelProdutoEmFalta, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 540, 1070, 70));

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1070, 670);

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenuBar1.setForeground(new java.awt.Color(0, 0, 0));

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setForeground(new java.awt.Color(0, 0, 0));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/configuracaoHOME.png"))); // NOI18N
        jMenu1.setText("Configuração");
        jMenu1.setBorderPainted(false);
        jMenu1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jMenu4.setBackground(new java.awt.Color(255, 255, 255));
        jMenu4.setForeground(new java.awt.Color(0, 0, 0));
        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONFIGURARusuarioHOME.png"))); // NOI18N
        jMenu4.setText("Usuário");

        botaoMeuUsuario.setBackground(new java.awt.Color(255, 255, 255));
        botaoMeuUsuario.setForeground(new java.awt.Color(0, 0, 0));
        botaoMeuUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usu.png"))); // NOI18N
        botaoMeuUsuario.setText("Meu Usuário");

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem9.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem9.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuSenha.png"))); // NOI18N
        jMenuItem9.setText("Alterar Senha");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        botaoMeuUsuario.add(jMenuItem9);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem10.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem10.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuMail.png"))); // NOI18N
        jMenuItem10.setText("Alterar E-mail");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        botaoMeuUsuario.add(jMenuItem10);

        jMenu4.add(botaoMeuUsuario);

        botaoGerenciarUsuario.setBackground(new java.awt.Color(255, 255, 255));
        botaoGerenciarUsuario.setForeground(new java.awt.Color(0, 0, 0));
        botaoGerenciarUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuGer.png"))); // NOI18N
        botaoGerenciarUsuario.setText("Gerenciar Usuários");

        jMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem8.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem8.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuAdd.png"))); // NOI18N
        jMenuItem8.setText("Novo Usuário");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        botaoGerenciarUsuario.add(jMenuItem8);

        jMenuItem11.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem11.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem11.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/consultarUsuario1.png"))); // NOI18N
        jMenuItem11.setText("Consultar Usuário");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        botaoGerenciarUsuario.add(jMenuItem11);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem13.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem13.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excluirUsuario.png"))); // NOI18N
        jMenuItem13.setText("Excluir Usuário");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        botaoGerenciarUsuario.add(jMenuItem13);

        botaoAuditoria.setBackground(new java.awt.Color(255, 255, 255));
        botaoAuditoria.setForeground(new java.awt.Color(0, 0, 0));
        botaoAuditoria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/auditar1.png"))); // NOI18N
        botaoAuditoria.setText("Auditoria");

        jMenuItem20.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem20.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem20.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/auditar2.png"))); // NOI18N
        jMenuItem20.setText("Consultar Ação Realizada");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        botaoAuditoria.add(jMenuItem20);

        botaoGerenciarUsuario.add(botaoAuditoria);

        jMenu4.add(botaoGerenciarUsuario);

        jMenu1.add(jMenu4);

        botaoBackup.setBackground(new java.awt.Color(255, 255, 255));
        botaoBackup.setForeground(new java.awt.Color(0, 0, 0));
        botaoBackup.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/backup.png"))); // NOI18N
        botaoBackup.setText("Backup");
        botaoBackup.setBorderPainted(false);
        botaoBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoBackupActionPerformed(evt);
            }
        });

        jMenuItem18.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        jMenuItem18.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem18.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/backupC.png"))); // NOI18N
        jMenuItem18.setText("Criar Backup");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        botaoBackup.add(jMenuItem18);

        jMenuItem19.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        jMenuItem19.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem19.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/backupR.png"))); // NOI18N
        jMenuItem19.setText("Restaurar Backup");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        botaoBackup.add(jMenuItem19);

        jMenu1.add(botaoBackup);

        jMenuBar1.add(jMenu1);

        botaoRelatorios.setBackground(new java.awt.Color(255, 255, 255));
        botaoRelatorios.setForeground(new java.awt.Color(0, 0, 0));
        botaoRelatorios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorioHOME.png"))); // NOI18N
        botaoRelatorios.setText("Relatórios");
        botaoRelatorios.setBorderPainted(false);
        botaoRelatorios.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        jMenu2.setBackground(new java.awt.Color(255, 255, 255));
        jMenu2.setForeground(new java.awt.Color(0, 0, 0));
        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios05.png"))); // NOI18N
        jMenu2.setText("Pedido");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem3.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem3.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios04.png"))); // NOI18N
        jMenuItem3.setText("Resumo de Pedido");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem5.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem5.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios04.png"))); // NOI18N
        jMenuItem5.setText("Consultar Pedido");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        botaoRelatorios.add(jMenu2);

        jMenu5.setBackground(new java.awt.Color(255, 255, 255));
        jMenu5.setForeground(new java.awt.Color(0, 0, 0));
        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios07.png"))); // NOI18N
        jMenu5.setText("Reposição");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem4.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem4.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios06.png"))); // NOI18N
        jMenuItem4.setText("Resumo de Reposição");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem4);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem6.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem6.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios06.png"))); // NOI18N
        jMenuItem6.setText("Consultar Reposição");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);

        botaoRelatorios.add(jMenu5);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem2.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem2.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios01.png"))); // NOI18N
        jMenuItem2.setText("Produto em Falta");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        botaoRelatorios.add(jMenuItem2);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem7.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem7.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/relatorios13.png"))); // NOI18N
        jMenuItem7.setText("Balanço Financeiro");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        botaoRelatorios.add(jMenuItem7);

        jMenuBar1.add(botaoRelatorios);

        botaoExclusoes.setBackground(new java.awt.Color(255, 255, 255));
        botaoExclusoes.setForeground(new java.awt.Color(0, 0, 0));
        botaoExclusoes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excluir2.png"))); // NOI18N
        botaoExclusoes.setText("Exclusões");
        botaoExclusoes.setBorderPainted(false);
        botaoExclusoes.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoExclusoes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoExclusoesActionPerformed(evt);
            }
        });

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem14.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem14.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excluir6.png"))); // NOI18N
        jMenuItem14.setText("Excluir Produto");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        botaoExclusoes.add(jMenuItem14);

        jMenuItem15.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem15.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem15.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excluir1.png"))); // NOI18N
        jMenuItem15.setText("Excluir Fornecedor");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        botaoExclusoes.add(jMenuItem15);

        jMenuItem16.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_J, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem16.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem16.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excluirPedido.png"))); // NOI18N
        jMenuItem16.setText("Excluir Pedido");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        botaoExclusoes.add(jMenuItem16);

        jMenuItem17.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem17.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem17.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/excluir5.png"))); // NOI18N
        jMenuItem17.setText("Excluir Reposição");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        botaoExclusoes.add(jMenuItem17);

        jMenuItem21.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem21.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem21.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/audita3.png"))); // NOI18N
        jMenuItem21.setText("Consultar Dado Excluído");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        botaoExclusoes.add(jMenuItem21);

        jMenuBar1.add(botaoExclusoes);

        jMenu3.setBackground(new java.awt.Color(255, 255, 255));
        jMenu3.setForeground(new java.awt.Color(0, 0, 0));
        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/AJUDAhome.png"))); // NOI18N
        jMenu3.setText("Suporte");
        jMenu3.setBorderPainted(false);
        jMenu3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        jMenuItem12.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem12.setForeground(new java.awt.Color(0, 0, 0));
        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/AJUDAhome.png"))); // NOI18N
        jMenuItem12.setText("Fale Conosco");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem12);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLogoutActionPerformed
        funcao.MensagemConfirmar(this,"Você realmente deseja realizar","logout do sistema?", "REALIZAR LOGOUT", () -> funcao.trocarDeTela(this, new Login()));
    }//GEN-LAST:event_botaoLogoutActionPerformed

    private void botaoConsultarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConsultarProdutoActionPerformed
        funcao.trocarDeTela(this, new ConsultarProduto());
    }//GEN-LAST:event_botaoConsultarProdutoActionPerformed

    private void botaoCadastrarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCadastrarProdutoActionPerformed
        cadastrarEditar.setCadastro(true);
        funcao.trocarDeTela(this, new CadastrarEditarProduto());
    }//GEN-LAST:event_botaoCadastrarProdutoActionPerformed

    private void botaoCadastrarFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoCadastrarFornecedorActionPerformed
        cadastrarEditar.setCadastro(true);
        funcao.trocarDeTela(this, new CadastrarEditarFornecedor());
    }//GEN-LAST:event_botaoCadastrarFornecedorActionPerformed

    private void botaoConsultarFornecedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConsultarFornecedorActionPerformed
        funcao.trocarDeTela(this, new ConsultarFornecedor());
    }//GEN-LAST:event_botaoConsultarFornecedorActionPerformed

    private void botaoRegistrarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRegistrarPedidoActionPerformed
        funcao.trocarDeTela(this, new RegistrarPedido());
    }//GEN-LAST:event_botaoRegistrarPedidoActionPerformed

    private void botaoRegistrarReposicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoRegistrarReposicaoActionPerformed
        funcao.trocarDeTela(this, new RegistrarReposicao());
    }//GEN-LAST:event_botaoRegistrarReposicaoActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        funcao.trocarDeTela(this, new RelatorioProdutoEmFalta());
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        funcao.trocarDeTela(this, new RelatorioResumoDePedido());
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        funcao.trocarDeTela(this, new RelatorioConsultarPedido());
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        funcao.trocarDeTela(this, new RelatorioResumoDeReposicao());
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        funcao.trocarDeTela(this, new RelatorioConsultarReposicao());
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        funcao.trocarDeTela(this, new RelatorioBalancoFinanceiro());
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        CadastrarUsuario dialog = new CadastrarUsuario((Frame) this, true);
        OverlayUtil.abrirTela((JFrame) this, dialog);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        cadastrarEditar.setCadastro(true);
        EditarSenhaEmail dialog = new EditarSenhaEmail((Frame) this, true);
        OverlayUtil.abrirTela((JFrame) this, dialog);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
       cadastrarEditar.setCadastro(false);
       EditarSenhaEmail dialog = new EditarSenhaEmail((Frame) this, true);
        OverlayUtil.abrirTela((JFrame) this, dialog);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        funcao.trocarDeTela(this, new ConsultarUsuario());
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
       
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        Suporte dialog = new Suporte((Frame) this, true);
        OverlayUtil.abrirTela((JFrame) this, dialog);
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void botaoExclusoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoExclusoesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botaoExclusoesActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        excluir.setTela("produto");
        funcao.trocarDeTela(this, new ExcluirDados());
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        excluir.setTela("fornecedor");
        funcao.trocarDeTela(this, new ExcluirDados());
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        excluir.setTela("pedido");
        funcao.trocarDeTela(this, new ExcluirDados());
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        excluir.setTela("reposicao");
        funcao.trocarDeTela(this, new ExcluirDados());
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        excluir.setTela("usuario");
        funcao.trocarDeTela(this, new ExcluirDados());
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        funcao.MensagemConfirmar(this,"Deseja realmente criar um novo backup?","","CONFIRMAR BACKUP",() -> novoBackup());
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void botaoBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoBackupActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botaoBackupActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        String caminhoUltimoBackup = Backup.getUltimoBackup();
        String nomeUltimoBackup = (caminhoUltimoBackup != null) 
                ? new File(caminhoUltimoBackup).getName() 
                : "nenhum backup encontrado";

        funcao.MensagemConfirmar(
                this,
                "Deseja realmente restaurar o arquivo \"" + nomeUltimoBackup + "\"?",
                "Essa ação pode alterar ou substituir dados existentes.",
                "CONFIRMAR RESTAURAÇÃO",
            () -> restaurarBackup()
        );       
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem21ActionPerformed

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
                java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(MenuPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new MenuPrincipal().setVisible(true);
                }
            });
        }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bordaConsultarF;
    private javax.swing.JPanel bordaConsultarP;
    private javax.swing.JPanel bordaResgistrarP;
    private javax.swing.JPanel bordaResgistrarR;
    private javax.swing.JPanel bordaSalvarF;
    private javax.swing.JPanel bordaSalvarP;
    private javax.swing.JMenu botaoAuditoria;
    private javax.swing.JMenu botaoBackup;
    private javax.swing.JButton botaoCadastrarFornecedor;
    private javax.swing.JButton botaoCadastrarProduto;
    private javax.swing.JButton botaoConsultarFornecedor;
    private javax.swing.JButton botaoConsultarProduto;
    private javax.swing.JMenu botaoExclusoes;
    private javax.swing.JMenu botaoGerenciarUsuario;
    private javax.swing.JButton botaoLogout;
    private javax.swing.JMenu botaoMeuUsuario;
    private javax.swing.JButton botaoRegistrarPedido;
    private javax.swing.JButton botaoRegistrarReposicao;
    private javax.swing.JMenu botaoRelatorios;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel labalLOGO;
    private javax.swing.JLabel labelAtalhos;
    private javax.swing.JLabel labelAtalhos1;
    private javax.swing.JLabel labelProdutoEmFalta;
    // End of variables declaration//GEN-END:variables
}
