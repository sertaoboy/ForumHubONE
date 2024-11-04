package com.api.forumAlura.domain.topico;

import com.api.forumAlura.domain.curso.DadosListagemCurso;
import com.api.forumAlura.domain.resposta.DadosListagemResposta;
import com.api.forumAlura.domain.resposta.Resposta;
import com.api.forumAlura.domain.usuario.DadosListagemUsuario;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DadosListagemTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        Boolean ativo,
        Boolean status,
        DadosListagemUsuario usuario,
        DadosListagemCurso curso,
        List<DadosListagemResposta> respostas
) {
    public DadosListagemTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getAtivo(),
                topico.getStatus(),
                new DadosListagemUsuario(topico.getAutor()), // DTO para Usuario
                new DadosListagemCurso(topico.getCurso()),   // DTO para Curso
                topico.getRespostas().stream()
                        .map(DadosListagemResposta::new) // Transformando Resposta em DadosListagemResposta
                        .collect(Collectors.toList())
        );
    }


}
