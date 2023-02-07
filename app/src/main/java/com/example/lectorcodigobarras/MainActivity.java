package com.example.lectorcodigobarras;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lectorcodigobarras.databinding.ActivityMainBinding;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class MainActivity extends AppCompatActivity  {
    //Declaramos la variable
    private Button btnSalir;
    //Creamos el MainBinding
    ActivityMainBinding binding;
    //Aquí evaluamos el resultado de la lectura del código, creamos el contrato de escaneo
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        //Si el resultado es nulo nos mostrará un mensaje de cancelación
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
        } else {
            //En caso contrario nos mostrará el resultado en el textView de la interfaz
            binding.textCodigo.setText(result.getContents());
        }
    });

    //Creamos el contrato para poder usar la cámara y leer los códigos de barras
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Enlazamos el controller con el botón salir
        btnSalir = (Button) findViewById(R.id.btnSalir);
        //Le añadimos el listener para que el sistema sepa si se ha hecho click en él
        //y por último, le añadimos la funcionalidad reescribiendo el onClick
        binding.btnSalir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){ finish();}
        });

        //Vamos a darle funcionalidad al botón escanear cuando se haga clic en él
        binding.btnLeerCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                escanear();
            }
        });
    }

    //Creamos el método escanear
    public void escanear() {
        ScanOptions options = new ScanOptions();
        //Editamos las opciones de escaneo que queremos configurar
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES); //Le decimos que lea todos los tipos de códigos de barras
        options.setPrompt("Escanear"); //Escribimos un mensaje
        options.setCameraId(0); //Seleccionamos la cámara
        options.setOrientationLocked(false); //Esta opción la ponemos en false para que se pueda girar el teléfono y cambie la parte visual del escaner
        options.setBeepEnabled(true); //Sirve para que pite cuando se realiza un escaneo
        options.setCaptureActivity(CaptureActivityPortrait.class); //Para que permita escanear cuando el móvil esté en horizontal o vertical
        options.setBarcodeImageEnabled(false); //Guarda el resultado del escaneo de la imagen

        barcodeLauncher.launch(options); //Lanzamos el contrato y le pasamos las opciones que hemos configurado
    }

}