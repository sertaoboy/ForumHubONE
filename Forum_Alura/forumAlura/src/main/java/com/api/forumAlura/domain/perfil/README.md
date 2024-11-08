# Dominio perfil
## Classes:
- Classes DTO para os metodos http de CRUD:
```java
public record DadosCadastroPerfil(@NotBlank String nome) {
}
```
```java
public record DadosAtualizacaoPerfil (@NotNull  Long id,
                                      String nome) {
}
```
```java
public record DadosListagemPerfil (Long id,
                                   String nome
                                   ) {
    public DadosListagemPerfil(Perfil perfil){
         this(perfil.getId(), perfil.getNome());
    }
}
```

## Repository
```java
public interface PerfilRepository extends JpaRepository<Perfil,Long> {
    Page<Perfil> findAllByAtivoTrue(Pageable paginacao);

}
```

## Entidade
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
    @JsonIgnore //ignora usuario ao serializar o perfil
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