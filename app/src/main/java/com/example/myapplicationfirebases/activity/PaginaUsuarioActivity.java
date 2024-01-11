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
    private Button btnEditar, btnExcluir;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_usuario);

        txtNomeUsuario = findViewById(R.id.txtNomeUsuario);
        txtEmailUsuario = findViewById(R.id.txtEmailUsuario);
        btnEditar = findViewById(R.id.btnEditar);
        btnExcluir = findViewById(R.id.btnExcluir);

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
                        configurarBotaoExcluir(emailUsuario);
                    } else {
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

    private void configurarBotaoExcluir(String emailUsuario) {
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chamar método para excluir usuário
                excluirUsuario(emailUsuario);
            }
        });
    }

    private void excluirUsuario(String emailUsuario) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Excluir o usuário do Firestore usando o e-mail como referência
        db.collection("usuarios")
                .whereEqualTo("email", emailUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        task.getResult().getDocuments().get(0).getReference().delete()
                                .addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        Toast.makeText(PaginaUsuarioActivity.this, "Usuário excluído com sucesso", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PaginaUsuarioActivity.this, CadastroActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(PaginaUsuarioActivity.this, "Erro ao excluir o usuário", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(PaginaUsuarioActivity.this, "Falha ao recuperar dados do usuário", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EDITAR_USUARIO && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("NOVO_NOME")) {
                String novoNome = data.getStringExtra("NOVO_NOME");
                txtNomeUsuario.setText("USUARIO: " + novoNome);
            }
        }
    }
}
