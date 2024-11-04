package com.api.forumAlura.domain.usuario;

import com.api.forumAlura.domain.perfil.DadosCadastroPerfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DadosCadastroUsuario (@NotBlank String nome,
                                    @NotNull @Email String email,
                                    @NotBlank String senha,
                                   List<DadosCadastroPerfil> perfil){
}
