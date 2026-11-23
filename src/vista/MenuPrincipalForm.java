package vista;

import conexion.Conexion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del Sistema de Gestión Bancaria Académica - Fase 2.
 */
public class MenuPrincipalForm extends JFrame {

    private static final Logger logger = LogManager.getLogger(MenuPrincipalForm.class);

    public MenuPrincipalForm() {
        setTitle("Sistema de Gestión Bancaria Académica - UDB (Fase 2)");
        setSize(540, 510);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        inicializarComponentes();
        logger.info("Menú principal iniciado correctamente.");
    }

    private void inicializarComponentes() {
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 540, 75);
        panelHeader.setBackground(new Color(24, 43, 73));
        panelHeader.setLayout(null);

        JLabel lblTitulo = new JLabel("BANCO ACADÉMICO UDB", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 12, 540, 25);
        panelHeader.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Proyecto de Cátedra POO901 - Fase 2", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(200, 215, 240));
        lblSubtitulo.setBounds(0, 38, 540, 20);
        panelHeader.add(lblSubtitulo);

        add(panelHeader);

        agregarBotonMenu("Gestión de Clientes (CRUD)", 100, 45, true, () -> {
            logger.info("Abriendo módulo de Gestión de Clientes.");
            new ClienteForm().setVisible(true);
        });

        agregarBotonMenu("Gestión de Cuentas Bancarias", 158, 45, true, () -> {
            logger.info("Abriendo módulo de Gestión de Cuentas Bancarias.");
            new CuentaBancariaForm().setVisible(true);
        });

        agregarBotonMenu("Gestión de Transacciones (Depósitos, Retiros y Transferencias)", 216, 45, true, () -> {
            logger.info("Abriendo módulo de Gestión de Transacciones.");
            new TransaccionesForm().setVisible(true);
        });

        agregarBotonMenu("Consultas y Reportes Bancarios", 274, 45, true, () -> {
            logger.info("Abriendo módulo de Consultas y Reportes.");
            new ReportesForm().setVisible(true);
        });

        agregarBotonMenu("Probar Conexión con MySQL (JDBC)", 336, 40, false, () -> {
            boolean conecto = Conexion.probarConexion();
            if (conecto) {
                JOptionPane.showMessageDialog(this,
                        "¡Conexión exitosa a la base de datos 'banco_academico' en MySQL!",
                        "Conexión Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo conectar a MySQL.\nVerifique que el servicio de MySQL esté iniciado y la base de datos 'banco_academico' exista.",
                        "Error de Conexión",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSalir.setBounds(215, 392, 110, 32);
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> {
            logger.info("Cerrando aplicación bancaria.");
            System.exit(0);
        });
        add(btnSalir);

        JLabel lblPie = new JLabel("Fase 2: Desktop Swing + Transacciones + Reportes + Excepciones + Log4J", SwingConstants.CENTER);
        lblPie.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblPie.setForeground(Color.GRAY);
        lblPie.setBounds(0, 440, 540, 20);
        add(lblPie);
    }

    private void agregarBotonMenu(String texto, int y, int alto, boolean negrita, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", negrita ? Font.BOLD : Font.PLAIN, negrita ? 13 : 13));
        boton.setBounds(50, y, 440, alto);
        boton.setFocusPainted(false);
        boton.addActionListener(e -> accion.run());
        add(boton);
    }
}
