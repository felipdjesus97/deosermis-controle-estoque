/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.math.BigInteger;

public class FornecedorReposicao {

        private static BigInteger codigo;
        private static String nome;

        public static BigInteger getCodigo() {
            return codigo;
        }

        public static void setCodigo(BigInteger codigo) {
            FornecedorReposicao.codigo = codigo;
        }

        public static String getNome() {
            return nome;
        }

        public static void setNome(String nome) {
            FornecedorReposicao.nome = nome;
        }
}
