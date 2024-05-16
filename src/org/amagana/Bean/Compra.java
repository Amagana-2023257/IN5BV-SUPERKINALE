package org.amagana.Bean;

/**
 *
 * @author informatica
 */
public class Compra {
    
    private int id;
    private String descripcion;
    private double total;
    private String detalleProducto;

    public Compra() {
    }

    public Compra(int id, String descripcion, double total, String detalleProducto) {
        this.id = id;
        this.descripcion = descripcion;
        this.total = total;
        this.detalleProducto = detalleProducto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getDetalleProducto() {
        return detalleProducto;
    }

    public void setDetalleProducto(String detalleProducto) {
        this.detalleProducto = detalleProducto;
    }
    
    
            
    
}
