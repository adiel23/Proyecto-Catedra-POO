package vista;

import dao.CuentaBancariaDAO;
import dao.TransaccionDAO;
import excepciones.TransaccionBancariaException;
import excepciones.ValidacionBancariaException;
import excepciones.ValidadorBanco;
import modelo.CuentaBancaria;
import modelo.MovimientoBancario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz gráfica Swing para gestionar depósitos, retiros, transferencias,
 * consulta individual de movimientos e historial completo.
 */
public class TransaccionesForm extends JFrame {

    private static final Logger logger = LogManager.getLogger(TransaccionesForm.class);
    private static final Color COLOR_HEADER = new Color(24, 43, 73);
    private static final Color COLOR_VERDE = new Color(25, 111, 61);
    private static final Color COLOR_AZUL = new Color(31, 97, 141);
    private static final Color COLOR_ROJO = new Color(169, 50, 38);

    private final TransaccionDAO transaccionDAO = new TransaccionDAO();
    private final CuentaBancariaDAO cuentaDAO = new CuentaBancariaDAO();

    private JTabbedPane tabbedPane;

    // Pestaña 1: Depósitos y Retiros
    private JTextField txtCuentaOp;
    private JLabel lblInfoCuentaOp;
    private JComboBox<String> cmbTipoOperacion;
    private JTextField txtMontoOp;
    private JTextField txtDescripcionOp;
    private JTextArea txtResultadoOp;

    // Pestaña 2: Transferencias
    private JTextField txtCuentaOrigen;
    private JLabel lblInfoOrigen;
    private JTextField txtCuentaDestino;
    private JLabel lblInfoDestino;
    private JTextField txtMontoTransf;
    private JTextField txtDescripcionTransf;
    private JTextArea txtResultadoTransf;

    // Pestaña 3: Consultar Movimiento e Historial
    private JTextField txtIdMovimiento;
    private JTextArea txtDetalleMovimiento;
    private JTextField txtFiltroCuenta;
    private JComboBox<String> cmbFiltroTipo;
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;
    private JLabel lblResumenHistorial;

    public TransaccionesForm() {
        setTitle("Banco Académico UDB - Módulo de Gestión de Transacciones");
        setSize(940, 640);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        inicializarComponentes();
        cargarHistorial("", "Todos");
    }

    private void inicializarComponentes() {
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 940, 60);
        panelHeader.setBackground(COLOR_HEADER);
        panelHeader.setLayout(null);

        JLabel lblTitulo = new JLabel("MÓDULO DE TRANSACCIONES BANCARIAS Y MOVIMIENTOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 17, 700, 26);
        panelHeader.add(lblTitulo);
        add(panelHeader);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBounds(15, 72, 895, 515);
        tabbedPane.addTab("1. Depósitos y Retiros", crearPanelDepositoRetiro());
        tabbedPane.addTab("2. Transferencias", crearPanelTransferencias());
        tabbedPane.addTab("3. Consultar Movimiento e Historial", crearPanelConsultaHistorial());
        add(tabbedPane);
    }

