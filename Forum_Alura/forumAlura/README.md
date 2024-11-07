# Projeto ForumHub, Alura
- Projeto proposto pelo curso de SpringBoot do programa de qualificacao ONE;
### Visao geral:
- Objetivo: ter a experiência de realizar um projeto, tal como ocorre no cotidiano de uma pessoa desenvolvedora; ter a oportunidade de praticar os conceitos aprendidos nos cursos.
- Referencias:
1. https://cursos.alura.com.br/course/spring-boot-3-desenvolva-api-rest-java
2. https://cursos.alura.com.br/course/spring-boot-aplique-boas-praticas-proteja-api-rest
3. https://cursos.alura.com.br/course/spring-boot-3-documente-teste-prepare-api-deploy
4. https://trello.com/b/OKIUKgxe/alura-f%C3%B3rum-challenge-one-sprint-01
5. https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpClient.html
6. https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpRequest.html
7. https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpResponse.html

## Dominios :
`
forumAlura
├── src
 ├── main
  ├── java
   └── com
       └── api
           └── forumAlura
               ├── controllers
                ├── AutenticacaoController.java
                ├── CursoController.java
                ├── PerfilController.java
                ├── RespostaController.java
                ├── TopicoController.java
                └── UsuarioController.java
               ├── domain
                ├── curso
                 ├── Categoria.java
                 ├── Curso.java
                 ├── CursoRepository.java
                 ├── DadosAtualizacaoCurso.java
                 ├── DadosCadastroCurso.java
                 ├── DadosCurso.java
                 └── DadosListagemCurso.java
                ├── perfil
                 ├── DadosAtualizacaoPerfil.java
                 ├── DadosCadastroPerfil.java
                 ├── DadosListagemPerfil.java
                 ├── DadosPerfil.java
                 ├── Perfil.java
                 └── PerfilRepository.java
                ├── resposta
                 ├── DadosCadastroResposta.java
                 ├── DadosListagemResposta.java
                 ├── DadosResposta.java
                 ├── Resposta.java
                 └── RespostaRepository.java
                ├── topico
                 ├── DadosAtualizacaoTopico.java
                 ├── DadosCadastroTopico.java
                 ├── DadosListagemTopico.java
                 ├── DadosTopico.java
                 ├── Topico.java
                 └── TopicoRepository.java
                └── usuario
                    ├── AutenticacaoService.java
                    ├── DadosAtualizacaoUsuario.java
                    ├── DadosAutenticacao.java
                    ├── DadosCadastroUsuario.java
                    ├── DadosListagemUsuario.java
                    ├── DadosUsuario.java
                    ├── Usuario.java
                    └── UsuarioRepository.java
               ├── ForumAluraApplication.java
               └── infra
                   ├── exceptions
                    └── TratadorDeErros.java
                   └── security
                       ├── DadosTokenJWT.java
                       ├── SecurityConfig.java
                       ├── SecurityFilter.java
                       └── TokenService.java
  └── resources
      ├── application.properties
      ├── db
       └── migration
           ├── V1__create-table-usuarios.sql
           ├── V2__create-table-perfis.sql
           ├── V3__create-table-cursos.sql
           ├── V4__create-table-topicos.sql
           └── V5__create-table-respostas.sql
`
## Entidades:



