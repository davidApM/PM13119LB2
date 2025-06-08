package com.example.pm13119lb2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imgviewFoto;
    private Uri imgURI;
    ListView lstdepartamentos;
    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // ListView y datos
        lstdepartamentos = findViewById(R.id.lstDepartamentos);
        items = new ArrayList<>();
        items.add("1 - Ahuchapán");
        items.add("2 - Santa Ana");
        items.add("3 - Sonsonate");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lstdepartamentos.setAdapter(adapter);

        // Imagen y recuperación tras cambio de modo
        imgviewFoto = findViewById(R.id.ivImagenLogo);
        if (savedInstanceState != null) {
            String uriGuardada = savedInstanceState.getString("img_uri");
            if (uriGuardada != null) {
                imgURI = Uri.parse(uriGuardada);
                imgviewFoto.setImageURI(imgURI);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias UI
        Switch swModo = findViewById(R.id.SwCambiarModo);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        EditText tvestadomodo = findViewById(R.id.edtEstadoModo);
        CheckBox chkboxdesamodo = findViewById(R.id.chboxDesactivacambiomodo);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        ImageButton imgbuttonfecha = findViewById(R.id.imgButtonFecha);
        ImageButton imgbuttonhora = findViewById(R.id.imgButtonHora);

        // Evento Switch modo
        swModo.setOnClickListener(view -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Cambiando modo")
                    .setMessage("Se cambiará el modo de la aplicación")
                    .setPositiveButton("OK", null)
                    .show();

            boolean estado = ((Switch) view).isChecked();
            if (estado) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                tvestadomodo.setText("Modo oscuro esta activado");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                tvestadomodo.setText("");
            }
        });

        // Evento RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != 1) {
                RadioButton radioButtonSelec = findViewById(checkedId);
                int indexbutton = radioGroup.indexOfChild(radioButtonSelec);
                if (indexbutton == 1) {
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialogo_personalizado, null);
                    EditText razonocultar = dialogView.findViewById(R.id.edtJustificacion);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(dialogView);
                    builder.setTitle("Venta - Razón para ocultar ");
                    builder.setPositiveButton("Registrar", (dialog, which) -> {
                        String razon = razonocultar.getText().toString();
                        tvestadomodo.setText(razon);
                    });
                    builder.setNegativeButton("Cancelar", (dialog, which) -> {
                        tvestadomodo.setText("");
                    });
                    builder.show();

                    imgviewFoto.setVisibility(View.INVISIBLE);
                } else {
                    imgviewFoto.setVisibility(View.VISIBLE);
                    tvestadomodo.setText("");
                }
            }
        });

        // Evento FloatingActionButton
        fab.setOnClickListener(view -> finish());

        // Evento CheckBox para deshabilitar cambio modo
        chkboxdesamodo.setOnClickListener(view -> {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Confirmar cambio de modo")
                    .setMessage("¿Está seguro que desea deshabilitar el cambio de modo?")
                    .setPositiveButton("Si", (dialog, which) -> swModo.setEnabled(!chkboxdesamodo.isChecked()))
                    .setNegativeButton("NO", (dialog, which) -> chkboxdesamodo.setChecked(!chkboxdesamodo.isChecked()))
                    .show();
        });

        // Evento click en ListView
        lstdepartamentos.setOnItemClickListener((parent, view, position, id) -> {
            TextView itemselected = (TextView) view;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Ingrese el nuevo valor para el elemento seleccionado");
            final EditText nuevotexto = new EditText(MainActivity.this);
            nuevotexto.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(nuevotexto);
            builder.setPositiveButton("Actualizar", (dialog, which) -> {
                String textonuevo = nuevotexto.getText().toString();
                itemselected.setText(textonuevo);
            });
            builder.setNegativeButton("Cancelar", null);
            builder.show();
        });

        // Evento click en imagen para abrir galería
        imgviewFoto.setOnClickListener(view -> OpenGalery());

        // Evento click para seleccionar fecha
        imgbuttonfecha.setOnClickListener(view -> {
            int annio = LocalDate.now().getYear();
            int mes = LocalDate.now().getMonthValue();
            int dia = LocalDate.now().getDayOfMonth();

            DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                    (DatePicker view1, int year, int month, int dayOfMonth) -> {
                        String fecha = dayOfMonth + "/" + (month + 1) + "/" + year;
                        tvestadomodo.setText("Fecha: " + fecha);
                    }, annio, mes - 1, dia);
            datePickerDialog.show();
        });

        // Evento click para seleccionar hora
        imgbuttonhora.setOnClickListener(view -> {
            int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + 1;
            int minutos = Calendar.getInstance().get(Calendar.MINUTE) + 1;

            TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this,
                    (TimePicker view12, int hourOfDay, int minute) -> {
                        String horaStr = hourOfDay + ":" + String.format("%02d", minute);
                        tvestadomodo.setText("Hora : " + horaStr);
                    }, hora - 1, minutos, true);
            timePickerDialog.show();
        });
    }

    private void OpenGalery() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgURI = data.getData();
            imgviewFoto.setImageURI(imgURI);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imgURI != null) {
            outState.putString("img_uri", imgURI.toString());
        }
    }
}
