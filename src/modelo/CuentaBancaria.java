package modelo;

public class CuentaBancaria {
    private int idCuenta;
    private String numeroCuenta;
    private String tipoCuenta; // Ahorro, Corriente
    private double saldo;
    private String estadoCuenta; // Activa, Inactiva, Bloqueada
    private int idCliente;
    private String codigoCliente; // Referencia visual o lógica
    private String nombreCliente; // Nombre asociado al listar

    public CuentaBancaria() {
        this.estadoCuenta = "Activa";
        this.tipoCuenta = "Ahorro";
    }

    // Constructor para registro básico
    public CuentaBancaria(String numeroCuenta, String tipoCuenta, double saldo, String estadoCuenta, int idCliente) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.estadoCuenta = (estadoCuenta != null && !estadoCuenta.isEmpty()) ? estadoCuenta : "Activa";
        this.idCliente = idCliente;
    }

    // Constructor completo con IDs y nombres
    public CuentaBancaria(int idCuenta, String numeroCuenta, String tipoCuenta, double saldo, String estadoCuenta, int idCliente, String codigoCliente, String nombreCliente) {
        this.idCuenta = idCuenta;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.estadoCuenta = estadoCuenta;
        this.idCliente = idCliente;
        this.codigoCliente = codigoCliente;
        this.nombreCliente = nombreCliente;
    }

    // Getters y Setters
    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
