/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;
// ItemPedido.java
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class ItemPedido {

    private BigInteger codigo;
    private String nome;
    private int quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal total;

    public ItemPedido(BigInteger codigo, String nome, int quantidade, BigDecimal precoUnitario) {
        this.codigo = codigo;
        this.nome = nome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        calcularTotal();
    }

    private void calcularTotal() {
        this.total = this.precoUnitario.multiply(new BigDecimal(this.quantidade)).setScale(2, RoundingMode.HALF_UP);
    }

    // Getters e Setters
    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        calcularTotal();
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }
}