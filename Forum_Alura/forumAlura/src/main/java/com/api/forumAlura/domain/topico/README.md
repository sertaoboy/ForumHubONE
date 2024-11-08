# Dominio topico
## Classes:
- **DadosAtualizacaoTopico**: DTO para o controller realizar o metodo http PUT em um determinado topico.
```java
public record DadosAtualizacaoTopico (@NotNull Long id,
                                      String titulo,
                                      String mensagem,
                                      Boolean status) {
}
```
- **DadosCadastroTopico**: DTO para o controller realizar o metodo http POST em um determinado topico.
```java
public record DadosCadastroTopico(
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotNull Long autorId,
        @NotNull Long cursoId) {
}
```
- **DadosListagemTopico**: DTO para o controller realizar o metodo http GET em um determinado topico.
```java
public record DadosListagemTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        Boolean ativo,
        Boolean status,
        DadosListagemUsuario usuario,
        DadosListagemCurso curso,
        List<DadosListagemResposta> respostas
) {
    public DadosListagemTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getAtivo(),
                topico.getStatus(),
                new DadosListagemUsuario(topico.getAutor()),
                new DadosListagemCurso(topico.getCurso()),  
                topico.getRespostas().stream()
                        .map(DadosListagemResposta::new)
                        .collect(Collectors.toList())
        );
    }

}
```
- Construtor: recebendo um parametro do tipo Topico e atribuindo os valores para os respectivos campos; instanciando um objeto DTO de usuario e curso para realizar a conversao de maneira correta.

### Repository
```java
public interface TopicoRepository extends JpaRepository<Topico,Long> {
    Optional<Topico> findByAutor_Nome(String nome);

    boolean existsByTituloAndMensagem(@NotBlank String titulo, @NotBlank String mensagem);

}
```

### Entidade:
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
