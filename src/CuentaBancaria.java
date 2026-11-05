public class CuentaBancaria {
    private String numeroCuenta;
    private String codigoCliente;
    private String tipoCuenta; // Ahorros, Corriente
    private double saldo;
    private String estadoCuenta; // Activa, Inactiva, Bloqueada

    public CuentaBancaria() {}

    public CuentaBancaria(String numeroCuenta, String codigoCliente, String tipoCuenta, double saldo, String estadoCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.codigoCliente = codigoCliente;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.estadoCuenta = estadoCuenta;
    }

    // Getters y Setters
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String codigoCliente) { this.codigoCliente = codigoCliente; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(String estadoCuenta) { this.estadoCuenta = estadoCuenta; }
}