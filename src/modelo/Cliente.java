package modelo;

public class Cliente {
    private int idCliente;
    private String codigoCliente;
    private String dui;
    private String nombreCompleto;
    private String telefono;
    private String correoElectronico;
    private String direccion;
    private String estado; // Activo, Inactivo, Bloqueado

    // Constructor vacío
    public Cliente() {
        this.estado = "Activo";
    }

    // Constructor sin id (para registros nuevos)
    public Cliente(String codigoCliente, String dui, String nombreCompleto, String telefono, String correoElectronico, String direccion, String estado) {
        this.codigoCliente = codigoCliente;
        this.dui = dui;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.estado = (estado != null && !estado.isEmpty()) ? estado : "Activo";
    }

    // Constructor completo con id (para datos desde BD)
    public Cliente(int idCliente, String codigoCliente, String dui, String nombreCompleto, String telefono, String correoElectronico, String direccion, String estado) {
        this.idCliente = idCliente;
        this.codigoCliente = codigoCliente;
        this.dui = dui;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.estado = estado;
    }

    // Getters y Setters
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

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return codigoCliente + " - " + nombreCompleto + " (" + dui + ")";
    }
}
