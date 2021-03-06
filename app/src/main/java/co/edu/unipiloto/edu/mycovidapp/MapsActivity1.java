package co.edu.unipiloto.edu.mycovidapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapsActivity1 extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<String> ubicacion;
    private DatabaseReference db;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapsMenu mapsMenu = new MapsMenu();
        db = FirebaseDatabase.getInstance().getReference();
        ubicacion = new ArrayList<String>();

        db.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMap.clear();
                ubicacion.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    if (ds.child("PersonalInfo").child("estado").getValue().toString().equals("1")){
                        ubicacion.add(ds.child("PersonalInfo").child("ubicacion").getValue().toString());
                    }
                }
                for (int i=0; i<ubicacion.size(); i++){
                    String[] latLon = ubicacion.get(i).split(";");
                    //LatLng marcador = new LatLng(Double.parseDouble(latLon[0]),Double.parseDouble(latLon[1]));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(latLon[0]),Double.parseDouble(latLon[1]))).title("Positivo").icon(BitmapDescriptorFactory.fromResource(R.drawable.imagemarkercovid)));
                    //mMap.addMarker(new MarkerOptions().position(marcador).title("marcador " +i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(latLon[0]),Double.parseDouble(latLon[1])),12));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

}