/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Felipe
 */
public class OverlayUtil {

    // Painel escuro reutilizável
    public static class OverlayPane extends JPanel {
        public OverlayPane() {
            setOpaque(false);
            setLayout(null);
            addMouseListener(new java.awt.event.MouseAdapter() {});
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {});
            addMouseWheelListener(e -> {});
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0, 0, 0, 150)); // Preto com transparência
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Método reutilizável para exibir diálogo com efeito de escurecimento
    public static void abrirTela(JFrame parent, JDialog dialog) {
        // Cria o overlay se necessário
        if (!(parent.getGlassPane() instanceof OverlayPane)) {
            parent.setGlassPane(new OverlayPane());
        }

        // Ativa o efeito escuro
        parent.getGlassPane().setVisible(true);

        // Centraliza o diálogo
        dialog.setLocationRelativeTo(parent);

        // Adiciona listener para remover o overlay ao fechar
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                parent.getGlassPane().setVisible(false);
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                parent.getGlassPane().setVisible(false);
            }
        });

        // Exibe o diálogo
        dialog.setVisible(true);
    }
    public static void abrirTelaJDialog(JDialog parent, JDialog dialog) {
        // Garante que o parent não seja null
        if (parent == null || dialog == null) return;

        // Cria o overlay se necessário
        if (!(parent.getGlassPane() instanceof OverlayPane)) {
            parent.setGlassPane(new OverlayPane());
        }

        // Ativa o efeito escuro
        parent.getGlassPane().setVisible(true);

        // Centraliza o diálogo em relação ao parent
        dialog.setLocationRelativeTo(parent);

        // Remove o overlay quando o diálogo for fechado
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                parent.getGlassPane().setVisible(false);
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                parent.getGlassPane().setVisible(false);
            }
        });

        // Exibe o diálogo
        dialog.setVisible(true);
    }

}
