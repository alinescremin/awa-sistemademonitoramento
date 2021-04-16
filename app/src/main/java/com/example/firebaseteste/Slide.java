package com.example.firebaseteste;

//import android.support.v7.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class Slide extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_slide);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new SimpleSlide.Builder().title("Conectando sua balança ...").
                description("Ligue sua balança e siga os passos a seguir").
                image(R.drawable.gas_image2).
                background(R.color.colorSlideBack).build());

        addSlide(new SimpleSlide.Builder().
                title("1º Passo").description("Esteja a menos de 2 metros da balança").
                image(R.drawable.gas_image2).
                background(R.color.colorSlideBack).build());

        addSlide(new SimpleSlide.Builder().
                title("2º Passo").description("Conecte-se à rede 'AWA', com senha '12345678' ").
                image(R.drawable.rede_senha).
                background(R.color.colorSlideBack).build());

        addSlide(new SimpleSlide.Builder().
                title("3º Passo").description("Abra o navegado e digite o endereço '192.168.4.1' ").
                image(R.drawable.url_wifi_manager).
                background(R.color.colorSlideBack).build());

        addSlide(new SimpleSlide.Builder().
                title("4º Passo").description("Escolha qual rede você deseja conectar sua balança ").
                image(R.drawable.wifi_scan).
                background(R.color.colorSlideBack).build());


    }
}