package com.api.forumAlura.domain.usuario;

import com.api.forumAlura.domain.perfil.DadosPerfil;

import java.util.List;

public record DadosUsuario(String nome,
                           String email,
                           String senha,
                           List<DadosPerfil> perfis) {
}
