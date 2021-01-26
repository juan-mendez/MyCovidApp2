package co.edu.unipiloto.edu.mycovidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSintomas extends AppCompatActivity {
    private String id;
    private String cedula;
    private TextView txt_name;
    private TextView txt_cedula;
    private TextView txt_age;
    private TextView txt_phone;
    private TextView txt_zona;
    private TextView txt_state;
    private TextView txt_congestionNasal;
    private TextView txt_dificultadRespirar;
    private TextView txt_dolorGarganta ;
    private TextView txt_fatiga;
    private TextView txt_tos;
    private TextView txt_fiebre;
    private TextView txt_perdidaGustoOlfato;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sintomas);
        db = FirebaseDatabase.getInstance().getReference();
        txt_name=(TextView)findViewById(R.id.txt_name);
        txt_cedula=(TextView)findViewById(R.id.txt_cedula);
        txt_age=(TextView)findViewById(R.id.txt_age);
        txt_phone=(TextView)findViewById(R.id.txt_phone);
        txt_zona=(TextView)findViewById(R.id.txt_zona);
        txt_state=(TextView)findViewById(R.id.txt_state);
        txt_congestionNasal=(TextView)findViewById(R.id.txt_congestionNasal);
        txt_dificultadRespirar=(TextView)findViewById(R.id.txt_dificultadRespirar);
        txt_dolorGarganta=(TextView)findViewById(R.id.txt_dolorGarganta);
        txt_fatiga=(TextView)findViewById(R.id.txt_fatiga);
        txt_tos=(TextView)findViewById(R.id.txt_tos);
        txt_fiebre=(TextView)findViewById(R.id.txt_fiebre);
        txt_perdidaGustoOlfato=(TextView)findViewById(R.id.txt_perdidaGustoOlfato);
        cedula=getIntent().getStringExtra("cedula");

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.child("Users").getChildren()){
                    if(ds.child("PersonalInfo").child("cedula").getValue().toString().equals(cedula)){
                        id= ds.getKey();
                        txt_name.setText(txt_name.getText()+" "+ds.child("PersonalInfo").child("name").getValue().toString());
                        txt_cedula.setText(txt_cedula.getText()+" "+cedula);
                        txt_age.setText(txt_age.getText()+" "+ds.child("PersonalInfo").child("edad").getValue().toString());
                        txt_phone.setText(txt_phone.getText()+" "+ds.child("PersonalInfo").child("phone").getValue().toString());
                        txt_state.setText(txt_state.getText()+" "+ds.child("PersonalInfo").child("estado").getValue().toString());
                        if(snapshot.child("Sintomas").child(id).exists()) {

                            txt_congestionNasal.setText(snapshot.child("Sintomas").child(id).child("congestionNasal").getValue().toString());
                            txt_dificultadRespirar.setText(snapshot.child("Sintomas").child(id).child("dificultadRespirar").getValue().toString());
                            txt_dolorGarganta.setText(snapshot.child("Sintomas").child(id).child("dolorGarganta").getValue().toString());
                            txt_fatiga.setText(snapshot.child("Sintomas").child(id).child("fatiga").getValue().toString());
                            txt_fiebre.setText(snapshot.child("Sintomas").child(id).child("fiebre").getValue().toString());
                            txt_perdidaGustoOlfato.setText(snapshot.child("Sintomas").child(id).child("perdidaGustoOlfato").getValue().toString());
                            txt_tos.setText(snapshot.child("Sintomas").child(id).child("tos").getValue().toString());
                        }

                    }
                    // cedulas.add(ds.child("PersonalInfo").child("cedula").getValue().toString());
                }
                Toast.makeText(UserSintomas.this, "id pulsado: "+ id, Toast.LENGTH_LONG).show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}