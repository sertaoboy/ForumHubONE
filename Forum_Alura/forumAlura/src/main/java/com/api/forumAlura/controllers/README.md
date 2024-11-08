# Domain _Controllers_
### Autenticacao
- Utilizada para autenticar o usuario atraves do token JWT;
- Injecoes de dependencias (`@Autorwired`) para o Spring gerenciar o metodo `efetuarLogin(DTO dados)`.
```java
@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados){
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.nome(),dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
```

### Curso
- Utilizada para realizar os metodos http de CRUD;
- Injecao de dependencia do _Repository_ para gerenciar as queries no banco de dados
```java
@RestController
@RequestMapping(value = "/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping("/cadastrarCursos")
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroCurso dados, UriComponentsBuilder uriComponentsBuilder){
        System.out.println("Salvando no banco...");
        Curso curso = new Curso(dados);
        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();
        repository.save(curso);
        return ResponseEntity.created(uri).body(new DadosListagemCurso(curso));
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemCurso>> listar (@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        var page =  repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemCurso::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/listarTodos")
    public Page<DadosListagemCurso> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        return repository.findAll(paginacao)
                .map(DadosListagemCurso::new);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoCurso dados){
        Curso curso = repository.getReferenceById(dados.id());
        curso.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemCurso(curso));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        Curso curso = repository.getReferenceById(id);
        curso.excluir();
        return ResponseEntity.noContent().build();
    }
}
```

### Perfil
- Utilizada para realizar os metodos http de CRUD;
- Injecao de dependencia do _Repository_ para gerenciar as queries no banco de dados.
```java
@RestController
@RequestMapping("perfis")
public class PerfilController {

    @Autowired
    private PerfilRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPerfil dados, UriComponentsBuilder uriComponentsBuilder) {
        System.out.println("Salvando no banco...");
        Perfil perfil = new Perfil(dados);
        var uri = uriComponentsBuilder.path("/perfis/{id}").buildAndExpand(perfil.getId()).toUri();
        System.out.println("Perfil a ser salvo: " + perfil);
        repository.save(perfil);
        return ResponseEntity.created(uri).body(new DadosListagemPerfil(perfil));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPerfil>> listar (@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemPerfil::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<Page<DadosListagemPerfil>> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        var page =  repository.findAll(paginacao)
                .map(DadosListagemPerfil::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPerfil dados){
        Perfil perfil = repository.getReferenceById(dados.id());
        perfil.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemPerfil(perfil));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var perfil = repository.getReferenceById(id);
        perfil.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var perfil = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemPerfil(perfil));
    }
}
```

### Resposta
- Utilizada para realizar os metodos http POST e GET;
- Injecoes de dependencias: _Repository_ de Reposta, Topico e Usuario.
```java
@RestController
@RequestMapping("respostas")
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid DadosCadastroResposta dados) {
        Topico topico = topicoRepository.findById(dados.topicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico não encontrado"));

        Usuario autor = usuarioRepository.findById(dados.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Resposta resposta = new Resposta();
        resposta.setMensagem(dados.mensagem());
        resposta.setTopico(topico);
        resposta.setAutor(autor);
        resposta.setSolucao(false);
        try{
            respostaRepository.save(resposta);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Erro ao salvar a resposta"+ e.getMessage());
        }

    }
    
    @GetMapping
    public ResponseEntity<Page<DadosListagemResposta>> listar(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        var page = respostaRepository.findAll(paginacao)
                .map(DadosListagemResposta::new);
        return ResponseEntity.ok(page);
    }
}
```

### Topico
- Utilizada para realizar os metodos http de CRUD;
- Injecoes de dependencias para o Spring: _Repository_ de Topico, Usuario e Curso.
```java
@RestController
@RequestMapping("topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid DadosCadastroTopico dados) {
        if(repository.existsByTituloAndMensagem(dados.titulo(),dados.mensagem())) {   //Regra de negocio
            throw new EntityExistsException("Ja existe um topico com o mesmo titulo e mensagem.");
        }
        Usuario autor = usuarioRepository.findById(dados.autorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        Topico topico = new Topico(dados);
        topico.setAutor(autor);
        topico.setCurso(curso);

        repository.save(topico);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // Retorna status 201 Created
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        var page = repository.findAll(paginacao)
                .map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity <DadosListagemTopico> atualizar(@PathVariable Long id,@RequestBody @Valid DadosAtualizacaoTopico dados) {
        Topico topico = repository.findById(dados.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico não encontrado"));
        updateTopicoProperties(topico, dados);
        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem()) && !topico.getId().equals(id)) {
            throw new EntityExistsException("Já existe outro tópico com o mesmo título e mensagem.");
        }
        repository.save(topico);
        return ResponseEntity.ok(new DadosListagemTopico(topico));
    }

    private void updateTopicoProperties(Topico topico, DadosAtualizacaoTopico dados) {
        if (dados.titulo() != null) {
            topico.setTitulo(dados.titulo());
        }
        if (dados.mensagem() != null) {
            topico.setMensagem(dados.mensagem());
        }
        if (dados.status() != null) {
            topico.setStatus(dados.status());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        Topico topico = repository.getReferenceById(id);
        topico.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        Topico topico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemTopico(topico));
    }

}
```

### Usuario
- Utilizada para realizar os metodos http de CRUD;
- Injecoes de dependencias: _Repository_ de Usuario; `BCryptPasswordEncoder`, classe utilizada para realizar a encriptografia da senha registrada atraves do metodo `encode(String senha)`;
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
