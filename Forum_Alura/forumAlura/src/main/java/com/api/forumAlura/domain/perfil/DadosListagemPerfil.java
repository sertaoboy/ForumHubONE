package com.api.forumAlura.domain.perfil;

public record DadosListagemPerfil (Long id,
                                   String nome
                                   ) {
    public DadosListagemPerfil(Perfil perfil){
         this(perfil.getId(), perfil.getNome());
    }
}
