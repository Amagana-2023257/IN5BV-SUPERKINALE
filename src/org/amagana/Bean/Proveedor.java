package org.amagana.Bean;

/**
 *
 * @author angel
 */
public class Proveedor {

    private int id;
    private int nit;
    private String nombre, apellido, direccion, razonSocial, contacto, web, email, numeroPrincipal, numeroSecundario, descripcionEmail, telefonoObservaciones;

    public Proveedor() {
    }

    public Proveedor(int id, int nit, String nombre, String apellido, String direccion, String razonSocial, String contacto, String web, String email, String descripcion, String numeroPrincipal, String numeroSecundario, String descripcionEmail, String telefonoObservaciones) {
        this.id = id;
        this.nit = nit;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.razonSocial = razonSocial;
        this.contacto = contacto;
        this.web = web;
        this.email = email;
        this.numeroPrincipal = numeroPrincipal;
        this.numeroSecundario = numeroSecundario;
        this.descripcionEmail = descripcionEmail;
        this.telefonoObservaciones = telefonoObservaciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNit() {
        return nit;
    }

    public void setNit(int nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroPrincipal() {
        return numeroPrincipal;
    }

    public void setNumeroPrincipal(String numeroPrincipal) {
        this.numeroPrincipal = numeroPrincipal;
    }

    public String getNumeroSecundario() {
        return numeroSecundario;
    }

    public void setNumeroSecundario(String numeroSecundario) {
        this.numeroSecundario = numeroSecundario;
    }

    public String getDescripcionEmail() {
        return descripcionEmail;
    }

    public void setDescripcionEmail(String descripcionEmail) {
        this.descripcionEmail = descripcionEmail;
    }

    public String getTelefonoObservaciones() {
        return telefonoObservaciones;
    }

    public void setTelefonoObservaciones(String telefonoObservaciones) {
        this.telefonoObservaciones = telefonoObservaciones;
    }

}
