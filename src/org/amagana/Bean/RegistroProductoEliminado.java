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
public class RegistroProductoEliminado {
    
    private int id;
    private String usu;
    private Timestamp fecha;
    private String productoE;

    public RegistroProductoEliminado() {
    }

    public RegistroProductoEliminado(int id, String usu, Timestamp fecha, String productoE) {
        this.id = id;
        this.usu = usu;
        this.fecha = fecha;
        this.productoE = productoE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsu() {
        return usu;
    }

    public void setUsu(String usu) {
        this.usu = usu;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getProductoE() {
        return productoE;
    }

    public void setProductoE(String productoE) {
        this.productoE = productoE;
    }

    
}
