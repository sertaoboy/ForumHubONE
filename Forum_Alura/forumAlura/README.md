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
```
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
```
## Entidades:
### Curso
- Campos: 
```
MariaDB [ForumHub]> describe cursos;
+-----------+--------------+------+-----+---------+----------------+
| Field     | Type         | Null | Key | Default | Extra          |
+-----------+--------------+------+-----+---------+----------------+
| id        | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| nome      | varchar(100) | NO   |     | NULL    |                |
| categoria | varchar(50)  | NO   |     | NULL    |                |
| ativo     | tinyint(1)   | NO   |     | NULL    |                |
+-----------+--------------+------+-----+---------+----------------+
```
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "Curso")
@Table(name = "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;


    @Enumerated (EnumType.STRING)
    private Categoria categoria;

    private Boolean ativo;
    
    
    public Curso( DadosCadastroCurso dados) {
        this.ativo = true;
        this.nome= dados.nome();
        this.categoria=dados.categoria();
    }
    
    public void excluir(){
        this.ativo=false;
    }
    
    public void atualizarInformacoes(@Valid DadosAtualizacaoCurso dados) {
        this.nome = dados.nome();
        this.categoria=dados.categoria();
    }
}
```
### Perfil
- Campos:
```
MariaDB [ForumHub]> describe perfis;
+------------+--------------+------+-----+---------+----------------+
| Field      | Type         | Null | Key | Default | Extra          |
+------------+--------------+------+-----+---------+----------------+
| id         | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| nome       | varchar(255) | NO   | UNI | NULL    |                |
| ativo      | tinyint(1)   | NO   |     | NULL    |                |
| usuario_id | bigint(20)   | YES  | MUL | NULL    |                |
+------------+--------------+------+-----+---------+----------------+
```
- Relacionamentos: Ha uma relacao com a entidade Usuario, onde essa seria de "muitos para um", atraves do cruzamento de dados nas tabelas (`usuario_id`).
```java
@Entity(name = "Perfil")
@Table(name = "perfis")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String nome;
    
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore 
    private Usuario usuario;

    public Perfil (DadosCadastroUsuario dadosUsuarioCadastro){
        this.ativo = true;
        this.nome= dadosUsuarioCadastro.nome();

    }

    public Perfil(DadosCadastroPerfil dados) {
        this.ativo = true;
        this.nome= dados.nome();
    }

    public void atualizarInformacoes(DadosAtualizacaoPerfil dados) {
        if(dados.nome()!=null) {
            this.nome= dados.nome();
        }
    }

    public void excluir() {
        this.ativo=false;
    }
}
```
```java
@Entity(name = "Resposta")
@Table(name = "respostas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Lob
    private String mensagem;

    @NotNull
    private LocalDateTime dataCriacao = LocalDateTime.now();

    private Boolean solucao = false;

    @ManyToOne
    @JoinColumn(name = "topico_id", nullable = false)
    private Topico topico;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autor;

}
```
```java
@Entity(name = "Topico")
@Table(name = "topicos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String titulo;

    @NotBlank
    @Lob
    private String mensagem;

    @NotNull
    private LocalDateTime dataCriacao = LocalDateTime.now();

    private Boolean ativo;

    private Boolean status;
    
    @Setter
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario autor;

    @Setter
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "topico")
    private List<Resposta> respostas = new ArrayList<>();

    public Topico(DadosCadastroTopico dados) {
        this.titulo = dados.titulo();
        this.mensagem = dados.mensagem();
        this.dataCriacao = LocalDateTime.now();
        this.ativo = true;
        this.status = false;
    }

    public void excluir() {
        this.ativo = false;
    }

}
```
```java
@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Column(name = "senha")
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Perfil> perfis = new ArrayList<>();

    private Boolean ativo;
    
    public Usuario(@Valid DadosCadastroUsuario dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.password = dados.senha();
        this.ativo = true;
        if (dados.perfil() != null) {
            for (DadosCadastroPerfil dadosPerfil : dados.perfil()) {
                Perfil perfil = new Perfil(dadosPerfil);
                perfil.setUsuario(this); 
                this.perfis.add(perfil);
            }
        }
    }
    
    public void atualizarInformacoes(DadosAtualizacaoUsuario dados) {
        this.nome= dados.nome();
        this.email= dados.email();
        this.password = dados.senha();
    }

    public void excluir() {
        this.ativo=false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return nome;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
```




