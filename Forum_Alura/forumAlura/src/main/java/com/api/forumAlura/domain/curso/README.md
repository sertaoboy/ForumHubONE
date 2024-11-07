# Dominio curso
## Classes:
- **Categoria**: Enum utilizado para definir a categoria do curso.
```java
public enum Categoria {
    PROGRAMACAO,
    DESIGN,
    DADOS,
    NEGOCIOS,
    LINUX
}
```

- **CursoRepository**: Repository Pattern para a JPA lidar com as queries e outras operacoes no MariaDB atraves JPQL;
- Metodo _findAllByAtivo(Pageable paginacao)_ consulta o banco de dados para retornar todos os cursos ativos;
```java
public interface CursoRepository extends JpaRepository<Curso, Long> {
    Page<Curso> findAllByAtivoTrue(Pageable paginacao);
}
```

- **DadosAtualizacaoCurso**: DTO para os metodos HTTP PUT de atualizacao de um determinado curso;
```java
public record DadosAtualizacaoCurso(@NotNull Long id,
                                    String nome,
                                    Categoria categoria) {
}
```

- **DadosCadastroCurso**: DTO para os metodos HTTP POST de cadastro para algum curso;
```java
public record DadosCadastroCurso(@NotBlank String nome,
                                 @NotNull Categoria categoria) {
}
```

- **DadosListagemCurso**: DTO usado para os metodos HTTP GET para vizualicao dos cursos.
```java
public record DadosListagemCurso(Long id,
                                 String nome,
                                 Categoria categoria) {
    public DadosListagemCurso(Curso curso){
        this(curso.getId(), curso.getNome(), curso.getCategoria());
    }
}
```

### Controller:
- **CursoController** usado para o Spring mapear as rotas com os seus respectivos metodos de CRUD:
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

### Entidade
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



