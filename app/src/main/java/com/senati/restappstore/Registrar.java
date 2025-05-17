package com.senati.restappstore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class Registrar extends AppCompatActivity {

    private final String URL = "https://rest-api-software-production-57a6.up.railway.app/api/softwares";

    RequestQueue requestQueue;

    Button btnRegistrarSoftware;
    EditText edtNombre, edtEspacioMb, edtVersionSoft, edtPrecio;

    private void loadUI() {
        edtNombre = findViewById(R.id.edtNombre);
        edtEspacioMb = findViewById(R.id.edtEspacioMb);
        edtVersionSoft = findViewById(R.id.edtVersionSoft);
        edtPrecio = findViewById(R.id.edtPrecio);
        btnRegistrarSoftware = findViewById(R.id.btnRegistrarSoftware);
    }

    private void clearForm() {
        edtNombre.setText("");
        edtEspacioMb.setText("");
        edtVersionSoft.setText("");
        edtPrecio.setText("");
    }

    private void saveData() {
        requestQueue = Volley.newRequestQueue(this);
        JSONObject datos = new JSONObject();

        String nombre = edtNombre.getText().toString().trim();
        String espacioMbSof = edtEspacioMb.getText().toString().trim();
        String versionSoft = edtVersionSoft.getText().toString().trim();
        String precioStr = edtPrecio.getText().toString().trim();

        if (nombre.isEmpty() || espacioMbSof.isEmpty() || versionSoft.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int espacioMb;
        double precio;
        try {
            espacioMb = Integer.parseInt(espacioMbSof);
            precio = Double.parseDouble(precioStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Espacio o precio no válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            datos.put("nombre", nombre);
            datos.put("espaciomb", espacioMb);
            datos.put("versionsoft", versionSoft);
            datos.put("precio", precio);
        } catch (Exception e) {
            Log.e("Error JSON", "Error al crear el JSON: " + e.getMessage());
            Toast.makeText(this, "Error al procesar los datos", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                datos,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Resultado", response.toString());
                        Toast.makeText(Registrar.this, "Registrado correctamente", Toast.LENGTH_SHORT).show();
                        // Redirigir a la actividad Listar
                        Intent intent = new Intent(Registrar.this, Listar.class);
                        startActivity(intent);
                        finish(); // Opcional: cierra la actividad actual
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error WS: ", error.toString());
                        Toast.makeText(Registrar.this, "Error al registrar: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(jsonRequest);
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Registro");
        builder.setMessage("¿Desea registrar el software?");
        builder.setPositiveButton("Sí", (dialog, which) -> {
            saveData();
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            clearForm();
            Toast.makeText(Registrar.this, "Formulario limpiado", Toast.LENGTH_SHORT).show();
        });
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            return insets;
        });

        loadUI();
        btnRegistrarSoftware.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }
}