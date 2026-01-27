/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Tela.Fornecedor;

import Classes.OpcaoDeTela;
import Classes.Fornecedor;
import Classes.Funcoes;
import Tela.Inicio.MenuPrincipal;
import javax.swing.JRootPane;
import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class CadastrarEditarFornecedor extends javax.swing.JFrame {
    private final Funcoes funcao = new Funcoes();
    private final Fornecedor fornecedor = new Fornecedor();
    private final OpcaoDeTela cadastrarEditar = new OpcaoDeTela();
    
    public CadastrarEditarFornecedor() {      
        setUndecorated(true);
        initComponents();
        montarTela();
        adicionarAtalhosTeclado();
    }
    
    private void montarTela(){
        funcao.preencherEstados(ufText);
        nomeText.requestFocus();
        cadastroEdicao();
        limpar(); 
        funcao.aplicarMascaraCEP(cepText);
        funcao.aplicarMascaraCpfCnpj(cpfcnpjText);
        funcao.aplicarMascaraWhatsApp(whatsappText);         
        funcao.aplicarEfeitoMouse(botaoLimpar);
        funcao.aplicarEfeitoMouse(botaoMenu);  
        funcao.aplicarEfeitoMouse(botaoSalvar); 
        digitacao();
        validarCampos();
    }
  
    private void cadastroEdicao(){
        if (cadastrarEditar.getCadastro()){
            limparCampos();
            labelTextoTitulo.setText("CADASTRAR FORNECEDOR");
            labelAtalhos.setText("ATALHOS: F1 = Salvar Cadastro do Fornecedor | F5 = Limpar Campos | F12 = Menu Principal");
            botaoMenu.setText("MENU");
            botaoMenu.setIcon(new ImageIcon(getClass().getResource("/imagens/home.png")));
            botaoSalvar.setText("SALVAR CADASTRO");
            botaoSalvar.setIcon(new ImageIcon(getClass().getResource("/imagens/SALVARcad.png")));
            botaoLimpar.setText("LIMPAR CAMPOS");
            IconeTitulo.setIcon(new ImageIcon(getClass().getResource("/imagens/ADDfornecedor.png")));
            botaoMenu.setToolTipText("Clique para voltar ao menu principal");
        }else{
            preencherCampos();
            labelTextoTitulo.setText("EDITAR FORNECEDOR");
            labelAtalhos.setText("ATALHOS: F1 = Salvar Edição do Fornecedor | F5 = Desfazer Alterações | F12 = Voltar Para Consultar Fornecedor");
            botaoMenu.setText("VOLTAR");
            botaoMenu.setIcon(new ImageIcon(getClass().getResource("/imagens/VOLTAR.png")));
            botaoSalvar.setText("SALVAR EDIÇÃO");
            botaoSalvar.setIcon(new ImageIcon(getClass().getResource("/imagens/SALVARedit.png")));
            botaoLimpar.setText("DESFAZER ALTERAÇÕES");
            IconeTitulo.setIcon(new ImageIcon(getClass().getResource("/imagens/Fornecedor.png")));
            botaoMenu.setToolTipText("Clique para voltar a consultar fornecedor");
        }
        codigoText.setVisible(!cadastrarEditar.getCadastro());
        labelCodigoFornecedor.setVisible(!cadastrarEditar.getCadastro());
    }
    
    private void digitacao(){
        funcao.limitarCaracteres(nomeText, 50);
        funcao.limitarCaracteres(cpfcnpjText, 18);
        funcao.limitarCaracteres(whatsappText, 14);
        funcao.limitarCaracteres(emailText, 100);
        funcao.limitarCaracteres(instagramText, 50);
        funcao.limitarCaracteres(siteText, 150);
        funcao.limitarCaracteres(cepText, 9);
        funcao.limitarCaracteres(cidadeText, 100);
        funcao.limitarCaracteres(bairroText, 50);
        funcao.limitarCaracteres(logradouroText, 100);
        funcao.limitarCaracteres(numeroText, 15);
        funcao.limitarCaracteres(complementoText, 30);
        funcao.limitarCaracteres(observacaoText, 255);
        funcao.validarCampoComDica(cpfcnpjText, "Exemplo: 123.456.789-10 ou 12.345.678/0001-90");
        funcao.validarCampoComDica(whatsappText, "Exemplo: (11) 98765-4321");
        funcao.validarCampoComDica(cepText, "Exemplo: 12345-678");
        validarCampos();
        funcaoEnabled();        
    }
    
    private void limparCampos(){
        funcao.limparComponentes(nomeText, 
                                cpfcnpjText,
                                emailText, 
                                instagramText,
                                whatsappText, 
                                siteText, 
                                cepText,
                                ufText,
                                cidadeText, 
                                bairroText,
                                logradouroText,
                                numeroText,
                                complementoText,
                                observacaoText);
        funcaoEnabled();
    }
        
   private void preencherCampos() {
        if (fornecedor.consultarFornecedor(fornecedor.getCodigoAlterar(),this)) {
            codigoText.setText(fornecedor.getCodigo().toString());

            nomeText.setText(fornecedor.getNome() == null ? "" : fornecedor.getNome());
            emailText.setText(fornecedor.getEmail() == null ? "" : fornecedor.getEmail());
            instagramText.setText(fornecedor.getInstagram() == null ? "" : fornecedor.getInstagram());
            siteText.setText(fornecedor.getSite() == null ? "" : fornecedor.getSite());
            cidadeText.setText(fornecedor.getCidade() == null ? "" : fornecedor.getCidade());
            bairroText.setText(fornecedor.getBairro() == null ? "" : fornecedor.getBairro());
            logradouroText.setText(fornecedor.getLogradouro() == null ? "" : fornecedor.getLogradouro());
            numeroText.setText(fornecedor.getNumero() == null ? "" : fornecedor.getNumero());
            complementoText.setText(fornecedor.getComplemento() == null ? "" : fornecedor.getComplemento());
            observacaoText.setText(fornecedor.getObs() == null ? "" : fornecedor.getObs());

            // Campos que usam máscara precisam da mesma verificação
            cpfcnpjText.setText(fornecedor.getCPFCNPJ() == null ? "" : funcao.formatarCpfCnpj(fornecedor.getCPFCNPJ()));
            whatsappText.setText(fornecedor.getWhatsApp() == null ? "" : funcao.formatarWhatsApp(fornecedor.getWhatsApp()));
            cepText.setText(fornecedor.getCEP() == null ? "" : funcao.formatarCEP(fornecedor.getCEP()));

            if (fornecedor.getUF() != null) {
                ufText.setSelectedItem(fornecedor.getUF());
            } else {
                ufText.setSelectedItem("Selecione");
            }

            funcaoEnabled();
        } else {
            funcao.Mensagens(this, "Fornecedor não encontrado.","", "ERRO", "erro");
            funcao.trocarDeTela(this, new ConsultarFornecedor());
        }
    }
    
    private void adicionarAtalhosTeclado() {
        JRootPane root = getRootPane();
        funcao.adicionarAtalho(root, "F1", botaoSalvar);
        funcao.adicionarAtalho(root, "F5", botaoLimpar);
        funcao.adicionarAtalho(root, "F12", botaoMenu);
    }

    private void salvarCadastrarFornecedor() {
        try {
                                    
            String nome = nomeText.getText().trim();
            String cpfCnpjFormatado = cpfcnpjText.getText().replaceAll("\\D", "");
            String whatsFormatado = whatsappText.getText().replaceAll("\\D", ""); 
            String cepFormatado = cepText.getText().replaceAll("\\D", "");

            if (fornecedor.fornecedorExisteCampo(cpfCnpjFormatado, "CPFCNPJ",this)) {
                funcao.Mensagens(this, "Já existe um fornecedor com este CPF/CNPJ","", "ATENÇÃO!", "aviso");
                cpfcnpjText.requestFocus();
                return;
            }

            if (fornecedor.fornecedorExisteCampo(nome, "nome",this)) {
                funcao.Mensagens(this, "Já existe um fornecedor com este nome cadastrado", "","ATENÇÃO!", "aviso");
                nomeText.requestFocus();
                return;
            }
            
            //Inserir dados
            fornecedor.setNome(nome);
            fornecedor.setCPFCNPJ(cpfCnpjFormatado);
            fornecedor.setEmail(funcao.textoOuNullMinusculo(emailText));
            fornecedor.setInstagram(funcao.textoOuNullMinusculo(instagramText));
            fornecedor.setWhatsApp(whatsFormatado.isEmpty() ? null : whatsFormatado);
            fornecedor.setSite(funcao.textoOuNullMinusculo(siteText));
            fornecedor.setCEP(cepFormatado.isEmpty() ? null : cepFormatado);  
            if (funcao.validarComboBox(ufText)){
                fornecedor.setUF((String) ufText.getSelectedItem());
            }else{
                fornecedor.setUF(null);
            }            
            fornecedor.setCidade(funcao.textoOuNull(cidadeText));
            fornecedor.setBairro(funcao.textoOuNull(bairroText));
            fornecedor.setLogradouro(funcao.textoOuNull(logradouroText));
            fornecedor.setNumero(funcao.textoOuNull(numeroText));
            fornecedor.setComplemento(funcao.textoOuNull(complementoText));
            fornecedor.setObs(funcao.textoOuNull(observacaoText));

            // Salva no banco
            fornecedor.inserirFornecedor(this);

            // Limpa campos após salvar
            limparCampos();

        } catch (Exception e) {
            funcao.Mensagens(this, "Erro ao salvar fornecedor: " , e.getMessage(), "ERRO", "erro");
        }
    }

    private void salvarEditarFornecedor() {
        try {
            String nome = nomeText.getText().trim();
            String cpfCnpjFormatado = cpfcnpjText.getText().replaceAll("\\D", "");
            String whatsFormatado = whatsappText.getText().replaceAll("\\D", "");
            String cepFormatado = cepText.getText().replaceAll("\\D", "");

            fornecedor.setCodigo(fornecedor.getCodigoAlterar());

            if (fornecedor.fornecedorExisteCampo(cpfCnpjFormatado, "CPFCNPJ",this)) {
                funcao.Mensagens(this, "Já existe um fornecedor com este CPF/CNPJ","", "ATENÇÃO!", "aviso");
                cpfcnpjText.requestFocus();
                return;
            }

            if (fornecedor.fornecedorExisteCampo(nome, "nome",this)) {
                funcao.Mensagens(this, "Já existe um fornecedor com este nome cadastrado.", "", "ATENÇÃO!", "aviso");
                nomeText.requestFocus();
                return;
            }

            fornecedor.setNome(nome);
            fornecedor.setCPFCNPJ(cpfCnpjFormatado);
            fornecedor.setEmail(funcao.textoOuNullMinusculo(emailText));
            fornecedor.setInstagram(funcao.textoOuNullMinusculo(instagramText));
            fornecedor.setWhatsApp(whatsFormatado.isEmpty() ? null : whatsFormatado);
            fornecedor.setSite(funcao.textoOuNullMinusculo(siteText));
            fornecedor.setCEP(cepFormatado.isEmpty() ? null : cepFormatado);
            if (funcao.validarComboBox(ufText)) {
                fornecedor.setUF((String) ufText.getSelectedItem());
            } else {
                fornecedor.setUF(null);
            }
            fornecedor.setCidade(funcao.textoOuNull(cidadeText));
            fornecedor.setBairro(funcao.textoOuNull(bairroText));
            fornecedor.setLogradouro(funcao.textoOuNull(logradouroText));
            fornecedor.setNumero(funcao.textoOuNull(numeroText));
            fornecedor.setComplemento(funcao.textoOuNull(complementoText));
            fornecedor.setObs(funcao.textoOuNull(observacaoText));

            fornecedor.alterarFornecedor(this);
            funcao.Mensagens(this, "Fornecedor atualizado com sucesso","", "SUCESSO" ,"informacao"); 
            trocarTela();
              
        } catch (Exception e) {
            funcao.Mensagens(this, "Erro ao salvar edição do fornecedor: " , e.getMessage(), "ERRO", "erro");
            return;
        }finally{
            fornecedor.setCodigoAlterar(null);
        }
    }
    
    private void funcaoEnabled(){
        labelNomeVazio.setVisible(!funcao.validarCampoVazio(nomeText));
        labelCPFCNPJvalido.setVisible(!funcao.validarCpfCnpjCampo(cpfcnpjText));
        labelEmailValido.setVisible(!emailValido());
        labelWhatsValido.setVisible(!whatsappValido());
        labelCEPvalido.setVisible(!cepValido());
        botaoSalvar.setEnabled(enabledSalvar());
        botaoSalvar.setOpaque(enabledSalvar());
        botaoLimpar.setEnabled(enabledLimpar());
        botaoLimpar.setOpaque(enabledLimpar());
        adicionarAtalhosTeclado();
    }
    
    private boolean enabledLimpar(){
        if (cadastrarEditar.getCadastro()){
            funcao.validarBotaoComDica(botaoLimpar, "Clique para limpar os campos");
            return !camposVazio();
        }else{
            funcao.validarBotaoComDica(botaoLimpar, "Clique para desfazer alterações");
            return !validarAlteracoes();
        }
    }
    
    private boolean enabledSalvar(){
        if (cadastrarEditar.getCadastro()){
            funcao.validarBotaoComDica(botaoSalvar, "Clique para salvar o cadastro do fornecedor");
            return elegivelSalvar();
        }else{
            funcao.validarBotaoComDica(botaoSalvar, "Clique para salvar edição do fornecedor");
            return !validarAlteracoes() && elegivelSalvar();
        }
    }
    
    private boolean elegivelSalvar(){
        return funcao.validarCampoVazio(nomeText) && funcao.validarCpfCnpjCampo(cpfcnpjText) && emailValido() && whatsappValido() && cepValido();
    }
    
    private boolean camposVazio(){
        return funcao.validarCamposVazios(nomeText, 
                                cpfcnpjText,
                                emailText, 
                                instagramText,
                                whatsappText, 
                                siteText, 
                                cepText,
                                ufText,
                                cidadeText, 
                                bairroText,
                                logradouroText,
                                numeroText,
                                complementoText,
                                observacaoText);
    }
    
    private boolean validarAlteracoes() {
        String obsAtual = observacaoText.getText().trim();
        String obsOriginal = fornecedor.getObs();

        if (obsAtual.isEmpty()) {
            obsAtual = null;
        }
        if (obsOriginal != null && obsOriginal.isEmpty()) {
            obsOriginal = null;
        }

        boolean obsIgual;
        if (obsAtual == null && obsOriginal == null) {
            obsIgual = true;
        } else if (obsAtual != null && obsOriginal != null) {
            obsIgual = obsAtual.equals(obsOriginal);
        } else {
            obsIgual = false;
        }

        return funcao.compararTextoComBanco(nomeText, fornecedor.getNome(), false) &&
               funcao.compararTextoComBanco(cpfcnpjText, fornecedor.getCPFCNPJ(), true) &&
               funcao.compararTextoComBanco(emailText, fornecedor.getEmail(), false) &&
               funcao.compararTextoComBanco(instagramText, fornecedor.getInstagram(), false) &&
               funcao.compararTextoComBanco(whatsappText, fornecedor.getWhatsApp(), true) &&
               funcao.compararTextoComBanco(siteText, fornecedor.getSite(), false) &&
               funcao.compararTextoComBanco(cepText, fornecedor.getCEP(), true) &&
               funcao.compararTextoComBanco(ufText, fornecedor.getUF(), false) &&
               funcao.compararTextoComBanco(cidadeText, fornecedor.getCidade(), false) &&
               funcao.compararTextoComBanco(bairroText, fornecedor.getBairro(), false) &&
               funcao.compararTextoComBanco(logradouroText, fornecedor.getLogradouro(), false) &&
               funcao.compararTextoComBanco(numeroText, fornecedor.getNumero(), false) &&
               funcao.compararTextoComBanco(complementoText, fornecedor.getComplemento(), false) &&
               obsIgual;
    }

    private boolean emailValido(){
        String email = emailText.getText().trim();
            if (email.isEmpty()) {
                return true;
            }
        return funcao.emailValido(email);
    }
    
    private boolean whatsappValido() {
        String whats = whatsappText.getText().replaceAll("\\D", "");
        if (whats.isEmpty()) {
            return true; // Campo vazio é considerado válido (não obrigatório)
        }
        // Verifica se bate com o formato de 11 dígitos
    return whats.matches("^\\d{11}$");
}
    
    private boolean cepValido() {
        String cep = cepText.getText().replaceAll("\\D", "");
        if (cep.isEmpty()) {
            return true; // Campo vazio é considerado válido (não obrigatório)
        }
        // Valida o formato de 8 dígitos
    return cep.matches("^\\d{8}$");
}
    
    private void trocarTela(){
        if (cadastrarEditar.getCadastro()){
            funcao.trocarDeTela(this, new MenuPrincipal());
        }else{
            funcao.trocarDeTela(this, new ConsultarFornecedor());
        }
    }
    
    private void salvar(){
        validarCampos();
        if(elegivelSalvar()){
            if (cadastrarEditar.getCadastro()){
                salvarCadastrarFornecedor();
            }else{
                salvarEditarFornecedor();
            }
            nomeText.requestFocus();
        }else{
            funcao.Mensagens(this, "Preencha os campos corretamente!","", "ATENÇÃO", "aviso");
             funcaoEnabled();
        }
    }
    
    private void limpar(){
        if (cadastrarEditar.getCadastro()){
            limparCampos();
        }else{
            preencherCampos();
        }
        nomeText.requestFocus();
    }
    private void validarCampos(){
        funcao.validarTexto(nomeText, 50);
        funcao.validarTexto(nomeText, 50);
        funcao.validarTexto(cpfcnpjText, 18);
        funcao.validarTexto(whatsappText, 14);
        funcao.validarTexto(emailText, 100);
        funcao.validarTexto(instagramText, 50);
        funcao.validarTexto(siteText, 150);
        funcao.validarTexto(cepText, 9);
        funcao.validarTexto(cidadeText, 100);
        funcao.validarTexto(bairroText, 50);
        funcao.validarTexto(logradouroText, 100);
        funcao.validarTexto(numeroText, 15);
        funcao.validarTexto(complementoText, 30);
        funcao.validarTextoArea(observacaoText, 255);
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
        painelSuperior = new javax.swing.JPanel();
        iconePrograma = new javax.swing.JLabel();
        IconeTitulo = new javax.swing.JLabel();
        labelCodigoFornecedor = new javax.swing.JLabel();
        codigoText = new javax.swing.JTextField();
        labelTextoTitulo = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoSalvar = new javax.swing.JButton();
        bordaLimpar = new javax.swing.JPanel();
        botaoLimpar = new javax.swing.JButton();
        painelFormulario = new javax.swing.JDesktopPane();
        ufText = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        siteText = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        instagramText = new javax.swing.JTextField();
        emailText = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        nomeText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cidadeText = new javax.swing.JTextField();
        bairroText = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        logradouroText = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        numeroText = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        complementoText = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        observacaoText = new javax.swing.JTextArea();
        labelEmailValido = new javax.swing.JLabel();
        labelNomeVazio = new javax.swing.JLabel();
        labelCPFCNPJvalido = new javax.swing.JLabel();
        whatsappText = new javax.swing.JTextField();
        labelWhatsValido = new javax.swing.JLabel();
        labelCEPvalido = new javax.swing.JLabel();
        cepText = new javax.swing.JTextField();
        cpfcnpjText = new javax.swing.JTextField();
        botaoMenu = new javax.swing.JButton();
        textoRodape = new javax.swing.JLabel();
        labelAtalhos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1070, 696));
        setSize(new java.awt.Dimension(1070, 696));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        painelSuperior.setBackground(new java.awt.Color(210, 164, 2));
        painelSuperior.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        painelSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconePrograma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        painelSuperior.add(iconePrograma, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        IconeTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ADDfornecedor.png"))); // NOI18N
        painelSuperior.add(IconeTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 10, 50, 50));

        labelCodigoFornecedor.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labelCodigoFornecedor.setForeground(new java.awt.Color(0, 0, 0));
        labelCodigoFornecedor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCodigoFornecedor.setText("CÓDIGO:");
        painelSuperior.add(labelCodigoFornecedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 0, 240, 30));

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
        painelSuperior.add(codigoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 25, 240, 30));

        labelTextoTitulo.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        labelTextoTitulo.setForeground(new java.awt.Color(0, 0, 0));
        labelTextoTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTextoTitulo.setText("EDITAR FORNECEDOR");
        painelSuperior.add(labelTextoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 1070, -1));

        jPanel1.add(painelSuperior);
        painelSuperior.setBounds(0, 0, 1070, 60);

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
        bordaSalvar.setBounds(240, 630, 91, 62);

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
        bordaLimpar.setBounds(740, 630, 91, 62);

        painelFormulario.setBackground(new java.awt.Color(255, 255, 255));
        painelFormulario.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        ufText.setBackground(new java.awt.Color(255, 255, 255));
        ufText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ufText.setForeground(new java.awt.Color(0, 0, 0));
        ufText.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Selecione" }));
        ufText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ufTextActionPerformed(evt);
            }
        });
        painelFormulario.add(ufText);
        ufText.setBounds(580, 210, 120, 23);

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("UF:");
        painelFormulario.add(jLabel10);
        jLabel10.setBounds(420, 210, 140, 20);

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("CEP:");
        painelFormulario.add(jLabel12);
        jLabel12.setBounds(0, 210, 140, 20);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Site:");
        painelFormulario.add(jLabel9);
        jLabel9.setBounds(0, 170, 140, 20);

        siteText.setBackground(new java.awt.Color(255, 255, 255));
        siteText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        siteText.setForeground(new java.awt.Color(0, 0, 0));
        siteText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                siteTextKeyReleased(evt);
            }
        });
        painelFormulario.add(siteText);
        siteText.setBounds(160, 170, 540, 23);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("WhatsApp:");
        painelFormulario.add(jLabel8);
        jLabel8.setBounds(400, 50, 140, 20);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Instagram:");
        painelFormulario.add(jLabel7);
        jLabel7.setBounds(0, 130, 140, 20);

        instagramText.setBackground(new java.awt.Color(255, 255, 255));
        instagramText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        instagramText.setForeground(new java.awt.Color(0, 0, 0));
        instagramText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                instagramTextKeyReleased(evt);
            }
        });
        painelFormulario.add(instagramText);
        instagramText.setBounds(160, 130, 540, 23);

        emailText.setBackground(new java.awt.Color(255, 255, 255));
        emailText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        emailText.setForeground(new java.awt.Color(0, 0, 0));
        emailText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailTextKeyReleased(evt);
            }
        });
        painelFormulario.add(emailText);
        emailText.setBounds(160, 90, 540, 23);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("E-mail:");
        painelFormulario.add(jLabel5);
        jLabel5.setBounds(0, 90, 140, 20);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("*CPF/CNPJ:");
        painelFormulario.add(jLabel6);
        jLabel6.setBounds(0, 50, 140, 20);

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
        painelFormulario.add(nomeText);
        nomeText.setBounds(160, 10, 540, 23);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("*Nome:");
        painelFormulario.add(jLabel1);
        jLabel1.setBounds(0, 10, 140, 20);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Cidade:");
        painelFormulario.add(jLabel13);
        jLabel13.setBounds(0, 250, 140, 20);

        cidadeText.setBackground(new java.awt.Color(255, 255, 255));
        cidadeText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cidadeText.setForeground(new java.awt.Color(0, 0, 0));
        cidadeText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cidadeTextKeyReleased(evt);
            }
        });
        painelFormulario.add(cidadeText);
        cidadeText.setBounds(160, 250, 540, 23);

        bairroText.setBackground(new java.awt.Color(255, 255, 255));
        bairroText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        bairroText.setForeground(new java.awt.Color(0, 0, 0));
        bairroText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                bairroTextKeyReleased(evt);
            }
        });
        painelFormulario.add(bairroText);
        bairroText.setBounds(160, 290, 540, 23);

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Bairro:");
        painelFormulario.add(jLabel14);
        jLabel14.setBounds(0, 290, 140, 20);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Logradouro:");
        painelFormulario.add(jLabel15);
        jLabel15.setBounds(0, 330, 140, 20);

        logradouroText.setBackground(new java.awt.Color(255, 255, 255));
        logradouroText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        logradouroText.setForeground(new java.awt.Color(0, 0, 0));
        logradouroText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                logradouroTextKeyReleased(evt);
            }
        });
        painelFormulario.add(logradouroText);
        logradouroText.setBounds(160, 330, 540, 23);

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Número:");
        painelFormulario.add(jLabel16);
        jLabel16.setBounds(0, 370, 140, 20);

        numeroText.setBackground(new java.awt.Color(255, 255, 255));
        numeroText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        numeroText.setForeground(new java.awt.Color(0, 0, 0));
        numeroText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                numeroTextKeyReleased(evt);
            }
        });
        painelFormulario.add(numeroText);
        numeroText.setBounds(160, 370, 150, 23);

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Complemento:");
        painelFormulario.add(jLabel17);
        jLabel17.setBounds(320, 370, 140, 20);

        complementoText.setBackground(new java.awt.Color(255, 255, 255));
        complementoText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        complementoText.setForeground(new java.awt.Color(0, 0, 0));
        complementoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                complementoTextKeyReleased(evt);
            }
        });
        painelFormulario.add(complementoText);
        complementoText.setBounds(480, 370, 220, 23);

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Observação:");
        painelFormulario.add(jLabel19);
        jLabel19.setBounds(0, 410, 140, 20);

        observacaoText.setBackground(new java.awt.Color(255, 255, 255));
        observacaoText.setColumns(20);
        observacaoText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        observacaoText.setForeground(new java.awt.Color(0, 0, 0));
        observacaoText.setLineWrap(true);
        observacaoText.setRows(5);
        observacaoText.setWrapStyleWord(true);
        observacaoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                observacaoTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                observacaoTextKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(observacaoText);

        painelFormulario.add(jScrollPane1);
        jScrollPane1.setBounds(160, 410, 540, 100);

        labelEmailValido.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelEmailValido.setForeground(new java.awt.Color(204, 0, 0));
        labelEmailValido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelEmailValido.setText("*DIGITE E-MAIL VÁLIDO");
        painelFormulario.add(labelEmailValido);
        labelEmailValido.setBounds(160, 110, 540, 20);

        labelNomeVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelNomeVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelNomeVazio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNomeVazio.setText("*DIGITE O NOME DO FORNECEDOR");
        painelFormulario.add(labelNomeVazio);
        labelNomeVazio.setBounds(160, 30, 470, 20);

        labelCPFCNPJvalido.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelCPFCNPJvalido.setForeground(new java.awt.Color(204, 0, 0));
        labelCPFCNPJvalido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelCPFCNPJvalido.setText("*DIGITE CPF OU CNPJ VÁLIDO");
        painelFormulario.add(labelCPFCNPJvalido);
        labelCPFCNPJvalido.setBounds(160, 70, 170, 20);

        whatsappText.setBackground(new java.awt.Color(255, 255, 255));
        whatsappText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        whatsappText.setForeground(new java.awt.Color(0, 0, 0));
        whatsappText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                whatsappTextKeyReleased(evt);
            }
        });
        painelFormulario.add(whatsappText);
        whatsappText.setBounds(560, 50, 140, 23);

        labelWhatsValido.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelWhatsValido.setForeground(new java.awt.Color(204, 0, 0));
        labelWhatsValido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelWhatsValido.setText("*DIGITE NÚMERO VÁLIDO");
        painelFormulario.add(labelWhatsValido);
        labelWhatsValido.setBounds(560, 70, 140, 20);

        labelCEPvalido.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelCEPvalido.setForeground(new java.awt.Color(204, 0, 0));
        labelCEPvalido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelCEPvalido.setText("*DIGITE CEP VÁLIDO");
        painelFormulario.add(labelCEPvalido);
        labelCEPvalido.setBounds(160, 230, 120, 20);

        cepText.setBackground(new java.awt.Color(255, 255, 255));
        cepText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cepText.setForeground(new java.awt.Color(0, 0, 0));
        cepText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cepTextFocusLost(evt);
            }
        });
        cepText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cepTextKeyReleased(evt);
            }
        });
        painelFormulario.add(cepText);
        cepText.setBounds(160, 210, 120, 23);

        cpfcnpjText.setBackground(new java.awt.Color(255, 255, 255));
        cpfcnpjText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cpfcnpjText.setForeground(new java.awt.Color(0, 0, 0));
        cpfcnpjText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cpfcnpjTextFocusLost(evt);
            }
        });
        cpfcnpjText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cpfcnpjTextKeyReleased(evt);
            }
        });
        painelFormulario.add(cpfcnpjText);
        cpfcnpjText.setBounds(160, 50, 170, 23);

        jPanel1.add(painelFormulario);
        painelFormulario.setBounds(170, 90, 730, 520);

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

        textoRodape.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        textoRodape.setForeground(new java.awt.Color(0, 0, 0));
        textoRodape.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        textoRodape.setText("*Preenchimento obrigatório de campo(s)");
        jPanel1.add(textoRodape);
        textoRodape.setBounds(170, 610, 730, 15);

        labelAtalhos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 14)); // NOI18N
        labelAtalhos.setForeground(new java.awt.Color(0, 102, 0));
        labelAtalhos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAtalhos.setText("ATALHOS: F1 = Salvar Cadastro Fornecedor | F5 = Limpar Campos | F12 = Menu Principal");
        jPanel1.add(labelAtalhos);
        labelAtalhos.setBounds(0, 60, 1070, 16);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1070, 696);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        trocarTela();
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        salvar();
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void botaoLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoLimparActionPerformed
        limpar();
    }//GEN-LAST:event_botaoLimparActionPerformed

    private void nomeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeTextKeyReleased
        digitacao();
    }//GEN-LAST:event_nomeTextKeyReleased

    private void emailTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextKeyReleased
        digitacao();
    }//GEN-LAST:event_emailTextKeyReleased

    private void instagramTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_instagramTextKeyReleased
        digitacao();
    }//GEN-LAST:event_instagramTextKeyReleased

    private void siteTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_siteTextKeyReleased
        digitacao();
    }//GEN-LAST:event_siteTextKeyReleased

    private void cidadeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cidadeTextKeyReleased
        digitacao();
    }//GEN-LAST:event_cidadeTextKeyReleased

    private void bairroTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bairroTextKeyReleased
        digitacao();
    }//GEN-LAST:event_bairroTextKeyReleased

    private void logradouroTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_logradouroTextKeyReleased
        digitacao();
    }//GEN-LAST:event_logradouroTextKeyReleased

    private void numeroTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numeroTextKeyReleased
        digitacao();
    }//GEN-LAST:event_numeroTextKeyReleased

    private void complementoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_complementoTextKeyReleased
        digitacao();
    }//GEN-LAST:event_complementoTextKeyReleased

    private void observacaoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_observacaoTextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_observacaoTextKeyTyped

    private void observacaoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_observacaoTextKeyReleased
        digitacao();
    }//GEN-LAST:event_observacaoTextKeyReleased

    private void whatsappTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_whatsappTextKeyReleased
         digitacao();
    }//GEN-LAST:event_whatsappTextKeyReleased

    private void cepTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cepTextKeyReleased
        digitacao();
    }//GEN-LAST:event_cepTextKeyReleased

    private void cepTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cepTextFocusLost
        String cep = cepText.getText().replaceAll("\\D", "");
        if (cep.length() == 8) {
            funcao.buscarEndereco(cepText.getText(),logradouroText,numeroText,bairroText,cidadeText,ufText,complementoText,this);
        }else{
            funcao.limparComponentes(logradouroText,numeroText,bairroText,cidadeText,ufText,complementoText);
        }
        digitacao();
    }//GEN-LAST:event_cepTextFocusLost

    private void codigoTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_codigoTextFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoTextFocusLost

    private void codigoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_codigoTextKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_codigoTextKeyReleased

    private void cpfcnpjTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cpfcnpjTextFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cpfcnpjTextFocusLost

    private void cpfcnpjTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cpfcnpjTextKeyReleased
        digitacao();
    }//GEN-LAST:event_cpfcnpjTextKeyReleased

    private void nomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeTextActionPerformed

    private void ufTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ufTextActionPerformed
        digitacao();
    }//GEN-LAST:event_ufTextActionPerformed

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
            java.util.logging.Logger.getLogger(Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Fornecedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new CadastrarEditarFornecedor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconeTitulo;
    private javax.swing.JTextField bairroText;
    private javax.swing.JPanel bordaLimpar;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoLimpar;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JButton botaoSalvar;
    private javax.swing.JTextField cepText;
    private javax.swing.JTextField cidadeText;
    private javax.swing.JTextField codigoText;
    private javax.swing.JTextField complementoText;
    private javax.swing.JTextField cpfcnpjText;
    private javax.swing.JTextField emailText;
    private javax.swing.JLabel iconePrograma;
    private javax.swing.JTextField instagramText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelAtalhos;
    private javax.swing.JLabel labelCEPvalido;
    private javax.swing.JLabel labelCPFCNPJvalido;
    private javax.swing.JLabel labelCodigoFornecedor;
    private javax.swing.JLabel labelEmailValido;
    private javax.swing.JLabel labelNomeVazio;
    private javax.swing.JLabel labelTextoTitulo;
    private javax.swing.JLabel labelWhatsValido;
    private javax.swing.JTextField logradouroText;
    private javax.swing.JTextField nomeText;
    private javax.swing.JTextField numeroText;
    private javax.swing.JTextArea observacaoText;
    private javax.swing.JDesktopPane painelFormulario;
    private javax.swing.JPanel painelSuperior;
    private javax.swing.JTextField siteText;
    private javax.swing.JLabel textoRodape;
    private javax.swing.JComboBox<String> ufText;
    private javax.swing.JTextField whatsappText;
    // End of variables declaration//GEN-END:variables
}
