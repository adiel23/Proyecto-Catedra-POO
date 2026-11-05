package vista;

import dao.ClienteDAO;
import excepciones.ValidacionBancariaException;
import excepciones.ValidadorBanco;
import modelo.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class ClienteForm extends JFrame {

    private JTextField txtCodigoCliente;
    private JTextField txtDui;
    private JTextField txtNombreCompleto;
    private JTextField txtTelefono;
    private JTextField txtCorreo;
    private JTextField txtDireccion;
    private JComboBox<String> cmbEstado;

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;

    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnBuscar;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private int idClienteSeleccionado = 0;

    public ClienteForm() {
        setTitle("Banco Académico UDB - Gestión de Clientes");
        setSize(920, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        inicializarComponentes();
        cargarClientesEnTabla();
    }

    private void inicializarComponentes() {
        JLabel lblTitulo = new JLabel("MÓDULO DE GESTIÓN DE CLIENTES (CRUD)");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(20, 15, 400, 25);
        add(lblTitulo);

        // Código Cliente
        JLabel lblCod = new JLabel("Cód. Cliente (*):");
        lblCod.setBounds(20, 55, 110, 25);
        add(lblCod);
        txtCodigoCliente = new JTextField();
        txtCodigoCliente.setBounds(140, 55, 140, 25);
        add(txtCodigoCliente);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(290, 55, 80, 25);
        btnBuscar.addActionListener(e -> buscarClientePorCodigo());
        add(btnBuscar);

        // DUI
        JLabel lblDui = new JLabel("DUI (*):");
        lblDui.setBounds(20, 95, 110, 25);
        add(lblDui);
        txtDui = new JTextField();
        txtDui.setToolTipText("Formato: 00000000-0");
        txtDui.setBounds(140, 95, 140, 25);
        add(txtDui);

        JLabel lblDuiEjemplo = new JLabel("Ej: 01234567-8");
        lblDuiEjemplo.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDuiEjemplo.setForeground(Color.GRAY);
        lblDuiEjemplo.setBounds(290, 95, 90, 25);
        add(lblDuiEjemplo);

        // Nombre Completo
        JLabel lblNom = new JLabel("Nombre Completo (*):");
        lblNom.setBounds(20, 135, 125, 25);
        add(lblNom);
        txtNombreCompleto = new JTextField();
        txtNombreCompleto.setBounds(140, 135, 230, 25);
        add(txtNombreCompleto);

        // Teléfono
        JLabel lblTel = new JLabel("Teléfono:");
        lblTel.setBounds(20, 175, 110, 25);
        add(lblTel);
        txtTelefono = new JTextField();
        txtTelefono.setBounds(140, 175, 230, 25);
        add(txtTelefono);

        // Correo Electrónico
        JLabel lblCor = new JLabel("Correo Electrónico:");
        lblCor.setBounds(20, 215, 120, 25);
        add(lblCor);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(140, 215, 230, 25);
        add(txtCorreo);

        // Dirección
        JLabel lblDir = new JLabel("Dirección:");
        lblDir.setBounds(20, 255, 110, 25);
        add(lblDir);
        txtDireccion = new JTextField();
        txtDireccion.setBounds(140, 255, 230, 25);
        add(txtDireccion);

        // Estado
        JLabel lblEst = new JLabel("Estado (*):");
        lblEst.setBounds(20, 295, 110, 25);
        add(lblEst);
        cmbEstado = new JComboBox<>(new String[]{"Activo", "Inactivo", "Bloqueado"});
        cmbEstado.setBounds(140, 295, 230, 25);
        add(cmbEstado);

        // Botones de acción
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(20, 345, 105, 32);
        btnRegistrar.addActionListener(e -> registrarCliente());
        add(btnRegistrar);

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(135, 345, 105, 32);
        btnActualizar.addActionListener(e -> actualizarCliente());
        add(btnActualizar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(250, 345, 105, 32);
        btnEliminar.addActionListener(e -> eliminarCliente());
        add(btnEliminar);

        btnLimpiar = new JButton("Limpiar Campos");
        btnLimpiar.setBounds(20, 390, 160, 30);
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        add(btnLimpiar);

        JButton btnRefrescar = new JButton("Refrescar Tabla");
        btnRefrescar.setBounds(195, 390, 160, 30);
        btnRefrescar.addActionListener(e -> cargarClientesEnTabla());
        add(btnRefrescar);

        // Tabla de Clientes
        String[] columnas = {"ID", "Código", "DUI", "Nombre Completo", "Teléfono", "Correo", "Dirección", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarFilaSeleccionada();
            }
        });

        JScrollPane scroll = new JScrollPane(tablaClientes);
        scroll.setBounds(390, 45, 500, 450);
        add(scroll);
    }

    private void registrarCliente() {
        try {
            String cod = txtCodigoCliente.getText().trim();
            ValidadorBanco.validarTextoObligatorio(cod, "Código de Cliente");

            String dui = txtDui.getText().trim();
            ValidadorBanco.validarDui(dui);

            String nom = txtNombreCompleto.getText().trim();
            ValidadorBanco.validarTextoObligatorio(nom, "Nombre Completo");

            String tel = txtTelefono.getText().trim();
            ValidadorBanco.validarTelefono(tel);

            String cor = txtCorreo.getText().trim();
            ValidadorBanco.validarCorreo(cor);

            String dir = txtDireccion.getText().trim();
            String est = cmbEstado.getSelectedItem().toString();

            Cliente cliente = new Cliente(cod, dui, nom, tel, cor, dir, est);

            boolean exito = clienteDAO.insertar(cliente);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente en MySQL.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarClientesEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ValidacionBancariaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación de Reglas", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCliente() {
        if (idClienteSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente de la tabla o búsquelo primero.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String cod = txtCodigoCliente.getText().trim();
            ValidadorBanco.validarTextoObligatorio(cod, "Código de Cliente");

            String dui = txtDui.getText().trim();
            ValidadorBanco.validarDui(dui);

            String nom = txtNombreCompleto.getText().trim();
            ValidadorBanco.validarTextoObligatorio(nom, "Nombre Completo");

            String tel = txtTelefono.getText().trim();
            ValidadorBanco.validarTelefono(tel);

            String cor = txtCorreo.getText().trim();
            ValidadorBanco.validarCorreo(cor);

            String dir = txtDireccion.getText().trim();
            String est = cmbEstado.getSelectedItem().toString();

            Cliente cliente = new Cliente(idClienteSeleccionado, cod, dui, nom, tel, cor, dir, est);

            boolean exito = clienteDAO.actualizar(cliente);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.", "Actualizado", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarClientesEnTabla();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (ValidacionBancariaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        if (idClienteSeleccionado == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione el cliente que desea eliminar de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de eliminar este cliente?\nNota: Si tiene cuentas bancarias asociadas, la base de datos protegerá la integridad referencial.",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                boolean exito = clienteDAO.eliminar(idClienteSeleccionado);
                if (exito) {
                    JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente.", "Eliminado", JOptionPane.INFORMATION_MESSAGE);
                    limpiarFormulario();
                    cargarClientesEnTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "No se puede eliminar el cliente porque tiene cuentas bancarias activas asociadas.\nCambie su estado a 'Inactivo' o 'Bloqueado'.\nDetalle: " + ex.getMessage(), "Restricción de Integridad", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void buscarClientePorCodigo() {
        String cod = txtCodigoCliente.getText().trim();
        if (cod.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese el código de cliente a buscar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Cliente c = clienteDAO.buscarPorCodigo(cod);
            if (c != null) {
                idClienteSeleccionado = c.getIdCliente();
                txtCodigoCliente.setText(c.getCodigoCliente());
                txtDui.setText(c.getDui());
                txtNombreCompleto.setText(c.getNombreCompleto());
                txtTelefono.setText(c.getTelefono() != null ? c.getTelefono() : "");
                txtCorreo.setText(c.getCorreoElectronico() != null ? c.getCorreoElectronico() : "");
                txtDireccion.setText(c.getDireccion() != null ? c.getDireccion() : "");
                cmbEstado.setSelectedItem(c.getEstado());
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el código: " + cod, "Búsqueda", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error BD: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarFilaSeleccionada() {
        int fila = tablaClientes.getSelectedRow();
        if (fila >= 0) {
            idClienteSeleccionado = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            txtCodigoCliente.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtDui.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtNombreCompleto.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtTelefono.setText(modeloTabla.getValueAt(fila, 4) != null ? modeloTabla.getValueAt(fila, 4).toString() : "");
            txtCorreo.setText(modeloTabla.getValueAt(fila, 5) != null ? modeloTabla.getValueAt(fila, 5).toString() : "");
            txtDireccion.setText(modeloTabla.getValueAt(fila, 6) != null ? modeloTabla.getValueAt(fila, 6).toString() : "");
            cmbEstado.setSelectedItem(modeloTabla.getValueAt(fila, 7).toString());
        }
    }

    private void cargarClientesEnTabla() {
        modeloTabla.setRowCount(0);
        try {
            List<Cliente> lista = clienteDAO.listarTodos();
            for (Cliente c : lista) {
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
        } catch (SQLException e) {
            System.err.println("Aviso al listar clientes: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        idClienteSeleccionado = 0;
        txtCodigoCliente.setText("");
        txtDui.setText("");
        txtNombreCompleto.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        cmbEstado.setSelectedIndex(0);
        tablaClientes.clearSelection();
        txtCodigoCliente.requestFocus();
    }
}
