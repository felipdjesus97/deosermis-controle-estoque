/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import Main.ConexaoBanco;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class BalançoFinanceiro {

    // Calcula o total de vendas (ValorTotal da tabela pedidoproduto) em um período
    public static BigDecimal calcularTotalVendas(Date dataInicial, Date dataFim) {
        BigDecimal totalVendas = BigDecimal.ZERO;
        String sql = "SELECT SUM(ValorTotal) FROM pedidoproduto WHERE Data BETWEEN ? AND ?";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(dataInicial.getTime()));
            stmt.setDate(2, new java.sql.Date(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalVendas = rs.getBigDecimal(1);
                    if (totalVendas == null) {
                        totalVendas = BigDecimal.ZERO;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalVendas;
    }

    // Calcula o total de gastos com reposição (ValorDaCompra da tabela reposicaoproduto) em um período
    public static BigDecimal calcularTotalReposicoes(Date dataInicial, Date dataFim) {
        BigDecimal totalReposicoes = BigDecimal.ZERO;
        String sql = "SELECT SUM(ValorDaCompra) FROM reposicaoproduto WHERE Data BETWEEN ? AND ?";

        try (Connection con = ConexaoBanco.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDate(1, new java.sql.Date(dataInicial.getTime()));
            stmt.setDate(2, new java.sql.Date(dataFim.getTime()));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalReposicoes = rs.getBigDecimal(1);
                    if (totalReposicoes == null) {
                        totalReposicoes = BigDecimal.ZERO;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalReposicoes;
    }
}
