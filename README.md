# Projeto de CRUD com Firebase em Java mobile para Dispositivos Móveis
Este projeto foi desenvolvido como parte da disciplina de Laboratório de Programação para Dispositivos Móveis. O objetivo principal é demonstrar a implementação de um ```CRUD (Create, Read, Update, Delete)``` utilizando o Firebase como banco de dados em um aplicativo mobile em Java.

## Passo a Passo Básico
Aqui está um guia básico para entender e configurar o projeto:

Configuração do Ambiente de Desenvolvimento
Configuração do Firebase:

Crie um projeto no Console do Firebase.
Adicione um aplicativo Android ao seu projeto e faça o download do arquivo de configuração google-services.json.
Configuração do Android Studio:

Crie um novo projeto no Android Studio.
Adicione o arquivo google-services.json ao diretório do seu projeto.
Integração com Firebase:

Adicione as dependências necessárias no arquivo build.gradle do módulo:

gradle
Copy code
implementation 'com.google.firebase:firebase-database:23.0.0'
implementation 'com.google.firebase:firebase-auth:23.0.0'
Certifique-se de usar a versão mais recente do SDK do Firebase.

Inicialize o Firebase no seu código Java:

java
Copy code
FirebaseApp.initializeApp(this);
## Implementação do CRUD
### CadastroAcitivity (Create):
  ```A CadastroActivity é uma activity cujo objetivo é o cadastro de usuários. O código inicia configurando a interface do usuário, como campos de texto e botões. Quando o botão de cadastro é clicado, o aplicativo verifica se os campos de nome, e-mail e senha foram preenchidos. Se estiverem preenchidos, o código utiliza o Firebase Authentication para criar um novo usuário. Em caso de sucesso no cadastro, os dados adicionais do usuário (nome e e-mail) são armazenados no Firebase Firestore. O usuário é então redirecionado para uma página de perfil. Caso ocorra algum erro no processo de cadastro, uma mensagem de erro é exibida ao usuário. O código é estruturado de maneira a separar as responsabilidades em métodos, facilitando a compreensão e manutenção do código.```

```java

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
            Intent intent = new Intent(CadastroActivity.this, PaginaUsuarioActivity.class);
            intent.putExtra("NOME_USUARIO", nome);
            intent.putExtra("EMAIL_USUARIO", email);
            // Iniciar a nova Activity
            startActivity(intent);
            finish();
        }
        private void Iniciar(){

            edtNome = findViewById(R.id.edtNome);
            edtEmail = findViewById(R.id.edtEmail);
            edtSenha = findViewById(R.id.edtSenha);
            btnBotao = findViewById(R.id.btnBotao);

        }
    }






```
Crie uma tela ou formulário para inserir novos dados.
Utilize o Firebase Realtime Database para adicionar dados.
Read (Ler):

Recupere os dados do Firebase e exiba-os na interface do usuário.
Update (Atualizar):

Permita que os usuários editem dados existentes.
Utilize o Firebase para atualizar os dados no banco de dados.
Delete (Excluir):

Adicione a capacidade de excluir itens.
Use o Firebase para remover os dados correspondentes.

