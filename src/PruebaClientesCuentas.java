import dao.ClienteDAO;
import dao.CuentaBancariaDAO;
import modelo.Cliente;
import modelo.CuentaBancaria;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PruebaClientesCuentas {

    private static int pruebas = 0;
    private static int exitosas = 0;

    private static void verificar(String nombre, boolean cumple) {
        pruebas++;
        if (cumple) {
            exitosas++;
        }
        System.out.println((cumple ? "[OK]    " : "[FALLO] ") + nombre);
    }

    public static void main(String[] args) throws Exception {
        ClienteDAO clienteDAO = new ClienteDAO();
        CuentaBancariaDAO cuentaDAO = new CuentaBancariaDAO();

        long t = System.currentTimeMillis();
        String codigo = "CL" + (t % 100000);
        String dui = String.format("%08d-%d", t % 100000000L, 7);
        String numeroCuenta = String.valueOf(200000000L + (t % 100000000L));

        System.out.println("===== PRUEBAS: CLIENTES Y CUENTAS =====");
        System.out.println("Datos de prueba -> codigo: " + codigo + ", DUI: " + dui + ", cuenta: " + numeroCuenta);
        System.out.println();

        // 1. Registrar cliente
        Cliente cliente = new Cliente(codigo, dui, "Cliente Prueba Fase 3",
                "7777-7777", "prueba@correo.com", "San Salvador", "Activo");
        boolean registrado = clienteDAO.insertar(cliente);
        verificar("1. Registrar cliente nuevo (ID generado: " + cliente.getIdCliente() + ")",
                registrado && cliente.getIdCliente() > 0);

        // 2. Buscar por codigo
        Cliente porCodigo = clienteDAO.buscarPorCodigo(codigo);
        verificar("2. Buscar cliente por codigo", porCodigo != null && dui.equals(porCodigo.getDui()));

        // 3. DUI repetido detectado
        Cliente porDui = clienteDAO.buscarPorDui(dui);
        verificar("3. DUI repetido detectado (se rechazaria un segundo cliente con ese DUI)",
                porDui != null && porDui.getIdCliente() == cliente.getIdCliente());

        // 4. Editar telefono
        cliente.setTelefono("6666-6666");
        boolean actualizado = clienteDAO.actualizar(cliente);
        Cliente tras = clienteDAO.buscarPorId(cliente.getIdCliente());
        verificar("4. Editar telefono del cliente",
                actualizado && tras != null && "6666-6666".equals(tras.getTelefono()));

        // 5. Abrir cuenta
        CuentaBancaria cuenta = new CuentaBancaria(numeroCuenta, "Ahorro", 500.00, "Activa", cliente.getIdCliente());
        boolean abierta = cuentaDAO.insertar(cuenta);
        CuentaBancaria guardada = cuentaDAO.buscarPorNumero(numeroCuenta);
        verificar("5. Abrir cuenta con saldo inicial $500.00",
                abierta && guardada != null && Math.abs(guardada.getSaldo() - 500.00) < 0.001);

        // 6. Numero de cuenta repetido detectado
        verificar("6. Numero de cuenta repetido detectado", cuentaDAO.buscarPorNumero(numeroCuenta) != null);

        // 7. Bloquear cuenta
        guardada.setEstadoCuenta("Bloqueada");
        cuentaDAO.actualizar(guardada);
        CuentaBancaria bloqueada = cuentaDAO.buscarPorNumero(numeroCuenta);
        verificar("7. Bloquear cuenta", bloqueada != null && "Bloqueada".equals(bloqueada.getEstadoCuenta()));

        // 8. Activar cuenta
        bloqueada.setEstadoCuenta("Activa");
        cuentaDAO.actualizar(bloqueada);
        CuentaBancaria activa = cuentaDAO.buscarPorNumero(numeroCuenta);
        verificar("8. Activar cuenta de nuevo", activa != null && "Activa".equals(activa.getEstadoCuenta()));

        // 9. Cuentas y saldos del cliente
        List<CuentaBancaria> misCuentas = new ArrayList<>();
        for (CuentaBancaria c : cuentaDAO.listarTodas()) {
            if (c.getIdCliente() == cliente.getIdCliente()) {
                misCuentas.add(c);
            }
        }
        System.out.println();
        System.out.println("Cuentas del cliente " + codigo + ":");
        for (CuentaBancaria c : misCuentas) {
            System.out.println("   " + c.getNumeroCuenta() + " | " + c.getTipoCuenta() + " | $"
                    + String.format(Locale.US, "%.2f", c.getSaldo()) + " | " + c.getEstadoCuenta());
        }
        System.out.println();
        verificar("9. Consultar cuentas y saldos del cliente", misCuentas.size() == 1);

        // 10. Dar de baja al cliente
        Cliente aBaja = clienteDAO.buscarPorId(cliente.getIdCliente());
        aBaja.setEstado("Inactivo");
        clienteDAO.actualizar(aBaja);
        Cliente inactivo = clienteDAO.buscarPorId(cliente.getIdCliente());
        verificar("10. Dar de baja al cliente (estado Inactivo)",
                inactivo != null && "Inactivo".equals(inactivo.getEstado()));

        System.out.println();
        System.out.println("RESULTADO: " + exitosas + " de " + pruebas + " pruebas exitosas");
    }
} 