package com.example.stockfreshmovil;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

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
