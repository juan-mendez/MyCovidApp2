package co.edu.unipiloto.edu.mycovidapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    //Button btnGPS, btn_miUbicacion, btn_chequeoSintomas;
    TextView tv_name;

    FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
    Button btn_salir, btn_datos;
    ImageButton ib_mapsmenu, ib_chequeoSintomas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //  tvUbicacion = (TextView) findViewById(R.id.tvUbicacion);

        ib_mapsmenu = (ImageButton)findViewById(R.id.ib_mapsmenu);
        ib_mapsmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsMenu.class);
                startActivity(intent);
            }
        });

        /*btn_miUbicacion = (Button) findViewById(R.id.btn_miUbicacion);
        btn_miUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity1.class);
                startActivity(intent);
            }
        });
*/
        btn_salir= (Button) findViewById(R.id.btn_salir);
        btn_salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        btn_datos = (Button)findViewById((R.id.btn_datos));
        btn_datos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ConfPerfil.class);
                startActivity(intent);
            }
        });
        /*
        btnGPS = (Button) findViewById(R.id.btn_reportarUbicacion);
        btnGPS.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LocationManager locationManager = (LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener()
                {
                    public void onLocationChanged( Location location) {
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(MainActivity.this, "Estamos ubicados en: \n"+location.getLatitude()+" "+location.getLongitude(), duration);
                        toast.show();
                      //  tvUbicacion.setText(""+location.getLatitude()+" "+location.getLongitude());
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras){}
                    public void onProviderEnabled( String provider) { }
                    public void onProviderDisabled( String provider) { }
                };
                int permissionCheck= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        });*/

        ib_chequeoSintomas = (ImageButton)findViewById(R.id.ib_chequeoSintomas);
        ib_chequeoSintomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChequeoSintomas.class);
                startActivity(intent);
            }
        });

        /*        btn_chequeoSintomas = (Button)findViewById(R.id.btn_chequeoSintomas);
        btn_chequeoSintomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChequeoSintomas.class);
                startActivity(intent);
            }
        });*/

        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_name.setText(authUser.getDisplayName()+".");

    }


    private  void signOut(){
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>(){
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent= new Intent(MainActivity.this,LoginActivity.class);

                startActivity(intent);
                finish();            }
        });

    }


}