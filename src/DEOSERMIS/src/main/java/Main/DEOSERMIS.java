package Main;

import Classes.Funcoes;
import Tela.Inicio.Carregamento;
import javax.swing.*;

public class DEOSERMIS {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Carregamento carregamento = new Carregamento();
            carregamento.setVisible(true);

            int tempoTotal = 5000; 
            int passos = 100;
            int delay = tempoTotal / passos;

            new Thread(() -> {
                try {
                    for (int i = 0; i <= 100; i++) {
                        int progresso = i;
                        SwingUtilities.invokeLater(() -> {
                            carregamento.setProgress(progresso);
                            if (progresso <= 50){
                                carregamento.lblStatus.setText("Verificando conexão... " + progresso + "%");
                            }else{
                                carregamento.lblStatus.setText("Carregando banco de dados... " + progresso + "%");
                            }
                        });
                        Thread.sleep(delay);
                    }

                    // Verifica a conexão
                    if (TesteConexao.verificarConexao()) {
                        SwingUtilities.invokeLater(() -> {
                            carregamento.setProgress(100);
                            carregamento.lblStatus.setText("Conexão OK!");
                        });
                        Thread.sleep(500);
                        carregamento.dispose();
                        new Tela.Inicio.Login().setVisible(true);
                    } else {
                        SwingUtilities.invokeLater(() -> {
                            carregamento.setProgress(0);
                            carregamento.lblStatus.setText("Erro ao conectar!");
                            
                        });
                        new Funcoes().Mensagens(carregamento, "Não foi possível conectar ao banco de dados.","Entre em contato com o suporte!", "Erro", "erro");
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }
}
