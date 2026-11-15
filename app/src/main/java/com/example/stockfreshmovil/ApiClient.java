package com.example.stockfreshmovil;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class ApiClient {
    // Emulador: 10.0.2.2 apunta al localhost lq Mac
    // Celular físico: IP de Mac, ej. "http://192.168.1.50:3060/"
    private static final String BASE_URL = "http://localhost:3060/";
    private static Retrofit retrofit;

    public static ApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
