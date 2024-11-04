package com.api.forumAlura.domain.topico;

import com.api.forumAlura.domain.curso.Curso;
import com.api.forumAlura.domain.curso.DadosCurso;
import com.api.forumAlura.domain.usuario.DadosUsuario;
import com.api.forumAlura.domain.usuario.Usuario;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
public record DadosCadastroTopico(
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotNull Long autorId,
        @NotNull Long cursoId) {
}
