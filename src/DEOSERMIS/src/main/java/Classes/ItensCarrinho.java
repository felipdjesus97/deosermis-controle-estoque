/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// ItensCarrinho.java
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ItensCarrinho {
    private static final List<ItemPedido> itens = new ArrayList<>();

    public static List<ItemPedido> getItens() {
        return itens;
    }

    public static void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    public static void removerItem(int index) {
        if (index >= 0 && index < itens.size()) {
            itens.remove(index);
        }
    }
    
    public static ItemPedido buscarItemPorCodigo(BigInteger codigo) {
        for (ItemPedido item : itens) {
            if (item.getCodigo().equals(codigo)) {
                return item;
            }
        }
        return null;
    }
}
