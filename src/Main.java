import vista.MenuPrincipalForm;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Establecer apariencia nativa del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        // Iniciar la interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            MenuPrincipalForm menu = new MenuPrincipalForm();
            menu.setVisible(true);
        });
    }
}
