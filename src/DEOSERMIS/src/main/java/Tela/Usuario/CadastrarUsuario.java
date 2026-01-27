/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Tela.Usuario;

import Classes.Funcoes;
import Classes.OverlayUtil;
import Classes.Usuario;
import Classes.UsuarioAtual;
import Classes.UsuarioNovo;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JRootPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


@SuppressWarnings("serial")
public class CadastrarUsuario extends javax.swing.JDialog {
    private final Funcoes funcao = new Funcoes();
    private final UsuarioNovo usuarioNovo = new UsuarioNovo();
    
     public CadastrarUsuario(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        setUndecorated(true);
        setResizable(false);
        initComponents();
        nomeText.requestFocus();
        funcao.configurarTabela(tabelaPermissoes, new Font("Tahoma", Font.BOLD, 14), Color.white, new Color(0, 0, 102));
        funcao.aplicarEfeitoMouse(botaoMenu);  
        funcao.aplicarEfeitoMouse(botaoConfirmar); 
        digitacao();
        carregarTabelaPermissoes();
    }
    
     private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoConfirmar, "Clique para confirmar cadastro");
    }
    
     private void digitacao(){
        funcao.limitarCaracteres(nomeText, 70); 
        funcao.limitarCaracteres(emailText, 100); 
        funcao.limitarCaracteres(confirmaEmailText, 100);
        funcaoEnabled();
    }
    
    private boolean emailValido(){
        return funcao.validarEmail(emailText);
    }
    
     private boolean elegivelSalvar(){
        return funcao.nomeCompleto(nomeText) && emailValido() && funcao.camposIguais(emailText,confirmaEmailText);
    }
    
     private void funcaoEnabled(){
        labelNomeVazio.setVisible(!funcao.nomeCompleto(nomeText));
        labelEmailValido.setVisible(!emailValido());
        labelEmailsIguais.setVisible(!funcao.camposIguais(emailText,confirmaEmailText));
        botaoConfirmar.setEnabled(elegivelSalvar());
        botaoConfirmar.setOpaque(elegivelSalvar());
        tabelaPermissoes.setEnabled(!getAdministrador());
        tabelaPermissoes.setOpaque(!getAdministrador());
        confimarVazio();
        verirficarTipo();
        adicionarAtalhosTeclado();
        adicionarLegendas();
    }
     private void confimarVazio(){
         if (!funcao.validarCampoVazio(confirmaEmailText)){
             labelEmailsIguais.setText("*DIGITE NOVAMENTE O NOVO E-MAIL");
        }else{
             labelEmailsIguais.setText("*E-MAILS DIGITADOS SÃO DIFERENTES ");
         }
    }
     private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoConfirmar);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
    }
     
     private void confirmar(){
        String email = emailText.getText();
        if(usuarioNovo.emailExiste(email)){
            funcao.MensagensJDialog(CadastrarUsuario.this, "O e-mail fornecido já consta no sistema.", "Por favor, utilize outro e-mail.", "ATENÇÃO", "aviso");
            return;
        }         
        if(elegivelSalvar()){
             usuarioNovo.setNome(nomeText.getText());
             usuarioNovo.setEmail(emailText.getText());
             usuarioNovo.setTipo(getAdministrador());
             confirmarPermissoes();
             UsuarioAtual.setAcao(() -> {
                            try {
                                salvar();
                            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                                ex.printStackTrace();
                                funcao.MensagensJDialog(this, "Erro ao salvar usuário: " + ex.getMessage(), "", "ERRO", "erro");
                            }
                        });
             ConfirmarUsuarioAtual dialog = new ConfirmarUsuarioAtual(CadastrarUsuario.this, true);
             OverlayUtil.abrirTelaJDialog(CadastrarUsuario.this, dialog);
             this.dispose();             
        }else{
             funcao.MensagensJDialog(CadastrarUsuario.this, "Preencha os campos corretamente!", "", "ATENÇÃO", "aviso");
        }
     }
     
     private String formatarNomeFuncionalidade(String nomeOriginal) {
        if (nomeOriginal.startsWith("Cad")) {
            return " Cadastrar " + nomeOriginal.substring(3);
        } else if (nomeOriginal.startsWith("Con")) {
            return " Consultar " + nomeOriginal.substring(3);
        } else if (nomeOriginal.startsWith("Reg")) {
            return " Registrar " + nomeOriginal.substring(3);
        } else {
            // padrão para nomes que não seguem o prefixo
            return " "+nomeOriginal;
        }
    }
     
     private void carregarTabelaPermissoes() {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Permitir", "Funcionalidade", "Código"}, 0
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

        try {
            
            List<UsuarioNovo.Funcionalidade> funcionalidades = usuarioNovo.listarFuncionalidades();

            for (UsuarioNovo.Funcionalidade f : funcionalidades) {
                String nomeFormatado = formatarNomeFuncionalidade(f.getNome());
                model.addRow(new Object[]{false, nomeFormatado, f.getCodigo()});
            }

            tabelaPermissoes.setModel(model);

            // Oculta coluna de código
            tabelaPermissoes.getColumnModel().getColumn(2).setMinWidth(0);
            tabelaPermissoes.getColumnModel().getColumn(2).setMaxWidth(0);
            tabelaPermissoes.getColumnModel().getColumn(2).setWidth(0);
            tabelaPermissoes.getColumnModel().getColumn(0).setPreferredWidth(80);

        } catch (Exception ex) {
            ex.printStackTrace();
            funcao.MensagensJDialog(this, "Erro ao carregar permissões!", "", "ERRO", "erro");
        }

        ajustarLarguraColunaPermitir();
    }

     private void selecionarTodosCheckboxes() {
        DefaultTableModel model = (DefaultTableModel) tabelaPermissoes.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(true, i, 0); // Coluna 0 é a de checkbox
        }
    }
     
     private void desmarcarTodosCheckboxes() {
        DefaultTableModel model = (DefaultTableModel) tabelaPermissoes.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, 0);
        }
    }

     private void verirficarTipo(){
         if(getAdministrador()){
             selecionarTodosCheckboxes();
         }else{
             desmarcarTodosCheckboxes();
         }
         tabelaPermissoes.clearSelection();
     }
     
     private void ajustarLarguraColunaPermitir() {
        JTableHeader header = tabelaPermissoes.getTableHeader();
        TableColumn coluna = tabelaPermissoes.getColumnModel().getColumn(0); // coluna "Permitir"

        // Usa o FontMetrics para calcular a largura da palavra "Permitir"
        FontMetrics fm = header.getFontMetrics(header.getFont());
        int larguraTexto = fm.stringWidth("Permitir");

        
        int larguraFinal = larguraTexto + 12;

        // Define a largura fixa da coluna
        coluna.setMinWidth(larguraFinal);
        coluna.setMaxWidth(larguraFinal);
        coluna.setPreferredWidth(larguraFinal);
    }
     
     private void confirmarPermissoes() {
        DefaultTableModel model = (DefaultTableModel) tabelaPermissoes.getModel();
        Map<Integer, Boolean> permissoes = new HashMap<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            boolean permitir = (Boolean) model.getValueAt(i, 0); // checkbox
            int funcionalidadeID = (Integer) model.getValueAt(i, 2); // código da funcionalidade

            permissoes.put(funcionalidadeID, permitir);
        }

        usuarioNovo.setPermissoes(permissoes);
    }
    
     private boolean getAdministrador(){
        return "Administrador".equalsIgnoreCase((String) tipoText.getSelectedItem());
    }

     private void salvar() throws NoSuchAlgorithmException, InvalidKeySpecException{
        String senhaGerada = funcao.gerarSenhaAleatoria(); // senha aleatória para o novo usuário
        String usuarioGerado = funcao.gerarUsuarioID(usuarioNovo.getNome());
        // Cria o novo usuário com hash e salt automáticos
        Usuario novoUsuario;
        if (usuarioNovo.getTipo()) {
            // Admin
            novoUsuario = new Usuario(usuarioGerado, usuarioNovo.getEmail(), senhaGerada, true);
        } else {
            // Não admin com permissões
            novoUsuario = new Usuario(usuarioGerado, usuarioNovo.getEmail(), senhaGerada, false, usuarioNovo.getPermissoes());
        }

        // Insere no banco (ID será gerado automaticamente)
        boolean sucesso = novoUsuario.inserir();
        if (!sucesso) {
            funcao.MensagensJDialog(this, "Erro ao cadastrar usuário!", "", "ERRO", "erro");
            return;
        }

        // Monta a mensagem para envio por e-mail
        String corpo = "Olá " + usuarioNovo.getNome() + ",\n\n"
                + "Seus dados de acesso ao sistema DEOSÉRMIS:\n"
                + "Usuário: " + usuarioGerado + "\n"
                + "Senha Temporária: " + senhaGerada + "\n\n"
                + "Você deve trocar a senha após o primeiro acesso.";

        // Envia email
        if (Funcoes.verificarConexaoInternet(CadastrarUsuario.this)) {
            boolean enviado = Funcoes.enviarEmail(usuarioNovo.getEmail(), "Acesso ao Sistema", corpo);
            if (enviado) {
                funcao.MensagensJDialog(this, "Usuário cadastrado com sucesso!", "Siga as instruções enviadas no e-mail cadastrado","SUCESSO","simples");
                this.dispose();
            } else {
                funcao.MensagensJDialog(this, "Usuário cadastrado, mas não foi possível enviar o e-mail.", "Verifique sua conexão de internet", "ATENÇÃO", "aviso");
                this.dispose();
            }
        }else{
             funcao.MensagensJDialog(this, "Erro: Não foi possível conectar à internet.","Verifique sua conexão e tente novamente", "ERRO DE CONEXÃO", "erro");   
             this.dispose();
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
        IconeTitulo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoConfirmar = new javax.swing.JButton();
        painelDataReposicao = new javax.swing.JDesktopPane();
        jLabel21 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        confirmaEmailText = new javax.swing.JTextField();
        labelEmailsIguais = new javax.swing.JLabel();
        labelEmailValido = new javax.swing.JLabel();
        emailText = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        nomeText = new javax.swing.JTextField();
        labelNomeVazio = new javax.swing.JLabel();
        tipoText = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelaPermissoes = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        labelAtalhos = new javax.swing.JLabel();
        botaoMenu = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        jPanel3.setBackground(new java.awt.Color(210, 164, 2));
        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        IconeTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ADDusuario.png"))); // NOI18N
        jPanel3.add(IconeTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 50, 50));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("NOVO USUÁRIO");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 710, -1));

        jPanel1.add(jPanel3);
        jPanel3.setBounds(0, 0, 710, 60);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoConfirmar.setBackground(new java.awt.Color(210, 164, 2));
        botaoConfirmar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        botaoConfirmar.setForeground(new java.awt.Color(0, 0, 0));
        botaoConfirmar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/CONFIRMAR.png"))); // NOI18N
        botaoConfirmar.setText("CONFIRMAR");
        botaoConfirmar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoConfirmar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoConfirmar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoConfirmar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        botaoConfirmar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoConfirmarActionPerformed(evt);
            }
        });
        bordaSalvar.add(botaoConfirmar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaSalvar);
        bordaSalvar.setBounds(14, 520, 91, 62);

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "DADOS DO USUÁRIO", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/usuarioDados.png"))); // NOI18N
        jLabel21.setOpaque(true);
        painelDataReposicao.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(116, 1, 30, 30));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Permissão:");
        painelDataReposicao.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 200, 360, 30));

        confirmaEmailText.setBackground(new java.awt.Color(255, 255, 255));
        confirmaEmailText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        confirmaEmailText.setForeground(new java.awt.Color(0, 0, 0));
        confirmaEmailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmaEmailTextActionPerformed(evt);
            }
        });
        confirmaEmailText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                confirmaEmailTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(confirmaEmailText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 360, -1));

        labelEmailsIguais.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelEmailsIguais.setForeground(new java.awt.Color(204, 0, 0));
        labelEmailsIguais.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelEmailsIguais.setText("*E-MAILS DIGITADOS SÃO DIFERENTES ");
        painelDataReposicao.add(labelEmailsIguais, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 360, 20));

        labelEmailValido.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelEmailValido.setForeground(new java.awt.Color(204, 0, 0));
        labelEmailValido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelEmailValido.setText("*DIGITE E-MAIL VÁLIDO");
        painelDataReposicao.add(labelEmailValido, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 360, 20));

        emailText.setBackground(new java.awt.Color(255, 255, 255));
        emailText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        emailText.setForeground(new java.awt.Color(0, 0, 0));
        emailText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailTextActionPerformed(evt);
            }
        });
        emailText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(emailText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 90, 360, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("*E-mail:");
        painelDataReposicao.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 90, 200, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("*Nome Completo:");
        painelDataReposicao.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 40, 200, -1));

        nomeText.setBackground(new java.awt.Color(255, 255, 255));
        nomeText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nomeText.setForeground(new java.awt.Color(0, 0, 0));
        nomeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nomeTextActionPerformed(evt);
            }
        });
        nomeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nomeTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(nomeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 360, -1));

        labelNomeVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelNomeVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelNomeVazio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNomeVazio.setText("*DIGITE O NOME COMPLETO DO USUÁRIO");
        painelDataReposicao.add(labelNomeVazio, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 360, 20));

        tipoText.setBackground(new java.awt.Color(255, 255, 255));
        tipoText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tipoText.setForeground(new java.awt.Color(0, 0, 0));
        tipoText.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Comum", "Administrador" }));
        tipoText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoTextActionPerformed(evt);
            }
        });
        painelDataReposicao.add(tipoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 190, 120, 30));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("*Confirme E-mail:");
        painelDataReposicao.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 140, 200, -1));

        tabelaPermissoes.setBackground(new java.awt.Color(255, 255, 255));
        tabelaPermissoes.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 16)); // NOI18N
        tabelaPermissoes.setForeground(new java.awt.Color(0, 0, 0));
        tabelaPermissoes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Permitir", "Funcionalidade"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaPermissoes.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaPermissoes.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaPermissoes.getTableHeader().setReorderingAllowed(false);
        tabelaPermissoes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaPermissoesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelaPermissoes);
        if (tabelaPermissoes.getColumnModel().getColumnCount() > 0) {
            tabelaPermissoes.getColumnModel().getColumn(0).setResizable(false);
            tabelaPermissoes.getColumnModel().getColumn(0).setPreferredWidth(5);
            tabelaPermissoes.getColumnModel().getColumn(1).setResizable(false);
            tabelaPermissoes.getColumnModel().getColumn(1).setPreferredWidth(100);
        }

        painelDataReposicao.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 360, 200));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("*Tipo de Usuário:");
        painelDataReposicao.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-40, 190, 200, 30));

        jPanel1.add(painelDataReposicao);
        painelDataReposicao.setBounds(70, 80, 570, 440);

        labelAtalhos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        labelAtalhos.setForeground(new java.awt.Color(0, 102, 0));
        labelAtalhos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAtalhos.setText("ATALHOS: F1 = Confirmar Novo Usuário | F12 = Menu Principal");
        jPanel1.add(labelAtalhos);
        labelAtalhos.setBounds(0, 60, 710, 16);

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
        botaoMenu.setBounds(640, 520, 60, 60);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoConfirmarActionPerformed
        confirmar();
    }//GEN-LAST:event_botaoConfirmarActionPerformed

    private void nomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeTextActionPerformed

    private void nomeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeTextKeyReleased
       digitacao();
    }//GEN-LAST:event_nomeTextKeyReleased

    private void emailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailTextActionPerformed

    private void emailTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextKeyReleased
        digitacao();
    }//GEN-LAST:event_emailTextKeyReleased

    private void confirmaEmailTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmaEmailTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_confirmaEmailTextActionPerformed

    private void confirmaEmailTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmaEmailTextKeyReleased
        digitacao();
    }//GEN-LAST:event_confirmaEmailTextKeyReleased

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        this.dispose();
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void tipoTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoTextActionPerformed
        digitacao();
    }//GEN-LAST:event_tipoTextActionPerformed

    private void tabelaPermissoesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaPermissoesMouseClicked

    }//GEN-LAST:event_tabelaPermissoesMouseClicked

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
            java.util.logging.Logger.getLogger(CadastrarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CadastrarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CadastrarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CadastrarUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CadastrarUsuario dialog = new CadastrarUsuario(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel IconeTitulo;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoConfirmar;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JTextField confirmaEmailText;
    private javax.swing.JTextField emailText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelAtalhos;
    private javax.swing.JLabel labelEmailValido;
    private javax.swing.JLabel labelEmailsIguais;
    private javax.swing.JLabel labelNomeVazio;
    private javax.swing.JTextField nomeText;
    private javax.swing.JDesktopPane painelDataReposicao;
    private javax.swing.JTable tabelaPermissoes;
    private javax.swing.JComboBox<String> tipoText;
    // End of variables declaration//GEN-END:variables
}
