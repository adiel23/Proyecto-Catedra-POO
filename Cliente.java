package modelo;

public class Cliente {
    // Atributos privados (Encapsulamiento)
    private int codigoCliente;
    private String dui;
    private String nombreCompleto;
    private String telefono;
    private String correoElectronico;
    private String direccion;
    private String estado; // Activo, Inactivo, Bloqueado

    // Constructor vacío
    public Cliente() {}

    // Constructor con parámetros
    public Cliente(int codigoCliente, String dui, String nombreCompleto, String telefono, String correoElectronico, String direccion, String estado) {
        this.codigoCliente = codigoCliente;
        this.dui = dui;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correoElectronico = correoElectronico;
        this.direccion = direccion;
        this.estado = estado;
    }

    // Getters y Setters
    public int getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(int codigoCliente) { this.codigoCliente = codigoCliente; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
