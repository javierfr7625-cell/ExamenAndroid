package javier.fernandez.examen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import javier.fernandez.examen.databinding.ActivityMainBinding;
import modelos.Presupuesto;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Presupuesto> listaPresupuestos;
    private ActivityResultLauncher<Intent> launcherCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        listaPresupuestos = new ArrayList<>();
        inicializarLauncher();
        mostrarElementos();

        binding.fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrearItemActivity.class);
            launcherCrear.launch(intent);
        });
    }

    private void inicializarLauncher() {
        launcherCrear = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getExtras() != null) {
                        Presupuesto item = extraerPresupuesto(result.getData().getExtras());
                        if (item != null) {
                            listaPresupuestos.add(item);
                            mostrarElementos();
                        }
                    } else if (result.getResultCode() == RESULT_CANCELED) {
                        Toast.makeText(MainActivity.this, R.string.msg_cancelado, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @SuppressWarnings("deprecation")
    private Presupuesto extraerPresupuesto(Bundle extras) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return extras.getSerializable(CrearItemActivity.EXTRA_PRESUPUESTO, Presupuesto.class);
        }
        return (Presupuesto) extras.getSerializable(CrearItemActivity.EXTRA_PRESUPUESTO);
    }

    private void mostrarElementos() {
        binding.contentMain.contenedorMain.removeAllViews();
        binding.contentMain.txtEstadoVacio.setVisibility(
                listaPresupuestos.isEmpty() ? View.VISIBLE : View.GONE
        );

        LayoutInflater inflater = LayoutInflater.from(this);
        double totalGeneral = 0.0;

        for (Presupuesto item : listaPresupuestos) {
            totalGeneral += item.getCosteTotal();

            View fila = inflater.inflate(R.layout.item_fila, binding.contentMain.contenedorMain, false);

            TextView txtNombre = fila.findViewById(R.id.txtNombreFila);
            TextView txtDetalle = fila.findViewById(R.id.txtDetalleFila);
            TextView txtCoste = fila.findViewById(R.id.txtCosteFila);

            txtNombre.setText(item.getNombrePlan());
            txtDetalle.setText(getString(
                    R.string.formato_detalle_fila,
                    item.getCategoria(),
                    item.getCantidad(),
                    item.getCosteBase(),
                    item.getPorcentajeRecargo()
            ));
            txtCoste.setText(getString(R.string.formato_coste_total, item.getCosteTotal()));

            binding.contentMain.contenedorMain.addView(fila);
        }

        binding.contentMain.txtResumenTotal.setText(
                getString(R.string.resumen_formato, listaPresupuestos.size(), totalGeneral)
        );
    }
}
