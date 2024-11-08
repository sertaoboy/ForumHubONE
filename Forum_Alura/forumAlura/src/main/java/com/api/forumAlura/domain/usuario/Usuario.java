package com.api.forumAlura.domain.usuario;

import com.api.forumAlura.domain.perfil.DadosCadastroPerfil;
import com.api.forumAlura.domain.perfil.Perfil;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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