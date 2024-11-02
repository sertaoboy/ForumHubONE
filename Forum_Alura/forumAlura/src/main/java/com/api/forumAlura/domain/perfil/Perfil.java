package com.api.forumAlura.domain.perfil;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

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
    private String nome;


    private Boolean ativo;



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
