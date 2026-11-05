package vista;

import dao.ClienteDAO;
import dao.CuentaBancariaDAO;
import excepciones.ValidacionBancariaException;
import excepciones.ValidadorBanco;
import modelo.Cliente;
import modelo.CuentaBancaria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class CuentaBancariaForm extends JFrame {

    private JTextField txtNumeroCuenta;
    private JTextField txtCodigoCliente;
    private JLabel lblNombreClienteDetectado;
    private JTextField txtSaldo;
    private JComboBox<String> cmbTipoCuenta;
    private JComboBox<String> cmbEstado;
    private JTable tablaCuentas;
    private DefaultTableModel modeloTabla;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnLimpiar;
    private JButton btnBuscar;
    private JButton btnVerificarCliente;

    private final CuentaBancariaDAO cuentaDAO = new CuentaBancariaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private int idCuentaSeleccionada = 0;

    public CuentaBancariaForm() {
        setTitle("Banco Académico UDB - Gestión de Cuentas Bancarias");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        inicializarComponentes();
        cargarCuentasEnTabla();
    }

    private void inicializarComponentes() {
        // Título del formulario
        JLabel lblTitulo = new JLabel("MÓDULO DE GESTIÓN DE CUENTAS BANCARIAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(20, 15, 450, 25);
        add(lblTitulo);

        // Número de Cuenta
        JLabel lblNum = new JLabel("Nº Cuenta (*):");
        lblNum.setBounds(20, 55, 110, 25);
        add(lblNum);
        txtNumeroCuenta = new JTextField();
        txtNumeroCuenta.setBounds(140, 55, 150, 25);
        add(txtNumeroCuenta);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(300, 55, 80, 25);
        btnBuscar.addActionListener(e -> buscarCuentaPorNumero());
        add(btnBuscar);

        // Código de Cliente
        JLabel lblCli = new JLabel("Cód. Cliente (*):");
        lblCli.setBounds(20, 95, 110, 25);
        add(lblCli);
        txtCodigoCliente = new JTextField();
        txtCodigoCliente.setBounds(140, 95, 150, 25);
        add(txtCodigoCliente);

        btnVerificarCliente = new JButton("Validar");
        btnVerificarCliente.setBounds(300, 95, 80, 25);
        btnVerificarCliente.addActionListener(e -> verificarCliente());
        add(btnVerificarCliente);

        lblNombreClienteDetectado = new JLabel("Cliente: (Ingrese código y valide)");
        lblNombreClienteDetectado.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNombreClienteDetectado.setForeground(new Color(60, 90, 150));
        lblNombreClienteDetectado.setBounds(140, 122, 240, 20);
        add(lblNombreClienteDetectado);

        // Tipo de Cuenta
        JLabel lblTipo = new JLabel("Tipo Cuenta (*):");
        lblTipo.setBounds(20, 150, 110, 25);
        add(lblTipo);
        cmbTipoCuenta = new JComboBox<>(new String[]{"Ahorro", "Corriente"});
        cmbTipoCuenta.setBounds(140, 150, 150, 25);
        add(cmbTipoCuenta);

        // Saldo Inicial
        JLabel lblSaldo = new JLabel("Saldo Inicial ($):");
        lblSaldo.setBounds(20, 190, 110, 25);
        add(lblSaldo);
        txtSaldo = new JTextField();
        txtSaldo.setBounds(140, 190, 150, 25);
        add(txtSaldo);

        // Estado
        JLabel lblEstado = new JLabel("Estado (*):");
        lblEstado.setBounds(20, 230, 110, 25);
        add(lblEstado);
        cmbEstado = new JComboBox<>(new String[]{"Activa", "Inactiva", "Bloqueada"});
        cmbEstado.setBounds(140, 230, 150, 25);
        add(cmbEstado);

        // Botones de acción
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(20, 280, 110, 32);
        btnRegistrar.addActionListener(e -> registrarCuenta());
        add(btnRegistrar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(140, 280, 110, 32);
        btnActualizar.addActionListener(e -> actualizarCuenta());
        add(btnActualizar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(260, 280, 110, 32);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        add(btnLimpiar);

        JButton btnRefrescar = new JButton("Refrescar Tabla");
        btnRefrescar.setBounds(20, 325, 350, 30);
        btnRefrescar.addActionListener(e -> cargarCuentasEnTabla());
        add(btnRefrescar);

        // Tabla de datos
        String[] columnas = {"ID", "Nº Cuenta", "Cód. Cliente", "Titular", "Tipo", "Saldo ($)", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCuentas = new JTable(modeloTabla);
        tablaCuentas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCuentas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarFilaSeleccionada();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaCuentas);
        scroll.setBounds(395, 45, 425, 415);
        add(scroll);
    }

    private Cliente verificarCliente() {
        String cod = txtCodigoCliente.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el código de cliente a validar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            Cliente c = clienteDAO.buscarPorCodigo(cod);
            if (c != null) {
                lblNombreClienteDetectado.setText("Titular: " + c.getNombreCompleto());
                return c;
            } else {
                lblNombreClienteDetectado.setText("Cliente no encontrado en BD");
                JOptionPane.showMessageDialog(this, "No existe ningún cliente registrado con el código '" + cod + "'.\nDebe registrar primero al cliente en el Módulo de Clientes.", "Cliente No Encontrado", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error BD", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void registrarCuenta() {
        try {
            String num = txtNumeroCuenta.getText().trim();
            ValidadorBanco.validarNumeroCuenta(num);

            Cliente cliente = verificarCliente();
            if (cliente == null) return;

            String saldoTxt = txtSaldo.getText().trim();
            ValidadorBanco.validarTextoObligatorio(saldoTxt, "Saldo Inicial");
            double saldo = Double.parseDouble(saldoTxt);
            ValidadorBanco.validarSaldoInicial(saldo);

            String tipo = cmbTipoCuenta.getSelectedItem().toString();
            String estado = cmbEstado.getSelectedItem().toString();

            CuentaBancaria cuenta = new CuentaBancaria(num, tipo, saldo, estado, cliente.getIdCliente());

            boolean exito = cuentaDAO.insertar(cuenta);
            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Cuenta bancaria registrada con éxito en MySQL!", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarCuentasEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El saldo debe ser una cantidad numérica válida.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (ValidacionBancariaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación de Reglas", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error en Base de Datos: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCuenta() {
        if (idCuentaSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una cuenta de la tabla para actualizarla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String num = txtNumeroCuenta.getText().trim();
            ValidadorBanco.validarNumeroCuenta(num);

            String saldoTxt = txtSaldo.getText().trim();
            ValidadorBanco.validarTextoObligatorio(saldoTxt, "Saldo");
            double saldo = Double.parseDouble(saldoTxt);
            ValidadorBanco.validarSaldoInicial(saldo);

            String tipo = cmbTipoCuenta.getSelectedItem().toString();
            String estado = cmbEstado.getSelectedItem().toString();

            CuentaBancaria cuenta = new CuentaBancaria();
            cuenta.setIdCuenta(idCuentaSeleccionada);
            cuenta.setNumeroCuenta(num);
            cuenta.setTipoCuenta(tipo);
            cuenta.setSaldo(saldo);
            cuenta.setEstadoCuenta(estado);

            boolean exito = cuentaDAO.actualizar(cuenta);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Cuenta actualizada correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarCuentasEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la cuenta bancaria.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El saldo debe ser numérico.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        } catch (ValidacionBancariaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarCuentaPorNumero() {
        String num = txtNumeroCuenta.getText().trim();
        if (num.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escriba un número de cuenta para buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            CuentaBancaria c = cuentaDAO.buscarPorNumero(num);
            if (c != null) {
                idCuentaSeleccionada = c.getIdCuenta();
                txtNumeroCuenta.setText(c.getNumeroCuenta());
                txtCodigoCliente.setText(c.getCodigoCliente());
                lblNombreClienteDetectado.setText("Titular: " + c.getNombreCliente());
                cmbTipoCuenta.setSelectedItem(c.getTipoCuenta());
                txtSaldo.setText(String.valueOf(c.getSaldo()));
                cmbEstado.setSelectedItem(c.getEstadoCuenta());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna cuenta con el número: " + num, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = tablaCuentas.getSelectedRow();
        if (fila >= 0) {
            idCuentaSeleccionada = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            txtNumeroCuenta.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtCodigoCliente.setText(modeloTabla.getValueAt(fila, 2).toString());
            lblNombreClienteDetectado.setText("Titular: " + modeloTabla.getValueAt(fila, 3).toString());
            cmbTipoCuenta.setSelectedItem(modeloTabla.getValueAt(fila, 4).toString());
            txtSaldo.setText(modeloTabla.getValueAt(fila, 5).toString());
            cmbEstado.setSelectedItem(modeloTabla.getValueAt(fila, 6).toString());
        }
    }

    private void cargarCuentasEnTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<CuentaBancaria> lista = cuentaDAO.listarTodas();
            for (CuentaBancaria c : lista) {
                modeloTabla.addRow(new Object[]{
                    c.getIdCuenta(),
                    c.getNumeroCuenta(),
                    c.getCodigoCliente(),
                    c.getNombreCliente(),
                    c.getTipoCuenta(),
                    c.getSaldo(),
                    c.getEstadoCuenta()
                });
            }
        } catch (SQLException e) {
            System.err.println("Aviso al listar cuentas: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        idCuentaSeleccionada = 0;
        txtNumeroCuenta.setText("");
        txtCodigoCliente.setText("");
        lblNombreClienteDetectado.setText("Cliente: (Ingrese código y valide)");
        txtSaldo.setText("");
        cmbTipoCuenta.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        tablaCuentas.clearSelection();
        txtNumeroCuenta.requestFocus();
    }
}
