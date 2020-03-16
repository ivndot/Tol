package com.tolcompany.tol;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    //variable que contiene el tiempo que va a durar el splash
    private final int time_splash_screen=3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //validación para poner la barra de notificaciones transparente
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        //metodo para pasar al siguiente activity
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent= new Intent(SplashScreen.this, MusicPlayer.class);
                startActivity(intent);
                finish();
            }
        },time_splash_screen);

    }


}
