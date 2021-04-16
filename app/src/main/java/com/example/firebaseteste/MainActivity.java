package com.example.firebaseteste;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference   referencia; // = FirebaseDatabase.getInstance().getReference("charTable");
    private DatabaseReference   referenciaUser; // = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference pushedPostRef;

    private TextView textCont, valorTipoGas, valorTara, textViewAlerta;
    private RadioGroup  radioGroup;
    private RadioButton radioButtonP13, radioButtonP40;
    private int tipoGas = 1;
    private int intGasRestante = 1;
    private int iniciar_pesagem = 0;
    private String textGasRestante;
    private String  key, key_shared, tipoGas_local, tara_local;
    private Button buttonWifiEsp, buttonAjuste, buttonIniciar;

    static {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    private static final String ARQUIVO_PREFERENCIA = "ArquivoPreferencia";
    //SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
    //SharedPreferences.Editor editor = preferences.edit();

    //GraphView   graphView;
    //LineGraphSeries  series;
    LineChart   lineChart;
    LineDataSet lineDataSet = new LineDataSet(null,null);
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
    LineData    lineData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

       // radioGroup = findViewById(R.id.radioGroup);
       // radioButtonP13 = findViewById(R.id.radioButtonP13);
       // radioButtonP40 = findViewById(R.id.radioButtonP40);
        buttonWifiEsp = findViewById(R.id.buttonWifiEsp);
        buttonAjuste = findViewById(R.id.buttonAjuste);
        buttonIniciar = findViewById(R.id.buttonIniciar);

        textViewAlerta = findViewById(R.id.textViewAlerta);
        textCont = findViewById(R.id.textCont);
        valorTipoGas = findViewById(R.id.valorTipoGas);
        valorTara = findViewById(R.id.valorTara);

        lineChart = findViewById(R.id.chart);
        lineDataSet.setLineWidth(4);
        lineChart.setNoDataText("");
        lineChart.setNoDataTextColor(Color.BLACK);
        lineChart.setDrawBorders(true);
        lineChart.getDescription().setEnabled(false);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);  // persistência em disco
        //referencia.keepSynced(false);
        referencia = FirebaseDatabase.getInstance().getReference("charTable");
        referenciaUser = FirebaseDatabase.getInstance().getReference("AWA_0001");

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);

        IAxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(0);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(xAxisFormatter);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(45);


        final MyMarkerView myMarkerView= new MyMarkerView(this, R.layout.layout, 0);
        myMarkerView.setChartView(lineChart);
        lineChart.setMarker(myMarkerView);

        //referenciaUser.child("TipoGas").setValue("P13");
        /*
        pushedPostRef = referenciaUser.push();
        key = pushedPostRef.getKey();
        referenciaUser.child(key).child("TipoGas").setValue("P13");
         */

        //------------- salvar dados localmente

        //SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
        //SharedPreferences.Editor editor = preferences.edit();
        //editor.putString("key", key);

        //key_shared = preferences.getString("key", "zero");

        if(key_shared=="zero"){
            pushedPostRef = referenciaUser.push();
            key = pushedPostRef.getKey();
            //SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
            //SharedPreferences.Editor editor = preferences.edit();
            //editor.putString("key", key);
            //editor.commit();
           // key_shared = preferences.getString("key", "zero");
        }

        //referenciaUser.child(key_shared).child("TipoGas").setValue("P13"
        //------------------------------
        recuperarDadosAjuste();
        firebase_date();
        //tipoGas();
        recuperarToken();
        wifiEsp();
        ajuste();
        iniciar_antes();
        iniciar();
        alerta();
        //referenciaToken.child("Token").setValue("100");

        //-------------------------------------- para salvar valor de iniciar_pesagem, para apagar background e text
        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
        SharedPreferences.Editor editor = preferences.edit();

        if(preferences.getInt("iniciar_pesa",0)==1){
            buttonIniciar.setBackground(null);
            buttonIniciar.setText(null);
        }
        //---------------------------------------------------------

        //-------------------------------------- para salvar valor de iniciar_pesagem, para apagar background e text

       /* if(preferences.getString("alerta","0").equals("0")){
            textViewAlerta.setBackground(null);
            textViewAlerta.setText(null);
        }*/
        //---------------------------------------------------------
    }

    public void recuperarDadosAjuste(){

        SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
        SharedPreferences.Editor editor = preferences.edit();

        tipoGas_local = preferences.getString("tipoGas", "zero");
        tara_local = preferences.getString("tara", "zero");

        if(tipoGas_local=="zero" && tara_local=="zero") {
            valorTipoGas.setText("Selecione o tipo de gás e a tara !");
            valorTara.setText("");
        }else{
            valorTipoGas.setText("Tipo de gas: " + tipoGas_local);
            valorTara.setText("Tara do butijão: " + tara_local + " g");
        }


        Bundle dados = getIntent().getExtras();
        if(dados != null) {
            valorTipoGas.setText("Tipo de gas: " + dados.getString("tipoGas"));
            valorTara.setText("Tara do butijão: " + dados.getString("tara") + " Kg");
            editor.putString("tipoGas", dados.getString("tipoGas"));
            editor.putString("tara", dados.getString("tara"));
            editor.commit();
        }else{
            //valorTipoGas.setText("Selecione o tipo de gás e a tara !");
        }
    }


    public void recuperarToken(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                referenciaUser.child("Token").setValue(instanceIdResult.getToken());
            }
        });
    }

