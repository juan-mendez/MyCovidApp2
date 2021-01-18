package co.edu.unipiloto.edu.mycovidapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
SignInButton btn_login;
LoginButton btn_loginFa;
//List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.FacebookBuilder().build(),new AuthUI.IdpConfig.GoogleBuilder().build());
private static final int RC_SIGN_IN=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_loginFa=(LoginButton)findViewById(R.id.btn_loginfa);
        btn_login= (SignInButton) findViewById(R.id.btn_login);

        btn_loginFa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(new AuthUI.IdpConfig.FacebookBuilder().build());
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Toast.makeText(this,"Bienvenido@ ${user!!.displayname}",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();
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