/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

/**
 *
 * @author Felipe
 */
public class Mensagem {
    private static String titulo, mensagem, mensagem2, tipo;
    private static Runnable acao;

    public static Runnable getAcao() {
        return acao;
    }

    public static void setAcao(Runnable acao) {
        Mensagem.acao = acao;
    }

    public static String getMensagem2() {
        return mensagem2;
    }

    public static void setMensagem2(String mensagem2) {
        Mensagem.mensagem2 = mensagem2;
    }

    public static String getTipo() {
        return tipo;
    }

    public static void setTipo(String tipo) {
        Mensagem.tipo = tipo;
    }

    public static String getTitulo() {
        return titulo;
    }

    public static void setTitulo(String titulo) {
        Mensagem.titulo = titulo;
    }

    public static String getMensagem() {
        return mensagem;
    }

    public static void setMensagem(String mensagem) {
        Mensagem.mensagem = mensagem;
    }
}
