/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.amagana.Bean;

import java.sql.Timestamp;

/**
 *
 * @author angel
 */
public class RegistroProducto {
    
    private int id;
    private String producto;
    private String usu;
    private double precioAV;
    private double precioDV;
    private double precioAC;
    private double precioDC;
    private Timestamp fecha;

    public RegistroProducto() {
    }

    public RegistroProducto(int id, String producto, String usu, double precioAV, double precioDV, double precioAC, double precioDC, Timestamp fecha) {
        this.id = id;
        this.producto = producto;
        this.usu = usu;
        this.precioAV = precioAV;
        this.precioDV = precioDV;
        this.precioAC = precioAC;
        this.precioDC = precioDC;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getUsu() {
        return usu;
    }

    public void setUsu(String usu) {
        this.usu = usu;
    }

    public double getPrecioAV() {
        return precioAV;
    }

    public void setPrecioAV(double precioAV) {
        this.precioAV = precioAV;
    }

    public double getPrecioDV() {
        return precioDV;
    }

    public void setPrecioDV(double precioDV) {
        this.precioDV = precioDV;
    }

    public double getPrecioAC() {
        return precioAC;
    }

    public void setPrecioAC(double precioAC) {
        this.precioAC = precioAC;
    }

    public double getPrecioDC() {
        return precioDC;
    }

    public void setPrecioDC(double precioDC) {
        this.precioDC = precioDC;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
    
    
    
}