/*
    public void tipoGas() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButtonP13){
                    //tipoGas = 1;
                    referenciaUser.child(key_shared).child("TipoGas").setValue("P13");
                }else if(checkedId==R.id.radioButtonP40){
                    //tipoGas = 2;
                    referenciaUser.child(key_shared).child("TipoGas").setValue("P40");
                }
                firebase_date();
            }
        });
    }
*/
        public void iniciar_antes(){
            referenciaUser.child("InicioApp").addValueEventListener(new ValueEventListener() {
                private String inicioApp;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    inicioApp = dataSnapshot.getValue().toString();
                    if(inicioApp.equals("0")){
                        buttonIniciar.setBackground(null);
                        buttonIniciar.setText(null);
                        buttonIniciar.setEnabled(false);
                    }if(inicioApp.equals("1")){
                        Toast.makeText(getApplicationContext(),"Colocar o botijão em cima da balança",Toast.LENGTH_LONG).show();
                        buttonIniciar.setEnabled(false);
                    }
                    if(inicioApp.equals("2")){
                        buttonIniciar.setBackgroundResource(R.drawable.iniciarpes);
                        //buttonIniciar.setText("Iniciar");
                        buttonIniciar.setEnabled(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        public void iniciar(){
            buttonIniciar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    referenciaUser.child("IniciarPesagem").setValue(1);
                    Toast.makeText(getApplicationContext(),"Pesagem Iniciada",Toast.LENGTH_LONG).show();
                    buttonIniciar.setBackground(null);
                    buttonIniciar.setText(null);
                    iniciar_pesagem = 1;

                    SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("iniciar_pesa", iniciar_pesagem);
                    editor.commit();
                }
            });
        }

        public void wifiEsp (){
            buttonWifiEsp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), Slide.class);
                    startActivity(intent);
                }
            });
        }

        public void ajuste(){
            buttonAjuste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ajuste.class);
                    startActivity(intent);
                }
            });
        }


    @Override
    protected void onStart() {
        super.onStart();
        firebase_date();

    }


    private void firebase_date(){

        referencia.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Entry> dataVals = new ArrayList<Entry>();

                if(dataSnapshot.hasChildren()){
                    for (DataSnapshot myDataSnapshot:dataSnapshot.getChildren()){
                        PointValue pointValue = myDataSnapshot.getValue(PointValue.class);
                        dataVals.add(new Entry(pointValue.getxValue(),pointValue.getyValue()/tipoGas));
                        if(myDataSnapshot.child("yValue").exists()) {
                            textGasRestante = myDataSnapshot.child("yValue").getValue().toString();
                            intGasRestante = Integer.parseInt(textGasRestante);
                            intGasRestante = intGasRestante/tipoGas;
                            textCont.setText(intGasRestante + "%");
                        }
                    }
                    showChart(dataVals);
                }else{
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    private void alerta(){
            referenciaUser.child("alerta").addValueEventListener(new ValueEventListener() {
               private String alerta;

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    alerta = dataSnapshot.getValue().toString();
                    if(alerta.equals("0")){
                        textViewAlerta.setBackground(null);
                        textViewAlerta.setText(null);
                    }else{
                        textViewAlerta.setBackgroundColor(Color.RED);
                        textViewAlerta.setText("ALERTA ! Vazamento de gás detectado");
                    }

                    SharedPreferences preferences = getSharedPreferences(ARQUIVO_PREFERENCIA,0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("alerta", alerta);
                    editor.commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }


    private void    showChart(ArrayList<Entry> dataVals){
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawHighlightIndicators(false);
        //lineDataSet.enableDashedLine(10,10,90);
        lineDataSet.setValues(dataVals);
        lineDataSet.setLabel("Gás restante (%)");
        iLineDataSets.clear();
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
