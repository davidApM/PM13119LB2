package com.example.pm13119lb2;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import
        com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    // Propiedades personalizadas de la clase MainActivity
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
        // Generando elemetos del componente ListView
        lstdepartamentos = findViewById(R.id.lstDepartamentos);
        items = new ArrayList<>();
        items.add("1 - Ahuchapán");
        items.add("2 - Santa Ana");
        items.add("3 - Sonsonate");
        //Estableciendo el tipo de distribución de los elementos: lista simple
                adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        //Asignando la lista de elementos de la clase Adapter aa componente ListView
        lstdepartamentos.setAdapter(adapter);
        //Recuperando imagen de la sesión actual
        //Se usa para evitar que la imagen se pierda cuando se cambia entre modo claro y modo oscuro
        imgviewFoto = findViewById(R.id.ivImagenLogo);
        if (savedInstanceState != null) {
            String uriGuardada = savedInstanceState.getString("img_uri");
            if (uriGuardada != null) {
                imgURI = Uri.parse(uriGuardada);
                imgviewFoto.setImageURI(imgURI);
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v,
                                                                            insets) -> {
            Insets systemBars =
                    insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top,
                    systemBars.right, systemBars.bottom);
            return insets;
        });
        // Refencias a elementos de la UI que serán utilizados desde la capa lógica.
        Switch swModo = findViewById(R.id.SwCambiarModo);
        FloatingActionButton fab =
                findViewById(R.id.floatingActionButton);
        EditText tvestadomodo = findViewById(R.id.edtEstadoModo);
        CheckBox chkboxdesamodo =
                findViewById(R.id.chboxDesactivacambiomodo);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        // Manejo del evento onClick del elemento de tipo Switch
        swModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean estado = ((Switch) view).isChecked();
                if (estado){

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    tvestadomodo.setText("Modo oscuro esta activado");
                }else{

                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    tvestadomodo.setText("");
                }
            }
        });
        // Manejo del evento onClick del elemento de tipo CheckBox
        chkboxdesamodo.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View view) {
            if (chkboxdesamodo.isChecked()){
                swModo.setEnabled(false);
            }else{
                swModo.setEnabled(true);
            }
        }
        });
        // Manejo del evento onCheckedChanged que representa una acción equivalente a onClick
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId != 1) {
                    RadioButton radioButtonSelec = findViewById(checkedId);
                    int indexbutton = radioGroup.indexOfChild(radioButtonSelec);
                    if(indexbutton==1){
                        imgviewFoto.setVisibility(View.INVISIBLE);
                    }else{
                        imgviewFoto.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // Manejo del evento onItemClick del elemento de tipo ListView
        lstdepartamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String elementoseleccionado = items.get(position);
                View rooLayout;
                rooLayout = findViewById(R.id.main);
                Snackbar.make(rooLayout, "El elemento seleccionado es " + elementoseleccionado, Snackbar.LENGTH_SHORT).show();
            }
        });

        // Manejo del evento onClick del elemento de tipo FloatinActionButtom
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Manejo del evento onClick del elemento de tipo CheckBox
        chkboxdesamodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chkboxdesamodo.isChecked()){
                    swModo.setEnabled(false);
                }else{
                    swModo.setEnabled(true);
                }
            }
        });

        // Manejo del evento onItemClick del elemento de tipo ListView
        lstdepartamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String elementoseleccionado = items.get(position);
                View rooLayout;
                rooLayout = findViewById(R.id.main);
                Snackbar.make(rooLayout, "El elemenot seleccionado es " + elementoseleccionado, Snackbar.LENGTH_SHORT).show();
            }
        });

        // Manejo del evento onClick del elemento de tipo ImageView
        imgviewFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGalery();
            }
        });
    }

    // Metodo personalizado de la clase que se usa para crear un Intend
    // que servira para abrir la galeria de fotos y seleccionar una
    //imagen que sera cargada en el ImageView
    private void OpenGalery(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }
    // Método de la clse sobreescrito para optener la URI de la imagen
    //seleccionda en la galeria de fotos
    // y asignarla como valor a la propiedad URI del elemento ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgURI = data.getData();
            imgviewFoto.setImageURI(imgURI);
        }
    }

    //Método de la clae para guardar cambios en el propiedad URI
    // Que servira para recuperar la imagen asiganda al elemento
    //ImageView cuando se haga un cambio de modo
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imgURI != null) {
            outState.putString("img_uri", imgURI.toString());
        }
    }
}