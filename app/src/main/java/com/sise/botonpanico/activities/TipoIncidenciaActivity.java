package com.sise.botonpanico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sise.botonpanico.R;
import com.sise.botonpanico.adapters.TipoIncidenciaSpinnerAdapter;
import com.sise.botonpanico.entities.EstadoIncidencia;
import com.sise.botonpanico.entities.Incidencia;
import com.sise.botonpanico.entities.TipoIncidencia;
import com.sise.botonpanico.shared.Message;
import com.sise.botonpanico.viewmodel.IncidenciaViewModel;
import com.sise.botonpanico.viewmodel.TipoIncidenciaViewModel;

import java.util.ArrayList;
import java.util.List;

public class TipoIncidenciaActivity extends AppCompatActivity {

    private final String TAG = TipoIncidenciaActivity.class.getSimpleName();
    private TipoIncidenciaViewModel tipoIncidenciaViewModel;
    private Spinner tipoIncidenciaSpinner;
    private EditText et_descripcion;
    private IncidenciaViewModel incidenciaViewModel;
    private LinearLayout linearLayoutBotones;
    private ProgressBar pbLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tipo_incidencia);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tipoIncidenciaSpinner = findViewById(R.id.tipo_incidencia_spinner);
        et_descripcion = findViewById(R.id.tipo_incidencia_descripcion);
        linearLayoutBotones = findViewById(R.id.activityotroincidente_ly_linear);
        pbLoading = findViewById(R.id.activityotroincidente_pb_loading);

        incidenciaViewModel = new ViewModelProvider(this).get(IncidenciaViewModel.class);
        tipoIncidenciaViewModel = new ViewModelProvider(this).get(TipoIncidenciaViewModel.class);

        observeTipoIncidenciaViewModel();
        observeIncidenciaViewModel();
    }

    @Override
    protected void onStart() {
        super.onStart();
        pbLoading.setVisibility(View.VISIBLE);
        tipoIncidenciaViewModel.listarTipoIncidencias();
    }

    private void observeTipoIncidenciaViewModel(){
        tipoIncidenciaViewModel.getListarTipoIncidenciasLiveData().observe(TipoIncidenciaActivity.this, liveDataResponse -> {
            pbLoading.setVisibility(View.GONE);

            if(!liveDataResponse.isSuccess() || liveDataResponse.getData() == null ) {
                Toast.makeText(TipoIncidenciaActivity.this, Message.INTENTAR_MAS_TARDE,Toast.LENGTH_LONG).show();
                return;
            }

            List<TipoIncidencia> tipoIncidencias = liveDataResponse.getData();

            List<TipoIncidencia> tipoIncidenciaNoBoton = new ArrayList<>();
            for ( TipoIncidencia _tipoIncidencia : tipoIncidencias) {
                if(_tipoIncidencia.getFlagBoton().equals("0")){
                    tipoIncidenciaNoBoton.add(_tipoIncidencia);
                }
            }
            TipoIncidenciaSpinnerAdapter adapter = new TipoIncidenciaSpinnerAdapter(TipoIncidenciaActivity.this,tipoIncidenciaNoBoton);
            tipoIncidenciaSpinner.setAdapter(adapter);

            for (TipoIncidencia tipoIncidencia: tipoIncidencias) {
                if(tipoIncidencia.getFlagBoton().equals("1")){
                    Button btnTipoIncidencia = new Button(TipoIncidenciaActivity.this);
                    btnTipoIncidencia.setText(tipoIncidencia.getDescripcion());
                    btnTipoIncidencia.setBackgroundResource(R.drawable.btn_otro_incidente_background);
                    //btnTipoIncidencia.setBackgroundTintMode(null);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 16, 0, 16);
                    btnTipoIncidencia.setLayoutParams(params);

                    btnTipoIncidencia.setOnClickListener(view -> {
                        Incidencia incidencia = new Incidencia();
                        incidencia.setDescripcion("Click en boton "+tipoIncidencia.getDescripcion());
                        incidencia.setTipoIncidencia(tipoIncidencia);

                        EstadoIncidencia estadoIncidencia = new EstadoIncidencia();
                        estadoIncidencia.setIdEstadoIncidencia(1);
                        incidencia.setEstadoIncidencia(estadoIncidencia);
                        incidenciaViewModel.insertarIncidencia(incidencia);
                    });
                    linearLayoutBotones.addView(btnTipoIncidencia);
                }

            }
        });
    }

    private void observeIncidenciaViewModel(){
        incidenciaViewModel.getInsertarIncidenciaLiveData().observe(TipoIncidenciaActivity.this, liveDataResponse -> {
            if(!liveDataResponse.isSuccess() || liveDataResponse.getData() == null ) {
                Toast.makeText(this, Message.INTENTAR_MAS_TARDE,Toast.LENGTH_LONG).show();
                return;
            }
            Toast.makeText(getApplicationContext(),"¡Se ha enviado correctamente!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(TipoIncidenciaActivity.this, InicioActivity.class);
            startActivity(intent);
        });
    }

    public void onClickEnviar(View view) {

        String descripcion = et_descripcion.getText().toString().trim();
        if(descripcion.isEmpty()){
            et_descripcion.setError("Debe ingresar una descripción");
            return;
        }

        Incidencia incidencia = new Incidencia();
        incidencia.setDescripcion(et_descripcion.getText().toString());

        EstadoIncidencia estadoIncidencia = new EstadoIncidencia();
        estadoIncidencia.setIdEstadoIncidencia(1);
        incidencia.setEstadoIncidencia(estadoIncidencia);

        TipoIncidencia tipoIncidencia = (TipoIncidencia)tipoIncidenciaSpinner.getSelectedItem();
        incidencia.setTipoIncidencia(tipoIncidencia);
        incidenciaViewModel.insertarIncidencia(incidencia);
    }
}