# Projeto de CRUD com Firebase em Java mobile para Dispositivos Móveis
Este projeto foi desenvolvido como parte da disciplina de Laboratório de Programação para Dispositivos Móveis. O objetivo principal é demonstrar a implementação de um ```CRUD (Create, Read, Update, Delete)``` utilizando o Firebase como banco de dados em um aplicativo mobile em Java.

##Passo a Passo Básico
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
Implementação do CRUD
Create (Criar):

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
## Subtítulo
### Sub-subtítulo
Destacar código: Para destacar um bloco de código, você pode usar três crases () antes e depois do bloco de código. Por exemplo:
```print(“Olá, mundo!”)```

Se você quiser especificar a linguagem de programação para o destaque de sintaxe, pode fazê-lo da seguinte maneira:

```java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Olá, mundo!");
    }
}
```
safasasfas
