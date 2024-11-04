package com.api.forumAlura.domain.resposta;

import com.api.forumAlura.domain.usuario.DadosListagemUsuario;

import java.time.LocalDateTime;

public record DadosListagemResposta(Long id,
                                    String mensagem,
                                    LocalDateTime dataCriacao,
                                    Boolean solucao,
                                    DadosListagemUsuario autor) {

    public DadosListagemResposta(Resposta resposta) {
        this(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getSolucao(),
                new DadosListagemUsuario(resposta.getAutor())
        );
    }
}
