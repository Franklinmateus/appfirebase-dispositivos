# Projeto de CRUD com Firebase em Java mobile para Dispositivos Móveis
Este projeto foi desenvolvido como parte da disciplina de Laboratório de Programação para Dispositivos Móveis. O objetivo principal é demonstrar a implementação de um ```CRUD (Create, Read, Update, Delete)``` utilizando o Firebase como banco de dados em um aplicativo mobile em Java.

# Passo a Passo Básico

## 1 Configuração do Ambiente de Desenvolvimento
Configuração do Firebase:


## 2 Implementação do CRUD

Nesta parte, abordaremos brevemente o funcionamento do CRUD em um contexto específico, considerando que os leitores já estejam familiarizados com a implementação básica desse padrão. O foco principal deste projeto é demonstrar a integração eficiente do Firebase, destacando como o Realtime Database e o Firestore são utilizados para gerenciar operações de criação, leitura, atualização e exclusão de dados em um aplicativo Android. Ao invés de uma abordagem convencional de banco de dados, este projeto visa oferecer uma visão prática e concisa do poder e da simplicidade que o Firebase traz para o desenvolvimento móvel, especialmente na manipulação em tempo real de dados na nuvem.

### 2.1 CadastroAcitivity (Create):
  A CadastroActivity é uma activity cujo objetivo é o cadastro de usuários. O código inicia configurando a interface do usuário, como campos de texto e botões. Quando o botão de cadastro é clicado, o aplicativo verifica se os campos de nome, e-mail e senha foram preenchidos. Se estiverem preenchidos, o código utiliza o Firebase Authentication para criar um novo usuário. Em caso de sucesso no cadastro, os dados adicionais do usuário (nome e e-mail) são armazenados no Firebase Firestore. O usuário é então redirecionado para uma página de perfil. Caso ocorra algum erro no processo de cadastro, uma mensagem de erro é exibida ao usuário. O código é estruturado de maneira a separar as responsabilidades em métodos, facilitando a compreensão e manutenção do código.

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

### 2.2 EditarUsuarioActivity (Read):

A EditarUsuarioActivity é uma atividade em um aplicativo Android responsável por permitir que os usuários editem seus nomes. Quando a atividade é iniciada, ela recupera o e-mail do usuário da intent que a iniciou. A interface do usuário contém um campo de texto (edtNovoValor) para inserção do novo nome e um botão de salvar (btnSalvar).

Quando o botão de salvar é clicado, o código verifica se o campo do novo valor não está vazio. Se não estiver vazio, ele utiliza o Firebase Firestore para procurar o documento correspondente ao usuário com base no e-mail. Se o usuário for encontrado, o nome do usuário é atualizado com o novo valor. Em caso de sucesso, a atividade é finalizada e retorna para a PaginaUsuarioActivity com os dados atualizados.

Se ocorrerem falhas durante esse processo, mensagens Toast são exibidas para informar o usuário sobre os problemas encontrados.

O código é estruturado de forma a garantir a validação do novo valor, manipulação adequada de dados no Firestore e feedback apropriado para o usuário durante o processo de edição.

```java


  
```




Recupere os dados do Firebase e exiba-os na interface do usuário.
Update (Atualizar):

Permita que os usuários editem dados existentes.
Utilize o Firebase para atualizar os dados no banco de dados.
Delete (Excluir):

Adicione a capacidade de excluir itens.
Use o Firebase para remover os dados correspondentes.

