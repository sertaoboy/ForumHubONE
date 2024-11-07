package com.api.forumAlura.domain.topico;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TopicoRepository extends JpaRepository<Topico,Long> {
    Optional<Topico> findByAutor_Nome(String nome);

    boolean existsByTituloAndMensagem(@NotBlank String titulo, @NotBlank String mensagem);

}
