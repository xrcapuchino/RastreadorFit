package com.example.rastreadorfit;

import android.graphics.Color;
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
import java.util.Calendar;
import java.util.List;

public class Home extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvResumenCalorias, tvMensajeLimite;
    private FloatingActionButton fabAgregar;
    private RegistroComidaAdapter adapter;
    private List<Object> listaMixta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        tvResumenCalorias = view.findViewById(R.id.tvResumenCalorias);
        tvMensajeLimite = view.findViewById(R.id.tvMensajeLimite);
        fabAgregar = view.findViewById(R.id.fabAgregar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaMixta = new ArrayList<>();
        adapter = new RegistroComidaAdapter(listaMixta);
        recyclerView.setAdapter(adapter);

        fabAgregar.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new NuevaComida())
                    .addToBackStack(null).commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarDatosYCalcular();
    }

    private void cargarDatosYCalcular() {
        listaMixta.clear();
        BaseDatos db = new BaseDatos(getContext());
        List<RegistroComida> registros = db.obtenerTodosLosRegistros();

        // 1. Calcular Fecha de Hoy
        Calendar c = Calendar.getInstance();
        String hoy = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);

        // 2. Variables para la suma
        int totalCaloriasHoy = 0;
        int limiteDiario = 2000; // Puedes cambiar esto

        String ultimaFecha = "";
        for (RegistroComida r : registros) {
            // Agrupar por fechas en la lista
            if (!r.getFecha().equals(ultimaFecha)) {
                listaMixta.add("Fecha: " + r.getFecha());
                ultimaFecha = r.getFecha();
            }
            listaMixta.add(r);

            // Sumar si es de HOY
            if (r.getFecha().equals(hoy)) {
                totalCaloriasHoy += r.getCalorias();
            }
        }

        // 3. Actualizar la interfaz
        tvResumenCalorias.setText(totalCaloriasHoy + " / " + limiteDiario + " kcal");

        if (totalCaloriasHoy > limiteDiario) {
            tvMensajeLimite.setText("¡Te has excedido del límite!");
            tvMensajeLimite.setTextColor(Color.RED);
        } else {
            int restantes = limiteDiario - totalCaloriasHoy;
            tvMensajeLimite.setText("Te quedan " + restantes + " kcal disponibles.");
            tvMensajeLimite.setTextColor(Color.parseColor("#558B2F"));
        }

        adapter.notifyDataSetChanged();
    }
}