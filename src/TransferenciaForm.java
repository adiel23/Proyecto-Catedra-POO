import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TransferenciaForm extends JFrame {
    private JTextField txtCuentaOrigen, txtCuentaDestino, txtMonto;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTablaHistorial;
    private TransferenciaDAO transferenciaDAO;

    public TransferenciaForm() {
        transferenciaDAO = new TransferenciaDAO();

        setTitle("Modulo de Transferencias e Historial - Fase 2");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // --- COMPONENTES DE LA INTERFAZ ---
        JLabel lblTitulo = new JLabel("Realizar Transferencia Bancaria");
        lblTitulo.setBounds(20, 15, 250, 25);
        add(lblTitulo);

        JLabel lblOrigen = new JLabel("Nº Cuenta Origen:");
        lblOrigen.setBounds(20, 60, 130, 25);
        add(lblOrigen);
        txtCuentaOrigen = new JTextField();
        txtCuentaOrigen.setBounds(150, 60, 150, 25);
        add(txtCuentaOrigen);

        JLabel lblDestino = new JLabel("Nº Cuenta Destino:");
        lblDestino.setBounds(20, 100, 130, 25);
        add(lblDestino);
        txtCuentaDestino = new JTextField();
        txtCuentaDestino.setBounds(150, 100, 150, 25);
        add(txtCuentaDestino);

        JLabel lblMonto = new JLabel("Monto a Transferir ($):");
        lblMonto.setBounds(20, 140, 130, 25);
        add(lblMonto);
        txtMonto = new JTextField();
        txtMonto.setBounds(150, 140, 150, 25);
        add(txtMonto);

        JButton btnTransferir = new JButton("Procesar Transferencia");
        btnTransferir.setBounds(20, 190, 280, 35);
        add(btnTransferir);

        // --- SECCION DE HISTORIAL ---
        JLabel lblHistorial = new JLabel("Historial de Movimientos / Transacciones");
        lblHistorial.setBounds(330, 15, 300, 25);
        add(lblHistorial);

        tablaHistorial = new JTable();
        modeloTablaHistorial = new DefaultTableModel(new Object[]{"ID", "Origen", "Destino", "Tipo", "Monto"}, 0);
        tablaHistorial.setModel(modeloTablaHistorial);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBounds(330, 50, 385, 380);
        add(scrollPane);

        // --- EVENTOS ---
        btnTransferir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ejecutarTransferenciaAccion();
            }
        });
    }

    private void ejecutarTransferenciaAccion() {
        String origen = txtCuentaOrigen.getText().trim();
        String destino = txtCuentaDestino.getText().trim();
        String txtValMonto = txtMonto.getText().trim();

        // 1. validar campos vacios
        if (origen.isEmpty() || destino.isEmpty() || txtValMonto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error de validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Validar formato numerico y monto mayor a cero
        double monto;
        try {
            monto = Double.parseDouble(txtValMonto);
            if (monto <= 0) {
                JOptionPane.showMessageDialog(this, "El monto de la transferencia debe ser mayor que cero.", "Regla de Negocio", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido para el monto.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Ejecutar la transferencia mediante el DAO
        boolean resultado = transferenciaDAO.realizarTransferencia(origen, destino, monto);

        if (resultado) {
            JOptionPane.showMessageDialog(this, "¡Transferencia realizada con éxito!", "Transacción Completa", JOptionPane.INFORMATION_MESSAGE);

            // Añadir simulacion visual rapida a la tabla de historial
            modeloTablaHistorial.addRow(new Object[]{"1", origen, destino, "TRANSFERENCIA", "$" + monto});

            limpiarCampos();
        } else {
            // =====================================================================================
            // [INTEGRANTE 5]: MANEJO DE EXCEPCIONES Y LOGS (LOG4J)
            // Aquí se atraparán excepciones personalizadas
            // y se registrarán con Log4J coordinado con el Integrante 4 y 5.
            // =====================================================================================
            JOptionPane.showMessageDialog(this, "No se pudo completar la transferencia. Verifique saldos, estados de cuenta o existencia.", "Error en Transacción", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtCuentaOrigen.setText("");
        txtCuentaDestino.setText("");
        txtMonto.setText("");
        txtCuentaOrigen.requestFocus();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TransferenciaForm().setVisible(true));
    }
}
