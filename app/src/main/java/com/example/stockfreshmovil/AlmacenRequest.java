package com.example.stockfreshmovil;

public class AlmacenRequest {

    private int idCliente;
    private String nombre;
    private int idCategoria;
    private int idUnidad;
    private double cantidad;
    private String fechaAgregada;
    private String fechaCaducidad;
    private String status;

    public AlmacenRequest(int idCliente, String nombre, int idCategoria, int idUnidad, double cantidad, String fechaAgregada, String fechaCaducidad, String status) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.idCategoria = idCategoria;
        this.idUnidad = idUnidad;
        this.cantidad = cantidad;
        this.fechaAgregada = fechaAgregada;
        this.fechaCaducidad = fechaCaducidad;
        this.status = status;
    }
}
