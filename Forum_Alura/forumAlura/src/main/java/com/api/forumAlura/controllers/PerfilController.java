package com.api.forumAlura.controllers;

import com.api.forumAlura.domain.perfil.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("perfis")
public class PerfilController {

    @Autowired
    private PerfilRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroPerfil dados) {
        System.out.println("Salvando no banco...");
        Perfil perfil = new Perfil(dados);
        System.out.println("Perfil a ser salvo: " + perfil);
        repository.save(perfil);
    }

    @DeleteMapping("/deletarTodos")
    @Transactional
    public void deletarTodos() {
        repository.deleteAll();
    }


    @GetMapping
    public Page<DadosListagemPerfil> listar (@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemPerfil::new);
    }

    @GetMapping("/listarTodos")
    public Page<DadosListagemPerfil> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        return repository.findAll(paginacao)
                .map(DadosListagemPerfil::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoPerfil dados){
        Perfil perfil = repository.getReferenceById(dados.id());
        perfil.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        var perfil = repository.getReferenceById(id);
        perfil.excluir();
    }


}
