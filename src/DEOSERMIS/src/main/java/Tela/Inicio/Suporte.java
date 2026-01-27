
package Tela.Inicio;

import Classes.Funcoes;
import Classes.OverlayUtil;
import Classes.SuporteEmail;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class Suporte extends javax.swing.JDialog {
    private final Funcoes funcao = new Funcoes();
    private List<Anexo> listaAnexos;
    private static final int MAX_ANEXOS = 3;
    
    public Suporte(Window parent, boolean modal) {
        super(parent, modal ? ModalityType.APPLICATION_MODAL : ModalityType.MODELESS);
        setUndecorated(true);
        setResizable(false);
        initComponents();
        this.setLocationRelativeTo(null);
        listaAnexos = new ArrayList<>();
        configurarTabelaAnexos();
        digitacao();
    }
    
    private void configurarTabelaAnexos() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"IMAGEM", "NOME DO ARQUIVO", "AÇÃO"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        tabelaAnexos.setModel(model);

        JTableHeader header = tabelaAnexos.getTableHeader();
        tabelaAnexos.getTableHeader().setReorderingAllowed(false);
        tabelaAnexos.setRowSorter(null);
        
        TableColumnModel columnModel = tabelaAnexos.getColumnModel();
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            TableColumn col = columnModel.getColumn(i);
            col.setHeaderRenderer(new HeaderRenderer());
            col.setResizable(false);
        }

        // Configurações das colunas individuais...
        TableColumn colunaImagem = tabelaAnexos.getColumn("IMAGEM");
        colunaImagem.setCellRenderer(new ImagemRenderer());
        colunaImagem.setPreferredWidth(30);

        TableColumn colunaNome = tabelaAnexos.getColumn("NOME DO ARQUIVO");
        colunaNome.setPreferredWidth(130);

        TableColumn colunaAcao = tabelaAnexos.getColumn("AÇÃO");
        colunaAcao.setCellRenderer(new BotaoRenderer());
        colunaAcao.setCellEditor(new BotaoEditor(tabelaAnexos));
        colunaAcao.setMinWidth(150);
        colunaAcao.setMaxWidth(150);
        
        tabelaAnexos.setRowHeight(50);
    }
    
    private void atualizarTabelaAnexos() {
        DefaultTableModel model = (DefaultTableModel) tabelaAnexos.getModel();
        model.setRowCount(0); // Limpa a tabela
        
        for (Anexo anexo : listaAnexos) {
            model.addRow(new Object[]{anexo.getImagemIcon(), anexo.getNomeArquivo(), "EXCLUIR"});
        }
    }
    
     private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        if (icon == null) return null;
        java.awt.Image img = icon.getImage();
        java.awt.Image resizedImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
    
    private static class Anexo {
        private String nomeArquivo;
        private byte[] dados;
        private ImageIcon imagemIcon;

        public Anexo(String nomeArquivo, byte[] dados, ImageIcon imagemIcon) {
            this.nomeArquivo = nomeArquivo;
            this.dados = dados;
            this.imagemIcon = imagemIcon;
        }

        public String getNomeArquivo() { return nomeArquivo; }
        public byte[] getDados() { return dados; }
        public ImageIcon getImagemIcon() { return imagemIcon; }
    }
    
    private class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setBackground(new Color(0, 0, 102));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            String columnName = table.getColumnName(column);
            if (columnName.equals("AÇÃO")) {
                c.setBackground(new Color(70, 130, 180));
            } else {
                c.setBackground(new Color(0, 0, 102));
            }
            
            if (column == table.getColumnCount() - 1) {
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.WHITE)); 
            } else {
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE)); 
            }
            c.setFont(new Font("Tahoma", Font.BOLD, 16));
            return c;
        }
    }
    
    private class ImagemRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
                label.setHorizontalAlignment(SwingConstants.CENTER);
            }
            return label;
        }
    }
    
    private class BotaoRenderer extends JButton implements TableCellRenderer {
        public BotaoRenderer() {
            setText("EXCLUIR");
            setIcon(new ImageIcon(getClass().getResource("/imagens/botaoExcluir.png")));
            setBackground(new Color(204, 0, 0));
            setForeground(new Color(255, 255, 255));
            setFont(new Font("Arial", Font.BOLD, 12));
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    private class BotaoEditor extends AbstractCellEditor implements TableCellEditor {
        private final JButton button;
        private final JTable tabela;
        private int linhaSelecionada;

        public BotaoEditor(JTable tabela) {
            this.tabela = tabela;
            button = new JButton("EXCLUIR");
            button.setBackground(new Color(204, 0, 0));
            button.setForeground(new Color(255, 255, 255));
            button.setFont(new Font("Arial", Font.BOLD, 12));
            button.setOpaque(true);
            button.setIcon(new ImageIcon(getClass().getResource("/imagens/botaoExcluir.png")));
            
            button.addActionListener(e -> {
                fireEditingStopped();
                if (linhaSelecionada >= 0 && linhaSelecionada < listaAnexos.size()) {
                    listaAnexos.remove(linhaSelecionada);
                    atualizarTabelaAnexos();
                    anexoCheio();
                }
            });
        }
        
        @Override
        public Object getCellEditorValue() {
            return "EXCLUIR";
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.linhaSelecionada = row;
            return button;
        }
        
        @Override
        public boolean isCellEditable(EventObject e) {
            return true;
        }
    }
    
    private void anexoCheio(){
        if (listaAnexos.size() >= MAX_ANEXOS) {
            botaoAnexar.setEnabled(false);
        }else{
            botaoAnexar.setEnabled(true);
        }
    }
    
    private void digitacao(){
        funcao.limitarCaracteres(nomeText, 70); 
        funcao.limitarCaracteres(emailText, 100); 
        funcao.limitarCaracteres(assuntoText, 50);
        funcao.limitarCaracteres(descricaoText, 255);
        funcaoEnabled();
    }
    
     private void funcaoEnabled(){
        labelNomeVazio.setVisible(!funcao.nomeCompleto(nomeText));
        labelEmailValido.setVisible(!emailValido());
        labelAssunto.setVisible(!funcao.validarCampoVazio(assuntoText));
        labelDescricao.setVisible(!funcao.validarCampoVazio(descricaoText));
        botaoSalvar.setEnabled(elegivelSalvar());
        botaoSalvar.setOpaque(elegivelSalvar());
        adicionarLegendas();
    }
    
    private boolean elegivelSalvar(){
        return funcao.nomeCompleto(nomeText) && emailValido() && funcao.validarCampoVazio(assuntoText) && funcao.validarCampoVazio(descricaoText);
    }
     
    private boolean emailValido(){
        return funcao.validarEmail(emailText);
    }
    
    private void enviar(){
        if (nomeText.getText().isEmpty() || emailText.getText().isEmpty() || assuntoText.getText().isEmpty() || descricaoText.getText().isEmpty()) {
            funcao.MensagensJDialog(Suporte.this, "Por favor, preencha todos os campos obrigatórios.","", "ATENÇÃO", "aviso");
            return;
        }
        
        // Coleta os bytes e nomes dos arquivos para enviar
        List<byte[]> bytesAnexos = new ArrayList<>();
        List<String> nomesAnexos = new ArrayList<>();
        for (Anexo anexo : listaAnexos) {
            bytesAnexos.add(anexo.getDados());
            nomesAnexos.add(anexo.getNomeArquivo());
        }

        boolean sucesso = SuporteEmail.enviarEmailSuporte(
                nomeText.getText(),
                emailText.getText(),
                assuntoText.getText(),
                descricaoText.getText(),
                bytesAnexos,
                nomesAnexos
        );

        if (sucesso) {
            funcao.MensagensJDialog(Suporte.this, "Email de suporte enviado com sucesso.", "", "SUCESSO", "informacao");
            this.dispose();
        } else {
            funcao.MensagensJDialog(Suporte.this, "Erro ao enviar o email.", "Verifique a conexão com a internet.", "ERRO", "erro");
        }
    }
    
    private void anexar() {
        if (listaAnexos.size() >= MAX_ANEXOS) {
            funcao.MensagensJDialog(Suporte.this, "Limite de " + MAX_ANEXOS + " imagens anexadas atingido.", "", "ATENÇÃO", "aviso");
            return;
        }

        // Ativa o overlay, como em carregarImagem()
        if (!(getGlassPane() instanceof OverlayUtil.OverlayPane)) {
            setGlassPane(new OverlayUtil.OverlayPane());
        }
        getGlassPane().setVisible(true);

        // Criação do JFileChooser com o mesmo design
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolher Arquivo");

        String userHome = System.getProperty("user.home");
        fileChooser.setCurrentDirectory(new File(userHome + "\\Pictures"));

        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagens (*.PNG, *.JPG, *.JPEG)", "png", "jpg", "jpeg");
        fileChooser.setFileFilter(filtro);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int returnValue = fileChooser.showOpenDialog(this);

        getGlassPane().setVisible(false);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                long tamanhoArquivo = selectedFile.length();
                final long LIMITE_MB = 40L * 1024 * 1024;

                if (tamanhoArquivo > LIMITE_MB) {
                    funcao.MensagensJDialog(this, "A imagem selecionada excede o limite de 40 MB.", "", "Imagem muito grande", "erro");
                    return;
                }

                byte[] fileBytes = Files.readAllBytes(selectedFile.toPath());

                // Redimensiona a imagem para a pré-visualização na tabela
                ImageIcon originalIcon = new ImageIcon(fileBytes);
                ImageIcon resizedIcon = resizeImageIcon(originalIcon, 45, 45);

                Anexo novoAnexo = new Anexo(selectedFile.getName(), fileBytes, resizedIcon);
                listaAnexos.add(novoAnexo);

                atualizarTabelaAnexos();

            } catch (IOException ex) {
                funcao.MensagensJDialog(this, "Erro ao ler o arquivo: ", ex.getMessage(), "ERRO", "erro");
            }
        }

        anexoCheio();
    }

    
    private void adicionarLegendas() {
        funcao.validarBotaoComDica(botaoSalvar, "Clique para enviar formulário");
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
        labelTextoTitulo = new javax.swing.JLabel();
        bordaSalvar = new javax.swing.JPanel();
        botaoSalvar = new javax.swing.JButton();
        botaoMenu = new javax.swing.JButton();
        textoRodape = new javax.swing.JLabel();
        painelDataReposicao = new javax.swing.JDesktopPane();
        nomeText = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        emailText = new javax.swing.JTextField();
        assuntoText = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        descricaoText = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        botaoAnexar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelaAnexos = new javax.swing.JTable();
        labelNomeVazio = new javax.swing.JLabel();
        labelEmailValido = new javax.swing.JLabel();
        labelDescricao = new javax.swing.JLabel();
        labelAssunto = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel1.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jPanel1.setLayout(null);

        painelSuperior.setBackground(new java.awt.Color(210, 164, 2));
        painelSuperior.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        painelSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        iconePrograma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/LogoMarca.png"))); // NOI18N
        painelSuperior.add(iconePrograma, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 5, 50, 50));

        IconeTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/SUPORTE.png"))); // NOI18N
        painelSuperior.add(IconeTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 50, 50));

        labelTextoTitulo.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        labelTextoTitulo.setForeground(new java.awt.Color(0, 0, 0));
        labelTextoTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelTextoTitulo.setText("FALE CONOSCO");
        painelSuperior.add(labelTextoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 720, -1));

        jPanel1.add(painelSuperior);
        painelSuperior.setBounds(0, 0, 720, 60);

        bordaSalvar.setBackground(new java.awt.Color(255, 255, 255));
        bordaSalvar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204), null, null));
        bordaSalvar.setForeground(new java.awt.Color(0, 0, 0));
        bordaSalvar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        botaoSalvar.setBackground(new java.awt.Color(210, 164, 2));
        botaoSalvar.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        botaoSalvar.setForeground(new java.awt.Color(0, 0, 0));
        botaoSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/ENVIARemail.png"))); // NOI18N
        botaoSalvar.setText("ENVIAR");
        botaoSalvar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        botaoSalvar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        botaoSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSalvarActionPerformed(evt);
            }
        });
        bordaSalvar.add(botaoSalvar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 1, 89, 59));

        jPanel1.add(bordaSalvar);
        bordaSalvar.setBounds(10, 570, 91, 62);

        botaoMenu.setBackground(new java.awt.Color(204, 0, 0));
        botaoMenu.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoMenu.setForeground(new java.awt.Color(255, 255, 255));
        botaoMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagens/VOLTAR.png"))); // NOI18N
        botaoMenu.setText("VOLTAR");
        botaoMenu.setToolTipText("Clique para voltar");
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
        botaoMenu.setBounds(650, 570, 60, 60);

        textoRodape.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        textoRodape.setForeground(new java.awt.Color(0, 0, 0));
        textoRodape.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        textoRodape.setText("*Preenchimento obrigatório de campo(s)");
        jPanel1.add(textoRodape);
        textoRodape.setBounds(40, 540, 640, 15);

        painelDataReposicao.setBackground(new java.awt.Color(255, 255, 255));
        painelDataReposicao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "PREENCHA O FORMULÁRIO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 26), new java.awt.Color(0, 0, 102))); // NOI18N
        painelDataReposicao.setForeground(new java.awt.Color(0, 102, 153));
        painelDataReposicao.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        painelDataReposicao.add(nomeText, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 40, 420, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("*Nome Completo:");
        painelDataReposicao.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 40, 180, -1));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("*E-mail:");
        painelDataReposicao.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 80, 180, -1));

        emailText.setBackground(new java.awt.Color(255, 255, 255));
        emailText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        emailText.setForeground(new java.awt.Color(0, 0, 0));
        emailText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                emailTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(emailText, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 80, 420, -1));

        assuntoText.setBackground(new java.awt.Color(255, 255, 255));
        assuntoText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        assuntoText.setForeground(new java.awt.Color(0, 0, 0));
        assuntoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                assuntoTextKeyReleased(evt);
            }
        });
        painelDataReposicao.add(assuntoText, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 120, 420, -1));

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("*Assunto:");
        painelDataReposicao.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 120, 180, -1));

        descricaoText.setBackground(new java.awt.Color(255, 255, 255));
        descricaoText.setColumns(20);
        descricaoText.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        descricaoText.setForeground(new java.awt.Color(0, 0, 0));
        descricaoText.setLineWrap(true);
        descricaoText.setRows(5);
        descricaoText.setWrapStyleWord(true);
        descricaoText.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                descricaoTextKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                descricaoTextKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(descricaoText);

        painelDataReposicao.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 160, 420, 100));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("*Descrição:");
        painelDataReposicao.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 160, 180, -1));

        botaoAnexar.setBackground(new java.awt.Color(204, 204, 204));
        botaoAnexar.setFont(new java.awt.Font("Impact", 0, 18)); // NOI18N
        botaoAnexar.setForeground(new java.awt.Color(0, 0, 0));
        botaoAnexar.setText("Anexar Imagem");
        botaoAnexar.setToolTipText("Clique para anexar imagem");
        botaoAnexar.setBorder(null);
        botaoAnexar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAnexar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        botaoAnexar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAnexarActionPerformed(evt);
            }
        });
        painelDataReposicao.add(botaoAnexar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 130, 50));

        tabelaAnexos.setBackground(new java.awt.Color(255, 255, 255));
        tabelaAnexos.setFont(new java.awt.Font("Franklin Gothic Medium Cond", 0, 12)); // NOI18N
        tabelaAnexos.setForeground(new java.awt.Color(51, 51, 51));
        tabelaAnexos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Imagem", "Nome do Arquivo", "Ação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Byte.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelaAnexos.setSelectionBackground(new java.awt.Color(210, 164, 2));
        tabelaAnexos.setSelectionForeground(new java.awt.Color(0, 0, 0));
        tabelaAnexos.getTableHeader().setReorderingAllowed(false);
        tabelaAnexos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaAnexosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelaAnexos);
        if (tabelaAnexos.getColumnModel().getColumnCount() > 0) {
            tabelaAnexos.getColumnModel().getColumn(0).setResizable(false);
            tabelaAnexos.getColumnModel().getColumn(1).setResizable(false);
        }

        painelDataReposicao.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 290, 420, 170));

        labelNomeVazio.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelNomeVazio.setForeground(new java.awt.Color(204, 0, 0));
        labelNomeVazio.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelNomeVazio.setText("*DIGITE SEU NOME COMPLETO");
        painelDataReposicao.add(labelNomeVazio, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 60, 420, 20));

        labelEmailValido.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelEmailValido.setForeground(new java.awt.Color(204, 0, 0));
        labelEmailValido.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelEmailValido.setText("*DIGITE E-MAIL VÁLIDO");
        painelDataReposicao.add(labelEmailValido, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 420, 20));

        labelDescricao.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelDescricao.setForeground(new java.awt.Color(204, 0, 0));
        labelDescricao.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelDescricao.setText("*RESUMA EM POUCAS PALAVRAS O MOTIVO DO CONTATO");
        painelDataReposicao.add(labelDescricao, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 420, 20));

        labelAssunto.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        labelAssunto.setForeground(new java.awt.Color(204, 0, 0));
        labelAssunto.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelAssunto.setText("*DIGITE O ASSUNTO DO CONTATO");
        painelDataReposicao.add(labelAssunto, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 420, 20));

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("Limite máximo de 3 imagens");
        painelDataReposicao.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 343, 150, 20));

        jPanel1.add(painelDataReposicao);
        painelDataReposicao.setBounds(40, 70, 640, 470);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
       enviar();
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void assuntoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_assuntoTextKeyReleased
      digitacao();
    }//GEN-LAST:event_assuntoTextKeyReleased

    private void emailTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTextKeyReleased
      digitacao();
    }//GEN-LAST:event_emailTextKeyReleased

    private void nomeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nomeTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nomeTextActionPerformed

    private void nomeTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nomeTextKeyReleased
       digitacao();
    }//GEN-LAST:event_nomeTextKeyReleased

    private void descricaoTextKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descricaoTextKeyReleased
      digitacao();
    }//GEN-LAST:event_descricaoTextKeyReleased

    private void descricaoTextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_descricaoTextKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_descricaoTextKeyTyped

    private void botaoMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoMenuActionPerformed
        this.dispose();
    }//GEN-LAST:event_botaoMenuActionPerformed

    private void botaoAnexarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAnexarActionPerformed
        anexar();
    }//GEN-LAST:event_botaoAnexarActionPerformed

    private void tabelaAnexosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaAnexosMouseClicked

    }//GEN-LAST:event_tabelaAnexosMouseClicked

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
            java.util.logging.Logger.getLogger(Suporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Suporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Suporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Suporte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Suporte dialog = new Suporte(new javax.swing.JFrame(), true);
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
    private javax.swing.JTextField assuntoText;
    private javax.swing.JPanel bordaSalvar;
    private javax.swing.JButton botaoAnexar;
    private javax.swing.JButton botaoMenu;
    private javax.swing.JButton botaoSalvar;
    private javax.swing.JTextArea descricaoText;
    private javax.swing.JTextField emailText;
    private javax.swing.JLabel iconePrograma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelAssunto;
    private javax.swing.JLabel labelDescricao;
    private javax.swing.JLabel labelEmailValido;
    private javax.swing.JLabel labelNomeVazio;
    private javax.swing.JLabel labelTextoTitulo;
    private javax.swing.JTextField nomeText;
    private javax.swing.JDesktopPane painelDataReposicao;
    private javax.swing.JPanel painelSuperior;
    private javax.swing.JTable tabelaAnexos;
    private javax.swing.JLabel textoRodape;
    // End of variables declaration//GEN-END:variables
}
