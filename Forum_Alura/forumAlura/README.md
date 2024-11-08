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
[README.md](src/main/java/com/api/forumAlura/domain/curso/README.md)
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
[README.md](src/main/java/com/api/forumAlura/domain/perfil/README.md)
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
### Resposta
[README.md](src/main/java/com/api/forumAlura/domain/resposta/README.md)
- Campos:
```
MariaDB [ForumHub]> describe respostas
    -> ;
+--------------+------------+------+-----+---------------------+----------------+
| Field        | Type       | Null | Key | Default             | Extra          |
+--------------+------------+------+-----+---------------------+----------------+
| id           | bigint(20) | NO   | PRI | NULL                | auto_increment |
| mensagem     | longtext   | NO   |     | NULL                |                |
| data_criacao | timestamp  | YES  |     | current_timestamp() |                |
| solucao      | tinyint(1) | YES  |     | 0                   |                |
| topico_id    | bigint(20) | NO   | MUL | NULL                |                |
| usuario_id   | bigint(20) | NO   | MUL | NULL                |                |
+--------------+------------+------+-----+---------------------+----------------+
```
- Relacionamentos: relacoes "muitos para um" entre as entidades Resposta, Topico(`topico_id`) e Usuario (`usuario_id`).
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
### Topico
[README.md](src/main/java/com/api/forumAlura/domain/topico/README.md)
- Campos:
```
  MariaDB [ForumHub]> describe topicos;
  +--------------+--------------+------+-----+---------------------+----------------+
  | Field        | Type         | Null | Key | Default             | Extra          |
  +--------------+--------------+------+-----+---------------------+----------------+
  | id           | bigint(20)   | NO   | PRI | NULL                | auto_increment |
  | titulo       | varchar(255) | NO   |     | NULL                |                |
  | mensagem     | longtext     | NO   |     | NULL                |                |
  | data_criacao | timestamp    | YES  |     | current_timestamp() |                |
  | status       | tinyint(1)   | NO   |     | NULL                |                |
  | usuario_id   | bigint(20)   | NO   | MUL | NULL                |                |
  | curso_id     | bigint(20)   | NO   | MUL | NULL                |                |
  | ativo        | tinyint(1)   | NO   |     | NULL                |                |
  +--------------+--------------+------+-----+---------------------+----------------+
  ```
- Relacionamentos: a entidade Topico possui duas relacoes "muitos para um" com Usuario (`usuario_id`) e Curso (`curso_id`), e uma relacao "um para muitos" com a entidade Resposta.
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
### Usuario
[README.md](src/main/java/com/api/forumAlura/domain/usuario/README.md)
- Campos:
```
MariaDB [ForumHub]> describe usuarios;
+-------+--------------+------+-----+---------+----------------+
| Field | Type         | Null | Key | Default | Extra          |
+-------+--------------+------+-----+---------+----------------+
| id    | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| nome  | varchar(100) | NO   |     | NULL    |                |
| email | varchar(255) | NO   | UNI | NULL    |                |
| senha | varchar(255) | NO   |     | NULL    |                |
| ativo | tinyint(1)   | NO   |     | NULL    |                |
+-------+--------------+------+-----+---------+----------------+
```
- Relacionamentos: "Um para muitos", seguindo a logica bidirecional da entidade Perfil.
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

## Controllers
[README.md](src/main/java/com/api/forumAlura/controllers/README.md)

## Spring Security
### SecurityConfig
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityFilter securityFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Desativa a proteção CSRF
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/login").permitAll()  // Permite acesso ao login e ao registro de usuário
                                .requestMatchers("/topicos/**").authenticated()  // Exige autenticação para a rota /topicos
                                .anyRequest().authenticated()  // Exige autenticação para todas as outras rotas
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)  // Filtro de segurança personalizado
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless (sem sessão)
                .build();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
### Security Filter
```java
@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("CHAMANDO FILTER");
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            UserDetails usuario = usuarioRepository.findByNome(subject); 
            if (usuario != null) {
                var authorities = usuario.getAuthorities();
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("LOGADO NA REQUISICAO");
            }
        }
        filterChain.doFilter(request, response);
    }
    
    private boolean shouldBypassAuthentication(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/usuarios") || uri.startsWith("/login"); 
    }
    
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
    
    private UserDetails getUsuarioFromToken(String tokenJWT) {
        var subject = tokenService.getSubject(tokenJWT);
        return usuarioRepository.findByNome(subject);
    }
}
```
### TokenService
```java
@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(5).toInstant(ZoneOffset.of("-03:00"));
    }

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("API FórumHub")
                    .withSubject(usuario.getUsername())
                    .withClaim("email",usuario.getEmail())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerrar token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API FórumHub")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado: "+tokenJWT);
        }
    }
}
```

## Exceptions
```java
@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException e){
        var erros = e.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream()
                .map(DadosErrosValidacao::new)
                .toList());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarErroAcessoNegado() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity tratarErro400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }




    
    private record DadosErrosValidacao(String campo, String mensagem){

        public DadosErrosValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
```

## Migrations flyway
- v1
```sql
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL
);
```
- v2
```sql
CREATE TABLE perfis (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL UNIQUE,
    ativo BOOLEAN NOT NULL,
    usuario_id BIGINT,  -- Adicionando a coluna para referência
    CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE ON UPDATE CASCADE
);
```
- v3
```sql
CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,  
    ativo BOOLEAN NOT NULL           
);
```
- v4
```sql
CREATE TABLE topicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status BOOLEAN NOT NULL,
    usuario_id BIGINT NOT NULL,  
    curso_id BIGINT NOT NULL,
    ativo BOOLEAN NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id), 
    FOREIGN KEY (curso_id) REFERENCES cursos(id)
); 
```
- v5
```sql
 CREATE TABLE respostas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mensagem TEXT NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    solucao BOOLEAN DEFAULT FALSE,
    topico_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,  
    FOREIGN KEY (topico_id) REFERENCES topicos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)  
);
```



