package co.edu.unipiloto.edu.mycovidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

public class Principal extends AppCompatActivity {
    FirebaseAuth authUser=FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onActivityResult();
    }
    protected void onActivityResult() {
        if (authUser.getCurrentUser() != null) {
            Intent intent= new Intent(Principal.this,MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent= new Intent(Principal.this,LoginActivity.class);

            startActivity(intent);
            finish();
        }
    }
}