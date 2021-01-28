package co.edu.unipiloto.edu.mycovidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {
    SignInButton btn_login;
    LoginButton btn_loginFa;
    DatabaseReference db;
    String provider;
    //List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build(),new AuthUI.IdpConfig.GoogleBuilder().build());
    private static final int RC_SIGN_IN=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_login);
        btn_loginFa=(LoginButton)findViewById(R.id.btn_loginfa);
        btn_login= (SignInButton) findViewById(R.id.btn_login_google);

        btn_loginFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provider="Facebook";
                signIn(new AuthUI.IdpConfig.FacebookBuilder().build());
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                provider="Google";
                signIn(new AuthUI.IdpConfig.GoogleBuilder().build());
            }
        });
    }
    private void signIn(AuthUI.IdpConfig provedor){
        Intent signInIntent=AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Collections.singletonList(provedor)).build();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this,"Bienvenid@ ${user!!.displayname}",Toast.LENGTH_LONG).show();

                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("Entidades").child(user.getUid()).exists())
                        {
                            String tipo =snapshot.child("Entidades").child(user.getUid()).child("tipo").getValue().toString();
                            switch (tipo)
                            {
                                case "hospital":
                                    startActivity(new Intent(getApplicationContext(), HospitalMain.class));
                                    finish();
                                    break;
                                case "seguimiento":
                                    startActivity(new Intent(getApplicationContext(), SeguimientoDeContagiosMain.class));
                                    finish();
                                    break;
                                case "decisiones":
                                    startActivity(new Intent(getApplicationContext(), TomaDeDecisionesMain.class));
                                    finish();
                                    break;
                            }
                        }else {
                            if (snapshot.child("Users").child(user.getUid()).exists()) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                db.child("Users").child(user.getUid()).child("PersonalInfo").child("proveedor").setValue(provider);
                            } else {
                                Intent intent = new Intent(LoginActivity.this, UsersData.class);
                                startActivity(intent);
                            }
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            } else {
                Toast.makeText(this,"Error encontrado ${response!!.getError()!!.getErrorCode()!!}",Toast.LENGTH_LONG).show();
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}