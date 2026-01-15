package com.example.rastreadorfit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class NuevaComida extends Fragment {

    private EditText etAlimento, etNotas, etCalorias;
    private RadioGroup rgMomento; // Usamos RadioGroup en vez de Spinner
    private CheckBox cbEsSaludable;
    private TextView tvFechaSeleccionada;
    private Button btnFecha, btnGuardar;
    private String fechaGuardada = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nueva_comida, container, false);

        etAlimento = view.findViewById(R.id.etAlimento);
        etNotas = view.findViewById(R.id.etNotas);
        etCalorias = view.findViewById(R.id.etCalorias); // Nuevo campo
        rgMomento = view.findViewById(R.id.rgMomento);   // Nuevo RadioGroup
        cbEsSaludable = view.findViewById(R.id.cbEsSaludable);
        tvFechaSeleccionada = view.findViewById(R.id.tvFechaSeleccionada);
        btnFecha = view.findViewById(R.id.btnFecha);
        btnGuardar = view.findViewById(R.id.btnGuardar);

        // Fecha por defecto
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        fechaGuardada = day + "/" + (month + 1) + "/" + year;
        tvFechaSeleccionada.setText("Fecha: " + fechaGuardada);

        btnFecha.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(getContext(),
                    (view1, year1, monthOfYear, dayOfMonth) -> {
                        fechaGuardada = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        tvFechaSeleccionada.setText("Fecha: " + fechaGuardada);
                    }, year, month, day);
            dpd.show();
        });

        btnGuardar.setOnClickListener(v -> guardarRegistro());

        return view;
    }

    private void guardarRegistro() {
        String alimento = etAlimento.getText().toString();
        String caloriasStr = etCalorias.getText().toString();

        // Validar que se seleccionó un momento
        int selectedId = rgMomento.getCheckedRadioButtonId();
        String momento = "Snack"; // Valor por defecto
        if (selectedId == R.id.rbDesayuno) momento = "Desayuno";
        else if (selectedId == R.id.rbComida) momento = "Comida";
        else if (selectedId == R.id.rbCena) momento = "Cena";

        if (alimento.isEmpty() || caloriasStr.isEmpty()) {
            Toast.makeText(getContext(), "Completa el alimento y las calorías", Toast.LENGTH_SHORT).show();
            return;
        }

        int calorias = Integer.parseInt(caloriasStr);

        RegistroComida nuevo = new RegistroComida(alimento, etNotas.getText().toString(), momento, calorias, fechaGuardada, cbEsSaludable.isChecked());
        BaseDatos db = new BaseDatos(getContext());
        db.agregarRegistro(nuevo);

        Toast.makeText(getContext(), "¡Guardado!", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack(); // Volver atrás
    }
}