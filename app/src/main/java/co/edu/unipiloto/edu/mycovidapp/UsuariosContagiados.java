package co.edu.unipiloto.edu.mycovidapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsuariosContagiados extends AppCompatActivity {
    private ListView listUsuariosContagiados;
    private ArrayList<String> cedulas, cedulasPositivas ;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_contagiados);
        db = FirebaseDatabase.getInstance().getReference();
        listUsuariosContagiados=(ListView)findViewById(R.id.list_usuariosContagiados);
        cedulas= new ArrayList<String>();
        cedulasPositivas= new ArrayList<String>();
        String localidad =getIntent().getStringExtra("localidad");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Users").exists()) {
                    cedulas.clear();
                    cedulasPositivas.clear();
                    for (DataSnapshot ds : snapshot.child("Users").getChildren()) {
                        if(ds.child("PersonalInfo").child("localidad").getValue().toString().equals(localidad)) {
                            cedulas.add(ds.child("PersonalInfo").child("cedula").getValue().toString());
                            if(ds.child("PersonalInfo").child("estado").getValue().toString().equals("1")){
                               cedulasPositivas.add(ds.child("PersonalInfo").child("cedula").getValue().toString());

                            }
                        }
                    }

                    Map<String,Object> maplocalidad=new HashMap<>();

                    maplocalidad.put("totalUsuarios",cedulas.size());
                    maplocalidad.put("totalContagiados",cedulasPositivas.size());
                    maplocalidad.put("taza",cedulasPositivas.size()*100/cedulas.size()+"%");
                    if (snapshot.child("Localidades").child(localidad).exists()){
                    maplocalidad.put("estado",snapshot.child("Localidades").child(localidad).child("estado").getValue().toString());
                    }else{
                        maplocalidad.put("estado",0);
                    }

                    db.child("Localidades").child(localidad).setValue(maplocalidad);
                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(UsuariosContagiados.this, android.R.layout.simple_list_item_1, cedulas);
                    listUsuariosContagiados.setAdapter(listAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(UsuariosContagiados.this, "La localidad de " + localidad+
                        " cuenta con "+ cedulasPositivas.size()+" casos positivos de covid", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}