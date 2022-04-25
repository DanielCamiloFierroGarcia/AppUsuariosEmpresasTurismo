package com.example.aplicacionusuariosempresa.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicacionusuariosempresa.Modelo.Ubicacion;
import com.example.aplicacionusuariosempresa.R;

import java.util.ArrayList;

public class AdaptadorUbicacion extends RecyclerView.Adapter<AdaptadorUbicacion.ViewHolder> {

    private int resource;
    private ArrayList<Ubicacion> ubicacionList;

    public AdaptadorUbicacion(ArrayList<Ubicacion> ubicacionList, int resource){
        this.resource = resource;
        this.ubicacionList = ubicacionList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ubicacion ubicacion = ubicacionList.get(position);

        holder.cont.setText("   Ubicacion " + (position+1));
        holder.textName.setText("   Nombre " + ubicacion.getNombre());
        holder.textLongitud.setText("   Longitud " + ubicacion.getLongitud());
        holder.textLatitud.setText("    Latitud " + ubicacion.getLatitud() + "\n");

    }

    @Override
    public int getItemCount() {
        return ubicacionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textLongitud, textName, textLatitud, cont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textName = (TextView) itemView.findViewById(R.id.nombre_ubi);
            this.textLongitud = (TextView) itemView.findViewById(R.id.x_ubicacion);
            this.textLatitud = (TextView) itemView.findViewById(R.id.y_ubicacion);
            this.cont = (TextView) itemView.findViewById(R.id.contador);
        }



    }

}
