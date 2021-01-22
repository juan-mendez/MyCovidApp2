package co.edu.unipiloto.edu.mycovidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UsersData extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button btn_confirmar;
    DatabaseReference db;
    EditText emailText;
    EditText nameText;
    EditText cedulaText;
    EditText edadText;
    EditText phoneText;
    String cedula;
    String edad;
    String phone;
    int state=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         db = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_users_data);
        btn_confirmar=(Button)findViewById(R.id.btn_confirmar);
        emailText=(EditText)findViewById(R.id.emailText);
        nameText=(EditText)findViewById(R.id.nameText);
        cedulaText=(EditText)findViewById(R.id.cedulaText);
        edadText=(EditText)findViewById(R.id.edadText);
        phoneText=(EditText)findViewById(R.id.phoneText);
        emailText.setText(user.getEmail());
        nameText.setText(user.getDisplayName());


        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cedula= cedulaText.getText().toString();
                edad=edadText.getText().toString();
                phone=phoneText.getText().toString();

                Map<String,Object>mapuser=new HashMap<>();
                mapuser.put("name",user.getDisplayName());
                mapuser.put("email",user.getEmail());
                mapuser.put("estado",state);
                mapuser.put("cedula",cedula);
                mapuser.put("edad",edad);
                mapuser.put("phone",phone);

                db.child("Users").child(user.getUid()).child("PersonalInfo").setValue(mapuser);
                finish();

            }
        });
    }
}