package com.example.myapplicationfirebases.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationfirebases.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PaginaUsuarioActivity extends AppCompatActivity {

    private static final int REQUEST_EDITAR_USUARIO = 1;
    private TextView txtNomeUsuario, txtEmailUsuario;
    private Button btnEditar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_usuario);

        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);
        btnEditar = findViewById(R.id.btnEditar);

        String emailUsuario = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        FirebaseFirestore.getInstance().collection("usuarios")
                .whereEqualTo("email", emailUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        String nomeUsuario = document.getString("nome");
                        txtNomeUsuario.setText("USUARIO: " + nomeUsuario);
                        txtEmailUsuario.setText("E-MAIL: " + emailUsuario);

                        configurarBotaoEdicao(emailUsuario);
                    } else {
                        // Tratar falha na recuperação dos dados
                        txtNomeUsuario.setText("Erro ao obter dados do usuário");
                        txtEmailUsuario.setText("");
                    }
                });
    }

    private void configurarBotaoEdicao(String emailUsuario) {
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaginaUsuarioActivity.this, EditarUsuarioActivity.class);
                // Passar o e-mail do usuário como extra da Intent
                intent.putExtra("EMAIL_USUARIO", emailUsuario);
                startActivityForResult(intent, REQUEST_EDITAR_USUARIO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDITAR_USUARIO && resultCode == RESULT_OK) {
            // Atualizar os dados após a edição bem-sucedida
            if (data != null && data.hasExtra("NOVO_NOME")) {
                String novoNome = data.getStringExtra("NOVO_NOME");
                txtNomeUsuario.setText("USUARIO: " + novoNome);
            }
        }
    }
}
