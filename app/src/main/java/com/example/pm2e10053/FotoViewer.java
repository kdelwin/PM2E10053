package com.example.pm2e10053;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class FotoViewer extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_viewer);

        imageView = findViewById(R.id.fotoview); // Asegúrate de que el ImageView en el diseño tenga el ID correcto

        // Obtener la imagen de la intención
        byte[] imagenBytes = getIntent().getByteArrayExtra("imagen");

        if (imagenBytes != null) {
            // Convertir los bytes de imagen en un objeto Bitmap
            Bitmap imagenBitmap = BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);

            // Mostrar la imagen en el ImageView
            imageView.setImageBitmap(imagenBitmap);
        } else {
            // Si no se proporcionaron bytes de imagen, muestra un mensaje de error
            Toast.makeText(this, "La imagen no está disponible."+imagenBytes, Toast.LENGTH_SHORT).show();
        }
    }
}
