package modelo;

public class CuentaBancaria {
    // Atributos privados
    private int numeroCuenta;
    private int codigoCliente; // Relación lógica con Cliente
    private String tipoCuenta; // Ahorros, Corriente
    private double saldo;
    private String estadoCuenta; // Activa, Bloqueada

    // Constructor vacío
    public CuentaBancaria() {}

    // Constructor con parámetros
    public CuentaBancaria(int numeroCuenta, int codigoCliente, String tipoCuenta, double saldo, String estadoCuenta) {
        this.numeroCuenta = numeroCuenta;
        this.codigoCliente = codigoCliente;
        this.tipoCuenta = tipoCuenta;
        this.saldo = saldo;
        this.estadoCuenta = estadoCuenta;
    }

    // Getters y Setters
    public int getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(int numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public int getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(int codigoCliente) { this.codigoCliente = codigoCliente; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }

    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }

    public String getEstadoCuenta() { return estadoCuenta; }
    public void setEstadoCuenta(String estadoCuenta) { this.estadoCuenta = estadoCuenta; }
}
