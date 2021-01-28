package co.edu.unipiloto.edu.mycovidapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LocalidadInfo extends AppCompatActivity {
    private TextView txt_zona,txt_totalUsuarios,txt_totalContagiados,txt_taza,txt_state;
    private Button btn_notificar,btn_aislamiento;
    private String localidad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localidad_info);
        btn_aislamiento=(Button)findViewById(R.id.btn_aislamiento);
        btn_notificar=(Button)findViewById(R.id.btn_notificarT);
        txt_zona=(TextView)findViewById(R.id.txt_zonaT);
        txt_totalUsuarios=(TextView)findViewById(R.id.txt_totalUsuarios);
        txt_totalContagiados=(TextView)findViewById(R.id.txt_totalContagiados);
        txt_zona=(TextView)findViewById(R.id.txt_zonaT);
        txt_state=(TextView)findViewById(R.id.txt_stateT);
        localidad=getIntent().getStringExtra("localidad");

    }
}