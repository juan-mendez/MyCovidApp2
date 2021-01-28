package co.edu.unipiloto.edu.mycovidapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MapsMenu extends AppCompatActivity {
    ImageButton ib_miUbicacion, ib_miLocacion,ib_recorrido, ib_chequeoSintomas, ib_perfil;


    double lat = 0.0, lon=0.0;
    DatabaseReference db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsmenu1);
        db = FirebaseDatabase.getInstance().getReference();

        ib_miUbicacion = (ImageButton) findViewById(R.id.ib_miUbicacion);
        ib_miUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity1.class);
                startActivity(intent);
            }
        });

        ib_miLocacion = (ImageButton)findViewById(R.id.ib_miLocacion);
        ib_miLocacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> mapUbicacion =new HashMap<>();
                LocationManager locationManager = (LocationManager) MapsMenu.this.getSystemService(Context.LOCATION_SERVICE);
                Time ahoraTiempo = new Time(Time.getCurrentTimezone());

                LocationListener locationListener = new LocationListener(){
                    public void onLocationChanged(Location location) {
                        lat = location.getLatitude();
                        lon = location.getLongitude();
                        db.child("Users").child(user.getUid()).child("PersonalInfo").child("ubicacion").setValue(lat+";"+lon);

                        ahoraTiempo.setToNow();
                        String fecha = ahoraTiempo.monthDay + "-" + ahoraTiempo.month+1 + "-" + ahoraTiempo.year;
                        mapUbicacion.put(ahoraTiempo.hour+":"+ahoraTiempo.minute, fecha +"/"+ahoraTiempo.hour+":"+ahoraTiempo.minute+"/"+ lat+";"+lon);
                        db.child("Ubicacion").child(user.getUid()).setValue(mapUbicacion);
                    }

                };
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                int permissionCheck= ContextCompat.checkSelfPermission(MapsMenu.this, Manifest.permission.ACCESS_FINE_LOCATION);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
            }
        });
        int permissionCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck== PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {
            } else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }

        ib_recorrido = (ImageButton)findViewById(R.id.ib_recorrido);
        ib_recorrido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecorridoMapsActivity.class);
                startActivity(intent);
            }
        });



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
