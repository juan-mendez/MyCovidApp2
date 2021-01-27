package co.edu.unipiloto.edu.mycovidapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MapsMenu extends AppCompatActivity {
    ImageButton ib_miUbicacion, ib_miLocacion, ib_chequeoSintomas, ib_perfil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsmenu1);

        ib_miUbicacion = (ImageButton) findViewById(R.id.ib_miUbicacion);
        ib_miUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity1.class);
                startActivity(intent);
            }
        });

        ib_miLocacion = (ImageButton)findViewById(R.id.ib_miLocacion);
        ib_miLocacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) MapsMenu.this.getSystemService(Context.LOCATION_SERVICE);
                LocationListener locationListener = new LocationListener(){

                    public void onLocationChanged( Location location) {
                        Toast.makeText(MapsMenu.this, "Estamos ubicados en: \n"
                                +location.getLatitude()+" "
                                +location.getLongitude(), Toast.LENGTH_SHORT).show();
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras){}
                    public void onProviderEnabled( String provider) { }
                    public void onProviderDisabled( String provider) { }
                };

                int permissionCheck= ContextCompat.checkSelfPermission(MapsMenu.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        });
        int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck== PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }


        ib_chequeoSintomas = (ImageButton)findViewById(R.id.ib_chequeoSintomas);
        ib_chequeoSintomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ChequeoSintomas.class);
                startActivity(intent);
            }
        });

        ib_perfil = (ImageButton) findViewById(R.id.ib_perfil);
        ib_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
