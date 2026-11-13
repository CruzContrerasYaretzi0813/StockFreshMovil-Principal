package com.example.stockfreshmovil;

public class RegistroRequest {

    private String idUsuario;
    private String nombre;
    private String correoElectronico;
    private String numeroCelular;
    private String contrasena;

    public RegistroRequest() {
    }

    public RegistroRequest(String idUsuario, String nombre, String correoElectronico, String numeroCelular, String contrasena) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correoElectronico = correoElectronico;
        this.numeroCelular = numeroCelular;
        this.contrasena = contrasena;
    }
}
