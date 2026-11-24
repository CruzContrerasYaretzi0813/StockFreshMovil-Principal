package com.example.stockfreshmovil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class bodegaviveresfragment extends Fragment {

    private Button btnAgregarProducto;
    private ImageButton btnPlus;
    private View layoutVacio, layoutConProductos;
    private RecyclerView rvProductos;
    private productoAlmacenAdapter adapter;
    private ApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bodega_viveresfragment, container, false);

        layoutVacio = view.findViewById(R.id.layoutVacio);
        layoutConProductos = view.findViewById(R.id.layoutConProductos);
        rvProductos = view.findViewById(R.id.rvProductosAlmacen);
        btnAgregarProducto = view.findViewById(R.id.btnAgregarProducto);
        btnPlus = view.findViewById(R.id.btnAgregarProductoPlus);
        apiService = ApiClient.getService();

        // Lista en cuadrícula de 3 columnas
        adapter = new productoAlmacenAdapter();
        rvProductos.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        rvProductos.setAdapter(adapter);

        btnAgregarProducto.setOnClickListener(v -> mostrarDialogoAgregarProducto());
        btnPlus.setOnClickListener(v -> mostrarDialogoAgregarProducto());

        cargarProductos();
        return view;
    }

    // ---------- Cargar lista desde la API ----------
    private void cargarProductos() {
        int idCliente = obtenerIdClienteSesion();
        if (idCliente <= 0) return;

        apiService.obtenerAlmacenCliente(idCliente).enqueue(new Callback<List<AlmacenItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<AlmacenItem>> call,
                                   @NonNull Response<List<AlmacenItem>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setItems(response.body());
                    actualizarVista(adapter.getTotal() > 0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AlmacenItem>> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarVista(boolean hayProductos) {
        layoutVacio.setVisibility(hayProductos ? View.GONE : View.VISIBLE);
        layoutConProductos.setVisibility(hayProductos ? View.VISIBLE : View.GONE);
        btnAgregarProducto.setVisibility(hayProductos ? View.GONE : View.VISIBLE);
        btnPlus.setVisibility(hayProductos ? View.VISIBLE : View.GONE);
    }

    // ---------- Diálogo para agregar ----------
    private void mostrarDialogoAgregarProducto() {
        if (getContext() == null) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.v_agregar_producto, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        ImageView ivPreviewProducto = dialogView.findViewById(R.id.iv_preview_producto);
        EditText etNombre = dialogView.findViewById(R.id.et_nombre_producto);
        Spinner spCategoria = dialogView.findViewById(R.id.sp_categoria_producto);
        Spinner spUnidad = dialogView.findViewById(R.id.sp_unidad_producto);
        ImageButton btnRestar = dialogView.findViewById(R.id.btn_restar_cantidad);
        ImageButton btnSumar = dialogView.findViewById(R.id.btn_sumar_cantidad);
        TextView tvCantidad = dialogView.findViewById(R.id.tv_cantidad_producto);
        View layoutFechaCompra = dialogView.findViewById(R.id.layout_fecha_compra);
        TextView tvFechaCompra = dialogView.findViewById(R.id.tv_fecha_compra);
        View layoutFechaCaducidad = dialogView.findViewById(R.id.layout_fecha_caducidad);
        TextView tvFechaCaducidad = dialogView.findViewById(R.id.tv_fecha_caducidad);
        MaterialButton btnConfirmar = dialogView.findViewById(R.id.btn_confirmar_agregar_producto);

        final int[] cantidad = {0};
        final String[] fechaCompraSql = {null};
        final String[] fechaCaducidadSql = {null};

        // Categorías (ids iguales a los de la tabla CATEGORIA_CLIENTE)
        List<CategoriaCliente> categorias = new ArrayList<>(Arrays.asList(
                new CategoriaCliente(0, "Selecciona una categoría"),
                new CategoriaCliente(1, "Frutas y verduras"),
                new CategoriaCliente(2, "Lácteos y huevos"),
                new CategoriaCliente(3, "Carnes y embutidos"),
                new CategoriaCliente(4, "Abarrotes y despensa"),
                new CategoriaCliente(5, "Bebidas"),
                new CategoriaCliente(6, "Chatarra")
        ));
        ArrayAdapter<CategoriaCliente> catAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, categorias);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(catAdapter);

        spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                CategoriaCliente sel = (CategoriaCliente) parent.getItemAtPosition(position);
                ivPreviewProducto.setImageResource(iconoPorCategoria(sel.getIdCategoria()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ivPreviewProducto.setImageResource(R.drawable.icono_default);
            }
        });

        // Unidades de medida
        List<UnidadMedida> unidades = new ArrayList<>(Arrays.asList(
                new UnidadMedida(1, "pz", "Pieza"),
                new UnidadMedida(2, "kg", "Kilogramo"),
                new UnidadMedida(3, "g", "Gramo"),
                new UnidadMedida(4, "L", "Litro"),
                new UnidadMedida(5, "ml", "Mililitro")
        ));
        ArrayAdapter<UnidadMedida> uniAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, unidades);
        uniAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spUnidad.setAdapter(uniAdapter);

        // Cantidad
        btnRestar.setOnClickListener(v -> {
            if (cantidad[0] > 0) {
                cantidad[0]--;
                actualizarTextoCantidad(tvCantidad, cantidad[0]);
            }
        });
        btnSumar.setOnClickListener(v -> {
            cantidad[0]++;
            actualizarTextoCantidad(tvCantidad, cantidad[0]);
        });

        // Fechas
        layoutFechaCompra.setOnClickListener(v -> abrirSelectorFecha((visible, sql) -> {
            tvFechaCompra.setText(visible);
            tvFechaCompra.setTextColor(Color.parseColor("#222222"));
            fechaCompraSql[0] = sql;
        }));
        layoutFechaCaducidad.setOnClickListener(v -> abrirSelectorFecha((visible, sql) -> {
            tvFechaCaducidad.setText(visible);
            tvFechaCaducidad.setTextColor(Color.parseColor("#222222"));
            fechaCaducidadSql[0] = sql;
        }));

        // Guardar
        btnConfirmar.setOnClickListener(v -> {
            String nombreProd = etNombre.getText().toString().trim();
            CategoriaCliente catSel = (CategoriaCliente) spCategoria.getSelectedItem();
            UnidadMedida uniSel = (UnidadMedida) spUnidad.getSelectedItem();

            if (nombreProd.isEmpty()) {
                etNombre.setError("Escribe el nombre del producto");
                etNombre.requestFocus();
                return;
            }
            if (catSel == null || catSel.getIdCategoria() <= 0) {
                Toast.makeText(getContext(), "Selecciona una categoría", Toast.LENGTH_SHORT).show();
                return;
            }
            if (uniSel == null) {
                Toast.makeText(getContext(), "Selecciona una unidad de medida", Toast.LENGTH_SHORT).show();
                return;
            }
            if (cantidad[0] <= 0) {
                Toast.makeText(getContext(), "La cantidad debe ser mayor a 0", Toast.LENGTH_SHORT).show();
                return;
            }

            int idCliente = obtenerIdClienteSesion();
            if (idCliente <= 0) {
                Toast.makeText(getContext(), "No se encontró la sesión del cliente", Toast.LENGTH_SHORT).show();
                return;
            }

            String fechaAgregada = fechaCompraSql[0] != null
                    ? fechaCompraSql[0]
                    : new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            AlmacenRequest request = new AlmacenRequest(
                    idCliente, nombreProd, catSel.getIdCategoria(), uniSel.getIdUnidad(),
                    (double) cantidad[0], fechaAgregada, fechaCaducidadSql[0], "Disponible");

            btnConfirmar.setEnabled(false);
            apiService.guardarEnAlmacen(request).enqueue(new Callback<AlmacenResponse>() {
                @Override
                public void onResponse(@NonNull Call<AlmacenResponse> call,
                                       @NonNull Response<AlmacenResponse> response) {
                    btnConfirmar.setEnabled(true);
                    if (!isAdded()) return;

                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(getContext(), response.body().getMensaje(),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        cargarProductos();   // refresca la lista
                    } else {
                        Toast.makeText(getContext(), "Error al guardar (" + response.code() + ")",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AlmacenResponse> call, @NonNull Throwable t) {
                    btnConfirmar.setEnabled(true);
                    if (!isAdded()) return;
                    Toast.makeText(getContext(), "Error de red: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void actualizarTextoCantidad(TextView tv, int valor) {
        tv.setText(String.valueOf(valor));
        tv.setTextColor(valor > 0 ? Color.parseColor("#222222") : Color.parseColor("#B5B5B5"));
    }

    private interface OnFechaSeleccionada {
        void onSeleccionada(String fechaVisible, String fechaSql);
    }

    private void abrirSelectorFecha(OnFechaSeleccionada callback) {
        if (getContext() == null) return;
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(getContext(), (view, year, month, day) -> {
            int mes = month + 1;
            String visible = String.format(Locale.getDefault(), "%02d/%02d/%02d", day, mes, year % 100);
            String sql = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, mes, day);
            callback.onSeleccionada(visible, sql);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private int iconoPorCategoria(int idCategoria) {
        switch (idCategoria) {
            case 1:
                return R.drawable.frutas_verduras;
            case 2:
                return R.drawable.lacteos_huevos;
            case 3:
                return R.drawable.carne_embutidos;
            case 4:
                return R.drawable.abarrotes_depensa;
            case 5:
                return R.drawable.bebidas;
            case 6:
                return R.drawable.chatarra;
            default:
                return R.drawable.icono_default;
        }
    }

    private int obtenerIdClienteSesion() {
        if (getContext() == null) return -1;
        SharedPreferences prefs = getContext().getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE);
        return prefs.getInt("idCliente", -1);
    }
}