package vista;

import conexion.Conexion;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalForm extends JFrame {

    public MenuPrincipalForm() {
        setTitle("Sistema de Gestión Bancaria Académica - UDB");
        setSize(520, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        // Encabezado
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 520, 75);
        panelHeader.setBackground(new Color(24, 43, 73));
        panelHeader.setLayout(null);

        JLabel lblTitulo = new JLabel("BANCO ACADÉMICO UDB", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(0, 12, 520, 25);
        panelHeader.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Proyecto de Cátedra POO901 - Fase 1", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(200, 215, 240));
        lblSubtitulo.setBounds(0, 38, 520, 20);
        panelHeader.add(lblSubtitulo);

        add(panelHeader);

        // Botón Gestión de Clientes
        JButton btnClientes = new JButton("👤  Gestión de Clientes (CRUD)");
        btnClientes.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnClientes.setBounds(80, 110, 350, 45);
        btnClientes.setFocusPainted(false);
        btnClientes.addActionListener(e -> {
            ClienteForm form = new ClienteForm();
            form.setVisible(true);
        });
        add(btnClientes);

        // Botón Gestión de Cuentas
        JButton btnCuentas = new JButton("💳  Gestión de Cuentas Bancarias");
        btnCuentas.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCuentas.setBounds(80, 170, 350, 45);
        btnCuentas.setFocusPainted(false);
        btnCuentas.addActionListener(e -> {
            CuentaBancariaForm form = new CuentaBancariaForm();
            form.setVisible(true);
        });
        add(btnCuentas);

        // Botón Probar Conexión
        JButton btnProbarConexion = new JButton("🔌  Probar Conexión con MySQL (JDBC)");
        btnProbarConexion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnProbarConexion.setBounds(80, 230, 350, 40);
        btnProbarConexion.setFocusPainted(false);
        btnProbarConexion.addActionListener(e -> {
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
        add(btnProbarConexion);

        // Botón Salir
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnSalir.setBounds(200, 285, 110, 32);
        btnSalir.addActionListener(e -> System.exit(0));
        add(btnSalir);

        // Pie de página
        JLabel lblPie = new JLabel("Fase 1: Desktop Swing + POO + MySQL + JDBC", SwingConstants.CENTER);
        lblPie.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblPie.setForeground(Color.GRAY);
        lblPie.setBounds(0, 345, 520, 20);
        add(lblPie);
    }
}
