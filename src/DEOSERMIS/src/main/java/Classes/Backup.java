package Classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.Component;
import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.zip.*;

public class Backup {

    private static final String BACKUP_DIR = System.getProperty("user.home") 
            + File.separator + "Deosermis" + File.separator + "Backup";

    public static void criarBackup(Component parent) throws Exception {
        String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH.mm").format(new Date());
        String nomeArquivo = "backup_" + timestamp;

        try (Connection conn = Main.ConexaoBanco.getConnection()) {

            JSONObject backupJson = new JSONObject();
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tabelas = metaData.getTables(conn.getCatalog(), null, "%", new String[]{"TABLE"});

            while (tabelas.next()) {
                String tabela = tabelas.getString("TABLE_NAME");
                if (tabela.startsWith("pma__")) continue;

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM " + tabela);
                ResultSetMetaData rsMeta = rs.getMetaData();
                JSONArray tabelaJson = new JSONArray();

                while (rs.next()) {
                    JSONObject linha = new JSONObject();
                    for (int i = 1; i <= rsMeta.getColumnCount(); i++) {
                        String coluna = rsMeta.getColumnName(i);
                        Object valor = rs.getObject(i);
                        if (valor instanceof byte[]) {
                            linha.put(coluna, Base64.getEncoder().encodeToString((byte[]) valor));
                        } else {
                            linha.put(coluna, valor);
                        }
                    }
                    tabelaJson.put(linha);
                }
                backupJson.put(tabela, tabelaJson);
            }

            Files.createDirectories(Paths.get(BACKUP_DIR));
            String jsonPath = BACKUP_DIR + File.separator + nomeArquivo + ".json";

            // Salva o JSON explicitamente em UTF-8
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(jsonPath), "UTF-8")) {
                writer.write(backupJson.toString(2));
            }

            String zipPath = BACKUP_DIR + File.separator + nomeArquivo + ".zip";
            try (FileOutputStream fos = new FileOutputStream(zipPath);
                 ZipOutputStream zos = new ZipOutputStream(fos)) {

                File fileToZip = new File(jsonPath);
                try (FileInputStream fis = new FileInputStream(fileToZip)) {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zos.putNextEntry(zipEntry);
                    byte[] bytes = new byte[1024];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes, 0, length);
                    }
                }
            }

            Files.deleteIfExists(Paths.get(jsonPath));
            apagarBackupsAntigos(zipPath);
            new Funcoes().Mensagens(parent, "Backup criado com sucesso!", "", "SUCESSO", "informacao");
        }
    }

    public static void restaurarBackup(String zipPath) throws Exception {
        String jsonContent;
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry = zis.getNextEntry();
            if (entry == null) throw new IOException("Arquivo ZIP vazio");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = zis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            jsonContent = baos.toString("UTF-8");
        }

        JSONObject backupJson = new JSONObject(jsonContent);

        try (Connection conn = Main.ConexaoBanco.getConnection()) {
            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");

            for (String tabela : backupJson.keySet()) {
                JSONArray dadosTabela = backupJson.getJSONArray(tabela);
                conn.createStatement().executeUpdate("DELETE FROM " + tabela);

                if (dadosTabela.length() == 0) continue;

                JSONObject primeiraLinha = dadosTabela.getJSONObject(0);
                String[] colunas = primeiraLinha.keySet().toArray(new String[0]);

                StringBuilder placeholders = new StringBuilder();
                for (int i = 0; i < colunas.length; i++) placeholders.append("?,");

                placeholders.setLength(placeholders.length() - 1);

                String sql = "INSERT INTO " + tabela + " (" + String.join(",", colunas) + ") VALUES (" + placeholders + ")";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (int i = 0; i < dadosTabela.length(); i++) {
                        JSONObject linha = dadosTabela.getJSONObject(i);
                        for (int j = 0; j < colunas.length; j++) {
                            Object valor = linha.isNull(colunas[j]) ? null : linha.get(colunas[j]);
                            if (valor == null) {
                                ps.setNull(j + 1, Types.NULL);
                            } else if (valor instanceof Boolean) {
                                ps.setInt(j + 1, ((Boolean) valor) ? 1 : 0);
                            } else if (valor instanceof Number) {
                                ps.setObject(j + 1, valor);
                            } else if (colunas[j].toLowerCase().contains("imagem")) {
                                byte[] bytes = Base64.getDecoder().decode(valor.toString());
                                ps.setBytes(j + 1, bytes);
                            } else {
                                ps.setString(j + 1, valor.toString());
                            }
                        }
                        ps.addBatch();
                        if (i % 500 == 0) ps.executeBatch();
                    }
                    ps.executeBatch();
                }
            }

            conn.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
        }
    }

    public static void apagarBackupsAntigos(String caminhoBackupNovo) {
        File pastaBackup = new File(BACKUP_DIR);

        if (!pastaBackup.exists() || !pastaBackup.isDirectory()) return;

        File[] arquivos = pastaBackup.listFiles((dir, name) -> name.toLowerCase().endsWith(".zip"));

        if (arquivos == null) return;

        try {
            String novoCanonical = new File(caminhoBackupNovo).getCanonicalPath();

            for (File f : arquivos) {
                String atualCanonical = f.getCanonicalPath();
                if (!atualCanonical.equals(novoCanonical)) {
                    boolean deletado = f.delete();
                    if (!deletado) {
                        System.err.println("Não consegui apagar: " + atualCanonical);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUltimoBackup() {
        File pastaBackup = new File(BACKUP_DIR);

        if (!pastaBackup.exists() || !pastaBackup.isDirectory()) {
            return null;
        }

        File[] arquivos = pastaBackup.listFiles((dir, name) -> name.endsWith(".zip"));

        if (arquivos == null || arquivos.length == 0) {
            return null;
        }

        Arrays.sort(arquivos, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));

        return arquivos[0].getAbsolutePath();
    }

    public static String getBackupDir() {
        return BACKUP_DIR;
    }
}