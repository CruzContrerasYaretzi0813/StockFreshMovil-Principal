package com.example.stockfreshmovil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class productoAlmacenAdapter extends RecyclerView.Adapter<productoAlmacenAdapter.VH> {

    private final List<AlmacenItem> items = new ArrayList<>();

    public void setItems(List<AlmacenItem> nuevos) {
        items.clear();
        if (nuevos != null) items.addAll(nuevos);
        notifyDataSetChanged();
    }

    public int getTotal() { return items.size(); }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producto_bodegav, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        AlmacenItem it = items.get(position);
        h.nombre.setText(it.getNombreProducto());

        double c = it.getCantidad();
        String cant = (c == Math.floor(c)) ? String.valueOf((int) c) : String.valueOf(c);
        h.cantidad.setText(cant + " " + (it.getCodigoUnidad() != null ? it.getCodigoUnidad() : ""));

        h.img.setImageResource(iconoPorCategoria(it.getIdCategoria()));
    }

    @Override
    public int getItemCount() { return items.size(); }

    private int iconoPorCategoria(int id) {
        switch (id) {
            case 1: return R.drawable.frutas_verduras;
            case 2: return R.drawable.lacteos_huevos;
            case 3: return R.drawable.carne_embutidos;
            case 4: return R.drawable.abarrotes_depensa;
            case 5: return R.drawable.bebidas;
            case 6: return R.drawable.chatarra;
            default: return R.drawable.icono_default;
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView nombre, cantidad;
        VH(View v) {
            super(v);
            img = v.findViewById(R.id.ivImgProductoItem);
            nombre = v.findViewById(R.id.tvNombreProductoItem);
            cantidad = v.findViewById(R.id.tvCantidadProductoItem);
        }
    }
}