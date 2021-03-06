package co.edu.unipiloto.edu.mycovidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeguimientoDeContagiosMain extends AppCompatActivity {
    private Button btn_salir;
    private Button btn_Users;
    private Button btn_Consultar;
    private Button btn_search;
    private EditText txt_consul;
    private ListView listUsersContagiados;
    private ArrayList<String> cedulas;
    private DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguimiento_de_contagios_main);
        db = FirebaseDatabase.getInstance().getReference();
        txt_consul=(EditText)findViewById(R.id.txt_consulS);
        btn_search=(Button)findViewById(R.id.btn_searchS);
        btn_Users=(Button)findViewById(R.id.btn_UsersContagiados);
        btn_Consultar=(Button)findViewById(R.id.btn_consultarS);
        listUsersContagiados=(ListView)findViewById(R.id.list_usersContagiados);
        cedulas= new ArrayList<String>();

        btn_salir= (Button) findViewById(R.id.btn_salirS);
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });



        db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cedulas.clear();

                for (DataSnapshot ds: snapshot.getChildren()){

                    if(ds.child("PersonalInfo").child("estado").getValue().toString().equals("1")) {
                        cedulas.add(ds.child("PersonalInfo").child("cedula").getValue().toString());
                    }
                }
                ArrayAdapter<String> listAdapter= new ArrayAdapter<String>(SeguimientoDeContagiosMain.this, android.R.layout.simple_list_item_1,cedulas);
                listUsersContagiados.setAdapter(listAdapter);
            }
            @Override public void onCancelled(@NonNull DatabaseError error) { }
        });


        btn_Users.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if(listUsersContagiados.getVisibility() == 0){
                    listUsersContagiados.setVisibility(View.INVISIBLE);
                }else {
                    txt_consul.setVisibility(View.INVISIBLE);
                    btn_search.setVisibility(View.INVISIBLE);
                    listUsersContagiados.setVisibility(View.VISIBLE);
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
                    listUsersContagiados.setVisibility(View.INVISIBLE);
                }
                // Toast.makeText(HospitalMain.this, "esta "+ txt_consual.getVisibility(), Toast.LENGTH_LONG).show();
            }
        });

        listUsersContagiados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(SeguimientoDeContagiosMain.this, "Has pulsado: "+ cedulas.get(position), Toast.LENGTH_LONG).show();
            }
        });

    }
    private  void signOut(){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>(){
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent= new Intent(getApplicationContext(),LoginActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}