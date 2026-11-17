public class MovimientoBancario {
    private int idMovimiento;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino; // Puede ser null si es depósito/retiro directo, pero vital para transferencias
    private String tipoMovimiento; // "TRANSFERENCIA", "DEPOSITO", "RETIRO"
    private double monto;
    private String fechaMovimiento;

    public MovimientoBancario() {}

    public MovimientoBancario(String numeroCuentaOrigen, String numeroCuentaDestino, String tipoMovimiento, double monto) {
        this.numeroCuentaOrigen = numeroCuentaOrigen;
        this.numeroCuentaDestino = numeroCuentaDestino;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
    }

    // Getters y Setters
    public int getIdMovimiento() { return idMovimiento; }
    public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }

    public String getNumeroCuentaOrigen() { return numeroCuentaOrigen; }
    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) { this.numeroCuentaOrigen = numeroCuentaOrigen; }

    public String getNumeroCuentaDestino() { return numeroCuentaDestino; }
    public void setNumeroCuentaDestino(String numeroCuentaDestino) { this.numeroCuentaDestino = numeroCuentaDestino; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public String getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(String fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }
}
