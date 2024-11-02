package com.api.forumAlura.domain.perfil;

import jakarta.validation.constraints.NotBlank;

public record DadosPerfil(@NotBlank String nome) {
}
