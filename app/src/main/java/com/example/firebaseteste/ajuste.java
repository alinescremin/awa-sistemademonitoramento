package com.example.firebaseteste;

//import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ajuste extends AppCompatActivity {

    private DatabaseReference referenciaUser;
    private RadioGroup radioGroup;
    private RadioButton radioButtonP13, radioButtonP45, radioButtonP05;
    private String tipoGas, valorTara;
    private float valorTarafloat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuste);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        radioGroup = findViewById(R.id.radioGroup);
        radioButtonP13 = findViewById(R.id.radioButtonP13);
        radioButtonP45 = findViewById(R.id.radioButtonP45);
        radioButtonP05 = findViewById(R.id.radioButtonP05);

        referenciaUser = FirebaseDatabase.getInstance().getReference("AWA_0001");

        //Toast.makeText(getApplicationContext(), "Atente-se ao prazao de validade da mangueira de gás e do regulador de pressão", Toast.LENGTH_LONG).show();

        tipoGas();
    }


    public void tipoGas() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonP13) {
                    tipoGas = "P13";
                } else if (checkedId == R.id.radioButtonP45) {
                    tipoGas = "P45";
                } else if (checkedId == R.id.radioButtonP05) {
                    tipoGas = "P05";
                }
            }
        });
    }

    public void Salvar(View view) {

        EditText tara = findViewById(R.id.editTextTara);
        valorTara = tara.getText().toString();

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo!= null && netInfo.isConnectedOrConnecting()) {

            if (tipoGas == null || valorTara == null || valorTara.equals("")) {
                Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
            } else {

                valorTarafloat = Float.parseFloat(valorTara);

                referenciaUser.child("TaraBotijao").setValue(valorTarafloat);
                referenciaUser.child("TipoBotijao").setValue(tipoGas);
                referenciaUser.child("NovoBotijao").setValue(1);


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("tipoGas", tipoGas);
                intent.putExtra("tara", valorTara);

                Toast.makeText(getApplicationContext(), "Dados salvos com sucesso", Toast.LENGTH_LONG).show();

                // FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                startActivity(intent);

            }
        }
        else{
            Toast.makeText(getApplicationContext(), "É necessário conectar à internet", Toast.LENGTH_LONG).show();
        }
    }
}