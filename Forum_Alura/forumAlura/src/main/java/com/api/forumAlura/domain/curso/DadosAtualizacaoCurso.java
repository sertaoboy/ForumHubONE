package com.api.forumAlura.domain.curso;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoCurso(@NotNull Long id,
                                    String nome,
                                    Categoria categoria) {
}
