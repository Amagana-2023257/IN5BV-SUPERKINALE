package org.amagana.Bean;

import java.time.LocalDate;

/**
 *
 * @author informatica
 */
public class Compra {
    
    private int id;
    private LocalDate fecha;
    private String descripcion;
    private double total;
    
    private double costoU;
    private int cantidad;
    private int producto;
    private int compra;
    private String productoS;
    
    public Compra() {
    }

    public Compra(int id, LocalDate fecha, String descripcion, double total, double costoU, int cantidad, int producto, String productoS) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.total = total;
        this.costoU = costoU;
        this.cantidad = cantidad;
        this.producto = producto;
        this.compra = compra;
        this.productoS = productoS;
    }

    public Compra(int id, LocalDate fecha, String descripcion, double total, double costoU, int cantidad, int producto, int compra) {
        this.id = id;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.total = total;
        this.costoU = costoU;
        this.cantidad = cantidad;
        this.producto = producto;
        this.compra = compra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    public double getCostoU() {
        return costoU;
    }

    public void setCostoU(double costoU) {
        this.costoU = costoU;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getProducto() {
        return producto;
    }

    public void setProducto(int producto) {
        this.producto = producto;
    }

    public int getCompra() {
        return compra;
    }

    public void setCompra(int compra) {
        this.compra = compra;
    }

    public String getProductoS() {
        return productoS;
    }

    public void setProductoS(String productoS) {
        this.productoS = productoS;
    }

  
    
}
