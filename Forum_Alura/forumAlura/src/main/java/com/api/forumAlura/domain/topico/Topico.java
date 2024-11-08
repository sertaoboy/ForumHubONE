package com.api.forumAlura.domain.topico;

import com.api.forumAlura.domain.curso.Curso;
import com.api.forumAlura.domain.resposta.Resposta;
import com.api.forumAlura.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
