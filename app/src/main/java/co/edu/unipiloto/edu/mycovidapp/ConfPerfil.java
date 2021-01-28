package co.edu.unipiloto.edu.mycovidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfPerfil extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference db;
    ToggleButton tb_estado;
    TextView tvE_nombre, tvE_identificacion, tvE_localidad, tvE_email , tvE_edad ,tvE_telefono;
    Button btn_actualizar;
    String fecha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_perfil);

        db = FirebaseDatabase.getInstance().getReference();

        tvE_nombre = (TextView)findViewById(R.id.tvE_nombre);
        tvE_identificacion = (TextView)findViewById(R.id.tvE_identificacion);
        tvE_email = (TextView)findViewById(R.id.tvE_email);
        tvE_edad = (TextView)findViewById(R.id.tvE_edad);
        tvE_telefono = (TextView)findViewById(R.id.tvE_telefono);
        tvE_localidad = (TextView)findViewById(R.id.tvE_localidad);
        btn_actualizar = (Button)findViewById(R.id.btn_actualizar);

        tvE_nombre.setText(user.getDisplayName());
        tvE_email.setText(user.getEmail());

        tb_estado = (ToggleButton)findViewById(R.id.tb_estado);


        db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tvE_identificacion.setText(snapshot.child(user.getUid()).child("PersonalInfo").child("cedula").getValue().toString());
                tvE_edad.setText(snapshot.child(user.getUid()).child("PersonalInfo").child("edad").getValue().toString());
                tvE_telefono.setText(snapshot.child(user.getUid()).child("PersonalInfo").child("phone").getValue().toString());
                tvE_localidad.setText(snapshot.child(user.getUid()).child("PersonalInfo").child("localidad").getValue().toString());
                if(snapshot.child(user.getUid()).child("PersonalInfo").child("estado").getValue().toString().equals("0")){
                    tb_estado.setChecked(false);
                }else{
                    tb_estado.setChecked(true);
                }
            }

            @Override public void onCancelled(DatabaseError error) { }
        });

        btn_actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                fecha= dateFormat.format(date)+"";

                Map<String,Object> mapuser=new HashMap<>();
                mapuser.put("name", tvE_nombre.getText().toString());
                mapuser.put("cedula", tvE_identificacion.getText().toString());
                mapuser.put("email", tvE_email.getText().toString());
                mapuser.put("edad", tvE_edad.getText().toString());
                mapuser.put("localidad", tvE_localidad.getText().toString());
                mapuser.put("phone", tvE_telefono.getText().toString());
                if (tb_estado.isChecked()){
                    mapuser.put("estado",1);
                }else{
                    mapuser.put("estado",0);
                }
                mapuser.put("fecha",fecha);

                db.child("Users").child(user.getUid()).child("PersonalInfo").setValue(mapuser);
                finish();

                Toast.makeText(ConfPerfil.this, "Datos actualizados", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
