package com.example.stockfreshmovil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @GET("api/catalogos/categorias-cliente")
    Call<List<CategoriaCliente>> obtenerCategoriasCliente();

    @GET("api/catalogos/unidades-medida")
    Call<List<UnidadMedida>> obtenerUnidadesMedida();

    @POST("api/almacen")
    Call<AlmacenResponse> guardarEnAlmacen(@Body AlmacenRequest request);

    @GET("api/almacen/cliente/{idCliente}")
    Call<List<AlmacenItem>> obtenerAlmacenCliente(@Path("idCliente") int idCliente);
    @DELETE("api/clientes/{id}")
    Call<PerfilResponses> eliminarCuenta(@Path("id") int idCliente);

    @GET("api/clientes/{id}")
    Call<PerfilResponses> obtenerPerfil(@Path("id") int idCliente);

    @PUT("api/clientes/{id}")
    Call<PerfilResponses> actualizarPerfil(@Path("id") int idCliente,
                                          @Body ActPerfilRequest body);

    @POST("api/clientes/login")
    Call<IniciarSesionResponse> iniciarSesionCliente(@Body IniciarSesionRequest body);

    @POST("api/clientes/registro")
    Call<RegistroResponse> registrarCliente(@Body RegistroRequest body);
}
