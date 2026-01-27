/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import Main.ConexaoBanco;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;


public class BuscarFormaDePagamento{

    public static List<String> buscarNomes() {
        List<String> nomes = new ArrayList<>();
        String sql = "SELECT Descricao FROM formadepagamento ORDER BY Codigo";

        try (Connection conn = ConexaoBanco.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String descricao = rs.getString("Descricao");
                nomes.add(descricao);
            }
        } catch (SQLException e) {
            Logger.getLogger(BuscarFormaDePagamento.class.getName()).log(Level.SEVERE, "Erro ao buscar nomes das formas de pagamento.", e);
        }
        return nomes;
    }

    /**
     * Preenche um JComboBox com todas as descrições de formas de pagamento.
     *
     * @param comboBox O JComboBox que será preenchido.
     */
    public static void addFP(JComboBox<String> comboBox) {
        // Limpa os itens existentes antes de preencher
        comboBox.removeAllItems();

        List<String> nomesFormasPgto = buscarNomes();
        
        for (String nome : nomesFormasPgto) {
            comboBox.addItem(nome);
        }
    }    
   
}