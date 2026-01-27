/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;
import java.math.BigDecimal;
import java.util.List;

public class CarrinhoPedido {
    private static  List<ItemPedido> itens;
    private static  BigDecimal subtotal;
    private static  BigDecimal desconto;
    private static  BigDecimal valorTotal;
    private static  boolean finalizouPedido;

    public static boolean getFinalizouPedido() {
        return finalizouPedido;
    }

    public static void setFinalizouPedido(boolean finalizouPedido) {
        CarrinhoPedido.finalizouPedido = finalizouPedido;
    }

    public CarrinhoPedido(List<ItemPedido> itens, BigDecimal subtotal, BigDecimal desconto, BigDecimal valorTotal) {
        this.itens = itens;
        this.subtotal = subtotal;
        this.desconto = desconto;
        this.valorTotal = valorTotal;
    }

    // Getters
    public static List<ItemPedido> getItens() {
        return itens;
    }

    public static BigDecimal getSubtotal() {
        return subtotal;
    }

    public static BigDecimal getDesconto() {
        return desconto;
    }

    public static BigDecimal getValorTotal() {
        return valorTotal;
    }
    public static void finalizarPedido() {
        if (itens != null) {
            itens.clear();
        }
        subtotal = BigDecimal.ZERO;
        desconto = BigDecimal.ZERO;
        valorTotal = BigDecimal.ZERO;
        finalizouPedido = false;
    }
}
