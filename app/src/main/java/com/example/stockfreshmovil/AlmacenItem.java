package com.example.stockfreshmovil;

public class AlmacenItem {
    private int idAlmacen;
    private int idBodega;
    private int idProducto;
    private double cantidad;
    private String fechaAgregada;
    private String fechaCaducidad;
    private String status;
    private String nombreProducto;
    private int idCategoria;
    private String nombreCategoria;
    private int idUnidad;
    private String codigoUnidad;
    private String descripcionUnidad;

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public int getIdBodega() {
        return idBodega;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getFechaAgregada() {
        return fechaAgregada;
    }

    public String getFechaCaducidad() {
        return fechaCaducidad;
    }

    public String getStatus() {
        return status;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public int getIdUnidad() {
        return idUnidad;
    }

    public String getCodigoUnidad() {
        return codigoUnidad;
    }

    public String getDescripcionUnidad() {
        return descripcionUnidad;
    }
}
