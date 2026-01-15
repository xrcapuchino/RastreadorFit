package com.example.rastreadorfit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rastreadorfit.BaseDatos;
import com.example.rastreadorfit.RegistroComida;

public class DetalleComida extends Fragment {

    private EditText etAlimento, etNotas;
    private Spinner spMomento, spCalorias;
    private CheckBox cbSaludable;
    private Button btnActualizar;

    private int idRegistro;
    private String fechaOriginal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_comida, container, false);

        etAlimento = view.findViewById(R.id.etAlimentoDetalle);
        etNotas = view.findViewById(R.id.etNotasDetalle);
        spMomento = view.findViewById(R.id.spMomentoDetalle);
        spCalorias = view.findViewById(R.id.spCaloriasDetalle);
        cbSaludable = view.findViewById(R.id.cbSaludableDetalle);
        btnActualizar = view.findViewById(R.id.btnActualizar);

        configurarSpinners();
        cargarDatos();

        btnActualizar.setOnClickListener(v -> actualizarRegistro());

        return view;
    }

    private void configurarSpinners() {
        // Configuramos igual que en NuevaComida
        ArrayAdapter<String> adapterMomento = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Desayuno", "Comida", "Cena", "Snack"});
        adapterMomento.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMomento.setAdapter(adapterMomento);

        ArrayAdapter<String> adapterCalorias = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"Bajo", "Medio", "Alto"});
        adapterCalorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCalorias.setAdapter(adapterCalorias);
    }

    private void cargarDatos() {
        if (getArguments() != null) {
            idRegistro = getArguments().getInt("id");
            etAlimento.setText(getArguments().getString("alimento"));
            etNotas.setText(getArguments().getString("notas"));
            fechaOriginal = getArguments().getString("fecha");
            cbSaludable.setChecked(getArguments().getBoolean("esSaludable"));

            // Seleccionar valor correcto en Spinners
            seleccionarSpinner(spMomento, getArguments().getString("momento"));
            seleccionarSpinner(spCalorias, getArguments().getString("calorias"));
        }
    }

    private void seleccionarSpinner(Spinner spinner, String valor) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        int position = adapter.getPosition(valor);
        if (position >= 0) {
            spinner.setSelection(position);
        }
    }

    private void actualizarRegistro() {
        RegistroComida registro = new RegistroComida();
        registro.setId(idRegistro);
        registro.setAlimento(etAlimento.getText().toString());
        registro.setNotas(etNotas.getText().toString());
        registro.setMomento(spMomento.getSelectedItem().toString());
        registro.setNivelCalorico(spCalorias.getSelectedItem().toString());
        registro.setFecha(fechaOriginal); // Mantenemos la fecha original por simplicidad
        registro.setEsSaludable(cbSaludable.isChecked());

        BaseDatos db = new BaseDatos(getContext());
        int filas = db.actualizarRegistro(registro);

        if (filas > 0) {
            Toast.makeText(getContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();
            // Regresar a la lista
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }
}