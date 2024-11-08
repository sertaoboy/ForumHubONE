# Dominio usuario
## Classes:
- Classes DTO para os metodos http de CRUD:
```java
public record DadosAtualizacaoUsuario(@NotNull Long id,
                                      String nome,
                                      String email,
                                      String senha) {
}
```
```java
public record DadosAutenticacao(String nome,
                                String senha) {
}
```
```java
public record DadosCadastroUsuario (@NotBlank String nome,
                                    @NotNull @Email String email,
                                    @NotBlank String senha,
                                   List<DadosCadastroPerfil> perfil){
}

```
```java
public record DadosListagemUsuario(Long id,
                                   String nome,
                                   String email,
                                   List<Perfil> perfis) {
    public DadosListagemUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfis());
    }
}
```
```java
public record DadosUsuario(Long id,
                           String nome,
                           String email,
                           String senha,
                           List<DadosPerfil> perfis) {
}
```

### Repository
```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

    UserDetails findByNome(String username);

    Optional<Usuario> findByEmail(String email);
}
```

### Service
- Servico de autenticacao do usuario:
```java
@Service
public class AutenticacaoService implements UserDetailsService {
    @Autowired
    private UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByNome(username);
    }
}
```

### Entidade
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