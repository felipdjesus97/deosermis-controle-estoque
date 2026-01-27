/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;


import jakarta.mail.*;
import jakarta.mail.internet.*;
import Main.ConexaoBanco;
import Tela.Mensagem.TelaMensagemConfirmar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;
import javax.swing.text.MaskFormatter;  
import org.apache.pdfbox.pdmodel.*;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JMenu;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import Tela.Mensagem.TelaMensagens;
import java.awt.Frame;
import java.awt.Window;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import javax.swing.JDialog;
import javax.swing.JPasswordField;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class Funcoes {
    private static final int ITERACOES = 65536;
    private static final int TAMANHO_CHAVE = 256;
    
        public void MensagemConfirmar(Component parent, String mensagem, String mensagem2, String titulo, Runnable acaoSim) {
            Mensagem.setTitulo(titulo);
            Mensagem.setMensagem(mensagem);
            Mensagem.setMensagem2(mensagem2);
            Mensagem.setAcao(acaoSim);
            TelaMensagemConfirmar dialog = new TelaMensagemConfirmar((Frame) parent, true);
            OverlayUtil.abrirTela((JFrame) parent, dialog);
        }
        
        public void MensagemConfirmarJDialog(JDialog parent, String mensagem, String mensagem2, String titulo, Runnable acaoSim) {
            Mensagem.setTitulo(titulo);
            Mensagem.setMensagem(mensagem);
            Mensagem.setMensagem2(mensagem2);
            Mensagem.setAcao(acaoSim);
            TelaMensagemConfirmar dialog = new TelaMensagemConfirmar(parent, true);
            OverlayUtil.abrirTelaJDialog(parent, dialog);
        }
        
        public void Mensagens(Component parent, String mensagem, String mensagem2, String titulo, String tipo) {
            Mensagem.setTitulo(titulo);
            Mensagem.setMensagem(mensagem);
            Mensagem.setMensagem2(mensagem2);
            Mensagem.setTipo(tipo);
            TelaMensagens dialog = new TelaMensagens((Frame) parent, true);
            OverlayUtil.abrirTela((JFrame) parent, dialog);
        }
        
        public void MensagensJDialog(JDialog parent, String mensagem, String mensagem2, String titulo, String tipo) {
            Mensagem.setTitulo(titulo);
            Mensagem.setMensagem(mensagem);
            Mensagem.setMensagem2(mensagem2);
            Mensagem.setTipo(tipo);
            TelaMensagens dialog = new TelaMensagens(parent, true); 
            OverlayUtil.abrirTelaJDialog(parent, dialog);

        }
        
        public void entradaNumerica(JSpinner.DefaultEditor editor) {
            JFormattedTextField spinnerTextField = editor.getTextField();

            // Restringe a entrada a apenas dígitos
            spinnerTextField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c)) {
                        e.consume();
                    }
                }
            });

            // Se o campo ficar vazio ao perder o foco, define como "0"
            spinnerTextField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (spinnerTextField.getText().trim().isEmpty()) {
                        spinnerTextField.setText("0");
                    }
                }
            });
        }
        
        public void entradaNumericaMonetaria(JFormattedTextField textField) {
            // Restringe a entrada a apenas dígitos, vírgula e ponto
            textField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!Character.isDigit(c) && c != ',') {
                        e.consume();
                    }
                }
            });

            // Se o campo ficar vazio ao perder o foco, define como "0,00"
            textField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    if (textField.getText().trim().isEmpty()) {
                        textField.setText("0,00");
                    }
                }
            });
        }
        
        public static boolean validarCPF(String cpf) {
            cpf = cpf.replaceAll("\\D", "");
            if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

            try {
                int sum = 0;
                for (int i = 0; i < 9; i++)
                    sum += (cpf.charAt(i) - '0') * (10 - i);
                int check1 = 11 - (sum % 11);
                if (check1 >= 10) check1 = 0;
                if (check1 != (cpf.charAt(9) - '0')) return false;

                sum = 0;
                for (int i = 0; i < 10; i++)
                    sum += (cpf.charAt(i) - '0') * (11 - i);
                int check2 = 11 - (sum % 11);
                if (check2 >= 10) check2 = 0;
                return check2 == (cpf.charAt(10) - '0');
            } catch (Exception e) {
                return false;
            }
        }
        
        public static boolean validarCNPJ(String cnpj) {
            cnpj = cnpj.replaceAll("\\D", "");
            if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;

            int[] weights1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] weights2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            try {
                int sum = 0;
                for (int i = 0; i < 12; i++)
                    sum += (cnpj.charAt(i) - '0') * weights1[i];
                int check1 = sum % 11;
                check1 = check1 < 2 ? 0 : 11 - check1;
                if (check1 != (cnpj.charAt(12) - '0')) return false;

                sum = 0;
                for (int i = 0; i < 13; i++)
                    sum += (cnpj.charAt(i) - '0') * weights2[i];
                int check2 = sum % 11;
                check2 = check2 < 2 ? 0 : 11 - check2;
                return check2 == (cnpj.charAt(13) - '0');
            } catch (Exception e) {
                return false;
            }
        }
        
        public static void limitarCaracteres(final JTextComponent campo, final int limite) {
            AbstractDocument doc = (AbstractDocument) campo.getDocument();
            doc.setDocumentFilter(new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) return;

                    // Verifica se vai ultrapassar o limite
                    if ((fb.getDocument().getLength() + string.length()) <= limite) {
                        super.insertString(fb, offset, string, attr);
                    } else {
                        // Se ultrapassar, permite só o que falta
                        int available = limite - fb.getDocument().getLength();
                        if (available > 0) {
                            super.insertString(fb, offset, string.substring(0, available), attr);
                        }
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) return;

                    int currentLength = fb.getDocument().getLength();
                    int overLimit = (currentLength + text.length()) - limite - length;

                    if (overLimit <= 0) {
                        super.replace(fb, offset, length, text, attrs);
                    } else {
                        // Se ultrapassar, permite só o que falta
                        int available = text.length() - overLimit;
                        if (available > 0) {
                            super.replace(fb, offset, length, text.substring(0, available), attrs);
                        }
                    }
                }
            });
        }
        
        public static void limitarCaracteresJSpinner(final JSpinner spinner, final int limite) {
            JComponent editor = spinner.getEditor();

            if (editor instanceof JSpinner.DefaultEditor) {
                JTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
                AbstractDocument doc = (AbstractDocument) textField.getDocument();

                doc.setDocumentFilter(new DocumentFilter() {
                    @Override
                    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                        if (string == null) return;

                        if ((fb.getDocument().getLength() + string.length()) <= limite) {
                            super.insertString(fb, offset, string, attr);
                        } else {
                            int available = limite - fb.getDocument().getLength();
                            if (available > 0) {
                                super.insertString(fb, offset, string.substring(0, available), attr);
                            }
                        }
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                        if (text == null) return;

                        int currentLength = fb.getDocument().getLength();
                        int overLimit = (currentLength + text.length()) - limite - length;

                        if (overLimit <= 0) {
                            super.replace(fb, offset, length, text, attrs);
                        } else {
                            int available = text.length() - overLimit;
                            if (available > 0) {
                                super.replace(fb, offset, length, text.substring(0, available), attrs);
                            }
                        }
                    }
                });
            }
        }
        
        public static void limitarCaracteresMonetarios(final JTextComponent campo, final int limiteInteiros, final int limiteDecimais) {
            AbstractDocument doc = (AbstractDocument) campo.getDocument();
            doc.setDocumentFilter(new DocumentFilter() {

                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                    if (string == null) return;

                    StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                    sb.insert(offset, string);

                    if (isValid(sb.toString())) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                    if (text == null) return;

                    StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
                    sb.replace(offset, offset + length, text);

                    if (isValid(sb.toString())) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }

                private boolean isValid(String value) {
                    // Remove todos os caracteres inválidos
                    value = value.replaceAll("[^0-9,]", "");

                    // Se tiver mais de uma vírgula, já é inválido
                    if (value.chars().filter(ch -> ch == ',').count() > 1) return false;

                    String[] partes = value.split(",");
                    String inteiros = partes[0];
                    String decimais = partes.length > 1 ? partes[1] : "";

                    return inteiros.length() <= limiteInteiros && decimais.length() <= limiteDecimais;
                }
            });
        }

        public void adicionarAtalhoTeclado(JRootPane rootPane, String tecla, Runnable acao) {
            String nomeAcao = "acao_" + tecla;

            rootPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                    .put(KeyStroke.getKeyStroke(tecla), nomeAcao);

            rootPane.getActionMap()
                    .put(nomeAcao, new AbstractAction() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            acao.run();
                        }
                    });
        }
        
        public void adicionarAtalho(JRootPane rootPane, String tecla, JButton botao) {
            adicionarAtalhoTeclado(rootPane, tecla, () -> {
                if (botao.isEnabled()) {
                    botao.doClick();
                }
            });
        }
        
        public void trocarDeTela(Window  telaAtual, Window novaTela) {
            novaTela.setVisible(true);   // Abre a nova tela
            telaAtual.dispose();         // Fecha a tela atual
        }
        
        private static boolean Permissao(int usuarioID, String funcionalidade, Component parent) {
            String sql = "SELECT p.Permitir " +
                         "FROM permissao p " +
                         "JOIN funcionalidades f ON p.FuncionalidadeID = f.Codigo " +
                         "WHERE p.UsuarioID = ? AND f.Nome = ?";

            try (Connection conexao = Main.ConexaoBanco.getConnection();
                 PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, usuarioID);
                stmt.setString(2, funcionalidade);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("Permitir") == 1;
                    }
                }

            } catch (SQLException e) {
                new Funcoes().Mensagens(parent, "Erro na Permissão do usuário: " + e.getMessage(),"", "ERRO", "erro");
            }

            return false;
        }

        
        public static void verificarPermissao(JComponent botao, int usuarioID, String funcionalidade, Component parent) {
            if (!Permissao(usuarioID, funcionalidade, parent)) {
                botao.setEnabled(false);
                botao.setOpaque(false);
            }
        }
        
        public static void verificarPermissaoJSpinner(JSpinner JSpinner, int usuarioID, String funcionalidade, Component parent) {
            if (!Permissao(usuarioID, funcionalidade, parent)) {
                JSpinner.setEnabled(false);
                JSpinner.setOpaque(false);
            }
        }
               
        public static void buscarEndereco(String cepDigitado,
                                        JTextField logradouroText,
                                        JTextField numeroText,
                                        JTextField bairroText,
                                        JTextField cidadeText,
                                        JComboBox<String> ufText,
                                        JTextField complementoText,
                                        Component parentComponent) {
             String cep = cepDigitado.replaceAll("\\D", "");

             try (BufferedReader br = new BufferedReader(new InputStreamReader(
                     new FileInputStream("src/main/resources/ceps.csv"), "UTF-8"))) {

                 String linha;
                 while ((linha = br.readLine()) != null) {
                     if (linha.trim().isEmpty()) continue;

                     String[] dados = linha.split(";", -1);

                     if (dados.length < 5) continue;

                     String cepArquivo = dados[0].replaceAll("\\D", "");

                     if (cepArquivo.equals(cep)) {
                         String logradouroCompleto = dados[4];

                         if (logradouroCompleto.contains(",")) {
                             String[] partes = logradouroCompleto.split(",", 2);
                             logradouroText.setText(partes[0].trim());
                             numeroText.setText(partes[1].trim());
                         } else {
                             logradouroText.setText(logradouroCompleto.trim());
                             limparComponente(numeroText);
                         }

                         bairroText.setText(dados[3]);
                         cidadeText.setText(dados[2]);
                         ufText.setSelectedItem(dados[1]);

                         if (dados.length >= 6) {
                             complementoText.setText(dados[5]);
                         } else {
                             limparComponente(complementoText);
                         }
                         return;
                     }
                 }

                 // CEP não encontrado - limpa campos
                 limparComponentes(ufText,cidadeText,bairroText,logradouroText,numeroText,complementoText);

             } catch (Exception e) {
                  new Funcoes().Mensagens(parentComponent, "Erro ao ler arquivo de CEPs: " + e.getMessage(),"", "ERRO", "erro");
             }
         }
        
        public static void aplicarMascaraCEP(JTextField cepText) {
            cepText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    // Permite apenas números
                    if (!Character.isDigit(e.getKeyChar())) {
                        e.consume();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    String digitos = cepText.getText().replaceAll("\\D", "");

                    // Limita a 8 dígitos (CEP)
                    if (digitos.length() > 8) {
                        digitos = digitos.substring(0, 8);
                    }

                    cepText.setText(formatCEP(digitos));
                }
            });
        }

        public static String formatCEP(String cep) {
            if (cep.length() <= 5) {
                return cep; // Até os primeiros 5 dígitos
            } else {
                return cep.replaceFirst("(\\d{5})(\\d+)", "$1-$2");
            }
        }

        public static void aplicarMascaraCpfCnpj(JTextField cpfcnpjText) {
             cpfcnpjText.addKeyListener(new KeyAdapter() {
                 @Override
                 public void keyTyped(KeyEvent e) {
                     if (!Character.isDigit(e.getKeyChar())) {
                         e.consume();
                     }
                 }

                 @Override
                 public void keyReleased(KeyEvent e) {
                     String digitos = cpfcnpjText.getText().replaceAll("\\D", "");

                     if (digitos.length() <= 11) {
                         cpfcnpjText.setText(formatCPF(digitos));
                     } else {
                         cpfcnpjText.setText(formatCNPJ(digitos));
                     }
                 }
             });
         }

        public static String formatCPF(String cpf) {
                cpf = cpf.length() > 11 ? cpf.substring(0, 11) : cpf;
            return cpf.replaceFirst("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
         }
        
        public static String formatCNPJ(String cnpj) {
                cnpj = cnpj.length() > 14 ? cnpj.substring(0, 14) : cnpj;
            return cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
         }
         
        public static void aplicarMascaraWhatsApp(JTextField whatsappText) {
            whatsappText.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();

                    // Bloqueia qualquer caractere que não seja número
                    if (!Character.isDigit(c)) {
                        e.consume();
                        return;
                    }

                    // Se já tiver 11 dígitos, bloqueia mais números
                    String digitos = whatsappText.getText().replaceAll("\\D", "");
                    if (digitos.length() >= 11) {
                        e.consume();
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    String digitos = whatsappText.getText().replaceAll("\\D", "");

                    // Aplica a máscara apenas se tiver algo
                    if (!digitos.isEmpty()) {
                        whatsappText.setText(formatWhatsApp(digitos));
                    }
                }
            });
        }

        public static String formatWhatsApp(String numero) {
            if (numero == null || numero.isEmpty()) {
                return null; // Ou retorne o valor que você preferir para campos vazios
            }
            if (numero.length() <= 2) {
                return numero; // Apenas DDD parcial
            } else if (numero.length() <= 7) {
                return numero.replaceFirst("(\\d{2})(\\d+)", "($1)$2");
            } else {
                return numero.replaceFirst("(\\d{2})(\\d{5})(\\d+)", "($1)$2-$3");
            }
        }
    
        public static void preencherEstados(JComboBox<String> comboUf) {
           String[] estados = {
               "Selecione",
               "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
               "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
               "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
           };

           comboUf.removeAllItems();
           for (String estado : estados) {
               comboUf.addItem(estado);
           }
        }
           
        public static boolean validarCpfCnpjCampo(JTextField cpfcnpjText) {
            String valor = cpfcnpjText.getText().replaceAll("\\D", "");
            boolean valido;

            if (valor.length() == 11) {
                valido = validarCPF(valor);
            } else if (valor.length() == 14) {
                valido = validarCNPJ(valor);
            } else {
                valido = false;
            }

            return valido;
        }
        
        public static boolean validarComboBox(JComboBox comboBox) {
            if (comboBox.getSelectedIndex() == 0 || comboBox.getSelectedItem().toString().equalsIgnoreCase("Selecione")) {
                comboBox.requestFocus();
                return false;
            }
            return true;
        }
        
        public static boolean validarCampoVazio(Component componente) {
            if (componente instanceof JFormattedTextField) {
                // Para campos formatados, remove a máscara e verifica se sobrou algo
                String texto = ((JFormattedTextField) componente).getText().replaceAll("\\D", "");
                return !texto.isEmpty();
            } else if (componente instanceof JTextField) {
                return !((JTextField) componente).getText().trim().isEmpty();
            } else if (componente instanceof JTextArea) {
                return !((JTextArea) componente).getText().trim().isEmpty();
            } else if (componente instanceof JComboBox) {
                return ((JComboBox<?>) componente).getSelectedIndex() != 0 ;
            } else if (componente instanceof JCheckBox) {
                return ((JCheckBox) componente).isSelected();
            } else if (componente instanceof JRadioButton) {
                return ((JRadioButton) componente).isSelected();
            } else if (componente instanceof JSpinner) {
                return !((JSpinner) componente).getValue().equals(0);
            }else if (componente instanceof JPasswordField) {
                return new String(((JPasswordField) componente).getPassword()).trim().length() > 0;
            }
            return true;
        }       
        
        public static boolean validarCamposVazios(Component... componentes) {
            for (Component componente : componentes) {
                if (validarCampoVazio(componente)) {
                    return false;                    
                }
            }
            return true;
        }
        
        public static boolean validarCamposPreenchidos(Component... componentes) {
            for (Component componente : componentes) {
                // Se um campo estiver vazio, retorna false imediatamente
                if (!validarCampoVazio(componente)) {
                    return false;
                }
            }
            // Se o loop terminou, significa que todos os campos estão preenchidos
            return true;
        }
       
        public static boolean compararTextoComBanco(Component componente, Object valorBanco, boolean removerMascara) {
            String textoTela = "";

            if (componente instanceof JTextField) {
                String txt = ((JTextField) componente).getText();
                textoTela = (txt == null) ? "" : txt.trim();
                if (removerMascara) {
                    textoTela = textoTela.replaceAll("\\D", "");
                }
            } else if (componente instanceof JComboBox) {
                Object selected = ((JComboBox<?>) componente).getSelectedItem();
                textoTela = (selected == null) ? "" : selected.toString().trim();
                if (textoTela.equalsIgnoreCase("Selecione")) {
                    textoTela = "";
                }
            }

            String valorBancoStr = (valorBanco == null) ? "" : valorBanco.toString().trim();

            return textoTela.equalsIgnoreCase(valorBancoStr);
        }

        public boolean validarListaSelecionada(List<Integer> lista) {
            return !(lista == null || lista.isEmpty());
        }
        
        public void configurarTabela(JTable tabela, Font headerFont, Color headerFgColor, Color headerBgColor) {
            JTableHeader header = tabela.getTableHeader();
            header.setFont(headerFont); // fonte e tamanho
            header.setForeground(headerFgColor);//cor letra
            header.setBackground(headerBgColor); // cor fundo
            tabela.getTableHeader().setReorderingAllowed(false);// bloqueia mover as colunas
            tabela.setRowSorter(null);//limpa tabela
        }
        
        public void exibirImagem(JLabel label, byte[] imagemBytes, String caminho) {
            if (imagemBytes != null && imagemBytes.length > 0) {
                ImageIcon icon = new ImageIcon(imagemBytes);
                Image img = icon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(img));
                label.setText(null);
            } else {
                label.setIcon(new javax.swing.ImageIcon(getClass().getResource(caminho)));
            }
        }
        
        public static void limparComponente(Component componente) {
             if (componente instanceof JTextField) {
                ((JTextField) componente).setText("");
            } else if (componente instanceof JTextArea) {
                ((JTextArea) componente).setText("");
            } else if (componente instanceof JComboBox) {
                ((JComboBox<?>) componente).setSelectedIndex(0);
            } else if (componente instanceof JCheckBox) {
                ((JCheckBox) componente).setSelected(false);
            } else if (componente instanceof JRadioButton) {
                ((JRadioButton) componente).setSelected(false);
            }else if (componente instanceof JSpinner) {
                ((JSpinner) componente).setValue(0);  
            }
        }
        
        public static void limparComponenteFormatadoNumerico(Component componente){
              if (componente instanceof JFormattedTextField) {
                 ((JFormattedTextField) componente).setValue(0.00);  
            }
         }
        
        public static void limparComponentes(Component... componentes) {
            for (Component c : componentes) {
                limparComponente(c);
            }
         }

        public static String textoOuNullMaiusculo(Component comp) {
            String texto = null;

            if (comp instanceof JTextField) {
                texto = ((JTextField) comp).getText().trim();
            } else if (comp instanceof JTextArea) {
                texto = ((JTextArea) comp).getText().trim();
            } else {
                texto = null;
            }

            return (texto == null || texto.isEmpty()) ? null : texto.toUpperCase();
        }

        public static String textoOuNullMinusculo(Component comp) {
            String texto = null;

            if (comp instanceof JTextField) {
                texto = ((JTextField) comp).getText().trim();
            } else if (comp instanceof JTextArea) {
                texto = ((JTextArea) comp).getText().trim();
            } else {
                texto = null;
            }

            return (texto == null || texto.isEmpty()) ? null : texto.toLowerCase();
        }
                  
        public static String textoOuNull(Component comp) {
            String texto = null;

            if (comp instanceof JTextField) {
                texto = ((JTextField) comp).getText().trim();
            } else if (comp instanceof JTextArea) {
                texto = ((JTextArea) comp).getText().trim();
            } else {
                texto = null;
            }

            return (texto == null || texto.isEmpty()) ? null : texto;
        }
         
        public boolean emailValido(String email) {
            if (email == null || email.isEmpty()) return false;
                String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
            return email.matches(regex);
        }
        
        public boolean validarEmailJPasswordField(JPasswordField campo){
            String email = new String(campo.getPassword()).trim();
            if (email.isEmpty()) {
                return false;
            }
            return emailValido(email);
        } 
        
        public boolean validarEmail(JTextField campo){
            String email = campo.getText().trim();
            if (email.isEmpty()) {
                return false;
            }
            return emailValido(email);
        } 
        
        public String formatarValor(BigDecimal valor) {
            if (valor == null) {
                return "0,00";
            }
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            symbols.setGroupingSeparator('.');
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
            return decimalFormat.format(valor);
        }

        public void byteToImage(JLabel label, byte[] imagemBytes) {
            if (imagemBytes != null && imagemBytes.length > 0) {
                try {
                    ImageIcon icon = new ImageIcon(imagemBytes);
                    Image imagem = icon.getImage().getScaledInstance(
                        label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(imagem));
                } catch (Exception e) {
                    System.err.println("Erro ao converter bytes para imagem: " + e.getMessage());
                    label.setIcon(null);
                }
            } else {
                label.setIcon(null);
            }
        }

        public byte[] carregarImagem(JFrame parent, JLabel comp) {
            // Ativa o overlay
            if (!(parent.getGlassPane() instanceof OverlayUtil.OverlayPane)) {
                parent.setGlassPane(new OverlayUtil.OverlayPane());
            }
            parent.getGlassPane().setVisible(true);

            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Escolher Arquivo");

            String userHome = System.getProperty("user.home");
            jfc.setCurrentDirectory(new File(userHome + "\\Pictures"));

            FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagens (*.PNG, *.JPG, *.JPEG)", "png", "jpg", "jpeg");
            jfc.setFileFilter(filtro);
            jfc.setAcceptAllFileFilterUsed(false);

            int resultado = jfc.showOpenDialog(parent);

            parent.getGlassPane().setVisible(false);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                try {
                    File arquivo = jfc.getSelectedFile();
                    long tamanhoArquivo = arquivo.length();

                    final long LIMITE_MB = 40L * 1024 * 1024;

                    if (tamanhoArquivo > LIMITE_MB) {
                        Mensagens(parent, "A imagem selecionada excede o limite de 40 MB.","", "Imagem muito grande", "erro");
                        return null;
                    }

                    byte[] imagemBytes = Files.readAllBytes(arquivo.toPath());

                    ImageIcon icon = new ImageIcon(imagemBytes);
                    Image imagem = icon.getImage().getScaledInstance(
                        comp.getWidth(), comp.getHeight(), Image.SCALE_SMOOTH);
                    comp.setIcon(new ImageIcon(imagem));

                    return imagemBytes;

                } catch (IOException e) {
                    Mensagens(parent, "Erro ao carregar imagem: " + e.getMessage(),"", "ERRO" ,"erro");
                } catch (Exception e) {
                     Mensagens(parent, "Erro inesperado ao processar imagem: " + e.getMessage(), "", "ERRO" ,"erro");
                }
            }

            return null;
        }
         
        public static void aplicarMascaraChaveNFe(JTextField campo) {
            campo.addKeyListener(new KeyAdapter() {
            private boolean isUpdating = false; // evita loop infinito

            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                // Permite só dígitos e backspace/delete
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (isUpdating) return;

                isUpdating = true;

                // Remove tudo que não for dígito
                String texto = campo.getText().replaceAll("\\D", "");

                // Limita a 44 caracteres
                if (texto.length() > 44) {
                    texto = texto.substring(0, 44);
                }

                // Formata em grupos de 4 números com espaço
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < texto.length(); i++) {
                    sb.append(texto.charAt(i));
                    // Insere espaço a cada 4 caracteres, exceto no final
                    if ((i + 1) % 4 == 0 && (i + 1) != texto.length()) {
                        sb.append(" ");
                    }
                }

                campo.setText(sb.toString());

                // Posiciona o cursor no fim do texto
                campo.setCaretPosition(campo.getText().length());

                isUpdating = false;
            }
        });
        }
         
        public String formatarCpfCnpj(String texto) {
            if (texto == null || texto.isEmpty()) {
                return null;
            }

            // Remove todos os caracteres não numéricos
            String numeros = texto.replaceAll("\\D", "");

            if (numeros.length() == 11) { // CPF
                try {
                    MaskFormatter mf = new MaskFormatter("###.###.###-##");
                    mf.setValueContainsLiteralCharacters(false);
                    return mf.valueToString(numeros);
                } catch (ParseException e) {
                    return numeros;
                }
            } else if (numeros.length() == 14) { // CNPJ
                try {
                    MaskFormatter mf = new MaskFormatter("##.###.###/####-##");
                    mf.setValueContainsLiteralCharacters(false);
                    return mf.valueToString(numeros);
                } catch (ParseException e) {
                    return numeros;
                }
            }

            return numeros;
        }
        
        public String formatarChaveNFe(String texto) {
            // Verifica se o texto é nulo ou vazio
            if (texto == null || texto.isEmpty()) {
                return "";
            }

            // Remove todos os caracteres que não são números
            String numeros = texto.replaceAll("\\D", "");

            // A Chave de NF-e deve ter exatamente 44 dígitos
            if (numeros.length() != 44) {
                return numeros; // Retorna o texto original se o tamanho estiver incorreto
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numeros.length(); i++) {
                sb.append(numeros.charAt(i));
                // Adiciona um espaço a cada 4 dígitos, exceto no final
                if ((i + 1) % 4 == 0 && (i + 1) != numeros.length()) {
                    sb.append(" ");
                }
            }
            return sb.toString();
        }

        public String formatarWhatsApp(String texto) {
            if (texto == null || texto.isEmpty()) {
                return null;
            }

            String numeros = texto.replaceAll("\\D", "");
            if (numeros.length() == 11) {
                try {
                    MaskFormatter mf = new MaskFormatter("(##)#####-####");
                    mf.setValueContainsLiteralCharacters(false);
                    return mf.valueToString(numeros);
                } catch (ParseException e) {
                    return numeros;
                }
            }
            return numeros;
        }

        public String formatarCEP(String texto) {
            if (texto == null || texto.isEmpty()) {
                return null;
            }

            String numeros = texto.replaceAll("\\D", "");
            if (numeros.length() == 8) {
                try {
                    MaskFormatter mf = new MaskFormatter("#####-###");
                    mf.setValueContainsLiteralCharacters(false);
                    return mf.valueToString(numeros);
                } catch (ParseException e) {
                    return numeros;
                }
            }
            return numeros;
        }
        
        public void aplicarEfeitoMouse(JButton botao) {
            Color corHover = new Color(41, 128, 185);
            Color corClick = new Color(204,204,204);
            // Armazena a cor de fundo original do botão
            Color corNormal = botao.getBackground();

            // Adiciona um listener para os eventos do mouse
            botao.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Muda a cor quando o mouse entra no botão
                    botao.setBackground(corHover);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    // Volta para a cor original quando o mouse sai
                    botao.setBackground(corNormal);
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    // Muda a cor quando o botão é pressionado (clicado)
                    botao.setBackground(corClick);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    // Volta para a cor de mouse over após soltar o clique
                    botao.setBackground(corHover);
                }
            });
        }
        
        public void validarCampoComDica(JTextField campo, String mensagemDica) {
            if (campo.getText().trim().isEmpty()) {
                campo.setToolTipText(mensagemDica);
            } else {
                campo.setToolTipText(null);
            }
        }
        
        public void validarBotaoComDica(JButton botao, String mensagemDica) {
            if (botao.isEnabled()) {
                botao.setToolTipText(mensagemDica);
            } else {
                botao.setToolTipText(null); 
            }
        }
        
        public void validarMenuComDica(JMenu menu, String mensagemDica) {
            if (menu.isEnabled()) {
                menu.setToolTipText(mensagemDica);
            } else {
                menu.setToolTipText(null); 
            }
        }
        
        public void validarDataComDica(JDateChooser data, String mensagemDica) {
            JTextFieldDateEditor editor = (JTextFieldDateEditor) data.getDateEditor();
            if (data.isEnabled()) {
                editor.setToolTipText(mensagemDica);
            } else {
                editor.setToolTipText(null); 
            }
        }
        
        public void validarSpinnerComDica(JSpinner Spinner, String mensagemDica) {
            if (Spinner.isEnabled()) {
                Spinner.setToolTipText(mensagemDica);
            } else {
                Spinner.setToolTipText(null); // Remove a dica se o botão não estiver habilitado
            }
        }
        
        public void validarTextFieldComDica(JTextField TextField, String mensagemDica) {
            if (TextField.isEnabled()) {
                TextField.setToolTipText(mensagemDica);
            } else {
                TextField.setToolTipText(null); // Remove a dica se o botão não estiver habilitado
            }
        }
        
        public void validarComboboxComDica(JComboBox combobox, String mensagemDica) {
            if (combobox.isEnabled()) {
                combobox.setToolTipText(mensagemDica);
            } else {
                combobox.setToolTipText(null); // Remove a dica se o botão não estiver habilitado
            }
        }
        
        public static void bloquearArrastar(Container container) {
            for (Component c : container.getComponents()) {
                // Bloqueia eventos de clique e arrasto
                c.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        e.consume(); // ignora
                    }
                });

                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        e.consume();
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        e.consume();
                    }
                });

                // Aplica recursivamente nos filhos, se houver
                if (c instanceof Container) {
                    bloquearArrastar((Container) c);
                }
            }
        }

        public static String formatarProdutosParaHtml(String produtosConcat) {
            if (produtosConcat == null || produtosConcat.trim().isEmpty()) {
                return "";
            }

            // produtosConcat vem do GROUP_CONCAT: "Produto1 (Qtd: 2 - R$ 10,00) | Produto2 (Qtd: 1 - R$ 5,00)"
            String[] produtos = produtosConcat.split("\\|");
            StringBuilder html = new StringBuilder("<html>");

            for (String p : produtos) {
                html.append(p.trim()).append("<br>");
            }

            html.append("</html>");
            return html.toString();
        }
        
        public enum TipoArquivo { EXCEL, PDF }

        private static int linhasMaxDoRow(DefaultTableModel model, int row) {
            int max = 1;
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(row, col);
                if (value != null) {
                    int linhas = value.toString().replaceAll("<html>|</html>", "").split("<br>").length;
                    if (linhas > max) max = linhas;
                }
            }
            return max;
        }

        private static void exportToPDF(DefaultTableModel model, String filePath, Component parent) throws IOException {
            PDDocument document = new PDDocument();
            PDRectangle pageSize = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
            float margin = 20;
            float yStart = pageSize.getHeight() - margin;

            PDFont fontHeader = PDType1Font.HELVETICA_BOLD;
            PDFont fontData = PDType1Font.HELVETICA;
            float fontSize = 6;
            float rowHeight = fontSize * 1.5f;

            int columnsPerPage = 8;
            int totalColumns = model.getColumnCount();
            int totalRows = model.getRowCount();
            int colStart = 0;

            while (colStart < totalColumns) {
                PDPage page = new PDPage(pageSize);
                document.addPage(page);
                PDPageContentStream content = new PDPageContentStream(document, page);

                float yPosition = yStart;
                float xStart = margin;
                int colEnd = Math.min(colStart + columnsPerPage, totalColumns);
                float colWidth = (pageSize.getWidth() - 2 * margin) / (colEnd - colStart);

                // Cabeçalho
                for (int i = colStart; i < colEnd; i++) {
                    String header = model.getColumnName(i);
                    float textWidth = fontHeader.getStringWidth(header) / 1000 * fontSize;
                    float offsetX = xStart + (i - colStart) * colWidth + (colWidth - textWidth) / 2; // centralizado horizontal
                    content.beginText();
                    content.setFont(fontHeader, fontSize);
                    content.newLineAtOffset(offsetX, yPosition);
                    content.showText(header);
                    content.endText();
                }

                yPosition -= rowHeight;

                // Dados
                for (int row = 0; row < totalRows; row++) {
                    for (int col = colStart; col < colEnd; col++) {
                        Object value = model.getValueAt(row, col);
                        String text = value != null ? value.toString() : "";
                        String[] linhas = text.replaceAll("<html>|</html>", "").split("<br>");
                        for (int l = 0; l < linhas.length; l++) {
                            float textWidth = fontData.getStringWidth(linhas[l]) / 1000 * fontSize;
                            float offsetX = xStart + (col - colStart) * colWidth + (colWidth - textWidth) / 2; // centralizado horizontal
                            content.beginText();
                            content.setFont(fontData, fontSize);
                            content.newLineAtOffset(offsetX, yPosition - l * rowHeight);
                            content.showText(linhas[l]);
                            content.endText();
                        }
                    }
                    yPosition -= rowHeight * linhasMaxDoRow(model, row);

                    if (yPosition < margin) {
                        content.close();
                        page = new PDPage(pageSize);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        yPosition = yStart;
                        // redesenha cabeçalho
                        for (int i = colStart; i < colEnd; i++) {
                            String header = model.getColumnName(i);
                            float textWidth = fontHeader.getStringWidth(header) / 1000 * fontSize;
                            float offsetX = xStart + (i - colStart) * colWidth + (colWidth - textWidth) / 2;
                            content.beginText();
                            content.setFont(fontHeader, fontSize);
                            content.newLineAtOffset(offsetX, yPosition);
                            content.showText(header);
                            content.endText();
                        }
                        yPosition -= rowHeight;
                    }
                }

                content.close();
                colStart += columnsPerPage;
            }

            document.save(filePath);
            document.close();
            new Funcoes().Mensagens(parent, "Salvo com sucesso", "", "SUCESSO", "informacao");
        }

        private static void exportToPDFComTotal(DefaultTableModel model, String filePath, Component parent, String totalGeral) throws IOException {
            PDDocument document = new PDDocument();
            PDRectangle pageSize = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
            float margin = 20;
            float yStart = pageSize.getHeight() - margin;

            PDFont fontHeader = PDType1Font.HELVETICA_BOLD;
            PDFont fontData = PDType1Font.HELVETICA;
            PDFont fontTotal = PDType1Font.HELVETICA_BOLD;
            float fontSize = 6;
            float rowHeight = fontSize * 1.5f;

            int columnsPerPage = 8;
            int totalColumns = model.getColumnCount();
            int totalRows = model.getRowCount();
            int colStart = 0;

            while (colStart < totalColumns) {
                PDPage page = new PDPage(pageSize);
                document.addPage(page);
                PDPageContentStream content = new PDPageContentStream(document, page);

                float yPosition = yStart;
                float xStart = margin;
                int colEnd = Math.min(colStart + columnsPerPage, totalColumns);
                float colWidth = (pageSize.getWidth() - 2 * margin) / (colEnd - colStart);

                // Cabeçalho
                for (int i = colStart; i < colEnd; i++) {
                    String header = model.getColumnName(i);
                    float textWidth = fontHeader.getStringWidth(header) / 1000 * fontSize;
                    float offsetX = xStart + (i - colStart) * colWidth + (colWidth - textWidth) / 2;
                    content.beginText();
                    content.setFont(fontHeader, fontSize);
                    content.newLineAtOffset(offsetX, yPosition);
                    content.showText(header);
                    content.endText();
                }

                yPosition -= rowHeight;

                // Dados
                for (int row = 0; row < totalRows; row++) {
                    for (int col = colStart; col < colEnd; col++) {
                        Object value = model.getValueAt(row, col);
                        String text = value != null ? value.toString() : "";
                        String[] linhas = text.replaceAll("<html>|</html>", "").split("<br>");
                        for (int l = 0; l < linhas.length; l++) {
                            float textWidth = fontData.getStringWidth(linhas[l]) / 1000 * fontSize;
                            float offsetX = xStart + (col - colStart) * colWidth + (colWidth - textWidth) / 2;
                            content.beginText();
                            content.setFont(fontData, fontSize);
                            content.newLineAtOffset(offsetX, yPosition - l * rowHeight);
                            content.showText(linhas[l]);
                            content.endText();
                        }
                    }
                    yPosition -= rowHeight * linhasMaxDoRow(model, row);

                    if (yPosition < margin) {
                        content.close();
                        page = new PDPage(pageSize);
                        document.addPage(page);
                        content = new PDPageContentStream(document, page);
                        yPosition = yStart;
                    }
                }

                // Linha de Total Geral
                String labelTotal = "Total Geral:";
                String valorTotal = totalGeral;

                float textWidthLabel = fontTotal.getStringWidth(labelTotal) / 1000 * fontSize;
                float textWidthValue = fontTotal.getStringWidth(valorTotal) / 1000 * fontSize;
                float offsetXLabel = xStart + (colEnd - colStart - 2) * colWidth + (colWidth - textWidthLabel) / 2;
                float offsetXValue = xStart + (colEnd - colStart - 1) * colWidth + (colWidth - textWidthValue) / 2;

                content.beginText();
                content.setFont(fontTotal, fontSize);
                content.newLineAtOffset(offsetXLabel, yPosition - rowHeight);
                content.showText(labelTotal);
                content.endText();

                content.beginText();
                content.setFont(fontTotal, fontSize);
                content.newLineAtOffset(offsetXValue, yPosition - rowHeight);
                content.showText(valorTotal);
                content.endText();

                content.close();
                colStart += columnsPerPage;
            }

            document.save(filePath);
            document.close();
            new Funcoes().Mensagens(parent, "Salvo com sucesso", "", "SUCESSO", "informacao");
        }

        private static void exportToPDFComValorQuantidade(DefaultTableModel model, String filePath, Component parent, String totalGeral, String quantidadeTotal) throws IOException {
    PDDocument document = new PDDocument();
    PDRectangle pageSize = new PDRectangle(PDRectangle.LETTER.getHeight(), PDRectangle.LETTER.getWidth());
    float margin = 20;
    float yStart = pageSize.getHeight() - margin;

    PDFont fontHeader = PDType1Font.HELVETICA_BOLD;
    PDFont fontData = PDType1Font.HELVETICA;
    PDFont fontTotal = PDType1Font.HELVETICA_BOLD;
    float fontSize = 6;
    float rowHeight = fontSize * 1.5f;

    int columnsPerPage = 8;
    int totalColumns = model.getColumnCount();
    int totalRows = model.getRowCount();
    int colStart = 0;

    while (colStart < totalColumns) {
        PDPage page = new PDPage(pageSize);
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);

        float yPosition = yStart;
        float xStart = margin;
        int colEnd = Math.min(colStart + columnsPerPage, totalColumns);
        float colWidth = (pageSize.getWidth() - 2 * margin) / (colEnd - colStart);

        // Cabeçalho
        for (int i = colStart; i < colEnd; i++) {
            String header = model.getColumnName(i);
            float textWidth = fontHeader.getStringWidth(header) / 1000 * fontSize;
            float offsetX = xStart + (i - colStart) * colWidth + (colWidth - textWidth) / 2;
            content.beginText();
            content.setFont(fontHeader, fontSize);
            content.newLineAtOffset(offsetX, yPosition);
            content.showText(header);
            content.endText();
        }

        yPosition -= rowHeight;

        // Dados
        for (int row = 0; row < totalRows; row++) {
            for (int col = colStart; col < colEnd; col++) {
                Object value = model.getValueAt(row, col);
                String text = value != null ? value.toString() : "";
                float textWidth = fontData.getStringWidth(text) / 1000 * fontSize;
                float offsetX = xStart + (col - colStart) * colWidth + (colWidth - textWidth) / 2; // centralizado horizontal
                content.beginText();
                content.setFont(fontData, fontSize);
                content.newLineAtOffset(offsetX, yPosition);
                content.showText(text);
                content.endText();
            }
            yPosition -= rowHeight;
        }

        // Linha de Quantidade Total
        String labelQuantidade = "Quantidade Total:";
        String valorQuantidade = quantidadeTotal;
        float textWidthLabelQtd = fontTotal.getStringWidth(labelQuantidade) / 1000 * fontSize;
        float textWidthValueQtd = fontTotal.getStringWidth(valorQuantidade) / 1000 * fontSize;
        float offsetXLabelQtd = xStart + (colEnd - colStart - 2) * colWidth + (colWidth - textWidthLabelQtd) / 2;
        float offsetXValueQtd = xStart + (colEnd - colStart - 1) * colWidth + (colWidth - textWidthValueQtd) / 2;

        content.beginText();
        content.setFont(fontTotal, fontSize);
        content.newLineAtOffset(offsetXLabelQtd, yPosition - rowHeight);
        content.showText(labelQuantidade);
        content.endText();

        content.beginText();
        content.setFont(fontTotal, fontSize);
        content.newLineAtOffset(offsetXValueQtd, yPosition - rowHeight);
        content.showText(valorQuantidade);
        content.endText();

        yPosition -= rowHeight;

        // Linha de Total Geral
        String labelTotal = "Total Geral:";
        String valorTotal = totalGeral;
        float textWidthLabel = fontTotal.getStringWidth(labelTotal) / 1000 * fontSize;
        float textWidthValue = fontTotal.getStringWidth(valorTotal) / 1000 * fontSize;
        float offsetXLabel = xStart + (colEnd - colStart - 2) * colWidth + (colWidth - textWidthLabel) / 2;
        float offsetXValue = xStart + (colEnd - colStart - 1) * colWidth + (colWidth - textWidthValue) / 2;

        content.beginText();
        content.setFont(fontTotal, fontSize);
        content.newLineAtOffset(offsetXLabel, yPosition - rowHeight);
        content.showText(labelTotal);
        content.endText();

        content.beginText();
        content.setFont(fontTotal, fontSize);
        content.newLineAtOffset(offsetXValue, yPosition - rowHeight);
        content.showText(valorTotal);
        content.endText();

        content.close();
        colStart += columnsPerPage;
    }

    document.save(filePath);
    document.close();
    new Funcoes().Mensagens(parent, "Salvo com sucesso", "", "SUCESSO", "informacao");
}       

        public static void exportarTabela(DefaultTableModel modelo, TipoArquivo tipo, String nome, Component parent) {
            JFrame parentFrame = null;
            if (parent instanceof JFrame) {
                parentFrame = (JFrame) parent;
            }
            try {
                // Ativa o overlay apenas se a janela pai for um JFrame válido
                    if (parentFrame != null) {
                        if (!(parentFrame.getGlassPane() instanceof OverlayUtil.OverlayPane)) {
                            parentFrame.setGlassPane(new OverlayUtil.OverlayPane());
                        }
                        parentFrame.getGlassPane().setVisible(true);
                    }
                // Diretório inicial: área de trabalho
                String userHome = System.getProperty("user.home");
                File desktop = new File(userHome, "Desktop");

                JFileChooser chooser = new JFileChooser(desktop);
                chooser.setDialogTitle("Salvar arquivo");

                // Filtro por extensão
                FileNameExtensionFilter filter;
                if (tipo == TipoArquivo.EXCEL) {
                    filter = new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx");
                    chooser.setSelectedFile(new File(desktop, nome+".xlsx"));
                } else {
                    filter = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
                    chooser.setSelectedFile(new File(desktop, nome+".pdf"));
                }
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false); // bloqueia "Todos os arquivos"

                int userSelection = chooser.showSaveDialog(null);

                // Desativa o overlay após o fechamento da janela de seleção
                if (parentFrame != null) {
                    parentFrame.getGlassPane().setVisible(false);
                }

                if (userSelection != JFileChooser.APPROVE_OPTION) return;

                File fileToSave = chooser.getSelectedFile();
                String path = fileToSave.getAbsolutePath();

                if (tipo == TipoArquivo.EXCEL) exportToExcel(modelo, path,parent);
                else exportToPDF(modelo, path,parent);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static void exportToExcel(DefaultTableModel model, String filePath, Component parent) {
            try {
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Dados");

                // Estilo do cabeçalho
                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                // Cabeçalho
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Dados
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Ajusta largura das colunas
                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }

                workbook.close();
                new Funcoes().Mensagens(parent, "Salvo com sucesso", "", "SUCESSO" ,"informacao");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void exportarTabelaComTotal(DefaultTableModel modelo, TipoArquivo tipo, String nome, Component parent, String totalGeral) {
            JFrame parentFrame = null;
            if (parent instanceof JFrame) {
                parentFrame = (JFrame) parent;
            }
            try {
                if (parentFrame != null) {
                    if (!(parentFrame.getGlassPane() instanceof OverlayUtil.OverlayPane)) {
                        parentFrame.setGlassPane(new OverlayUtil.OverlayPane());
                    }
                    parentFrame.getGlassPane().setVisible(true);
                }

                String userHome = System.getProperty("user.home");
                File desktop = new File(userHome, "Desktop");

                JFileChooser chooser = new JFileChooser(desktop);
                chooser.setDialogTitle("Salvar arquivo");

                FileNameExtensionFilter filter;
                if (tipo == TipoArquivo.EXCEL) {
                    filter = new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx");
                    chooser.setSelectedFile(new File(desktop, nome + ".xlsx"));
                } else {
                    filter = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
                    chooser.setSelectedFile(new File(desktop, nome + ".pdf"));
                }
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);

                int userSelection = chooser.showSaveDialog(null);

                if (parentFrame != null) {
                    parentFrame.getGlassPane().setVisible(false);
                }

                if (userSelection != JFileChooser.APPROVE_OPTION) return;

                File fileToSave = chooser.getSelectedFile();
                String path = fileToSave.getAbsolutePath();

                if (tipo == TipoArquivo.EXCEL) {
                    exportToExcelComTotal(modelo, path, parent, totalGeral);
                } else {
                    exportToPDFComTotal(modelo, path, parent, totalGeral);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private static void exportToExcelComTotal(DefaultTableModel model, String filePath, Component parent, String totalGeral) {
            try {
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Dados");

                // Estilo cabeçalho
                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                // Dados
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        String texto = "";
                        if (value != null) {
                            texto = value.toString().replaceAll("(?i)<br>", "\n")
                                                    .replaceAll("(?i)<html>|</html>", "");
                        }
                        Cell cell = row.createCell(j);
                        cell.setCellValue(texto);

                        CellStyle style = workbook.createCellStyle();
                        style.setWrapText(true);
                        style.setAlignment(HorizontalAlignment.CENTER);
                        style.setVerticalAlignment(VerticalAlignment.CENTER);
                        cell.setCellStyle(style);
                    }
                }

                // Linha de total
                int lastRowIndex = sheet.getPhysicalNumberOfRows();
                Row totalRow = sheet.createRow(lastRowIndex);
                Cell totalLabelCell = totalRow.createCell(model.getColumnCount() - 2);
                totalLabelCell.setCellValue("Total Geral:");

                Cell totalValueCell = totalRow.createCell(model.getColumnCount() - 1);
                totalValueCell.setCellValue(totalGeral);

                CellStyle totalStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font totalFont = workbook.createFont();
                totalFont.setBold(true);
                totalStyle.setFont(totalFont);
                totalStyle.setAlignment(HorizontalAlignment.RIGHT);
                totalLabelCell.setCellStyle(totalStyle);
                totalValueCell.setCellStyle(totalStyle);

                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }
                workbook.close();

                new Funcoes().Mensagens(parent, "Salvo com sucesso", "", "SUCESSO", "informacao");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public static void exportarTabelaComValorQuantidade(DefaultTableModel modelo, TipoArquivo tipo, String nome, Component parent, String totalGeral, String quantidadeTotal) {
            JFrame parentFrame = null;
            if (parent instanceof JFrame) {
                parentFrame = (JFrame) parent;
            }
            try {
                if (parentFrame != null) {
                    if (!(parentFrame.getGlassPane() instanceof OverlayUtil.OverlayPane)) {
                        parentFrame.setGlassPane(new OverlayUtil.OverlayPane());
                    }
                    parentFrame.getGlassPane().setVisible(true);
                }

                String userHome = System.getProperty("user.home");
                File desktop = new File(userHome, "Desktop");

                JFileChooser chooser = new JFileChooser(desktop);
                chooser.setDialogTitle("Salvar arquivo");

                FileNameExtensionFilter filter;
                if (tipo == TipoArquivo.EXCEL) {
                    filter = new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx");
                    chooser.setSelectedFile(new File(desktop, nome + ".xlsx"));
                } else {
                    filter = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
                    chooser.setSelectedFile(new File(desktop, nome + ".pdf"));
                }
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);

                int userSelection = chooser.showSaveDialog(null);

                if (parentFrame != null) {
                    parentFrame.getGlassPane().setVisible(false);
                }

                if (userSelection != JFileChooser.APPROVE_OPTION) return;

                File fileToSave = chooser.getSelectedFile();
                String path = fileToSave.getAbsolutePath();

                if (tipo == TipoArquivo.EXCEL) {
                    exportToExcelComValorQuantidade(modelo, path, parent, totalGeral, quantidadeTotal);
                } else {
                    exportToPDFComValorQuantidade(modelo, path, parent, totalGeral, quantidadeTotal);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private static void exportToExcelComValorQuantidade(DefaultTableModel model, String filePath, Component parent, String totalGeral, String quantidadeTotal) {
            try {
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Dados");

                CellStyle headerStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                    cell.setCellStyle(headerStyle);
                }

                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Adiciona a linha de quantidade total
                int lastRowIndex = sheet.getPhysicalNumberOfRows();
                Row quantidadeRow = sheet.createRow(lastRowIndex);
                Cell quantidadeLabelCell = quantidadeRow.createCell(model.getColumnCount() - 2);
                quantidadeLabelCell.setCellValue("Quantidade Total:");

                Cell quantidadeValueCell = quantidadeRow.createCell(model.getColumnCount() - 1);
                quantidadeValueCell.setCellValue(quantidadeTotal);

                // Adiciona a linha de total geral
                lastRowIndex = sheet.getPhysicalNumberOfRows();
                Row totalRow = sheet.createRow(lastRowIndex);
                Cell totalLabelCell = totalRow.createCell(model.getColumnCount() - 2);
                totalLabelCell.setCellValue("Total Geral:");

                Cell totalValueCell = totalRow.createCell(model.getColumnCount() - 1);
                totalValueCell.setCellValue(totalGeral);

                // Estilo negrito para as linhas de total
                CellStyle totalStyle = workbook.createCellStyle();
                org.apache.poi.ss.usermodel.Font totalFont = workbook.createFont();
                totalFont.setBold(true);
                totalStyle.setFont(totalFont);
                totalStyle.setAlignment(HorizontalAlignment.RIGHT);

                quantidadeLabelCell.setCellStyle(totalStyle);
                quantidadeValueCell.setCellStyle(totalStyle);
                totalLabelCell.setCellStyle(totalStyle);
                totalValueCell.setCellStyle(totalStyle);

                for (int i = 0; i < model.getColumnCount(); i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream out = new FileOutputStream(filePath)) {
                    workbook.write(out);
                }

                workbook.close();
                new Funcoes().Mensagens(parent, "Salvo com sucesso", "", "SUCESSO" ,"informacao");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        public static void exportarBalançoFinanceiro(BigDecimal totalVendas, BigDecimal totalGastos, BigDecimal balanco, TipoArquivo tipo, Component parent) {
            DefaultTableModel modeloTemp = new DefaultTableModel();
            modeloTemp.addColumn("Descrição");
            modeloTemp.addColumn("Valor (R$)");

            DecimalFormat formatoMoeda = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(new Locale("pt", "BR")));
            String valorVendasFormatado = formatoMoeda.format(totalVendas);
            String valorGastosFormatado = formatoMoeda.format(totalGastos);
            String balancoFormatado = formatoMoeda.format(balanco);

            modeloTemp.addRow(new Object[]{"Receita de Pedidos", valorVendasFormatado});
            modeloTemp.addRow(new Object[]{"Custo de Reposição", valorGastosFormatado});
            modeloTemp.addRow(new Object[]{"Balanço do Período", balancoFormatado});

            exportarTabela(modeloTemp, tipo, "BalançoFinanceiro", parent);
        }
        
        public static void validarNumero(JTextField campo, int limiteInteiros, int limiteDecimais) {
            // Validação ao perder foco
            campo.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    ajustarNumero(campo, limiteInteiros, limiteDecimais);
                }
            });

            // Validação leve durante digitação: apenas impede caracteres inválidos
            campo.addKeyListener(new KeyAdapter() {
                @Override
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    String texto = campo.getText();

                    // Permite apenas dígitos, vírgula ou ponto
                    if (!Character.isDigit(c) && c != ',' && c != '.') {
                        e.consume();
                        return;
                    }

                    // Impede mais de uma vírgula ou ponto
                    if ((c == ',' || c == '.') && (texto.contains(",") || texto.contains("."))) {
                        e.consume();
                        return;
                    }
                }
            });
        }

        private static void ajustarNumero(JTextField campo, int limiteInteiros, int limiteDecimais) {
            String texto = campo.getText().trim().replace(",", ".");
            if (texto.isEmpty()) {
                campo.setText("0,00");
                return;
            }

            try {
                Double.parseDouble(texto);
                String[] partes = texto.split("\\.");
                String inteiros = partes[0];
                String decimais = (partes.length > 1) ? partes[1] : "";

                if (inteiros.length() > limiteInteiros) inteiros = inteiros.substring(0, limiteInteiros);
                if (decimais.length() > limiteDecimais) decimais = decimais.substring(0, limiteDecimais);

                // Completa zeros se necessário
                while (decimais.length() < limiteDecimais) decimais += "0";

                campo.setText(inteiros + "," + decimais);
            } catch (NumberFormatException ex) {
                campo.setText("0,00");
            }
        }

        public static void validarTexto(JTextField campo, int limite) {
            campo.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    String texto = campo.getText();
                    if (texto.length() > limite) {
                        campo.setText(texto.substring(0, limite));
                    }
                }
            });
        }

        public static void validarTextoArea(JTextArea campo, int limite) {
            campo.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    String texto = campo.getText();
                    if (texto.length() > limite) {
                        campo.setText(texto.substring(0, limite));
                    }
                }
            });
        }
        
        public static boolean verificarConexaoInternet(Window parent) {
            try (Socket socket = new Socket("www.google.com", 80)) {
                // A conexão foi bem-sucedida
                return true;
            } catch (IOException e) {
                               
                return false;
            }
        }

        public boolean nomeCompleto(JTextField campoNome) {
            if (campoNome == null || campoNome.getText() == null) return false;

            String nome = campoNome.getText().trim();

            // Verifica se tem pelo menos duas palavras
            String[] partes = nome.split("\\s+");
            if (partes.length < 2) return false;

            // Verifica se cada parte tem pelo menos 2 letras e contém apenas letras/acento
            for (String parte : partes) {
                if (parte.length() < 2 || !parte.matches("[A-Za-zÀ-ÿ]+")) {
                    return false;
                }
            }

            return true;
        }
        
        public String gerarSenhaAleatoria() {
            int tamanho = 8;

            // Caracteres possíveis
            String letrasMaiusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String letrasMinusculas = "abcdefghijklmnopqrstuvwxyz";
            String numeros = "0123456789";
            String especiais = "!@#$%&*";

            String todos = letrasMaiusculas + letrasMinusculas + numeros + especiais;

            SecureRandom random = new SecureRandom();
            StringBuilder senha = new StringBuilder();

            // Garante pelo menos 1 de cada tipo
            senha.append(letrasMaiusculas.charAt(random.nextInt(letrasMaiusculas.length())));
            senha.append(letrasMinusculas.charAt(random.nextInt(letrasMinusculas.length())));
            senha.append(numeros.charAt(random.nextInt(numeros.length())));
            senha.append(especiais.charAt(random.nextInt(especiais.length())));

            // Preenche o resto da senha com caracteres aleatórios
            for (int i = 4; i < tamanho; i++) {
                senha.append(todos.charAt(random.nextInt(todos.length())));
            }

            // Embaralha para não deixar os primeiros caracteres previsíveis
            return embaralharString(senha.toString());
        }
        
        private String embaralharString(String input) {
            List<Character> caracteres = new ArrayList<>();
            for (char c : input.toCharArray()) {
                caracteres.add(c);
            }
            Collections.shuffle(caracteres);
            StringBuilder resultado = new StringBuilder();
            for (char c : caracteres) {
                resultado.append(c);
            }
            return resultado.toString();
        }
        
        public boolean camposIguais(JTextField campo1, JTextField campo2) {
            String texto1 = campo1.getText() != null ? campo1.getText().trim() : "";
            String texto2 = campo2.getText() != null ? campo2.getText().trim() : "";
            
            return texto1.equals(texto2);
        }
        
        public boolean senhasIguais(JPasswordField campo1, JPasswordField campo2) {
            char[] senha1 = campo1.getPassword();
            char[] senha2 = campo2.getPassword();

            return Arrays.equals(senha1, senha2);
        }
        
        public static String gerarUsuarioID(String nomeCompleto) {
            // Divide em partes
            String[] partes = nomeCompleto.trim().split("\\s+");
            if (partes.length < 2) {
                throw new IllegalArgumentException("O nome precisa ter pelo menos nome e sobrenome");
            }

            String primeiroNome = partes[0].toUpperCase();
            String segundoNome = partes[1].toUpperCase();

            String baseUsuario = primeiroNome + segundoNome;

            // Agora verificar no banco se já existe
            String sql = "SELECT COUNT(*) FROM usuario WHERE Nome LIKE ?";
            try (Connection conn = ConexaoBanco.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, baseUsuario + "%");
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    int quantidade = rs.getInt(1);

                    // Se já existir, acrescenta número
                    if (quantidade > 0) {
                        return baseUsuario + quantidade;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return baseUsuario;
        }

        public static boolean enviarEmail(String destinatario, String assunto, String mensagem) {
            final String remetente = "deosermis@gmail.com"; 
            final String senha = "jhqk whxt gobp jsjg";

            // Configurações do servidor SMTP do Gmail
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            // Autenticação usando Jakarta Mail
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(remetente, senha);
                }
            });

            try {
                // Criação da mensagem
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(remetente));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
                message.setSubject(assunto);
                message.setText(mensagem);

                // Envio da mensagem
                Transport.send(message);
                return true;
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
        }
 
        public static boolean validarSenha(String senhaDigitada, String hashArmazenado, String saltBase64) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltBase64);

            PBEKeySpec spec = new PBEKeySpec(
                    senhaDigitada.toCharArray(),
                    salt,
                    ITERACOES,
                    TAMANHO_CHAVE
            );

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            byte[] hashDigitado = skf.generateSecret(spec).getEncoded();
            String hashDigitadoBase64 = Base64.getEncoder().encodeToString(hashDigitado);

            return hashDigitadoBase64.equals(hashArmazenado);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
    }
        
        public boolean senhaValida(String senha) {
            if (senha.length() < 8 || senha.length() > 20) {
                return false;
            }

            boolean hasLetter = false;
            boolean hasNumber = false;

            for (char c : senha.toCharArray()) {
                if (Character.isLetter(c)) {
                    hasLetter = true;
                } else if (Character.isDigit(c)) {
                    hasNumber = true;
                }
                // Podemos sair do loop assim que encontrarmos os dois
                if (hasLetter && hasNumber) {
                    return true; 
                }
            }

            return hasLetter && hasNumber;
        }
        
        public static boolean enviarCredenciais(String destinatario, String nome, String senha) {
            String assunto = "Recuperação de Credenciais";
            StringBuilder mensagem = new StringBuilder();
            mensagem.append("Seus dados de acesso ao sistema DEOSÉRMIS:\n\n");
            // Verifica se o nome deve ser incluído na mensagem
            if (nome != null && !nome.trim().isEmpty()) {
                mensagem.append("Nome de Usuário: ").append(nome);
            }

            // Verifica se a senha deve ser incluída na mensagem
            if (senha != null && !senha.trim().isEmpty()) {
                if (mensagem.length() > 0) {
                    mensagem.append("\n"); // Adiciona uma nova linha se já houver texto
                }
                mensagem.append("Senha: ").append(senha);
            }

            // Se a mensagem estiver vazia, significa que nenhum dado foi passado
            if (mensagem.length() == 0) {
                System.err.println("Erro: Nenhum dado (nome ou senha) foi fornecido para o envio.");
                return false;
            }

            // Chama a função original para enviar o email com a mensagem construída
            return enviarEmail(destinatario, assunto, mensagem.toString());
        }
}
