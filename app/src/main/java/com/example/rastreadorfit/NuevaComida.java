package com.example.rastreadorfit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rastreadorfit.BaseDatos;
import com.example.rastreadorfit.R;
import com.example.rastreadorfit.RegistroComida;

import java.util.Calendar;

public class NuevaComida extends Fragment {

    private EditText etAlimento, etNotas;
    private Spinner spMomento, spCalorias;
    private CheckBox cbEsSaludable;
    private TextView tvFechaSeleccionada;
    private Button btnFecha, btnGuardar;
    private String fechaGuardada = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_comida, container, false);

        // 1. Encontrar Vistas
        etAlimento = view.findViewById(R.id.etAlimento);
        etNotas = view.findViewById(R.id.etNotas);
        spMomento = view.findViewById(R.id.spMomento);
        spCalorias = view.findViewById(R.id.spCalorias);
        cbEsSaludable = view.findViewById(R.id.cbEsSaludable);
        tvFechaSeleccionada = view.findViewById(R.id.tvFechaSeleccionada);
        btnFecha = view.findViewById(R.id.btnFecha);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // 2. Configurar Spinners (Listas desplegables)
        configurarSpinners();

        // 3. Fecha por defecto (Hoy)
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        fechaGuardada = day + "/" + (month + 1) + "/" + year;
        tvFechaSeleccionada.setText("Fecha: " + fechaGuardada);

        // 4. Selector de Fecha
        btnFecha.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        fechaGuardada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        tvFechaSeleccionada.setText("Fecha: " + fechaGuardada);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // 5. Guardar en BD
        btnGuardar.setOnClickListener(v -> guardarRegistro());

        return view;
    }

    private void configurarSpinners() {
        // Opciones para Momento
        String[] momentos = {"Desayuno", "Comida", "Cena", "Snack"};
        ArrayAdapter<String> adapterMomento = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, momentos);
        adapterMomento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMomento.setAdapter(adapterMomento);

        // Opciones para Calorías
        String[] calorias = {"Bajo", "Medio", "Alto"};
        ArrayAdapter<String> adapterCalorias = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, calorias);
        adapterCalorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCalorias.setAdapter(adapterCalorias);
    }

    private void guardarRegistro() {
        String alimento = etAlimento.getText().toString();
        String notas = etNotas.getText().toString();
        String momento = spMomento.getSelectedItem().toString();
        String calorias = spCalorias.getSelectedItem().toString();
        boolean esSaludable = cbEsSaludable.isChecked();

        if (alimento.isEmpty()) {
            Toast.makeText(getContext(), "Escribe el nombre del alimento", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear Objeto y Guardar
        RegistroComida nuevoRegistro = new RegistroComida(alimento, notas, momento, calorias, fechaGuardada, esSaludable);
        BaseDatos db = new BaseDatos(getContext());
        long id = db.agregarRegistro(nuevoRegistro);

        if (id > 0) {
            Toast.makeText(getContext(), "¡Comida registrada!", Toast.LENGTH_SHORT).show();
            // Limpiar campos
            etAlimento.setText("");
            etNotas.setText("");
            cbEsSaludable.setChecked(false);
        } else {
            Toast.makeText(getContext(), "Error al guardar", Toast.LENGTH_SHORT).show();
        }
    }
}