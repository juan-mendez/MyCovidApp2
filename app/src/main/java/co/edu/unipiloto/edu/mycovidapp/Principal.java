package co.edu.unipiloto.edu.mycovidapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Principal extends AppCompatActivity {
    FirebaseAuth authUser=FirebaseAuth.getInstance();
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance().getReference();
        onActivityResult();
    }
    protected void onActivityResult() {

        if (authUser.getCurrentUser() != null) {
            db.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(authUser.getCurrentUser().getUid()).exists()){
                        Intent intent= new Intent(Principal.this,MainActivity.class);
                        startActivity(intent);
                        finish();


                    }else{
                        //String provider= getIntent().getExtras().getString("Proveedor");
                        Intent intent= new Intent(getApplicationContext(),UsersData.class);
                        startActivity(intent);
                        finish();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        } else {
            Intent intent= new Intent(Principal.this,LoginActivity.class);

            startActivity(intent);
            finish();
        }
    }
}