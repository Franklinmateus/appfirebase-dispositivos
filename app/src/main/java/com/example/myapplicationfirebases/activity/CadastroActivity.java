package com.example.myapplicationfirebases.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplicationfirebases.R;
import com.example.myapplicationfirebases.Util.ConfiguraBd;
import com.example.myapplicationfirebases.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {
    Usuario usuario;
    FirebaseAuth autenticacao;
    EditText edtNome, edtEmail, edtSenha;
    Button btnBotao;

    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        Iniciar();

        btnBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = edtNome.getText().toString();
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();

                if (nome.isEmpty() || email.isEmpty()|| senha.isEmpty()) {

                    Toast.makeText(CadastroActivity.this, "Preencha o Campo", Toast.LENGTH_SHORT).show();

                }else {
                    cadastrarUsuario(v);
                }
            }
        });
    }
        private void cadastrarUsuario(View v) {

            String email = edtEmail.getText().toString();
            String senha = edtSenha.getText().toString();
            String nome = edtNome.getText().toString();


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        SalvarDadosUsuario();
                        redirecionarParaPaginaUsuario(nome, email);
                        Toast.makeText(CadastroActivity.this, "Cadastrado com Sucesso", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(CadastroActivity.this, "Erro ao Cadastrar", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private  void SalvarDadosUsuario(){

        String nome = edtNome.getText().toString();
        String email = edtEmail.getText().toString();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String,Object> usuarios = new HashMap<>();
            usuarios.put("nome",nome);
            usuarios.put("email",email);

            usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = db.collection("usuarios").document(usuarioID);
            documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {

                    Toast.makeText(CadastroActivity.this, "Dados Armazenados", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CadastroActivity.this, "Erro Dados Armazenados", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        private void redirecionarParaPaginaUsuario(String nome, String email) {
            // Criar uma Intent para a nova Activity
            Intent intent = new Intent(CadastroActivity.this, PaginaUsuarioActivity.class);
            // Passar dados do usuário para a nova Activity
            intent.putExtra("NOME_USUARIO", nome);
            intent.putExtra("EMAIL_USUARIO", email);
            // Iniciar a nova Activity
            startActivity(intent);
            finish();  // Opcional, se você quiser encerrar a atividade atual após o redirecionamento.
        }
        private void Iniciar(){

            edtNome = findViewById(R.id.edtNome);
            edtEmail = findViewById(R.id.edtEmail);
            edtSenha = findViewById(R.id.edtSenha);
            btnBotao = findViewById(R.id.btnBotao);

        }
    }





