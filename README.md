# Projeto de CRUD com Firebase em Java mobile para Dispositivos Móveis
Este projeto foi desenvolvido como parte da disciplina de Laboratório de Programação para Dispositivos Móveis. O objetivo principal é demonstrar a implementação de um ```CRUD (Create, Read, Update, Delete)``` utilizando o Firebase como banco de dados em um aplicativo mobile em Java.

# Passo a Passo Básico

## 1 Configuração do Ambiente de Desenvolvimento
Configuração do Firebase:


## 2 Resumo activities

Nesta parte, abordaremos brevemente o funcionamento do CRUD pelas activities, considerando que os leitores já estejam familiarizados com a implementação básica desse padrão. O foco principal deste projeto é demonstrar a integração eficiente do Firebase, destacando como o Realtime Database e o Firestore são utilizados para gerenciar operações de criação, leitura, atualização e exclusão de dados em um aplicativo Android. Ao invés de uma abordagem convencional de banco de dados, este projeto visa oferecer uma visão prática e concisa do poder e da simplicidade que o Firebase traz para o desenvolvimento móvel, especialmente na manipulação em tempo real de dados na nuvem.

### 2.1 CadastroAcitivity (Create):
  A CadastroActivity é uma activity cujo objetivo é o cadastro de usuários. O código inicia configurando a interface do usuário, como campos de texto e botões. Quando o botão de cadastro é clicado, o aplicativo verifica se os campos de nome, e-mail e senha foram preenchidos. Se estiverem preenchidos, o código utiliza o Firebase Authentication para criar um novo usuário. <br>
  Em caso de sucesso no cadastro, os dados adicionais do usuário (nome e e-mail) são armazenados no Firebase Firestore. O usuário é então redirecionado para uma página de perfil. Caso ocorra algum erro no processo de cadastro, uma mensagem de erro é exibida ao usuário. O código é estruturado de maneira a separar as responsabilidades em métodos, facilitando a compreensão e manutenção do código.

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

### 2.2 EditarUsuarioActivity (Update):

A EditarUsuarioActivity é uma atividade em um aplicativo Android responsável por permitir que os usuários editem seus nomes. Quando a atividade é iniciada, ela recupera o e-mail do usuário da intent que a iniciou. A interface do usuário contém um campo de texto (edtNovoValor) para inserção do novo nome e um botão de salvar (btnSalvar). <br>
Quando o botão de salvar é clicado, o código verifica se o campo do novo valor não está vazio. Se não estiver vazio, ele utiliza o Firebase Firestore para procurar o documento correspondente ao usuário com base no e-mail. Se o usuário for encontrado, o nome do usuário é atualizado com o novo valor. <br>
Em caso de sucesso, a atividade é finalizada e retorna para a PaginaUsuarioActivity com os dados atualizados. Se ocorrerem falhas durante esse processo, mensagens Toast são exibidas para informar o usuário sobre os problemas encontrados. O código é estruturado de forma a garantir a validação do novo valor, manipulação adequada de dados no Firestore e feedback apropriado para o usuário durante o processo de edição.

```java

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


  
```


### 2.3 PaginaUsuarioActivity (Read,Delete):

A PaginaUsuarioActivity é uma atividade em um aplicativo Android que exibe os detalhes do usuário, como nome e e-mail. Além disso, permite que o usuário edite seu nome ou exclua sua conta. Aqui está um resumo do que o código faz:
```
  Recuperação dos Dados do Usuário:<br>
    Obtém o e-mail do usuário atualmente autenticado no Firebase.<br>
    Consulta o Firestore para obter os dados associados a esse e-mail.<br>
    Exibe o nome e o e-mail do usuário na interface.<br>
    Configuração dos Botões de Edição e Exclusão: <br>
```
Configura o botão "Editar" para iniciar a EditarUsuarioActivity com o e-mail do usuário como um extra da Intent.<br>
Configura o botão "Excluir" para chamar o método excluirUsuario().<br>

Edição do Usuário:
  ```
  Ao clicar em "Editar", inicia a EditarUsuarioActivity e espera um resultado.
  Se o resultado for RESULT_OK e houver um novo nome retornado, atualiza o nome exibido na interface.
  Exclusão do Usuário:
  ```

Ao clicar em "Excluir", chama o método excluirUsuario().
Consulta o Firestore para encontrar o documento associado ao e-mail do usuário.
Exclui o documento do Firestore e exibe mensagens de sucesso ou falha.
Retorno para a CadastroActivity:

Após excluir com sucesso, retorna para a CadastroActivity (página de login/cadastro).
Manipulação de Resultados da Atividade de Edição:

No método onActivityResult(), verifica se a atividade de edição retornou com sucesso e se há um novo nome retornado.
Atualiza o nome exibido na interface com o novo nome, se disponível.
O código é estruturado para interagir eficientemente com o Firebase Firestore, fornecendo uma experiência de usuário interativa para edição e exclusão de informações do usuário. Mensagens Toast são utilizadas para fornecer feedback ao usuário sobre o resultado das operações.


Permita que os usuários editem dados existentes.
Utilize o Firebase para atualizar os dados no banco de dados.
Delete (Excluir):

Adicione a capacidade de excluir itens.
Use o Firebase para remover os dados correspondentes.

