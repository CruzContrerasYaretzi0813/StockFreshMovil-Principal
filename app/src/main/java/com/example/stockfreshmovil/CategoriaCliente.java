package com.example.stockfreshmovil;

import androidx.annotation.NonNull;

public class CategoriaCliente {
    private int idCategoria;
    private String nombre;
    private String descripcion;

    public CategoriaCliente() {
    }

    public CategoriaCliente(int idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    public int getIdCategoria() { return idCategoria; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    @NonNull
    @Override
    public String toString() {
        return nombre != null ? nombre : "";
    }
}