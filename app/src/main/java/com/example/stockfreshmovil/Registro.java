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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import org.w3c.dom.Text;

public class Registro extends AppCompatActivity implements View.OnClickListener {

    TextView VolverInicioSesion;
    Button btnRegistro;

    //Notese q idCliente(CódigoAndroid) y IdUsuario(En Base de MySQL) son lo mismo
    EditText nombreCliente, idCliente, correoCliente, numeroTelefCliente, contraCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);

        //Campos que llena el usuario (EditText)
        nombreCliente = findViewById(R.id.nombreUsuario);
        idCliente = findViewById(R.id.IDUsuario);
        correoCliente = findViewById(R.id.correoUsuario);
        numeroTelefCliente = findViewById(R.id.telefonoUsuario);
        contraCliente = findViewById(R.id.contrasenaUR);

        //Botones
        VolverInicioSesion = findViewById(R.id.tvYatengocuenta);
        VolverInicioSesion.setOnClickListener(this);

        btnRegistro = findViewById(R.id.btnRegristrarseEnviar);
        btnRegistro.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tvYatengocuenta){
            Intent volverInicioSesion = new Intent(this, IniciarSesion.class);
            startActivity(volverInicioSesion);
        }

        if(id == R.id.btnRegristrarseEnviar){
            registrarCliente();
        }
    }

    private void registrarCliente(){
        String idUsuario = idCliente.getText().toString().trim();
        String nombre = nombreCliente.getText().toString().trim();
        String correo = correoCliente.getText().toString().trim();
        String telefono = numeroTelefCliente.getText().toString().trim();
        String contrasenaIngresada = contraCliente.getText().toString().trim();

        if( idUsuario.isEmpty() || nombre.isEmpty() || correo.isEmpty() || telefono.isEmpty() || contrasenaIngresada.isEmpty()){
            Toast.makeText(this, "Completa todos los datos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegistro.setEnabled(false);

        RegistroRequest body = new RegistroRequest(idUsuario, nombre, correo, telefono,contrasenaIngresada);
        ApiClient.getService().registrarCliente(body).enqueue(new Callback<RegistroResponse>() {
            @Override
            public void onResponse(Call<RegistroResponse> call, Response<RegistroResponse> response) {
                btnRegistro.setEnabled(true);
                if (response.isSuccessful() && response.body() != null){

                    //Obtiene los datos de nobre y correo para almacenarlos en Datos de Usuario
                    String nombreUsuario = response.body().nombre;
                    int idCliente = response.body().idCliente;
                    String idUsuario = response.body().idUsuario;
                    String correoUsuario = response.body().correoElectronico;
                    String numeroCelular = response.body().numeroCelular;

                    android.content.SharedPreferences prefs = getSharedPreferences("DatosUsuario", MODE_PRIVATE);
                    prefs.edit()
                            .putString("nombre", nombreUsuario)
                            .putInt("idCliente", idCliente)
                            .putString("idUsuario", idUsuario)
                            .putString("correoElectronico", correoUsuario)
                            .putString("numeroCelular", numeroCelular)
                            .putString("contrasena", contrasenaIngresada)
                            .apply();

                    Toast.makeText(Registro.this, response.body().mensaje, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Registro.this, Home.class));
                    finish();
                } else {
                    String msj = "Ocurrió un error al registrar el Cliente";
                    try {
                        if (response.errorBody() != null){
                            RegistroResponse err = new Gson().fromJson(response.errorBody().string(), RegistroResponse.class);
                            if (err != null && err.error != null) {
                                msj = err.error; // Toma el texto que manda el servidor <<node server.js>< (ej. "El ID ya está en uso")
                            }
                        }
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    // Despliegue de mensaje que manda node server.js
                    Toast.makeText(Registro.this, msj, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegistroResponse> call, Throwable throwable) {
                btnRegistro.setEnabled(true);
                Toast.makeText(Registro.this, "No se pudo conectar: " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}