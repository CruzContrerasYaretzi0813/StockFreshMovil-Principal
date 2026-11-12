package com.example.stockfreshmovil;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("api/clientes/registro")
    Call<RegistroResponse> registrarCliente(@Body RegistroRequest body);
}
