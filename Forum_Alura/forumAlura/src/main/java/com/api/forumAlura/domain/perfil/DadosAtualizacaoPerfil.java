package com.api.forumAlura.domain.perfil;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPerfil (@NotNull  Long id,
                                      String nome) {
}
