package com.example.myapplicationfirebases.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationfirebases.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaginaUsuarioActivity extends AppCompatActivity {

    private TextView txtNomeUsuario, txtEmailUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_usuario);

        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);

        // Obter o e-mail do usuário atualmente autenticado
        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Recuperar os dados do usuário do Firestore usando o e-mail
        FirebaseFirestore.getInstance().collection("usuarios")
                .whereEqualTo("email", emailUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String nomeUsuario = document.getString("nome");
                            txtNomeUsuario.setText("USUARIO: " + nomeUsuario);
                            txtEmailUsuario.setText("E-MAIL: " + emailUsuario);
                        }
                    } else {
                        // Tratar falha na recuperação dos dados
                        txtNomeUsuario.setText("Erro ao obter dados do usuário");
                        txtEmailUsuario.setText("");
                    }
                });
    }
}
