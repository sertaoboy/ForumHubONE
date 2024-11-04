package com.api.forumAlura.domain.curso;


import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


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
