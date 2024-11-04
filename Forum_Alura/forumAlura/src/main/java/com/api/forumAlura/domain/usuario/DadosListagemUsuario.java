package com.api.forumAlura.domain.usuario;

import com.api.forumAlura.domain.curso.DadosListagemCurso;
import com.api.forumAlura.domain.perfil.DadosListagemPerfil;
import com.api.forumAlura.domain.perfil.Perfil;

import java.util.List;

public record DadosListagemUsuario(Long id,
                                   String nome,
                                   String email,
                                   List<Perfil> perfis) {
    public DadosListagemUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfis());
    }
}
