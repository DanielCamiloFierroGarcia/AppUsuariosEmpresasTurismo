package com.example.aplicacionusuariosempresa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.example.aplicacionusuariosempresa.Modelo.Ubicacion;
import com.google.android.gms.internal.maps.zzx;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;

public class OpenStreetMap extends AppCompatActivity {

    MapView map;
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

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_open_street_map);
        map = findViewById(R.id.osmMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setMultiTouchControls(true);

    }
    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
        getUbicacionesFirebase();

    }
    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
    public void getUbicacionesFirebase(){

        myRef.child("Registro_ubicaciones").child(auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ubicacionesList.clear();
                    for (DataSnapshot ds: snapshot.getChildren()) {
                        String nombre = ds.child("nombre").getValue().toString();
                        String longitud = ds.child("longitud").getValue().toString();
                        String latitud = ds.child("latitud").getValue().toString();
                        String tipo = ds.child("tipo").getValue().toString();
                        ubicacionesList.add(new Ubicacion(longitud, latitud, nombre, tipo));
                    }

                    GeoPoint posicion_actual = new GeoPoint(Double.parseDouble(ubicacionesList.get(0).getLatitud()), Double.parseDouble(ubicacionesList.get(0).getLongitud()));


                    IMapController mapController = map.getController();
                    mapController.setCenter(posicion_actual);
                    mapController.setZoom(10);

                    Marker marker = new Marker(map);
                    Drawable myIcon = getResources().getDrawable(R.drawable.ic_location_red);

                    marker.setTitle(ubicacionesList.get(0).getNombre() + "\n Tipo: " + ubicacionesList.get(0).getTipo());
                    marker.setIcon(myIcon);
                    marker.setPosition(posicion_actual);
                    marker.setAnchor(Marker.ANCHOR_CENTER,
                            Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(marker);

                    for(int i = 1; i < ubicacionesList.size(); i++){

                        posicion_actual = new GeoPoint(Double.parseDouble(ubicacionesList.get(i).getLatitud()), Double.parseDouble(ubicacionesList.get(i).getLongitud()));
                        Marker mark = new Marker(map);
                        mark.setTitle(ubicacionesList.get(i).getNombre() + "\n Tipo: " + ubicacionesList.get(i).getTipo());
                        mark.setIcon(myIcon);
                        mark.setPosition(posicion_actual);
                        mark.setAnchor(Marker.ANCHOR_CENTER,
                                Marker.ANCHOR_BOTTOM);
                        map.getOverlays().add(mark);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}