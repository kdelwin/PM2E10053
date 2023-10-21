package com.example.pm2e10053;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;

import com.example.pm2e10053.Connection.Contactos;
import com.example.pm2e10053.Connection.SQLiteConexion;
import com.example.pm2e10053.Models.ContactoModel;

import java.util.ArrayList;

public class PantallaContactos extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listView;
    ArrayList<ContactoModel> listcontactos;
    ArrayList<String> ArregloContactos;
    Button btnshare, btneliminar, btneditar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_contactos);

        btnshare = findViewById(R.id.btncompartir);
        btneliminar = findViewById(R.id.btneliminar);
        try
        {
            conexion = new SQLiteConexion(this, Contactos.namedb, null, 1);
            listView = (ListView) findViewById(R.id.listcontact);
            GetContactos();

            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice,ArregloContactos);
            listView.setChoiceMode(ListView.FOCUSABLES_TOUCH_MODE);
            listView.setAdapter(adp);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String ItemContact = listcontactos.get(i).getTelefonos();
                }
            });

        }
        catch (Exception ex)
        {
            ex.toString();
        }

            btnshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    compartirContacto();
                }
            });

            btneliminar.setOnClickListener(view -> eliminarContacto());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Verifica que se haya hecho doble clic (el mismo elemento se selecciona dos veces seguidas)
                if (listView.isItemChecked(i)) {
                    mostrarDialogoLlamada(i);
                }
            }
        });

    }

    private void mostrarDialogoLlamada(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas llamar a este contacto?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        realizarLlamada(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }


    private void realizarLlamada(int position) {
        if (position >= 0 && position < listcontactos.size()) {
            String numeroTelefono = listcontactos.get(position).getTelefonos();

            // Verifica si se tienen los permisos para realizar llamadas
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "+"+numeroTelefono));
                startActivity(intent);
            } else {
                // Si no se tienen los permisos, solicítalos al usuario
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }


    public void eliminarContacto() {
        int selectedContactIndex = listView.getCheckedItemPosition();
        if (selectedContactIndex != AdapterView.INVALID_POSITION) {
            ContactoModel selectedContact = listcontactos.get(selectedContactIndex);
            int contactId = selectedContact.getId();

            SQLiteDatabase db = conexion.getWritableDatabase();
            String deleteQuery = "DELETE FROM " + Contactos.Tabla + " WHERE " + Contactos.id + " = " + contactId;

            db.execSQL(deleteQuery);
            GetContactos();
            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, ArregloContactos);
            listView.setChoiceMode(ListView.FOCUSABLES_TOUCH_MODE);
            listView.setAdapter(adp);
            adp.notifyDataSetChanged();
            Toast.makeText(this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Selecciona un contacto para eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    public void compartirContacto() {
        int selectedContactIndex = listView.getCheckedItemPosition();
        if (selectedContactIndex != AdapterView.INVALID_POSITION) {
            ContactoModel selectedContact = listcontactos.get(selectedContactIndex);
            String contactoTexto = "Nombre: " + selectedContact.getNombres() + "\n" +
                    "Teléfono: " + selectedContact.getTelefonos() + "\n" +
                    "País: " + selectedContact.getPaises();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, contactoTexto);

            startActivity(Intent.createChooser(intent, "Compartir Contacto"));
        } else {
            Toast.makeText(this, "Selecciona un contacto para compartir", Toast.LENGTH_SHORT).show();
        }
    }


    private void GetContactos()
    {
        SQLiteDatabase db = conexion.getReadableDatabase();
        ContactoModel contacto = null;
        listcontactos = new ArrayList<ContactoModel>();

        Cursor cursor = db.rawQuery(Contactos.SelectTableContactos,null);
        while(cursor.moveToNext())
        {
            contacto = new ContactoModel();
            contacto.setId(cursor.getInt(0));
            contacto.setNombres(cursor.getString(1));
            contacto.setTelefonos(cursor.getString(2));
            contacto.setPaises(cursor.getString(3));
            contacto.setImagen(cursor.getBlob(4));

            listcontactos.add(contacto);
        }

        cursor.close();
        FillList();
    }

    private void FillList()
    {
        ArregloContactos = new ArrayList<>();

        for(int i = 0; i < listcontactos.size(); i++)
        {
            ArregloContactos.add(listcontactos.get(i).getNombres() + " - " +
                    listcontactos.get(i).getTelefonos() );
        }
    }
}