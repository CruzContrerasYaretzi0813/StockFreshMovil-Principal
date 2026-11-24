package com.example.stockfreshmovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {

    TextView VolverRegistro;
    Button btnIniciarSesion;

    //notese que idCliente(App Móvil) y idUsuario(servidor y base de datos) son lo mismo
    EditText idCliente, contrasenaCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_sesion);

        VolverRegistro = findViewById(R.id.tvNotengoCuenta);
        VolverRegistro.setOnClickListener(this);

        //Boton de iniciar sesion
        btnIniciarSesion = findViewById(R.id.btnIniciarSesionEnviar);
        btnIniciarSesion.setOnClickListener(this);

        //Edit Text que ingresa el usuarip
        idCliente = findViewById(R.id.IDUsuarioIS);
        contrasenaCliente = findViewById(R.id.contrasenaIS);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.tvNotengoCuenta){
            Intent volverRegistro = new Intent(this, Registro.class);
            startActivity(volverRegistro);
        }
        if(id == R.id.btnIniciarSesionEnviar){
            iniciarSesionCliente();
        }
    }

    private void iniciarSesionCliente(){
        String idUsuario = idCliente.getText().toString().trim();
        String contrasenaIngresada = contrasenaCliente.getText().toString().trim();

        if ( idUsuario.isEmpty() || contrasenaIngresada.isEmpty()){
            Toast.makeText(this, "Completa todos los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnIniciarSesion.setEnabled(false);

        IniciarSesionRequest body = new IniciarSesionRequest(idUsuario, contrasenaIngresada);
        ApiClient.getService().iniciarSesionCliente(body).enqueue(new Callback<IniciarSesionResponse>() {

            @Override
            public void onResponse(Call<IniciarSesionResponse> call, Response<IniciarSesionResponse> response) {

                btnIniciarSesion.setEnabled(true);
                if (response.isSuccessful() && response.body() !=null){

                    //Obtiene los datos de nobre y correo para almacenarlos en Datos de Usuario
                    String nombreUsuario = response.body().nombre;
                    int idCliente = response.body().idCliente;
                    String idUsuario = response.body().idUsuario;
                    String correoUsuario = response.body().correoElectronico;
                    String numeroCelular = response.body().numeroCelular;
                    String contrasena = response.body().contrasena;

                    android.content.SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
                    prefs.edit()

                                    .putString("nombre", nombreUsuario)
                                    .putInt("idCliente", idCliente)
                                    .putString("idUsuario", idUsuario)
                                    .putString("correoElectronico", correoUsuario)
                                    .putString("numeroCelular", numeroCelular)
                                    .putString("contrasena", contrasenaIngresada)
                                    .apply();



                    Toast.makeText(IniciarSesion.this, response.body().mensaje, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(IniciarSesion.this, Home.class));
                    finish();
                } else {
                    String msj = "Ocurrio un error al Iniciar Sesión";
                    try {
                        if(response.errorBody() != null){
                            IniciarSesionResponse err = new Gson().fromJson(response.errorBody().string(), IniciarSesionResponse.class);
                            if (err != null && err.error != null){
                                msj = err.error;
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    Toast.makeText(IniciarSesion.this, msj, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<IniciarSesionResponse> call, Throwable throwable) {
                btnIniciarSesion.setEnabled(true);
                Toast.makeText(IniciarSesion.this, "No se pudo conectar: " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}