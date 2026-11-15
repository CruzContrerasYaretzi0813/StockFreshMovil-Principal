package com.example.stockfreshmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class perfilfragment extends Fragment {

    private EditText etidUsuario, etNombre, etContrasena, etTelefono, etCorreo;
    private ImageButton btnEditar;
    private Button btnCerrarSesion;
    private boolean editando = false;
    private int idCliente;
    private SharedPreferences prefs;

    private TextInputLayout ojitoContrasena;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfilfragment, container, false);

        etidUsuario = view.findViewById(R.id.etIdUsuario);
        etNombre = view.findViewById(R.id.etNombre);
        etContrasena = view.findViewById(R.id.etContrasena);
        etTelefono = view.findViewById(R.id.etTelefono);
        etCorreo = view.findViewById(R.id.etCorreo);
        btnEditar = view.findViewById(R.id.btnEditar);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);
        ojitoContrasena = view.findViewById(R.id.ojitoContrasena);
        ojitoContrasena.setEndIconVisible(false);
        prefs = requireContext().getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        idCliente = prefs.getInt("idCliente", -1);

        cargarPerfil();

        btnEditar.setOnClickListener(v -> {
            if (!editando) {
                activarEdicion(true);
                Toast.makeText(getContext(), "Modo edición activado", Toast.LENGTH_SHORT).show();
            } else {
                guardarCambios();
            }
        });

        btnCerrarSesion.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Intent i = new Intent(requireContext(), IniciarSesion.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        return view;
    }

    private void cargarPerfil() {
        etidUsuario.setText(prefs.getString("idUsuario", ""));
        etNombre.setText(prefs.getString("nombre", ""));
        etCorreo.setText(prefs.getString("correoElectronico", ""));
        etTelefono.setText(prefs.getString("numeroCelular", ""));
        etContrasena.setText(prefs.getString("contrasena", ""));
    }

    private void activarEdicion(boolean activar) {
        editando = activar;
        etidUsuario.setEnabled(activar);
        etNombre.setEnabled(activar);
        etContrasena.setEnabled(activar);
        etTelefono.setEnabled(activar);
        etCorreo.setEnabled(activar);
        etContrasena.setEnabled(activar);
        ojitoContrasena.setEndIconVisible(activar);
        if (!activar) {
            etContrasena.setTransformationMethod(
                    android.text.method.PasswordTransformationMethod.getInstance());
        }
        if (activar) etNombre.requestFocus();
    }

    private void guardarCambios() {
        if (idCliente == -1) {
            Toast.makeText(getContext(), "Sesión no encontrada, inicia sesión de nuevo",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String idUsuario = etidUsuario.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contrasena = etContrasena.getText().toString();

        if (idUsuario.isEmpty() || nombre.isEmpty() || correo.isEmpty()) {
            Toast.makeText(getContext(), "ID, nombre y correo son obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ActPerfilRequest body =
                new ActPerfilRequest(idUsuario, nombre, correo, telefono, contrasena);
        ApiClient.getService().actualizarPerfil(idCliente, body).enqueue(new Callback<PerfilResponses>() {
            @Override
            public void onResponse(Call<PerfilResponses> call, Response<PerfilResponses> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    prefs.edit()
                            .putString("idUsuario", idUsuario)
                            .putString("nombre", nombre)
                            .putString("correoElectronico", correo)
                            .putString("numeroCelular", telefono)
                            .putString("contrasena", contrasena)
                            .apply();
                    activarEdicion(false);
                    Toast.makeText(getContext(), response.body().mensaje,
                            Toast.LENGTH_SHORT).show();
                } else {
                    String msg = "No se pudieron guardar los cambios";
                    try {
                        if (response.errorBody() != null) {
                            PerfilResponses err = new Gson()
                                    .fromJson(response.errorBody().string(), PerfilResponses.class);
                            if (err != null && err.error != null) msg = err.error;
                        }
                    } catch (Exception ignored) { }
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PerfilResponses> call, Throwable throwable) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "No se pudo conectar: " + throwable.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}