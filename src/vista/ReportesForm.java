package vista;

import dao.CuentaBancariaDAO;
import dao.ReporteDAO;
import excepciones.ValidacionBancariaException;
import excepciones.ValidadorBanco;
import modelo.Cliente;
import modelo.CuentaBancaria;
import modelo.ReporteMovimiento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ReportesForm extends JFrame {

    private static final Logger logger = LogManager.getLogger(ReportesForm.class);

    private JComboBox<String> cmbTipoReporte;
    private JTextField txtNumeroCuenta;
    private JButton btnGenerarReporte;
    private JButton btnActualizarTotales;

    private JLabel lblTotalDepositado;
    private JLabel lblTotalRetirado;
    private JLabel lblTotalTransferido;
    private JLabel lblResumenRegistros;

    private JTable tablaReportes;
    private DefaultTableModel modeloTabla;

    private final ReporteDAO reporteDAO = new ReporteDAO();
    private final CuentaBancariaDAO cuentaDAO = new CuentaBancariaDAO();

    public ReportesForm() {
        setTitle("Banco Académico UDB - Módulo de Consultas y Reportes");
        setSize(960, 610);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        inicializarComponentes();
        cargarTotalesGenerales();
        generarReporteSeleccionado();
    }

    private void inicializarComponentes() {
        // Encabezado
        JPanel panelHeader = new JPanel();
        panelHeader.setBounds(0, 0, 960, 60);
        panelHeader.setBackground(new Color(24, 43, 73));
        panelHeader.setLayout(null);

        JLabel lblTitulo = new JLabel("MÓDULO DE GESTIÓN DE REPORTES Y CONSULTAS BANCARIAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBounds(20, 17, 650, 25);
        panelHeader.add(lblTitulo);
        add(panelHeader);

        // --- TARJETAS DE TOTALES (Total Depositado, Total Retirado, Total Transferido) ---
        lblTotalDepositado = crearTarjetaTotal("TOTAL DEPOSITADO", "$ 0.00", new Color(25, 111, 61), 20, 75);
        lblTotalRetirado = crearTarjetaTotal("TOTAL RETIRADO", "$ 0.00", new Color(169, 50, 38), 260, 75);
        lblTotalTransferido = crearTarjetaTotal("TOTAL TRANSFERIDO", "$ 0.00", new Color(31, 97, 141), 500, 75);

        btnActualizarTotales = new JButton("Actualizar Totales");
        btnActualizarTotales.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnActualizarTotales.setBounds(740, 88, 190, 42);
        btnActualizarTotales.setFocusPainted(false);
        btnActualizarTotales.addActionListener(e -> cargarTotalesGenerales());
        add(btnActualizarTotales);

        // --- PANEL DE FILTROS DE REPORTES ---
        JLabel lblSeleccione = new JLabel("Seleccione el Reporte:");
        lblSeleccione.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSeleccione.setBounds(20, 165, 160, 25);
        add(lblSeleccione);

        cmbTipoReporte = new JComboBox<>(new String[]{
                "1. Cuentas Activas",
                "2. Cuentas Bloqueadas",
                "3. Clientes Registrados",
                "4. Historial General de Transacciones",
                "5. Transacciones por Cuenta"
        });
        cmbTipoReporte.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTipoReporte.setBounds(175, 165, 260, 28);
        cmbTipoReporte.addActionListener(e -> actualizarEstadoFiltroCuenta());
        add(cmbTipoReporte);

        JLabel lblCuenta = new JLabel("Nº Cuenta:");
        lblCuenta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCuenta.setBounds(455, 165, 80, 25);
        add(lblCuenta);

        txtNumeroCuenta = new JTextField();
        txtNumeroCuenta.setBounds(535, 165, 185, 28);
        txtNumeroCuenta.setEnabled(false);
        txtNumeroCuenta.setToolTipText("Ingrese el número de cuenta (6 a 20 dígitos)");
        add(txtNumeroCuenta);

        btnGenerarReporte = new JButton("Generar Reporte");
        btnGenerarReporte.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGenerarReporte.setBounds(740, 163, 190, 32);
        btnGenerarReporte.setFocusPainted(false);
        btnGenerarReporte.addActionListener(e -> generarReporteSeleccionado());
        add(btnGenerarReporte);

        // --- TABLA DE RESULTADOS ---
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaReportes = new JTable(modeloTabla);
        tablaReportes.setRowHeight(24);
        tablaReportes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scroll = new JScrollPane(tablaReportes);
        scroll.setBounds(20, 210, 910, 320);
        add(scroll);

        // Pie de resumen
        lblResumenRegistros = new JLabel("Registros encontrados: 0");
        lblResumenRegistros.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblResumenRegistros.setBounds(20, 540, 500, 22);
        add(lblResumenRegistros);
    }

    private JLabel crearTarjetaTotal(String titulo, String valorInicial, Color colorBorde, int x, int y) {
        JPanel tarjeta = new JPanel();
        tarjeta.setBounds(x, y, 225, 68);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createMatteBorder(0, 5, 0, 0, colorBorde));
        tarjeta.setLayout(null);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblTit.setForeground(Color.DARK_GRAY);
        lblTit.setBounds(12, 8, 200, 18);
        tarjeta.add(lblTit);

        JLabel lblVal = new JLabel(valorInicial);
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblVal.setForeground(colorBorde);
        lblVal.setBounds(12, 30, 200, 28);
        tarjeta.add(lblVal);

        add(tarjeta);
        return lblVal;
    }

    private void actualizarEstadoFiltroCuenta() {
        boolean requiereCuenta = cmbTipoReporte.getSelectedIndex() == 4;
        txtNumeroCuenta.setEnabled(requiereCuenta);
        if (requiereCuenta) {
            txtNumeroCuenta.requestFocus();
        } else {
            txtNumeroCuenta.setText("");
        }
    }

    private void cargarTotalesGenerales() {
        try {
            double totalDep = reporteDAO.obtenerTotalPorTipoMovimiento("DEPOSITO");
            double totalRet = reporteDAO.obtenerTotalPorTipoMovimiento("RETIRO");
            double totalTrans = reporteDAO.obtenerTotalPorTipoMovimiento("TRANSFERENCIA");

            lblTotalDepositado.setText(String.format("$ %.2f", totalDep));
            lblTotalRetirado.setText(String.format("$ %.2f", totalRet));
            lblTotalTransferido.setText(String.format("$ %.2f", totalTrans));
        } catch (SQLException ex) {
            logger.error("Error al actualizar tarjetas de totales: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "No se pudieron calcular los totales de movimientos.\nDetalle: " + ex.getMessage(),
                    "Error SQL en Totales", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarReporteSeleccionado() {
        int opcion = cmbTipoReporte.getSelectedIndex();

        try {
            switch (opcion) {
                case 0: // Cuentas Activas
                    mostrarReporteCuentas("Activa");
                    break;
                case 1: // Cuentas Bloqueadas
                    mostrarReporteCuentas("Bloqueada");
                    break;
                case 2: // Clientes Registrados
                    mostrarReporteClientes();
                    break;
                case 3: // Historial General de Transacciones
                    mostrarReporteHistorialGeneral();
                    break;
                case 4: // Transacciones por Cuenta
                    mostrarReportePorCuenta();
                    break;
            }
        } catch (ValidacionBancariaException ex) {
            logger.warn("Validación en reportes: {}", ex.getMessage());
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            logger.error("Error SQL al generar reporte: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this,
                    "Error al consultar la base de datos: " + ex.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarReporteCuentas(String estado) throws SQLException {
        String[] columnas = {"ID", "Nº Cuenta", "Cód. Cliente", "Titular", "Tipo Cuenta", "Saldo ($)", "Estado"};
        modeloTabla.setDataVector(new Object[0][0], columnas);

        List<CuentaBancaria> cuentas = reporteDAO.listarCuentasPorEstado(estado);
        for (CuentaBancaria c : cuentas) {
            modeloTabla.addRow(new Object[]{
                    c.getIdCuenta(),
                    c.getNumeroCuenta(),
                    c.getCodigoCliente(),
                    c.getNombreCliente(),
                    c.getTipoCuenta(),
                    String.format("%.2f", c.getSaldo()),
                    c.getEstadoCuenta()
            });
        }
        lblResumenRegistros.setText("Cuentas en estado '" + estado + "': " + cuentas.size());
    }

    private void mostrarReporteClientes() throws SQLException {
        String[] columnas = {"ID", "Código", "DUI", "Nombre Completo", "Teléfono", "Correo", "Dirección", "Estado"};
        modeloTabla.setDataVector(new Object[0][0], columnas);

        List<Cliente> clientes = reporteDAO.listarClientesRegistrados();
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                    c.getIdCliente(),
                    c.getCodigoCliente(),
                    c.getDui(),
                    c.getNombreCompleto(),
                    c.getTelefono(),
                    c.getCorreoElectronico(),
                    c.getDireccion(),
                    c.getEstado()
            });
        }
        lblResumenRegistros.setText("Total de clientes registrados: " + clientes.size());
    }

    private void mostrarReporteHistorialGeneral() throws SQLException {
        String[] columnas = {"ID Mov.", "Tipo", "Cuenta Origen", "Cuenta Destino", "Monto ($)", "Descripción", "Fecha"};
        modeloTabla.setDataVector(new Object[0][0], columnas);

        List<ReporteMovimiento> movimientos = reporteDAO.listarHistorialTransacciones();
        for (ReporteMovimiento m : movimientos) {
            modeloTabla.addRow(new Object[]{
                    m.getIdMovimiento(),
                    m.getTipoMovimiento(),
                    m.getNumeroCuentaOrigen(),
                    m.getNumeroCuentaDestino(),
                    String.format("%.2f", m.getMonto()),
                    m.getDescripcion(),
                    m.getFechaMovimiento()
            });
        }
        lblResumenRegistros.setText("Total de transacciones registradas: " + movimientos.size());
    }

    private void mostrarReportePorCuenta() throws ValidacionBancariaException, SQLException {
        String numCuenta = txtNumeroCuenta.getText().trim();
        ValidadorBanco.validarNumeroCuenta(numCuenta);

        CuentaBancaria cuenta = cuentaDAO.buscarPorNumero(numCuenta);
        if (cuenta == null) {
            throw new ValidacionBancariaException("La cuenta '" + numCuenta + "' no existe en la base de datos.");
        }

        String[] columnas = {"ID Mov.", "Tipo", "Cuenta Origen", "Cuenta Destino", "Monto ($)", "Descripción", "Fecha"};
        modeloTabla.setDataVector(new Object[0][0], columnas);

        List<ReporteMovimiento> movimientos = reporteDAO.listarTransaccionesPorCuenta(numCuenta);
        for (ReporteMovimiento m : movimientos) {
            modeloTabla.addRow(new Object[]{
                    m.getIdMovimiento(),
                    m.getTipoMovimiento(),
                    m.getNumeroCuentaOrigen(),
                    m.getNumeroCuentaDestino(),
                    String.format("%.2f", m.getMonto()),
                    m.getDescripcion(),
                    m.getFechaMovimiento()
            });
        }
        lblResumenRegistros.setText("Movimientos de la cuenta " + numCuenta + " (Titular: " + cuenta.getNombreCliente() + "): " + movimientos.size());
    }
}