    private JPanel crearPanelDepositoRetiro() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(248, 250, 252));

        JLabel lblSeccion = new JLabel("Operaciones de Depósito y Retiro en Cuenta");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeccion.setForeground(COLOR_HEADER);
        lblSeccion.setBounds(25, 18, 450, 24);
        panel.add(lblSeccion);

        JLabel lblCuenta = new JLabel("Nº Cuenta (*):");
        lblCuenta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCuenta.setBounds(25, 60, 140, 26);
        panel.add(lblCuenta);

        txtCuentaOp = new JTextField();
        txtCuentaOp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCuentaOp.setBounds(175, 60, 220, 28);
        panel.add(txtCuentaOp);

        JButton btnVerificarOp = new JButton("Verificar Cuenta");
        btnVerificarOp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVerificarOp.setBounds(410, 60, 145, 28);
        btnVerificarOp.setFocusPainted(false);
        btnVerificarOp.addActionListener(e -> consultarCuentaParaEtiqueta(txtCuentaOp, lblInfoCuentaOp));
        panel.add(btnVerificarOp);

        lblInfoCuentaOp = new JLabel("Cuenta: (Ingrese un número de cuenta y presione Verificar)");
        lblInfoCuentaOp.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfoCuentaOp.setForeground(COLOR_AZUL);
        lblInfoCuentaOp.setBounds(175, 92, 650, 22);
        panel.add(lblInfoCuentaOp);

        JLabel lblTipo = new JLabel("Tipo Operación (*):");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTipo.setBounds(25, 128, 140, 26);
        panel.add(lblTipo);

        cmbTipoOperacion = new JComboBox<>(new String[]{"Depósito", "Retiro"});
        cmbTipoOperacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTipoOperacion.setBounds(175, 128, 220, 28);
        panel.add(cmbTipoOperacion);

        JLabel lblMonto = new JLabel("Monto ($):");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMonto.setBounds(25, 172, 140, 26);
        panel.add(lblMonto);

        txtMontoOp = new JTextField();
        txtMontoOp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMontoOp.setBounds(175, 172, 220, 28);
        panel.add(txtMontoOp);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesc.setBounds(25, 216, 140, 26);
        panel.add(lblDesc);

        txtDescripcionOp = new JTextField();
        txtDescripcionOp.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcionOp.setBounds(175, 216, 380, 28);
        panel.add(txtDescripcionOp);

        JButton btnProcesar = new JButton("Procesar Operación");
        btnProcesar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnProcesar.setBounds(175, 264, 185, 36);
        btnProcesar.setFocusPainted(false);
        btnProcesar.addActionListener(e -> procesarDepositoORetiro());
        panel.add(btnProcesar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLimpiar.setBounds(375, 264, 120, 36);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(e -> limpiarPanelDepositoRetiro());
        panel.add(btnLimpiar);

        JPanel panelReglas = new JPanel(null);
        panelReglas.setBackground(Color.WHITE);
        panelReglas.setBorder(BorderFactory.createTitledBorder("Reglas de Negocio Aplicadas"));
        panelReglas.setBounds(580, 125, 285, 175);
        JTextArea txtReglas = new JTextArea(
                "Depósito:\n" +
                " - La cuenta debe existir y estar Activa.\n" +
                " - El monto debe ser mayor que cero.\n" +
                " - El saldo aumenta atómicamente.\n\n" +
                "Retiro:\n" +
                " - La cuenta debe existir y estar Activa.\n" +
                " - El monto debe ser mayor que cero.\n" +
                " - El saldo disponible debe ser suficiente."
        );
        txtReglas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtReglas.setEditable(false);
        txtReglas.setBackground(Color.WHITE);
        txtReglas.setBounds(12, 22, 260, 142);
        panelReglas.add(txtReglas);
        panel.add(panelReglas);

        JLabel lblRes = new JLabel("Comprobante de Última Operación:");
        lblRes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRes.setBounds(25, 322, 300, 24);
        panel.add(lblRes);

        txtResultadoOp = crearAreaResultado();
        JScrollPane scrollRes = new JScrollPane(txtResultadoOp);
        scrollRes.setBounds(25, 350, 840, 115);
        panel.add(scrollRes);

        return panel;
    }

    private JPanel crearPanelTransferencias() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(248, 250, 252));

        JLabel lblSeccion = new JLabel("Transferencia Bancaria entre Cuentas");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblSeccion.setForeground(COLOR_HEADER);
        lblSeccion.setBounds(25, 18, 450, 24);
        panel.add(lblSeccion);

        JLabel lblOrigen = new JLabel("Nº Cuenta Origen (*):");
        lblOrigen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblOrigen.setBounds(25, 58, 155, 26);
        panel.add(lblOrigen);

        txtCuentaOrigen = new JTextField();
        txtCuentaOrigen.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCuentaOrigen.setBounds(185, 58, 210, 28);
        panel.add(txtCuentaOrigen);

        JButton btnVerificarOrigen = new JButton("Verificar Origen");
        btnVerificarOrigen.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVerificarOrigen.setBounds(410, 58, 145, 28);
        btnVerificarOrigen.setFocusPainted(false);
        btnVerificarOrigen.addActionListener(e -> consultarCuentaParaEtiqueta(txtCuentaOrigen, lblInfoOrigen));
        panel.add(btnVerificarOrigen);

        lblInfoOrigen = new JLabel("Origen: (Sin verificar)");
        lblInfoOrigen.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfoOrigen.setForeground(COLOR_AZUL);
        lblInfoOrigen.setBounds(185, 89, 660, 20);
        panel.add(lblInfoOrigen);

        JLabel lblDestino = new JLabel("Nº Cuenta Destino (*):");
        lblDestino.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDestino.setBounds(25, 118, 155, 26);
        panel.add(lblDestino);

        txtCuentaDestino = new JTextField();
        txtCuentaDestino.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCuentaDestino.setBounds(185, 118, 210, 28);
        panel.add(txtCuentaDestino);

        JButton btnVerificarDestino = new JButton("Verificar Destino");
        btnVerificarDestino.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnVerificarDestino.setBounds(410, 118, 145, 28);
        btnVerificarDestino.setFocusPainted(false);
        btnVerificarDestino.addActionListener(e -> consultarCuentaParaEtiqueta(txtCuentaDestino, lblInfoDestino));
        panel.add(btnVerificarDestino);

        lblInfoDestino = new JLabel("Destino: (Sin verificar)");
        lblInfoDestino.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfoDestino.setForeground(COLOR_AZUL);
        lblInfoDestino.setBounds(185, 149, 660, 20);
        panel.add(lblInfoDestino);

        JLabel lblMonto = new JLabel("Monto a Transferir ($):");
        lblMonto.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMonto.setBounds(25, 180, 155, 26);
        panel.add(lblMonto);

        txtMontoTransf = new JTextField();
        txtMontoTransf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMontoTransf.setBounds(185, 180, 210, 28);
        panel.add(txtMontoTransf);

        JLabel lblDesc = new JLabel("Descripción:");
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDesc.setBounds(25, 222, 155, 26);
        panel.add(lblDesc);

        txtDescripcionTransf = new JTextField();
        txtDescripcionTransf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescripcionTransf.setBounds(185, 222, 370, 28);
        panel.add(txtDescripcionTransf);

        JButton btnTransferir = new JButton("Procesar Transferencia");
        btnTransferir.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnTransferir.setBounds(185, 268, 200, 36);
        btnTransferir.setFocusPainted(false);
        btnTransferir.addActionListener(e -> procesarTransferencia());
        panel.add(btnTransferir);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnLimpiar.setBounds(400, 268, 120, 36);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.addActionListener(e -> limpiarPanelTransferencia());
        panel.add(btnLimpiar);

        JPanel panelReglas = new JPanel(null);
        panelReglas.setBackground(Color.WHITE);
        panelReglas.setBorder(BorderFactory.createTitledBorder("Reglas de Transferencia"));
        panelReglas.setBounds(580, 175, 285, 135);
        JTextArea txtReglas = new JTextArea(
                " - Origen y destino deben existir.\n" +
                " - Ambas cuentas deben estar Activas.\n" +
                " - Cuenta origen y destino distintas.\n" +
                " - Monto mayor que cero.\n" +
                " - Saldo origen suficiente (ACID)."
        );
        txtReglas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        txtReglas.setEditable(false);
        txtReglas.setBackground(Color.WHITE);
        txtReglas.setBounds(12, 22, 260, 102);
        panelReglas.add(txtReglas);
        panel.add(panelReglas);

        JLabel lblRes = new JLabel("Comprobante de Transferencia:");
        lblRes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblRes.setBounds(25, 322, 300, 24);
        panel.add(lblRes);

        txtResultadoTransf = crearAreaResultado();
        JScrollPane scrollRes = new JScrollPane(txtResultadoTransf);
        scrollRes.setBounds(25, 350, 840, 115);
        panel.add(scrollRes);

        return panel;
    }

    private JPanel crearPanelConsultaHistorial() {
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(248, 250, 252));

        // Sección superior: Consulta individual de movimiento por ID
        JPanel panelIndividual = new JPanel(null);
        panelIndividual.setBackground(Color.WHITE);
        panelIndividual.setBorder(BorderFactory.createTitledBorder("Consultar Movimiento Individual por ID"));
        panelIndividual.setBounds(15, 12, 860, 125);

        JLabel lblId = new JLabel("ID Movimiento:");
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblId.setBounds(15, 26, 110, 26);
        panelIndividual.add(lblId);

        txtIdMovimiento = new JTextField();
        txtIdMovimiento.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtIdMovimiento.setBounds(125, 26, 110, 28);
        panelIndividual.add(txtIdMovimiento);

        JButton btnConsultarId = new JButton("Consultar Movimiento");
        btnConsultarId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnConsultarId.setBounds(248, 26, 175, 28);
        btnConsultarId.setFocusPainted(false);
        btnConsultarId.addActionListener(e -> consultarMovimientoPorIdAccion());
        panelIndividual.add(btnConsultarId);

        txtDetalleMovimiento = new JTextArea("Ingrese un ID de movimiento para consultar su detalle completo.");
        txtDetalleMovimiento.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtDetalleMovimiento.setEditable(false);
        txtDetalleMovimiento.setBackground(new Color(245, 247, 250));
        JScrollPane scrollDetalle = new JScrollPane(txtDetalleMovimiento);
        scrollDetalle.setBounds(15, 62, 830, 50);
        panelIndividual.add(scrollDetalle);
        panel.add(panelIndividual);

        // Sección inferior: Filtros e historial de movimientos
        JLabel lblFiltroCuenta = new JLabel("Filtrar por Nº Cuenta:");
        lblFiltroCuenta.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFiltroCuenta.setBounds(15, 150, 135, 26);
        panel.add(lblFiltroCuenta);

        txtFiltroCuenta = new JTextField();
        txtFiltroCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtFiltroCuenta.setBounds(150, 150, 165, 28);
        panel.add(txtFiltroCuenta);

        JLabel lblFiltroTipo = new JLabel("Tipo:");
        lblFiltroTipo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblFiltroTipo.setBounds(330, 150, 40, 26);
        panel.add(lblFiltroTipo);

        cmbFiltroTipo = new JComboBox<>(new String[]{"Todos", "Deposito", "Retiro", "Transferencia"});
        cmbFiltroTipo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbFiltroTipo.setBounds(375, 150, 145, 28);
        panel.add(cmbFiltroTipo);

        JButton btnFiltrar = new JButton("Filtrar Historial");
        btnFiltrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnFiltrar.setBounds(535, 150, 150, 28);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.addActionListener(e -> aplicarFiltroHistorial());
        panel.add(btnFiltrar);

        JButton btnMostrarTodos = new JButton("Mostrar Todos");
        btnMostrarTodos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnMostrarTodos.setBounds(698, 150, 177, 28);
        btnMostrarTodos.setFocusPainted(false);
        btnMostrarTodos.addActionListener(e -> {
            txtFiltroCuenta.setText("");
            cmbFiltroTipo.setSelectedIndex(0);
            cargarHistorial("", "Todos");
        });
        panel.add(btnMostrarTodos);

        modeloHistorial = new DefaultTableModel(
                new Object[]{"ID", "Tipo", "Cuenta Origen", "Cuenta Destino", "Monto ($)", "Descripción", "Fecha"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.setRowHeight(23);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        JScrollPane scrollTabla = new JScrollPane(tablaHistorial);
        scrollTabla.setBounds(15, 190, 860, 255);
        panel.add(scrollTabla);

        lblResumenHistorial = new JLabel("Movimientos mostrados: 0");
        lblResumenHistorial.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblResumenHistorial.setBounds(15, 452, 600, 22);
        panel.add(lblResumenHistorial);

        return panel;
    }

    private JTextArea crearAreaResultado() {
        JTextArea area = new JTextArea("Esperando operación...");
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setEditable(false);
        area.setBackground(Color.WHITE);
        return area;
    }

    private void consultarCuentaParaEtiqueta(JTextField campoCuenta, JLabel etiquetaDestino) {
        String numeroCuenta = campoCuenta.getText().trim();
        try {
            ValidadorBanco.validarNumeroCuenta(numeroCuenta);
            CuentaBancaria cuenta = cuentaDAO.buscarPorNumero(numeroCuenta);
            if (cuenta == null) {
                etiquetaDestino.setForeground(COLOR_ROJO);
                etiquetaDestino.setText("Cuenta '" + numeroCuenta + "' no encontrada en la base de datos.");
                logger.warn("Verificación de cuenta fallida: '{}' no existe.", numeroCuenta);
                return;
            }
            etiquetaDestino.setForeground("Activa".equalsIgnoreCase(cuenta.getEstadoCuenta()) ? COLOR_VERDE : COLOR_ROJO);
            etiquetaDestino.setText(String.format("Titular: %s | Tipo: %s | Estado: %s | Saldo: $%.2f",
                    cuenta.getNombreCliente(), cuenta.getTipoCuenta(), cuenta.getEstadoCuenta(), cuenta.getSaldo()));
        } catch (ValidacionBancariaException ex) {
            etiquetaDestino.setForeground(COLOR_ROJO);
            etiquetaDestino.setText(ex.getMessage());
            logger.warn("Validación de número de cuenta: {}", ex.getMessage());
        } catch (SQLException ex) {
            etiquetaDestino.setForeground(COLOR_ROJO);
            etiquetaDestino.setText("Error SQL al consultar cuenta: " + ex.getMessage());
            logger.error("Error SQL al verificar cuenta '{}': {}", numeroCuenta, ex.getMessage(), ex);
        }
    }

    private void procesarDepositoORetiro() {
        String cuenta = txtCuentaOp.getText().trim();
        String tipoSeleccionado = (String) cmbTipoOperacion.getSelectedItem();
        String descripcion = txtDescripcionOp.getText().trim();

        try {
            ValidadorBanco.validarNumeroCuenta(cuenta);
            BigDecimal monto = ValidadorBanco.validarMontoPositivo(txtMontoOp.getText());

            MovimientoBancario mov;
            if ("Retiro".equalsIgnoreCase(tipoSeleccionado)) {
                mov = transaccionDAO.realizarRetiro(cuenta, monto, descripcion);
            } else {
                mov = transaccionDAO.realizarDeposito(cuenta, monto, descripcion);
            }

            CuentaBancaria actualizada = cuentaDAO.buscarPorNumero(cuenta);
            String saldoTexto = actualizada != null ? String.format("$%.2f", actualizada.getSaldo()) : "N/D";
            if (actualizada != null) {
                consultarCuentaParaEtiqueta(txtCuentaOp, lblInfoCuentaOp);
            }

            txtResultadoOp.setText(String.format(
                    "OPERACIÓN EXITOSA\n" +
                    "ID Movimiento : #%d\n" +
                    "Tipo          : %s\n" +
                    "Cuenta        : %s\n" +
                    "Monto         : $%.2f\n" +
                    "Nuevo Saldo   : %s\n" +
                    "Fecha         : %s",
                    mov.getIdMovimiento(), mov.getTipoMovimiento(), mov.getNumeroCuentaOrigen(),
                    mov.getMonto(), saldoTexto, mov.getFechaMovimiento()));

            JOptionPane.showMessageDialog(this,
                    mov.getTipoMovimiento() + " realizado exitosamente. ID Movimiento: #" + mov.getIdMovimiento(),
                    "Transacción Completada", JOptionPane.INFORMATION_MESSAGE);

            txtMontoOp.setText("");
            txtDescripcionOp.setText("");
            cargarHistorial("", "Todos");
        } catch (ValidacionBancariaException | TransaccionBancariaException ex) {
            logger.warn("Operación rechazada ({}): {}", tipoSeleccionado, ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación / Regla de Negocio", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            logger.error("Error SQL posterior a transacción: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarTransferencia() {
        String origen = txtCuentaOrigen.getText().trim();
        String destino = txtCuentaDestino.getText().trim();
        String descripcion = txtDescripcionTransf.getText().trim();

        try {
            ValidadorBanco.validarNumeroCuenta(origen);
            ValidadorBanco.validarNumeroCuenta(destino);
            BigDecimal monto = ValidadorBanco.validarMontoPositivo(txtMontoTransf.getText());

            MovimientoBancario mov = transaccionDAO.realizarTransferencia(origen, destino, monto, descripcion);
            consultarCuentaParaEtiqueta(txtCuentaOrigen, lblInfoOrigen);
            consultarCuentaParaEtiqueta(txtCuentaDestino, lblInfoDestino);

            txtResultadoTransf.setText(String.format(
                    "TRANSFERENCIA EXITOSA\n" +
                    "ID Movimiento  : #%d\n" +
                    "Cuenta Origen  : %s\n" +
                    "Cuenta Destino : %s\n" +
                    "Monto          : $%.2f\n" +
                    "Descripción    : %s\n" +
                    "Fecha          : %s",
                    mov.getIdMovimiento(), mov.getNumeroCuentaOrigen(), mov.getNumeroCuentaDestino(),
                    mov.getMonto(), mov.getDescripcion(), mov.getFechaMovimiento()));

            JOptionPane.showMessageDialog(this,
                    "Transferencia realizada con éxito. ID Movimiento: #" + mov.getIdMovimiento(),
                    "Transferencia Completada", JOptionPane.INFORMATION_MESSAGE);

            txtMontoTransf.setText("");
            txtDescripcionTransf.setText("");
            cargarHistorial("", "Todos");
        } catch (ValidacionBancariaException | TransaccionBancariaException ex) {
            logger.warn("Transferencia rechazada: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación / Regla de Negocio", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void consultarMovimientoPorIdAccion() {
        try {
            int id = ValidadorBanco.validarIdMovimiento(txtIdMovimiento.getText());
            MovimientoBancario mov = transaccionDAO.consultarMovimientoPorId(id);
            if (mov == null) {
                txtDetalleMovimiento.setText("No existe ningún movimiento con el ID #" + id + ".");
                JOptionPane.showMessageDialog(this,
                        "No se encontró el movimiento con ID #" + id + ".",
                        "Movimiento No Encontrado", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            txtDetalleMovimiento.setText(String.format(
                    "ID: #%d | Tipo: %s | Origen: %s | Destino: %s | Monto: $%.2f | Fecha: %s\nDescripción: %s",
                    mov.getIdMovimiento(), mov.getTipoMovimiento(),
                    mov.getNumeroCuentaOrigen(), mov.getNumeroCuentaDestino(),
                    mov.getMonto(), mov.getFechaMovimiento(), mov.getDescripcion()));
        } catch (ValidacionBancariaException | TransaccionBancariaException ex) {
            logger.warn("Error al consultar movimiento por ID: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Consulta de Movimiento", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroHistorial() {
        String cuenta = txtFiltroCuenta.getText().trim();
        String tipo = (String) cmbFiltroTipo.getSelectedItem();
        if (!cuenta.isEmpty()) {
            try {
                ValidadorBanco.validarNumeroCuenta(cuenta);
            } catch (ValidacionBancariaException ex) {
                logger.warn("Filtro de historial inválido: {}", ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        cargarHistorial(cuenta, tipo);
    }

    public void cargarHistorial(String numeroCuenta, String tipo) {
        modeloHistorial.setRowCount(0);
        try {
            List<MovimientoBancario> lista = transaccionDAO.listarHistorialMovimientos(numeroCuenta, tipo);
            for (MovimientoBancario m : lista) {
                modeloHistorial.addRow(new Object[]{
                        m.getIdMovimiento(),
                        m.getTipoMovimiento(),
                        m.getNumeroCuentaOrigen(),
                        m.getNumeroCuentaDestino(),
                        String.format("%.2f", m.getMonto()),
                        m.getDescripcion(),
                        m.getFechaMovimiento()
                });
            }
            lblResumenHistorial.setText("Movimientos mostrados: " + lista.size());
        } catch (TransaccionBancariaException ex) {
            logger.error("No se pudo cargar el historial de movimientos: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al Cargar Historial", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarPanelDepositoRetiro() {
        txtCuentaOp.setText("");
        txtMontoOp.setText("");
        txtDescripcionOp.setText("");
        lblInfoCuentaOp.setForeground(COLOR_AZUL);
        lblInfoCuentaOp.setText("Cuenta: (Ingrese un número de cuenta y presione Verificar)");
        txtResultadoOp.setText("Esperando operación...");
    }

    private void limpiarPanelTransferencia() {
        txtCuentaOrigen.setText("");
        txtCuentaDestino.setText("");
        txtMontoTransf.setText("");
        txtDescripcionTransf.setText("");
        lblInfoOrigen.setForeground(COLOR_AZUL);
        lblInfoOrigen.setText("Origen: (Sin verificar)");
        lblInfoDestino.setForeground(COLOR_AZUL);
        lblInfoDestino.setText("Destino: (Sin verificar)");
        txtResultadoTransf.setText("Esperando operación...");
    }

    public void seleccionarPestana(int indice) {
        if (indice >= 0 && indice < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(indice);
        }
    }
}
