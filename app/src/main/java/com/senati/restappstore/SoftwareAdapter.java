package com.senati.restappstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class SoftwareAdapter extends ArrayAdapter<Software> {
    private Context context;
    private List<Software> softwares;
    private OnViewClickListener viewClickListener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    // Interfaz para manejar el clic en Ver
    public interface OnViewClickListener {
        void onViewClick(int position);
    }

    // Interfaz para manejar el clic en Editar
    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    // Interfaz para manejar el clic en Eliminar
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    // Constructor
    public SoftwareAdapter(Context context, List<Software> softwares,
                           OnViewClickListener viewListener, OnEditClickListener editListener,
                           OnDeleteClickListener deleteListener) {
        super(context, R.layout.activity_software, softwares);
        this.context = context;
        this.softwares = softwares;
        this.viewClickListener = viewListener;
        this.editClickListener = editListener;
        this.deleteClickListener = deleteListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.activity_software, parent, false);
        }

        Software software = softwares.get(position);

        // Vincular TextView
        TextView tvId = view.findViewById(R.id.tvId);
        TextView tvNombre = view.findViewById(R.id.tvNombre);

        tvId.setText("ID: " + software.getId());
        tvNombre.setText("Nombre: " + software.getNombre());

        // Botón Ver
        Button btnVer = view.findViewById(R.id.btnVer);
        btnVer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewClickListener != null) {
                    viewClickListener.onViewClick(position);
                }
            }
        });

        // Botón Editar
        Button btnEditar = view.findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editClickListener != null) {
                    editClickListener.onEditClick(position);
                }
            }
        });

        // Botón Eliminar
        Button btnEliminar = view.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteClickListener != null) {
                    deleteClickListener.onDeleteClick(position);
                }
            }
        });

        return view;
    }
}