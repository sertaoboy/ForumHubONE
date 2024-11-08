# Dominio resposta
## Classes:
- Classes DTO para os metodos http de CRUD:
```java
public record DadosCadastroResposta(@NotBlank String mensagem,
                                    @NotNull Long topicoId,
                                    @NotNull Long usuarioId) {
}
```
```java
public record DadosListagemResposta(Long id,
                                    String mensagem,
                                    LocalDateTime dataCriacao,
                                    Boolean solucao,
                                    DadosListagemUsuario autor) {

    public DadosListagemResposta(Resposta resposta) {
        this(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getSolucao(),
                new DadosListagemUsuario(resposta.getAutor())
        );
    }
}
```

### Repository 
```java
public interface RespostaRepository extends JpaRepository<Resposta,Long> {
}
```

### Entidade
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