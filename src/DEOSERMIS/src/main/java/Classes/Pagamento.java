/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;
import java.math.BigDecimal;

public class Pagamento {
    private final String forma;
    private final BigDecimal valor;

    public Pagamento(String forma, BigDecimal valor) {
        this.forma = forma;
        this.valor = valor;
    }

    public String getForma() {
        return forma;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
