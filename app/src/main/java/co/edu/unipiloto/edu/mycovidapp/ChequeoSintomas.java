package co.edu.unipiloto.edu.mycovidapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChequeoSintomas extends AppCompatActivity {

    ToggleButton tbFiebre, tbDolorGarganta, tbTos, tbDificultadRespirar, tbPerdidaGustoOlfato, tbFatiga, tbCongestionNasal;
    Button  btn_chequear;
    DatabaseReference db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chequeo_sintomas);
        db = FirebaseDatabase.getInstance().getReference();


        tbFiebre = (ToggleButton)findViewById(R.id.tbFiebre);
        tbDolorGarganta = (ToggleButton)findViewById(R.id.tbDolorGarganta);
        tbTos = (ToggleButton)findViewById(R.id.tbTos);
        tbDificultadRespirar = (ToggleButton)findViewById(R.id.tbDificultadRespirar);
        tbPerdidaGustoOlfato = (ToggleButton)findViewById(R.id.tbPerdidaGustoOlfato);
        tbFatiga = (ToggleButton)findViewById(R.id.tbFatiga);
        tbCongestionNasal = (ToggleButton)findViewById(R.id.tbCongestionNasal);


        btn_chequear = (Button)findViewById(R.id.btn_chequear);
        btn_chequear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tbFiebre.isChecked() || tbDolorGarganta.isChecked() || tbTos.isChecked() || tbDificultadRespirar.isChecked() ||
                tbPerdidaGustoOlfato.isChecked() || tbFatiga.isChecked() || tbCongestionNasal.isChecked()){
                    Toast.makeText(ChequeoSintomas.this,"Tomar distancia de sus cercanos y realizarse una prueba para descartar si es positivo de Covid-19.",Toast.LENGTH_LONG).show();
                }

                Map<String,Object> mapSintomas =new HashMap<>();
                mapSintomas.put("fiebre", tbFiebre.isChecked());
                mapSintomas.put("dolorGarganta",tbDolorGarganta.isChecked());
                mapSintomas.put("tos",tbTos.isChecked());
                mapSintomas.put("dificultadRespirar",tbDificultadRespirar.isChecked());
                mapSintomas.put("perdidaGustoOlfato",tbPerdidaGustoOlfato.isChecked());
                mapSintomas.put("fatiga",tbFatiga.isChecked());
                mapSintomas.put("congestionNasal",tbCongestionNasal.isChecked());

                db.child("Sintomas").child(user.getUid()).setValue(mapSintomas);

            }
        });

    }


}
