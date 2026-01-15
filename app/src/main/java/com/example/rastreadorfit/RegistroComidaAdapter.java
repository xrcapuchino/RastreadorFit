package com.example.rastreadorfit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegistroComidaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TIPO_HEADER = 0;
    private static final int TIPO_REGISTRO = 1;

    private List<Object> listaItems;

    public RegistroComidaAdapter(List<Object> listaItems) {
        this.listaItems = listaItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (listaItems.get(position) instanceof String) {
            return TIPO_HEADER;
        } else {
            return TIPO_REGISTRO;
        }
    }

    // --- VIEWHOLDERS ---
    public static class ComidaViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlimento, tvDetalles, tvEsSaludable;
        ImageButton btnBorrar;
        ImageView ivIcono;

        public ComidaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlimento = itemView.findViewById(R.id.tvAlimento);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
            tvEsSaludable = itemView.findViewById(R.id.tvEsSaludable); // Nuevo texto informativo
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            ivIcono = itemView.findViewById(R.id.ivIcono);
        }
    }

    public static class SeccionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSeccion;
        public SeccionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSeccion = itemView.findViewById(R.id.tvSeccion);
        }
    }

    // --- CREAR VISTAS ---
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TIPO_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seccion, parent, false);
            return new SeccionViewHolder(view);
        } else {
            // Usamos el nuevo layout sin checkbox
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registro_comida, parent, false);
            return new ComidaViewHolder(view);
        }
    }

    // --- ENLAZAR DATOS ---
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Animación suave
        setAnimation(holder.itemView);

        if (getItemViewType(position) == TIPO_HEADER) {
            String titulo = (String) listaItems.get(position);
            ((SeccionViewHolder) holder).tvSeccion.setText(titulo);

        } else {
            RegistroComida registro = (RegistroComida) listaItems.get(position);
            ComidaViewHolder comidaHolder = (ComidaViewHolder) holder;

            // 1. Datos principales
            comidaHolder.tvAlimento.setText(registro.getAlimento());
            comidaHolder.tvDetalles.setText(registro.getMomento() + " • " + registro.getCalorias() + " kcal");

            // 2. Mostrar "Saludable" solo si es true (ya no es editable aquí)
            if (registro.isEsSaludable()) {
                comidaHolder.tvEsSaludable.setVisibility(View.VISIBLE);
            } else {
                comidaHolder.tvEsSaludable.setVisibility(View.GONE);
            }

            // 3. Imágenes (Asegúrate de tenerlas en res/drawable)
            switch (registro.getMomento()) {
                case "Desayuno":
                    comidaHolder.ivIcono.setImageResource(R.drawable.ic_desayuno);
                    break;
                case "Comida":
                    comidaHolder.ivIcono.setImageResource(R.drawable.ic_comida);
                    break;
                case "Cena":
                    comidaHolder.ivIcono.setImageResource(R.drawable.ic_cena);
                    break;
                default:
                    comidaHolder.ivIcono.setImageResource(android.R.drawable.ic_menu_gallery);
                    break;
            }

            // 4. Botón Borrar
            comidaHolder.btnBorrar.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Eliminar")
                        .setMessage("¿Borrar " + registro.getAlimento() + "?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            BaseDatos db = new BaseDatos(v.getContext());
                            db.eliminarRegistro(registro.getId());
                            listaItems.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            Toast.makeText(v.getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

            // 5. Click para Editar Detalles
            comidaHolder.itemView.setOnClickListener(v -> {
                DetalleComida fragment = new DetalleComida();
                Bundle args = new Bundle();

                args.putInt("id", registro.getId());
                args.putString("alimento", registro.getAlimento());
                args.putString("notas", registro.getNotas());
                args.putString("momento", registro.getMomento());
                args.putString("fecha", registro.getFecha());
                args.putInt("calorias", registro.getCalorias());
                args.putBoolean("esSaludable", registro.isEsSaludable());

                fragment.setArguments(args);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, fragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
    }

    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    private void setAnimation(View viewToAnimate) {
        android.animation.ObjectAnimator animatorY = android.animation.ObjectAnimator.ofFloat(viewToAnimate, "translationY", 50f, 0f);
        animatorY.setDuration(300);
        animatorY.start();
    }
}