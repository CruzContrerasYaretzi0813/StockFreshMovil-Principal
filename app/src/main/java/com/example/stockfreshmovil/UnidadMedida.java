package com.example.stockfreshmovil;

import androidx.annotation.NonNull;

public class UnidadMedida {
    private int idUnidad;
    private String codigo;
    private String descripcion;

    public int getIdUnidad() { return idUnidad; }
    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }

    public UnidadMedida(int idUnidad, String codigo, String descripcion) {
        this.idUnidad = idUnidad;
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    @NonNull
    @Override
    public String toString() {
        String desc = descripcion != null ? descripcion : "";
        String cod = codigo != null ? codigo : "";
        return cod.isEmpty() ? desc : desc + " (" + cod + ")";
    }
}
