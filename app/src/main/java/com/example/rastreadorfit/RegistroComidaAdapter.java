package com.example.rastreadorfit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RegistroComidaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Constantes para tipos de vista
    private static final int TIPO_HEADER = 0;   // Títulos de sección
    private static final int TIPO_REGISTRO = 1; // Renglón de comida

    private List<Object> listaItems; // Lista que mezcla Strings (títulos) y RegistroComida

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

    // 1. ViewHolder para la Comida
    public static class ComidaViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlimento, tvDetalles;
        CheckBox cbSaludable;
        ImageButton btnBorrar;
        ImageView ivIcono;

        public ComidaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAlimento = itemView.findViewById(R.id.tvAlimento);
            tvDetalles = itemView.findViewById(R.id.tvDetalles);
            cbSaludable = itemView.findViewById(R.id.cbSaludable);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
            ivIcono = itemView.findViewById(R.id.ivIcono);
        }
    }

    // 2. ViewHolder para el Título
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
            // Aquí usamos el nuevo layout item_registro_comida
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_registro_comida, parent, false);
            return new ComidaViewHolder(view);
        }
    }

    // --- ENLAZAR DATOS ---
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        // Animación simple de entrada
        setAnimation(holder.itemView);

        if (getItemViewType(position) == TIPO_HEADER) {
            String titulo = (String) listaItems.get(position);
            ((SeccionViewHolder) holder).tvSeccion.setText(titulo);

        } else {
            // Convertimos el objeto genérico a RegistroComida
            RegistroComida registro = (RegistroComida) listaItems.get(position);
            ComidaViewHolder comidaHolder = (ComidaViewHolder) holder;

            // Llenamos los textos
            comidaHolder.tvAlimento.setText(registro.getAlimento());
            comidaHolder.tvDetalles.setText(registro.getMomento() + " - " + registro.getFecha());

            // Asignamos icono según el momento del día
            // NOTA: Asegúrate de tener estas imágenes en drawable o cámbialas por una genérica
            switch (registro.getMomento()) {
                case "Desayuno":
                    // comidaHolder.ivIcono.setImageResource(R.drawable.ic_desayuno);
                    // Usamos un icono default de Android por si no tienes imagen aun:
                    comidaHolder.ivIcono.setImageResource(android.R.drawable.ic_menu_day);
                    break;
                case "Comida":
                    // comidaHolder.ivIcono.setImageResource(R.drawable.ic_comida);
                    comidaHolder.ivIcono.setImageResource(android.R.drawable.ic_menu_gallery);
                    break;
                case "Cena":
                    // comidaHolder.ivIcono.setImageResource(R.drawable.ic_cena);
                    comidaHolder.ivIcono.setImageResource(android.R.drawable.ic_menu_my_calendar);
                    break;
                case "Snack":
                    // comidaHolder.ivIcono.setImageResource(R.drawable.ic_snack);
                    comidaHolder.ivIcono.setImageResource(android.R.drawable.ic_menu_compass);
                    break;
                default:
                    comidaHolder.ivIcono.setImageResource(android.R.drawable.ic_menu_help);
                    break;
            }

            // Lógica del Checkbox (Es Saludable)
            comidaHolder.cbSaludable.setOnCheckedChangeListener(null);
            comidaHolder.cbSaludable.setChecked(registro.isEsSaludable());

            comidaHolder.cbSaludable.setOnCheckedChangeListener((buttonView, isChecked) -> {
                registro.setEsSaludable(isChecked);
                BaseDatos db = new BaseDatos(buttonView.getContext());
                db.actualizarRegistro(registro);
            });

            // Lógica del Botón Borrar
            comidaHolder.btnBorrar.setOnClickListener(v -> {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Eliminar Registro")
                        .setMessage("¿Eliminar '" + registro.getAlimento() + "'?")
                        .setPositiveButton("Sí", (dialog, which) -> {
                            BaseDatos db = new BaseDatos(v.getContext());
                            db.eliminarRegistro(registro.getId());
                            // Borramos de la lista visual y notificamos
                            listaItems.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                            Toast.makeText(v.getContext(), "Eliminado", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });

            // Click en todo el renglón para ver detalles (Esto abrirá el Fragment de la Parte 3)
            comidaHolder.itemView.setOnClickListener(v -> {
                DetalleComida fragment = new DetalleComida(); // Marcará rojo hasta que creemos esta clase
                Bundle args = new Bundle();

                args.putInt("id", registro.getId());
                args.putString("alimento", registro.getAlimento());
                args.putString("notas", registro.getNotas());
                args.putString("momento", registro.getMomento());
                args.putString("fecha", registro.getFecha());
                args.putString("calorias", registro.getNivelCalorico());
                args.putBoolean("esSaludable", registro.isEsSaludable());

                fragment.setArguments(args);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, fragment) // Ojo: Verifica el ID de tu contenedor en activity_main
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
        android.animation.ObjectAnimator animatorY = android.animation.ObjectAnimator.ofFloat(viewToAnimate, "translationY", 100f, 0f);
        animatorY.setDuration(300);
        animatorY.start();
    }
}