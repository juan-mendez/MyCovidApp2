
package co.edu.unipiloto.edu.mycovidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TomaDeDecisionesMain extends AppCompatActivity {

    private  Button btn_salir,btn_zonas,btn_Consultar,btn_search;
    private EditText txt_consul;
    private ListView listzonas;
    private ArrayList<String> localidades;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toma_de_decisiones_main);
        db = FirebaseDatabase.getInstance().getReference();
        btn_zonas = (Button) findViewById(R.id.btn_zonas);
        btn_search = (Button) findViewById(R.id.btn_searchT);
        btn_Consultar = (Button) findViewById(R.id.btn_consultarT);
        btn_salir = (Button) findViewById(R.id.btn_salirT);
        listzonas=(ListView)findViewById(R.id.list_zonas);
        txt_consul=(EditText) findViewById(R.id.txt_consulT);
        localidades= new ArrayList<String>();

        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                localidades.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                   if(!localidades.contains(ds.child("PersonalInfo").child("localidad").getValue().toString()))
                   {
                       localidades.add(ds.child("PersonalInfo").child("localidad").getValue().toString());
                   }
                }

                //cedulas.add(snapshot.child("PBkf6G1Wo7TTchA9OZXkv28ysFB2").child("PersonalInfo").child("cedula").getValue().toString());
                ArrayAdapter<String>listAdapter= new ArrayAdapter<String>(TomaDeDecisionesMain.this, android.R.layout.simple_list_item_1,localidades);
                listzonas.setAdapter(listAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_zonas.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if(listzonas.getVisibility() == 0){
                    listzonas.setVisibility(View.INVISIBLE);
                }else {
                    txt_consul.setVisibility(View.INVISIBLE);
                    btn_search.setVisibility(View.INVISIBLE);
                    listzonas.setVisibility(View.VISIBLE);
                }

                // Toast.makeText(HospitalMain.this, "esta "+ listUsers.getVisibility(), Toast.LENGTH_LONG).show();
            }
        });

        btn_Consultar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if(txt_consul.getVisibility() == 0 && txt_consul.getVisibility()==0){
                    txt_consul.setVisibility(View.INVISIBLE);
                    btn_search.setVisibility(View.INVISIBLE);
                }else {
                    txt_consul.setVisibility(View.VISIBLE);
                    btn_search.setVisibility(View.VISIBLE);
                    listzonas.setVisibility(View.INVISIBLE);
                }

                // Toast.makeText(HospitalMain.this, "esta "+ txt_consual.getVisibility(), Toast.LENGTH_LONG).show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(TomaDeDecisionesMain.this, LocalidadInfo.class)
                        .putExtra("localidad",txt_consul.getText().toString()));




            }
        });

        listzonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), UsuariosContagiados.class).putExtra("localidad",localidades.get(position)));
                // Toast.makeText(HospitalMain.this, "Has pulsado: "+ cedulas.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    private  void signOut(){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>(){
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent= new Intent(getApplicationContext(), LoginActivity.class);

                startActivity(intent);
                finish();            }
        });

    }
}