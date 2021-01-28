package co.edu.unipiloto.edu.mycovidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PersonasConExamen extends AppCompatActivity {
    private ListView listexam;
    private ArrayList<String> cedulas ;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas_con_examen);
        db = FirebaseDatabase.getInstance().getReference();
        listexam=(ListView)findViewById(R.id.list_exam);
        cedulas= new ArrayList<String>();
        String fecha=getIntent().getStringExtra("fecha");


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               if(snapshot.child("Examen").exists()) {
                   cedulas.clear();
                   for (DataSnapshot ds : snapshot.child("Examen").getChildren()) {
                       if(ds.child("fecha").getValue().toString().equals(fecha)) {
                           cedulas.add(snapshot.child("Users").child(ds.getKey()).child("PersonalInfo").child("cedula").getValue().toString());
                       }
                   }

                   //cedulas.add(snapshot.child("PBkf6G1Wo7TTchA9OZXkv28ysFB2").child("PersonalInfo").child("cedula").getValue().toString());
                   ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(PersonasConExamen.this, android.R.layout.simple_list_item_1, cedulas);
                   listexam.setAdapter(listAdapter);
               }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        listexam.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),UserSintomas.class).putExtra("cedula",cedulas.get(position)));

            }
        });
    }
}