package com.example.myapplicationfirebases.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplicationfirebases.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditarUsuarioActivity extends AppCompatActivity {

    private EditText edtNovoValor;
    private Button btnSalvar;
    private String emailUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);

        edtNovoValor = findViewById(R.id.edtNovoValor);
        btnSalvar = findViewById(R.id.btnSalvar);

        // Obter o e-mail do usuário da intent
        emailUsuario = getIntent().getStringExtra("EMAIL_USUARIO");

        // Configurar botão de salvar
        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String novoValor = edtNovoValor.getText().toString();
                // Verificar se o novo valor não está vazio
                if (!novoValor.isEmpty()) {
                    // Editar o nome do usuário no Firestore
                    editarNomeUsuario(novoValor);
                } else {
                    // Exibir Toast informando que o novo valor não pode estar vazio
                    Toast.makeText(EditarUsuarioActivity.this, "O novo valor não pode estar vazio", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void editarNomeUsuario(String novoNome) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Atualizar o nome do usuário no Firestore usando o e-mail como referência
        db.collection("usuarios")
                .whereEqualTo("email", emailUsuario)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentReference documentReference = task.getResult().getDocuments().get(0).getReference();
                        documentReference.update("nome", novoNome)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        // Retornar para a PaginaUsuarioActivity com os dados atualizados
                                        retornarParaPaginaUsuario(novoNome);
                                    } else {
                                        // Exibir Toast informando falha na edição
                                        Toast.makeText(EditarUsuarioActivity.this, "Erro ao editar o nome do usuário", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Tratar falha na recuperação dos dados do usuário
                        Toast.makeText(EditarUsuarioActivity.this, "Falha ao recuperar dados do usuário", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void retornarParaPaginaUsuario(String novoNome) {
        // Criar uma Intent para retornar para a PaginaUsuarioActivity
        Intent intent = new Intent();
        // Passar o novo nome como extra da Intent
        intent.putExtra("NOVO_NOME", novoNome);
        // Definir o resultado como RESULT_OK
        setResult(RESULT_OK, intent);
        // Finalizar a atividade de edição e retornar para a PaginaUsuarioActivity
        finish();
    }
}


