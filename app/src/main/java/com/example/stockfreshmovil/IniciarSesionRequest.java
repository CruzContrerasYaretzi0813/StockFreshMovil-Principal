package com.example.stockfreshmovil;

public class IniciarSesionRequest {

    private String idUsuario;
    private String contrasena;

    public IniciarSesionRequest() {
    }

    public IniciarSesionRequest(String idUsuario, String contrasena) {
        this.idUsuario = idUsuario;
        this.contrasena = contrasena;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}