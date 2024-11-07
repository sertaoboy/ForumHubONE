package com.api.forumAlura.controllers;


import com.api.forumAlura.domain.curso.Curso;
import com.api.forumAlura.domain.curso.CursoRepository;
import com.api.forumAlura.domain.topico.*;

import com.api.forumAlura.domain.usuario.Usuario;
import com.api.forumAlura.domain.usuario.UsuarioRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.Id;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid DadosCadastroTopico dados) {
        if(repository.existsByTituloAndMensagem(dados.titulo(),dados.mensagem())) {   //Regra de negocio
            throw new EntityExistsException("Ja existe um topico com o mesmo titulo e mensagem.");
        }
        Usuario autor = usuarioRepository.findById(dados.autorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Curso curso = cursoRepository.findById(dados.cursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso não encontrado"));

        Topico topico = new Topico(dados);
        topico.setAutor(autor);
        topico.setCurso(curso);

        repository.save(topico);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // Retorna status 201 Created
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> listar(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        var page = repository.findAll(paginacao)
                .map(DadosListagemTopico::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity <DadosListagemTopico> atualizar(@PathVariable Long id,@RequestBody @Valid DadosAtualizacaoTopico dados) {
        Topico topico = repository.findById(dados.id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico não encontrado"));
        updateTopicoProperties(topico, dados);
        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem()) && !topico.getId().equals(id)) {
            throw new EntityExistsException("Já existe outro tópico com o mesmo título e mensagem.");
        }
        repository.save(topico);
        return ResponseEntity.ok(new DadosListagemTopico(topico));
    }

    private void updateTopicoProperties(Topico topico, DadosAtualizacaoTopico dados) {
        if (dados.titulo() != null) {
            topico.setTitulo(dados.titulo());
        }
        if (dados.mensagem() != null) {
            topico.setMensagem(dados.mensagem());
        }
        if (dados.status() != null) {
            topico.setStatus(dados.status());
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        Topico topico = repository.getReferenceById(id);
        topico.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        Topico topico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemTopico(topico));
    }

}
