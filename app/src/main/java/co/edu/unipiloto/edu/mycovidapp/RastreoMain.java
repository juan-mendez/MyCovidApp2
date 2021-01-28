package co.edu.unipiloto.edu.mycovidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class RastreoMain extends AppCompatActivity {

    private Button btn_pacientesAislamiento, btn_usersRiesgoContagio, btn_salir;
    private DatabaseReference db;
    private ListView list_users;
    private ArrayList<String> aislados, riesgoContagio;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rastreo);

        db = FirebaseDatabase.getInstance().getReference();
        list_users = (ListView) findViewById(R.id.list_usersAislados);
        aislados = new ArrayList<String>();
        riesgoContagio = new ArrayList<String>();

        btn_salir= (Button) findViewById(R.id.btn_salir);
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aislados.clear();
                for (DataSnapshot ds: snapshot.child("Users").getChildren()){
                    //Toast.makeText(RastreoMain.this, "id alv ", Toast.LENGTH_LONG).show();
                    if(ds.child("PersonalInfo").child("estado").getValue().toString().equals("1")){
                        aislados.add(ds.child("PersonalInfo").child("cedula").getValue().toString());
                        //Toast.makeText(RastreoMain.this, "si: "+ ds.child("PersonalInfo").child("cedula").getValue().toString(), Toast.LENGTH_LONG).show();
                    }
                }

                for (DataSnapshot ds: snapshot.child("Sintomas").getChildren()){
                    if(ds.child("congestionNasal").getValue().toString().equals("true") ||
                            ds.child("dificultadRespirar").getValue().toString().equals("true")||
                            ds.child("dolorGarganta").getValue().toString().equals("true")||
                            ds.child("fatiga").getValue().toString().equals("true")||
                            ds.child("fiebre").getValue().toString().equals("true")||
                            ds.child("perdidaGustoOlfato").getValue().toString().equals("true")||
                            ds.child("tos").getValue().toString().equals("true")){

                        riesgoContagio.add(snapshot.child("Users").child(ds.getKey()).child("PersonalInfo").child("cedula").getValue().toString());
                        //Toast.makeText(RastreoMain.this, "si: "+ snapshot.child("Users").child(ds.getKey()).child("PersonalInfo").child("cedula").getValue().toString(), Toast.LENGTH_LONG).show();
                    }
                }

            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });

        btn_pacientesAislamiento = (Button)findViewById(R.id.btn_pacientesAislamiento);
        btn_pacientesAislamiento.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                list_users.setAdapter(new ArrayAdapter<String>(RastreoMain.this, android.R.layout.simple_list_item_1,aislados));

                if(list_users.getVisibility() == 0){
                    list_users.setVisibility(View.INVISIBLE);
                }else {
                    list_users.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_usersRiesgoContagio = (Button)findViewById(R.id.btn_usersRiesgoContagio);
        btn_usersRiesgoContagio.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                list_users.setAdapter(new ArrayAdapter<String>(RastreoMain.this, android.R.layout.simple_list_item_1,riesgoContagio));

                if(list_users.getVisibility() == 0){
                    list_users.setVisibility(View.INVISIBLE);
                }else {
                    list_users.setVisibility(View.VISIBLE);
                }
            }
        });

        list_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                startActivity(new Intent(getApplicationContext(),UserSintomas.class).putExtra("cedula",aislados.get(position)));
                Toast.makeText(RastreoMain.this, "Has pulsado: "+ aislados.get(position), Toast.LENGTH_LONG).show();
            }
        });

    }
    private  void signOut(){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>(){
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent= new Intent(RastreoMain.this,LoginActivity.class);

                startActivity(intent);
                finish();
            }
        });

    }
}
