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

import java.util.ArrayList;
import java.util.List;


@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 100)
    private String nome;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private String senha;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Perfil> perfis = new ArrayList<>(); // Inicializa a lista

    private Boolean ativo;


    public Usuario(@Valid DadosCadastroUsuario dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.senha = dados.senha();
        this.ativo = true;
        // adiciona perfis recebidos
        if (dados.perfil() != null) {
            for (DadosCadastroPerfil dadosPerfil : dados.perfil()) {
                Perfil perfil = new Perfil(dadosPerfil);
                perfil.setUsuario(this); // vincula o perfil ao usuário
                this.perfis.add(perfil);
            }
        }
    }


    public void atualizarInformacoes(DadosAtualizacaoUsuario dados) {
        this.nome= dados.nome();
        this.email= dados.email();
        this.senha= dados.senha();
    }

    public void excluir() {
        this.ativo=false;
    }
}