package javier.fernandez.examen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javier.fernandez.examen.databinding.ActivityCrearItemBinding;
import modelos.Presupuesto;

public class CrearItemActivity extends AppCompatActivity {

    public static final String EXTRA_PRESUPUESTO = "PRESUPUESTO";

    private ActivityCrearItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityCrearItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        configurarListenersCalculo();
        configurarBotones();
    }

    private void configurarListenersCalculo() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizarPreview();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        binding.txtCosteBase.addTextChangedListener(watcher);
        binding.txtCantidad.addTextChangedListener(watcher);

        binding.spCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarPreview();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                actualizarPreview();
            }
        });
    }

    private void configurarBotones() {
        binding.btnCancelar.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        binding.btnGuardar.setOnClickListener(v -> {
            Presupuesto presupuesto = construirPresupuesto();
            if (presupuesto != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_PRESUPUESTO, presupuesto);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void actualizarPreview() {
        int posicion = binding.spCategoria.getSelectedItemPosition();
        String costeTexto = binding.txtCosteBase.getText().toString().trim().replace(',', '.');
        String cantidadTexto = binding.txtCantidad.getText().toString().trim();

        if (posicion <= 0 || costeTexto.isEmpty() || cantidadTexto.isEmpty()) {
            binding.txtPreviewTotal.setText(R.string.preview_coste_inicial);
            return;
        }

        try {
            double costeBase = Double.parseDouble(costeTexto);
            int cantidad = Integer.parseInt(cantidadTexto);
            if (costeBase <= 0 || cantidad < 1) {
                binding.txtPreviewTotal.setText(R.string.preview_coste_inicial);
                return;
            }
            double recargo = Presupuesto.obtenerPorcentajePorCategoria(posicion);
            double total = Presupuesto.calcularCosteTotal(costeBase, cantidad, recargo);
            binding.txtPreviewTotal.setText(getString(R.string.preview_coste_formato, total, recargo));
        } catch (NumberFormatException e) {
            binding.txtPreviewTotal.setText(R.string.preview_coste_inicial);
        }
    }

    private Presupuesto construirPresupuesto() {
        String nombrePlan = binding.txtNombrePlan.getText().toString().trim();
        String costeTexto = binding.txtCosteBase.getText().toString().trim().replace(',', '.');
        String cantidadTexto = binding.txtCantidad.getText().toString().trim();

        if (nombrePlan.isEmpty() || costeTexto.isEmpty() || cantidadTexto.isEmpty()) {
            Toast.makeText(this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
            return null;
        }

        int posicion = binding.spCategoria.getSelectedItemPosition();
        if (posicion <= 0) {
            Toast.makeText(this, R.string.error_categoria, Toast.LENGTH_SHORT).show();
            return null;
        }

        double costeBase;
        int cantidad;
        try {
            costeBase = Double.parseDouble(costeTexto);
            cantidad = Integer.parseInt(cantidadTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.error_valores_invalidos, Toast.LENGTH_SHORT).show();
            return null;
        }

        if (costeBase <= 0 || cantidad < 1) {
            Toast.makeText(this, R.string.error_valores_invalidos, Toast.LENGTH_SHORT).show();
            return null;
        }

        String categoria = String.valueOf(binding.spCategoria.getSelectedItem());
        double recargo = Presupuesto.obtenerPorcentajePorCategoria(posicion);
        return new Presupuesto(nombrePlan, categoria, costeBase, cantidad, recargo);
    }
}
