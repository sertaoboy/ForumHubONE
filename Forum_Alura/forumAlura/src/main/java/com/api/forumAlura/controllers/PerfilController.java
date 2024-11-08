package com.api.forumAlura.controllers;

import com.api.forumAlura.domain.perfil.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("perfis")
public class PerfilController {

    @Autowired
    private PerfilRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroPerfil dados, UriComponentsBuilder uriComponentsBuilder) {
        System.out.println("Salvando no banco...");
        Perfil perfil = new Perfil(dados);
        var uri = uriComponentsBuilder.path("/perfis/{id}").buildAndExpand(perfil.getId()).toUri();
        System.out.println("Perfil a ser salvo: " + perfil);
        repository.save(perfil);
        return ResponseEntity.created(uri).body(new DadosListagemPerfil(perfil));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemPerfil>> listar (@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        var page = repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemPerfil::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<Page<DadosListagemPerfil>> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        var page =  repository.findAll(paginacao)
                .map(DadosListagemPerfil::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPerfil dados){
        Perfil perfil = repository.getReferenceById(dados.id());
        perfil.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemPerfil(perfil));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        var perfil = repository.getReferenceById(id);
        perfil.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        var perfil = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemPerfil(perfil));
    }
}
