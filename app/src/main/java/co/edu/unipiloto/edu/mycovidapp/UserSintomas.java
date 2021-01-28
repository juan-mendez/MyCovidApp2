package co.edu.unipiloto.edu.mycovidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserSintomas extends AppCompatActivity {
    private String id;
    private String cedula,email,name;
    private Date fechaActual;
    private TextView txt_name,txt_examen;
    private TextView txt_cedula;
    private TextView txt_age;
    private TextView txt_phone;
    private TextView txt_zona;
    private TextView txt_state;
    private TextView txt_S1;
    private TextView txt_S2;
    private TextView txt_S3;
    private TextView txt_S4;
    private TextView txt_S7;
    private TextView txt_S5;
    private TextView txt_S6;
    private DatabaseReference db;
    private Button btn_reportar,btn_notificar,btn_citar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sintomas);
        db = FirebaseDatabase.getInstance().getReference();
        btn_citar=(Button)findViewById(R.id.btn_citar);
        btn_notificar=(Button)findViewById(R.id.btn_notificar);
        btn_reportar=(Button)findViewById(R.id.btn_reportar);
        txt_name=(TextView)findViewById(R.id.txt_name);
        txt_cedula=(TextView)findViewById(R.id.txt_cedula);
        txt_age=(TextView)findViewById(R.id.txt_age);
        txt_phone=(TextView)findViewById(R.id.txt_phone);
        txt_zona=(TextView)findViewById(R.id.txt_zona);
        txt_state=(TextView)findViewById(R.id.txt_state);
        txt_examen=(TextView)findViewById(R.id.txt_examen);
        txt_S1 =(TextView)findViewById(R.id.txt_S1);
        txt_S2 =(TextView)findViewById(R.id.txt_S2);
        txt_S3 =(TextView)findViewById(R.id.txt_S3);
        txt_S4 =(TextView)findViewById(R.id.txt_S4);
        txt_S7 =(TextView)findViewById(R.id.txt_S7);
        txt_S5 =(TextView)findViewById(R.id.txt_S5);
        txt_S6 =(TextView)findViewById(R.id.txt_S6);
        cedula=getIntent().getStringExtra("cedula");




        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.child("Users").getChildren()){
                    if(ds.child("PersonalInfo").child("cedula").getValue().toString().equals(cedula)){
                        email=ds.child("PersonalInfo").child("email").getValue().toString();
                        name=ds.child("PersonalInfo").child("name").getValue().toString();
                        id= ds.getKey();
                        txt_name.setText(txt_name.getText()+" "+name);
                        txt_cedula.setText(txt_cedula.getText()+" "+cedula);
                        txt_age.setText(txt_age.getText()+" "+ds.child("PersonalInfo").child("edad").getValue().toString());
                        txt_phone.setText(txt_phone.getText()+" "+ds.child("PersonalInfo").child("phone").getValue().toString());
                        txt_zona.setText(txt_zona.getText()+" "+ds.child("PersonalInfo").child("localidad").getValue().toString());
                        if (ds.child("PersonalInfo").child("estado").getValue().toString().equals("0")) {
                            txt_state.setText(txt_state.getText() + " " + "Negativo" );
                        }else{
                            txt_state.setText(txt_state.getText() + " " + "Positivo" );
                        }

                    }

                }
                if(snapshot.child("Sintomas").child(id).exists()) {
                    int i=0;

                    for (DataSnapshot ds: snapshot.child("Sintomas").child(id).getChildren()){
                        if(ds.getValue().toString().equals("true")){
                            //Toast.makeText(UserSintomas.this, "id pulsado: "+ ds.getKey(), Toast.LENGTH_LONG).show();
                            i++;
                            switch (i){
                                case 1:
                                    txt_S1.setText(sintomas(ds.getKey()));
                                    break;
                                case 2:
                                    txt_S2.setText(sintomas(ds.getKey()));
                                    break;
                                case 3:
                                    txt_S3.setText(sintomas(ds.getKey()));
                                    break;
                                case 4:
                                    txt_S4.setText(sintomas(ds.getKey()));
                                    break;
                                case 5:
                                    txt_S5.setText(sintomas(ds.getKey()));
                                    break;
                                case 6:
                                    txt_S6.setText(sintomas(ds.getKey()));
                                    break;
                                case 7:
                                    txt_S7.setText(sintomas(ds.getKey()));
                                    break;
                            }
                        }

                    }


                }
                if(snapshot.child("Examen").child(id).exists()){
                    if (snapshot.child("Examen").child(id).child("entrega").getValue().toString().equals("True")){
                        txt_examen.setText("El usuario tomó el examen el "+ snapshot.child("Examen").child(id).child("fecha").getValue().toString()+", fue entregado el  "+
                                snapshot.child("Examen").child(id).child("fechaEntrega").getValue().toString()+", con un resultado de "+
                                snapshot.child("Examen").child(id).child("resultado").getValue().toString()+" para covid");
                    }else{
                        Date date = new Date();
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        fechaActual = date;
                        Date fechaExam= null;
                        try {
                            fechaExam = dateFormat.parse(snapshot.child("Examen").child(id).child("fecha").getValue().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        int dias=(int) ((fechaActual.getTime()-fechaExam.getTime())/86400000);
                        txt_examen.setText("El usuario tomó el examen el "+ snapshot.child("Examen").child(id).child("fecha").getValue().toString()+", no ha sido entregado y" +
                                " lleva en revisión "+ dias+ " días");
                    }
                }else {
                    txt_examen.setText("El usuario no ha tomado el examen");
                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.child("Users").child(id).child("PersonalInfo").child("estado").setValue(1);
            }
        });

        btn_notificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_citar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fecha = dateFormat.format(date)+"";

                Map<String,Object> mapexam=new HashMap<>();
                mapexam.put("fecha",fecha);
                mapexam.put("entrega",false);
                mapexam.put("fechaEntrega",null);
                mapexam.put("resultado",null);
                db.child("Examen").child(id).setValue(mapexam);


                // Intent intent = new Intent(this,ReceiveMessageActivity.class);
                //intent.putExtra(ReceiveMessageActivity.EXTRA_MESSAGE,messageText);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Toma de examen Covid-19");
                intent.putExtra(Intent.EXTRA_TEXT, "Sr/a "+name+" se envia correo de cita para la toma de examen el "+ fecha );
                intent.setType("text/plain");

                startActivity((intent));
            }
        });

    }
    public String sintomas(String sintoma){

        switch (sintoma){
            case "congestionNasal":
                return "Congestion Nasal";
            case "dificultadRespirar":
                return "Dificultad para Respirar";
            case "dolorGarganta":
                return "Dolor de Garganta";
            case "fatiga":
                return "Fatiga";
            case "fiebre":
                return "Fiebre";
            case "perdidaGustoOlfato":
                return "Perdida de Gusto o Olfato";
            case "tos":
                return "Tos";
        }
        return null;
    }
}