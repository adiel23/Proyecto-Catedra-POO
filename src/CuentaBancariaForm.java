import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CuentaBancariaForm extends JFrame {
    private JTextField txtNumeroCuenta, txtCodigoCliente, txtSaldo;
    private JComboBox<String> cmbTipoCuenta, cmbEstado;
    private JTable tablaCuentas;
    private DefaultTableModel modeloTabla;

    public CuentaBancariaForm() {
        setTitle("Módulo de Cuentas Bancarias - Integrante 2");
        setTitle("Módulo de Cuentas Bancarias - Banco Académico UDB");
        setSize(750, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // --- COMPONENTES DE LA INTERFAZ GRÁFICA ---
        JLabel lblNum = new JLabel("Nº Cuenta:");
        lblNum.setBounds(20, 20, 100, 25);
        add(lblNum);
        txtNumeroCuenta = new JTextField();
        txtNumeroCuenta.setBounds(130, 20, 160, 25);
        add(txtNumeroCuenta);

        JLabel lblCli = new JLabel("Cód. Cliente:");
        lblCli.setBounds(20, 60, 100, 25);
        add(lblCli);
        txtCodigoCliente = new JTextField();
        txtCodigoCliente.setBounds(130, 60, 160, 25);
        add(txtCodigoCliente);

        JLabel lblTipo = new JLabel("Tipo Cuenta:");
        lblTipo.setBounds(20, 100, 100, 25);
        add(lblTipo);
        cmbTipoCuenta = new JComboBox<>(new String[]{"Ahorros", "Corriente"});
        cmbTipoCuenta.setBounds(130, 100, 160, 25);
        add(cmbTipoCuenta);

        JLabel lblSaldo = new JLabel("Saldo Inicial ($):");
        lblSaldo.setBounds(20, 140, 100, 25);
        add(lblSaldo);
        txtSaldo = new JTextField();
        txtSaldo.setBounds(130, 140, 160, 25);
        add(txtSaldo);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(20, 180, 100, 25);
        add(lblEstado);
        cmbEstado = new JComboBox<>(new String[]{"Activa", "Inactiva", "Bloqueada"});
        cmbEstado.setBounds(130, 180, 160, 25);
        add(cmbEstado);

        // Botones
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(20, 230, 120, 30);
        add(btnRegistrar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(150, 230, 100, 30);
        add(btnLimpiar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(20, 270, 120, 30);
        add(btnBuscar);

        // Tabla visual de control
        tablaCuentas = new JTable();
        modeloTabla = new DefaultTableModel(new Object[]{"Nº Cuenta", "Cód. Cliente", "Tipo", "Saldo", "Estado"}, 0);
        tablaCuentas.setModel(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaCuentas);
        scroll.setBounds(310, 20, 400, 370);
        add(scroll);

        // ==========================================
        //  CARGAR DATOS EN LA TABLA
        // Aquí se llamará al método DAO para llenar la JTable
        // con las cuentas que ya estén guardadas en MySQL al iniciar la ventana.
        // Ejemplo: cargarCuentasDesdeBD();
        // ==========================================

        // Eventos de Botones
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validarYRegistrar();
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarCuentaAccion();
            }
        });
    }

    private void validarYRegistrar() {
        String numCuenta = txtNumeroCuenta.getText().trim();
        String codCliente = txtCodigoCliente.getText().trim();
        String tipo = cmbTipoCuenta.getSelectedItem().toString();
        String estado = cmbEstado.getSelectedItem().toString();
        String txtSal = txtSaldo.getText().trim();

        // 1. Validación de campos obligatorios
        if (numCuenta.isEmpty() || codCliente.isEmpty() || txtSal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validación de saldo inicial (numérico y mayor a cero)
        double saldo;
        try {
            saldo = Double.parseDouble(txtSal);
            if (saldo <= 0) {
                JOptionPane.showMessageDialog(this, "El saldo inicial debe ser mayor que cero.", "Error de validación", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El saldo debe ser un número válido.", "Formato incorrecto", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // =====================================================================================
        // INTEGRANTE 4 e INTEGRANTE 1: VALIDAR EXISTENCIA DEL CLIENTE Y GUARDAR EN BD
        // Antes de registrar, se debe consultar si el 'codCliente' existe en la base de datos.
        // Ejemplo:
        // 
        //   boolean clienteExiste = suClaseDAOCliente.existe(codCliente);
        //   if (!clienteExiste) {
        //       JOptionPane.showMessageDialog(this, "El código de cliente no existe en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
        //       return;
        //   }
        //
        //   CuentaBancaria nuevaCuenta = new CuentaBancaria(numCuenta, codCliente, tipo, saldo, estado);
        //   boolean exito = suClaseDAOCuenta.registrar(nuevaCuenta);
        //   if (exito) { ... }
        // =====================================================================================

        // Simulación temporal en interfaz (mientras se integra la BD):
        modeloTabla.addRow(new Object[]{numCuenta, codCliente, tipo, saldo, estado});
        JOptionPane.showMessageDialog(this, "Cuenta registrada exitosamente (Modo Local).", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiarCampos();
    }

    private void buscarCuentaAccion() {
        String numCuentaBuscada = txtNumeroCuenta.getText().trim();
        if (numCuentaBuscada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el número de cuenta en el campo correspondiente para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // =====================================================================================
        // INTEGRANTE 4: PROGRAMAR LA CONSULTA ESPECIFICA
        // Aquí se invocara el método de búsqueda:
        //   CuentaBancaria c = suClaseDAOCuenta.buscarPorNumero(numCuentaBuscada);
        //   if (c != null) { ... mostrar datos en pantalla ... }
        // =====================================================================================

        JOptionPane.showMessageDialog(this, "Función de búsqueda lista para conectarse al DAO del Integrante 4.", "Consulta", JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarCampos() {
        txtNumeroCuenta.setText("");
        txtCodigoCliente.setText("");
        txtSaldo.setText("");
        cmbTipoCuenta.setSelectedIndex(0);
        cmbEstado.setSelectedIndex(0);
        txtNumeroCuenta.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CuentaBancariaForm().setVisible(true));
    }
}