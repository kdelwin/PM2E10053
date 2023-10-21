package com.example.pm2e10053;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pm2e10053.Connection.Contactos;
import com.example.pm2e10053.Connection.SQLiteConexion;
import com.example.pm2e10053.Models.ContactoModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class PantallaInicial extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    ImageView imageView;
    private EditText inputNombre;
    private EditText inputAreaCode;
    private EditText inputTelefono;
    private EditText inputNotas;
    private Spinner inputPaises;
    private byte[] imagenBytes;
    Button btnsalvarcontacto;
    Button btncontsalvados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicial);

        imageView = findViewById(R.id.pfp);
        inputNombre = findViewById(R.id.inputnombre);
        inputAreaCode = findViewById(R.id.inputareacode);
        inputTelefono = findViewById(R.id.inputtelefono);
        inputNotas = findViewById(R.id.inputnotas);
        inputPaises = findViewById(R.id.cbpaises);
        btnsalvarcontacto = findViewById(R.id.btnsalvarcontacto);
        btncontsalvados = findViewById(R.id.btncontsalvados);

        inputAreaCode.setEnabled(false);

        HashMap<String, String> paisesYCodigos = new HashMap<>();
        paisesYCodigos.put("Belice", "501");
        paisesYCodigos.put("Seleccionar Pais", "");
        paisesYCodigos.put("Costa Rica", "506");
        paisesYCodigos.put("El Salvador", "503");
        paisesYCodigos.put("Guatemala", "502");
        paisesYCodigos.put("Honduras", "504");
        paisesYCodigos.put("Nicaragua", "505");
        paisesYCodigos.put("Panamá", "507");

        Spinner spinnerPaises = findViewById(R.id.cbpaises);
        final TextView textViewCodigoArea = findViewById(R.id.inputareacode);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paisesYCodigos.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaises.setAdapter(adapter);

        ContactoModel contactToEdit = (ContactoModel) getIntent().getSerializableExtra("contactToEdit");
        if (contactToEdit != null) {
            inputNombre.setText(contactToEdit.getNombres());
            inputNotas.setText(contactToEdit.getNotas());
            inputTelefono.setText(contactToEdit.getTelefonos());
            // Establecer otros valores en los campos de edición
        }
        spinnerPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCountry = parentView.getItemAtPosition(position).toString();
                String codigoArea = paisesYCodigos.get(selectedCountry);

                if (codigoArea != null) {
                    textViewCodigoArea.setText(codigoArea);
                } else {
                    textViewCodigoArea.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                textViewCodigoArea.setText("");
            }

        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        btnsalvarcontacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddContact();
            }
        });

        btncontsalvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para iniciar la nueva actividad
                Intent intent = new Intent(PantallaInicial.this, PantallaContactos.class);
                startActivity(intent);
            }
        });
    }

    private void AddContact() {
        try {
            SQLiteConexion conexion = new SQLiteConexion(this, Contactos.namedb, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();
            valores.put(Contactos.nombres, inputNombre.getText().toString());
            valores.put(Contactos.telefonos, inputAreaCode.getText().toString() + inputTelefono.getText().toString());
            valores.put(Contactos.notas, inputNotas.getText().toString());
            valores.put(Contactos.paises, inputPaises.getSelectedItem().toString());
            valores.put(Contactos.imagen, imagenBytes);

            long Result = db.insert(Contactos.Tabla, Contactos.id, valores);

            Toast.makeText(this, getString(R.string.Respuesta), Toast.LENGTH_SHORT).show();
            db.close();
        } catch (Exception exception) {
            Toast.makeText(this, getString(R.string.ErrorResp), Toast.LENGTH_SHORT).show();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                imagenBytes = outputStream.toByteArray();
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
