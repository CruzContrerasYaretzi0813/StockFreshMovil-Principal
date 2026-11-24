package com.example.stockfreshmovil;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragmentHome extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarBodega(view);
        TextView tvVerMas = view.findViewById(R.id.tvVerMasBodega);
        if (tvVerMas != null) {
            tvVerMas.setOnClickListener(v -> {
                if (!isAdded()) return;

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new bodegaviveresfragment())
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

    private void cargarBodega(View root) {
        int idCliente = requireContext()
                .getSharedPreferences("DatosUsuario", Context.MODE_PRIVATE)
                .getInt("idCliente", -1);
        if (idCliente == -1) return;

        ApiClient.getService().obtenerAlmacenCliente(idCliente)
                .enqueue(new Callback<List<AlmacenItem>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<AlmacenItem>> call,
                                           @NonNull Response<List<AlmacenItem>> response) {
                        if (!isAdded()) return;
                        if (response.isSuccessful() && response.body() != null) {
                            pintarBodega(root, response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<AlmacenItem>> call, @NonNull Throwable t) {
                        if (!isAdded()) return;
                        Toast.makeText(requireContext(), "No se pudo cargar la bodega",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void pintarBodega(View root, List<AlmacenItem> todos) {
        int[] ids = {
                R.id.itemBodega1, R.id.itemBodega2, R.id.itemBodega3,
                R.id.itemBodega4, R.id.itemBodega5, R.id.itemBodega6
        };

        LayoutInflater inflater = LayoutInflater.from(requireContext());

        for (int i = 0; i < ids.length; i++) {
            FrameLayout cont = root.findViewById(ids[i]);
            if (cont == null) continue;
            cont.removeAllViews();

            if (i < todos.size()) {
                AlmacenItem item = todos.get(i);
                View celda = inflater.inflate(R.layout.item_bodega_home, cont, false);

                TextView tvNombre = celda.findViewById(R.id.tvNombreProducto);
                ImageView imgProducto = celda.findViewById(R.id.imgProducto);

                if (tvNombre != null) {
                    tvNombre.setText(item.getNombreProducto());
                }

                if (imgProducto != null) {
                    int idCat = item.getIdCategoria();
                    imgProducto.setImageResource(iconoPorCategoria(idCat));
                }

                cont.addView(celda);
            }
        }
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
}