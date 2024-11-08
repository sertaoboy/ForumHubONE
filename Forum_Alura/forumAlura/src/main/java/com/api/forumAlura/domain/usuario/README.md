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

## Repository
```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Page<Usuario> findAllByAtivoTrue(Pageable paginacao);

    UserDetails findByNome(String username);

    Optional<Usuario> findByEmail(String email);
}
```

## Service
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