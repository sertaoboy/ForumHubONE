package com.api.forumAlura.controllers;

import com.api.forumAlura.domain.curso.*;
import com.api.forumAlura.domain.perfil.DadosAtualizacaoPerfil;
import com.api.forumAlura.domain.perfil.DadosListagemPerfil;
import com.api.forumAlura.domain.perfil.Perfil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping("/cadastrarCursos")
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroCurso dados){
        System.out.println("Salvando no banco...");
        Curso curso = new Curso(dados);
        repository.save(curso);
    }

    @DeleteMapping("/deletarTodos")
    @Transactional
    public void deletarTodos() {
        repository.deleteAll();
    }

    @GetMapping
    public Page<DadosListagemCurso> listar (@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemCurso::new);
    }

    @GetMapping("/listarTodos")
    public Page<DadosListagemCurso> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        return repository.findAll(paginacao)
                .map(DadosListagemCurso::new);
    }

    @PutMapping //so funciona sem rotas adicionais (?)
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoCurso dados){
        Curso curso = repository.getReferenceById(dados.id());
        curso.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        Curso curso = repository.getReferenceById(id);
        curso.excluir();
    }





}
