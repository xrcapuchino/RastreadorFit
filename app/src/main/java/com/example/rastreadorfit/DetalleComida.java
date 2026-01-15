package com.example.rastreadorfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetalleComida extends Fragment {

    private EditText etAlimento, etNotas, etCalorias;
    private RadioGroup rgMomento;
    private RadioButton rbDesayuno, rbComida, rbCena;
    private CheckBox cbSaludable;
    private Button btnActualizar;

    private int idRegistro;
    private String fechaOriginal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_comida, container, false);

        // 1. Encontrar vistas
        etAlimento = view.findViewById(R.id.etAlimentoDetalle);
        etNotas = view.findViewById(R.id.etNotasDetalle);
        etCalorias = view.findViewById(R.id.etCaloriasDetalle); // Nuevo EditText numérico

        rgMomento = view.findViewById(R.id.rgMomentoDetalle);
        rbDesayuno = view.findViewById(R.id.rbDesayunoDetalle);
        rbComida = view.findViewById(R.id.rbComidaDetalle);
        rbCena = view.findViewById(R.id.rbCenaDetalle);

        cbSaludable = view.findViewById(R.id.cbSaludableDetalle);
        btnActualizar = view.findViewById(R.id.btnActualizar);

        // 2. Cargar los datos que vienen del Adaptador
        cargarDatos();

        // 3. Botón Guardar
        btnActualizar.setOnClickListener(v -> actualizarRegistro());

        return view;
    }

    private void cargarDatos() {
        if (getArguments() != null) {
            idRegistro = getArguments().getInt("id");
            etAlimento.setText(getArguments().getString("alimento"));
            etNotas.setText(getArguments().getString("notas"));
            fechaOriginal = getArguments().getString("fecha");
            cbSaludable.setChecked(getArguments().getBoolean("esSaludable"));

            // Cargar Calorías (int -> String para mostrarlo)
            int cal = getArguments().getInt("calorias");
            etCalorias.setText(String.valueOf(cal));

            // Marcar el RadioButton correcto según el texto guardado
            String momento = getArguments().getString("momento");
            if (momento != null) {
                switch (momento) {
                    case "Desayuno":
                        rbDesayuno.setChecked(true);
                        break;
                    case "Comida":
                        rbComida.setChecked(true);
                        break;
                    case "Cena":
                        rbCena.setChecked(true);
                        break;
                    // Si es "Snack" u otro, no marcamos nada o podrías agregar un rbSnack
                }
            }
        }
    }

    private void actualizarRegistro() {
        String nuevoAlimento = etAlimento.getText().toString();
        String nuevasCaloriasStr = etCalorias.getText().toString();

        if (nuevoAlimento.isEmpty() || nuevasCaloriasStr.isEmpty()) {
            Toast.makeText(getContext(), "El alimento y las calorías son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener momento seleccionado del RadioGroup
        String nuevoMomento = "Snack"; // Valor por defecto
        if (rbDesayuno.isChecked()) nuevoMomento = "Desayuno";
        else if (rbComida.isChecked()) nuevoMomento = "Comida";
        else if (rbCena.isChecked()) nuevoMomento = "Cena";

        // Crear objeto actualizado
        RegistroComida registro = new RegistroComida();
        registro.setId(idRegistro);
        registro.setAlimento(nuevoAlimento);
        registro.setNotas(etNotas.getText().toString());
        registro.setMomento(nuevoMomento);
        registro.setCalorias(Integer.parseInt(nuevasCaloriasStr)); // Convertir a int
        registro.setFecha(fechaOriginal);
        registro.setEsSaludable(cbSaludable.isChecked());

        // Guardar en BD
        BaseDatos db = new BaseDatos(getContext());
        int filas = db.actualizarRegistro(registro);

        if (filas > 0) {
            Toast.makeText(getContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack(); // Regresar
        } else {
            Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }
}