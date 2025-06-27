package com.sise.botonpanico.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sise.botonpanico.MainActivity;
import com.sise.botonpanico.R;
import com.sise.botonpanico.adapters.TipoDocumentoSpinnerAdapter;
import com.sise.botonpanico.dto.TipoDocumento;
import com.sise.botonpanico.entities.Usuario;
import com.sise.botonpanico.shared.Data;
import com.sise.botonpanico.shared.LiveDataResponse;
import com.sise.botonpanico.viewmodel.UsuarioViewModel;

public class PerfilCiudadanoActivity extends AppCompatActivity {

    private final String TAG = PerfilCiudadanoActivity.class.getSimpleName();
    private Spinner spTipoDocumentos;
    private EditText etNumeroDocumento;
    private EditText etApellidoPaterno;
    private EditText etApellidoMaterno;
    private EditText etNombres;
    private EditText etCelular;
    private EditText etCorreo;
    private EditText etDireccion;

    private UsuarioViewModel usuarioViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Ejecutado metodo onCreate()");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_perfil_ciudadano);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spTipoDocumentos = findViewById(R.id.activityperfilciudadano_spn_tipodocumento);
        etNumeroDocumento = findViewById(R.id.activityperfilciudadano_et_numerodocumento);
        etNombres = findViewById(R.id.activityperfilciudadano_et_nombres);
        etApellidoMaterno = findViewById(R.id.activityperfilciudadano_et_apellidomaterno);
        etApellidoPaterno = findViewById(R.id.activityperfilciudadano_et_apellidopaterno);
        etCelular = findViewById(R.id.activityperfilciudadano_et_celular);
        etCorreo = findViewById(R.id.activityperfilciudadano_et_correo);
        etDireccion = findViewById(R.id.activityperfilciudadano_et_direccion);

        TipoDocumentoSpinnerAdapter tipoDocumentoSpinnerAdapter = new TipoDocumentoSpinnerAdapter(PerfilCiudadanoActivity.this, Data.getTipoDocumentos());
        spTipoDocumentos.setAdapter(tipoDocumentoSpinnerAdapter);

        usuarioViewModel = new ViewModelProvider(this).get(UsuarioViewModel.class);

    }

    public void onClickRegistrarse(View view){
        Usuario usuario = new Usuario();
        usuario.setTipoDocumento(((TipoDocumento)spTipoDocumentos.getSelectedItem()).getCodigo());
        usuario.setNumeroDocumento(etNumeroDocumento.getText().toString());
        usuario.setNombres(etNombres.getText().toString());
        usuario.setApellidoPaterno(etApellidoPaterno.getText().toString());
        usuario.setApellidoMaterno(etApellidoMaterno.getText().toString());
        usuario.setCelular(etCelular.getText().toString());
        usuario.setCorreo(etCorreo.getText().toString());
        usuario.setDireccion(etDireccion.getText().toString());

        usuarioViewModel.insertarUsuario(usuario);

        usuarioViewModel.getInsertarUsuarioLiveData().observe(this, new Observer<LiveDataResponse<Boolean>>() {
            @Override
            public void onChanged(LiveDataResponse<Boolean> response) {
                if (response != null) {
                    if (response.getData()) {
                        Intent intent = new Intent(PerfilCiudadanoActivity.this, MainActivity.class);
                        Toast.makeText(getApplicationContext(),"¡Se ha registrado correctamente!", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(PerfilCiudadanoActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}