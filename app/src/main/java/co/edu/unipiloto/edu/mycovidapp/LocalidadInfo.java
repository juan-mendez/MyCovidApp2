package co.edu.unipiloto.edu.mycovidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LocalidadInfo extends AppCompatActivity {
    private TextView txt_zona,txt_totalUsuarios,txt_totalContagiados,txt_taza,txt_state;
    private Button btn_notificar,btn_aislamiento;
    private String localidad,estadoN;
    private ArrayList<String> totalusuarios,totalContagiados,emails;
    private DatabaseReference db;
   // private String[] emails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localidad_info);
        db = FirebaseDatabase.getInstance().getReference();
        totalContagiados=new ArrayList<String>();
        totalusuarios=new ArrayList<String>();
        emails=new ArrayList<String>();

        btn_aislamiento=(Button)findViewById(R.id.btn_aislamiento);
        btn_notificar=(Button)findViewById(R.id.btn_notificarT);
        txt_zona=(TextView)findViewById(R.id.txt_zonaT);
        txt_totalUsuarios=(TextView)findViewById(R.id.txt_totalUsuarios);
        txt_totalContagiados=(TextView)findViewById(R.id.txt_totalContagiados);
        txt_state=(TextView)findViewById(R.id.txt_stateT);
        txt_taza=(TextView)findViewById(R.id.txt_taza);
        localidad=getIntent().getStringExtra("localidad");
        estadoN="";
        //emails=new String[100];

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Localidades").child(localidad).exists()) {
                    emails.clear();
                    for (DataSnapshot ds : snapshot.child("Users").getChildren()){
                       // int i=0;
                        if(ds.child("PersonalInfo").child("localidad").getValue().toString().equals(localidad)) {

                            emails.add(ds.child("PersonalInfo").child("email").getValue().toString());

                        }
                        //i++;
                    }

                    txt_zona.setText(" Localidad:" + " " + localidad);
                    txt_totalUsuarios.setText(" Usuarios:" + " " + snapshot.child("Localidades").
                            child(localidad).child("totalUsuarios").getValue().toString());
                    txt_totalContagiados.setText(" Usuarios Positivos:" + " " + snapshot.child("Localidades").
                            child(localidad).child("totalContagiados").getValue().toString());

                    if (snapshot.child("Localidades").child(localidad).child("estado").getValue().toString().equals("0")) {
                         estadoN="Cierre";
                        txt_state.setText(" Estado:" + " " + "No se encuentra en Aislamiento");
                    } else {
                         estadoN="Inicio";
                        txt_state.setText(" Estado:" + " " + "Localidad en Aislamiento");
                    }

                    txt_taza.setText(" Taza de Contagiados:"+" "+snapshot.child("Localidades").
                            child(localidad).child("taza").getValue().toString());

                } else {
                    emails.clear();
                    totalusuarios.clear();
                    totalContagiados.clear();
                    for (DataSnapshot ds : snapshot.child("Users").getChildren()){
                        int i=0;
                        if(ds.child("PersonalInfo").child("localidad").getValue().toString().equals(localidad)) {
                            totalusuarios.add(ds.child("PersonalInfo").child("cedula").getValue().toString());
                            emails.add(ds.child("PersonalInfo").child("email").getValue().toString());
                            if(ds.child("PersonalInfo").child("estado").getValue().toString().equals("1")) {
                                totalContagiados.add(ds.child("PersonalInfo").child("cedula").getValue().toString());

                            }
                        }
                        i++;
                    }
                    Map<String,Object> maplocalidad=new HashMap<>();
                    maplocalidad.put("totalUsuarios",totalusuarios.size());
                    maplocalidad.put("totalContagiados",totalContagiados.size());
                    maplocalidad.put("taza",totalContagiados.size()*100/totalusuarios.size());
                    maplocalidad.put("estado",0);
                    db.child("Localidades").child(localidad).setValue(maplocalidad);


                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_notificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = dateFormat.format(date)+"";


                // Intent intent = new Intent(this,ReceiveMessageActivity.class);
                //intent.putExtra(ReceiveMessageActivity.EXTRA_MESSAGE,messageText);
                Intent intent = new Intent(Intent.ACTION_SEND);
                String[]emailsE=new String[emails.size()];
                for(int i=0; i<emails.size(); i++){
                    emailsE[i]=emails.get(i);

                }

                intent.putExtra(Intent.EXTRA_EMAIL, emailsE);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Aislamiento localidad de "+ localidad);
                intent.putExtra(Intent.EXTRA_TEXT, "Habitantes de la localidad "+localidad+" se envia este correo para notificar el "
                        +estadoN+ " de aislamiento  en esta zona el  "+ fecha );
                intent.setType("text/plain");

                startActivity((intent));
            }
        });
        btn_aislamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.child("Localidades").child(localidad).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.child("estado").getValue().toString().equals("0")) {
                             estadoN="Inicio";

                            db.child("Localidades").child(localidad).child("estado").setValue(1);
                        }else {
                             estadoN="Cierre";
                            db.child("Localidades").child(localidad).child("estado").setValue(0);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






            }
        });
    }
}