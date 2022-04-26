package com.example.aplicacionusuariosempresa;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.aplicacionusuariosempresa.Adaptadores.AdaptadorUbicacion;
import com.example.aplicacionusuariosempresa.Modelo.Ubicacion;
import com.example.aplicacionusuariosempresa.databinding.ActivityVerUbicacionesBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.aplicacionusuariosempresa.databinding.VerUbicacionMapaBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class verUbicacionMapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityVerUbicacionesBinding binding;//ActivityVerUbicacionMapaBinding
    FirebaseDatabase database;
    DatabaseReference myRef;

    FirebaseAuth auth;
    private ArrayList<Ubicacion> ubicacionesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        auth = FirebaseAuth.getInstance();

        binding = ActivityVerUbicacionesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getUbicacionesFirebase(googleMap);

        // Add a marker in Sydney and move the camera

      //  mMap.addMarker(new MarkerOptions().position(primera_posicion).title(ubicacionesList.get(0).getNombre()));

    }

    public void getUbicacionesFirebase(GoogleMap googleMap){

        myRef.child("Registro_ubicaciones").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMap = googleMap;
                if(snapshot.exists()){
                    ubicacionesList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        String longitud = ds.child("longitud").getValue().toString();
                        String latitud = ds.child("latitud").getValue().toString();
                        ubicacionesList.add(new Ubicacion(longitud, latitud, nombre));
                    }
                    LatLng posicion_actual = new LatLng(Double.parseDouble(ubicacionesList.get(0).getLatitud()), Double.parseDouble(ubicacionesList.get(0).getLongitud()));
                    mMap.addMarker(new MarkerOptions().position(posicion_actual).title(ubicacionesList.get(0).getNombre()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(posicion_actual));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(12),2000,null);
                    for(int i = 1; i < ubicacionesList.size(); i++){

                        posicion_actual = new LatLng(Double.parseDouble(ubicacionesList.get(i).getLatitud()), Double.parseDouble(ubicacionesList.get(i).getLongitud()));
                        mMap.addMarker(new MarkerOptions().position(posicion_actual).title(ubicacionesList.get(i).getNombre()));

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}