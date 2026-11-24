package com.example.stockfreshmovil;

public class AlmacenResponse {
    private String mensaje;
    private int idAlmacen;
    private int idProducto;
    private int idBodega;
    private String error;

    public String getMensaje() {
        return mensaje;
    }

    public int getIdAlmacen() {
        return idAlmacen;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getIdBodega() {
        return idBodega;
    }

    public String getError() {
        return error;
    }
}
