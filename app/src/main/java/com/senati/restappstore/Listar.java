package com.senati.restappstore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listar extends AppCompatActivity {
    private final String URL = "https://rest-api-software-production-57a6.up.railway.app/api/softwares";
    private RequestQueue requestQueue;
    private ListView listViewSoftwares;
    private List<Software> softwareList;
    private SoftwareAdapter adapter;
    private EditText edtSearchId;
    private List<Software> originalSoftwareList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        listViewSoftwares = findViewById(R.id.listViewSoftwares);
        edtSearchId = findViewById(R.id.edtSearchId);
        Button btnSearch = findViewById(R.id.btnSearch);

        softwareList = new ArrayList<>();
        originalSoftwareList = new ArrayList<>();
        adapter = new SoftwareAdapter(this, softwareList,
                new SoftwareAdapter.OnViewClickListener() {
                    @Override
                    public void onViewClick(int position) {
                        showDetailsDialog(position);
                    }
                },
                new SoftwareAdapter.OnEditClickListener() {
                    @Override
                    public void onEditClick(int position) {
                        showEditDialog(position);
                    }
                },
                new SoftwareAdapter.OnDeleteClickListener() {
                    @Override
                    public void onDeleteClick(int position) {
                        showDeleteDialog(position);
                    }
                });
        listViewSoftwares.setAdapter(adapter);

        // Agregar TextWatcher para monitorear cambios en el EditText
        edtSearchId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty()) {
                    // Si el EditText está vacío, mostrar todos los softwares
                    softwareList.clear();
                    softwareList.addAll(originalSoftwareList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Configurar el botón Buscar
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchIdStr = edtSearchId.getText().toString().trim();
                if (!searchIdStr.isEmpty()) {
                    try {
                        int searchId = Integer.parseInt(searchIdStr);
                        softwareList.clear();
                        boolean found = false;
                        for (Software software : originalSoftwareList) {
                            if (software.getId() == searchId) {
                                softwareList.add(software);
                                found = true;
                                break; // Asumimos que el ID es único
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (!found) {
                            Toast.makeText(Listar.this, "No se encontró software con ID " + searchId, Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(Listar.this, "Ingresa un ID válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Si el campo está vacío al hacer clic en Buscar, no hace nada (el TextWatcher ya maneja esto)
                }
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            softwareList.clear();
                            originalSoftwareList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject json = response.getJSONObject(i);
                                int id = json.getInt("id");
                                String nombre = json.getString("nombre");
                                int espaciomb = json.getInt("espaciomb");
                                String versionsoft = json.getString("versionsoft");
                                double precio = json.getDouble("precio");

                                Software software = new Software(id, nombre, espaciomb, versionsoft, precio);
                                softwareList.add(software);
                                originalSoftwareList.add(software);
                            }
                            adapter.notifyDataSetChanged();
                            Log.d("Lista", response.toString());
                            if (softwareList.isEmpty()) {
                                Toast.makeText(Listar.this, "No hay softwares registrados", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("Error JSON", "Error al parsear datos: " + e.getMessage());
                            Toast.makeText(Listar.this, "Error al procesar datos", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error WS", error.toString());
                        Toast.makeText(Listar.this, "Error al cargar datos: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }

    private void showDetailsDialog(int position) {
        Software software = softwareList.get(position);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_software_details, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        TextView tvDialogId = dialogView.findViewById(R.id.tvDialogId);
        TextView tvDialogNombre = dialogView.findViewById(R.id.tvDialogNombre);
        TextView tvDialogEspacioMb = dialogView.findViewById(R.id.tvDialogEspacioMb);
        TextView tvDialogVersionSoft = dialogView.findViewById(R.id.tvDialogVersionSoft);
        TextView tvDialogPrecio = dialogView.findViewById(R.id.tvDialogPrecio);

        tvDialogId.setText("ID: " + software.getId());
        tvDialogNombre.setText("Nombre: " + software.getNombre());
        tvDialogEspacioMb.setText("Espacio: " + software.getEspaciomb());
        tvDialogVersionSoft.setText("Version: " + software.getVersionsoft());
        tvDialogPrecio.setText(String.format("Precio: %.2f", software.getPrecio()));

        AlertDialog dialog = builder.create();

        Button btnDialogVolver = dialogView.findViewById(R.id.btnDialogVolver);
        btnDialogVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Simplemente cerrar el diálogo sin confirmación
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showEditDialog(int position) {
        Software software = softwareList.get(position);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.editar_software, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        EditText edtEditNombre = dialogView.findViewById(R.id.edtEditNombre);
        EditText edtEditVersion = dialogView.findViewById(R.id.edtEditVersion);
        EditText edtEditEspacioMb = dialogView.findViewById(R.id.edtEditEspacioMb);
        EditText edtEditPrecio = dialogView.findViewById(R.id.edtEditPrecio);

        // Rellenar los campos con los datos actuales
        edtEditNombre.setText(software.getNombre());
        edtEditVersion.setText(software.getVersionsoft());
        edtEditEspacioMb.setText(String.valueOf(software.getEspaciomb()));
        edtEditPrecio.setText(String.valueOf(software.getPrecio()));

        AlertDialog dialog = builder.create();

        Button btnCancelEdit = dialogView.findViewById(R.id.btnCancelEdit);
        Button btnSaveEdit = dialogView.findViewById(R.id.btnSaveEdit);

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el diálogo sin confirmación
                dialog.dismiss();
            }
        });

        btnSaveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevoNombre = edtEditNombre.getText().toString().trim();
                String nuevaVersion = edtEditVersion.getText().toString().trim();
                String nuevoEspacioStr = edtEditEspacioMb.getText().toString().trim();
                String nuevoPrecioStr = edtEditPrecio.getText().toString().trim();

                if (nuevoNombre.isEmpty() || nuevaVersion.isEmpty() || nuevoEspacioStr.isEmpty() || nuevoPrecioStr.isEmpty()) {
                    Toast.makeText(Listar.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Mostrar diálogo de confirmación antes de guardar
                new AlertDialog.Builder(Listar.this)
                        .setTitle("Confirmar guardado")
                        .setMessage("¿Estás seguro de que deseas guardar los cambios?")
                        .setPositiveButton("Sí", (d, which) -> {
                            try {
                                int nuevoEspacio = Integer.parseInt(nuevoEspacioStr);
                                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);

                                // Actualizar el objeto local
                                software.setNombre(nuevoNombre);
                                software.setVersionsoft(nuevaVersion);
                                software.setEspaciomb(nuevoEspacio);
                                software.setPrecio(nuevoPrecio);

                                // Actualizar la lista original
                                for (int i = 0; i < originalSoftwareList.size(); i++) {
                                    if (originalSoftwareList.get(i).getId() == software.getId()) {
                                        originalSoftwareList.set(i, software);
                                        break;
                                    }
                                }

                                // Actualizar la API (PUT request)
                                updateSoftwareInApi(software);

                                // Notificar al adaptador
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();
                                Toast.makeText(Listar.this, "Software actualizado", Toast.LENGTH_SHORT).show();
                            } catch (NumberFormatException e) {
                                Toast.makeText(Listar.this, "Espacio y Precio deben ser números válidos", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        dialog.show();
    }

    private void updateSoftwareInApi(Software software) {
        String updateUrl = URL + "/" + software.getId();

        Map<String, String> params = new HashMap<>();
        params.put("nombre", software.getNombre());
        params.put("versionsoft", software.getVersionsoft());
        params.put("espaciomb", String.valueOf(software.getEspaciomb()));
        params.put("precio", String.valueOf(software.getPrecio()));

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.PUT,
                updateUrl,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Update", "Software actualizado en la API");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error WS", "Error al actualizar: " + error.toString());
                        Toast.makeText(Listar.this, "Error al actualizar en la API", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }

    private void showDeleteDialog(int position) {
        Software software = softwareList.get(position);
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar " + software.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> deleteSoftware(position))
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteSoftware(int position) {
        Software software = softwareList.get(position);
        String deleteUrl = URL + "/" + software.getId();

        JsonObjectRequest deleteRequest = new JsonObjectRequest(
                Request.Method.DELETE,
                deleteUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        softwareList.remove(position);
                        originalSoftwareList.remove(software);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(Listar.this, "Software eliminado correctamente", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error WS", error.toString());
                        Toast.makeText(Listar.this, "Error al eliminar: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(deleteRequest);
    }
}