package modelo;

import java.math.BigDecimal;

/**
 * Representa un movimiento financiero registrado en la tabla 'movimientos'.
 */
public class MovimientoBancario {
    private int idMovimiento;
    private String tipoMovimiento; // "Deposito", "Retiro", "Transferencia"
    private BigDecimal monto;
    private String descripcion;
    private String fechaMovimiento;
    private int idCuenta;
    private Integer idCuentaDestino;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino;

    public MovimientoBancario() {
    }

    public MovimientoBancario(String tipoMovimiento, BigDecimal monto, String descripcion,
                              int idCuenta, Integer idCuentaDestino) {
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.descripcion = descripcion;
        this.idCuenta = idCuenta;
        this.idCuentaDestino = idCuentaDestino;
    }

    public MovimientoBancario(int idMovimiento, String tipoMovimiento, BigDecimal monto,
                              String descripcion, String fechaMovimiento,
                              int idCuenta, Integer idCuentaDestino,
                              String numeroCuentaOrigen, String numeroCuentaDestino) {
        this.idMovimiento = idMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fechaMovimiento = fechaMovimiento;
        this.idCuenta = idCuenta;
        this.idCuentaDestino = idCuentaDestino;
        this.numeroCuentaOrigen = numeroCuentaOrigen;
        this.numeroCuentaDestino = numeroCuentaDestino;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(String fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public int getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(int idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Integer getIdCuentaDestino() {
        return idCuentaDestino;
    }

    public void setIdCuentaDestino(Integer idCuentaDestino) {
        this.idCuentaDestino = idCuentaDestino;
    }

    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }

    public void setNumeroCuentaOrigen(String numeroCuentaOrigen) {
        this.numeroCuentaOrigen = numeroCuentaOrigen;
    }

    public String getNumeroCuentaDestino() {
        return numeroCuentaDestino;
    }

    public void setNumeroCuentaDestino(String numeroCuentaDestino) {
        this.numeroCuentaDestino = numeroCuentaDestino;
    }

    @Override
    public String toString() {
        return "Movimiento #" + idMovimiento + " [" + tipoMovimiento + " $" + monto + "]";
    }
}
