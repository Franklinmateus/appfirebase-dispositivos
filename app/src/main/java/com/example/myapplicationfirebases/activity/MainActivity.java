package com.example.myapplicationfirebases.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.myapplicationfirebases.R;
import com.google.firebase.ktx.Firebase;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Intent it = new Intent(MainActivity.this, CadastroActivity.class);
                startActivity(it);
            }
        },5000);
    }
}