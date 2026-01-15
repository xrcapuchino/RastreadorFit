package com.example.rastreadorfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvVacio;
    private FloatingActionButton fabAgregar;
    private RegistroComidaAdapter adapter;
    private List<Object> listaMixta; // Lista que tendrá Títulos (String) y Registros (RegistroComida)

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvVacio = view.findViewById(R.id.tvVacio);
        fabAgregar = view.findViewById(R.id.fabAgregar);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaMixta = new ArrayList<>();
        adapter = new RegistroComidaAdapter(listaMixta);
        recyclerView.setAdapter(adapter);

        // Acción del botón flotante -> Ir a Nueva Comida
        fabAgregar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new NuevaComida())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatos(); // Recargar lista cada vez que volvemos a esta pantalla
    }

    private void cargarDatos() {
        listaMixta.clear();
        BaseDatos db = new BaseDatos(getContext());
        List<RegistroComida> registrosDB = db.obtenerTodosLosRegistros();

        if (registrosDB.isEmpty()) {
            tvVacio.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvVacio.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            // Algoritmo simple para agrupar por fechas
            String ultimaFecha = "";
            for (RegistroComida r : registrosDB) {
                // Si la fecha cambia, agregamos un Título separador
                if (!r.getFecha().equals(ultimaFecha)) {
                    listaMixta.add("Fecha: " + r.getFecha());
                    ultimaFecha = r.getFecha();
                }
                // Agregamos el registro
                listaMixta.add(r);
            }
            adapter.notifyDataSetChanged();
        }
    }
}