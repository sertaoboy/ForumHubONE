package com.api.forumAlura.controllers;

import com.api.forumAlura.domain.curso.*;
import com.api.forumAlura.domain.perfil.DadosAtualizacaoPerfil;
import com.api.forumAlura.domain.perfil.DadosListagemPerfil;
import com.api.forumAlura.domain.perfil.Perfil;
import com.api.forumAlura.domain.usuario.DadosListagemUsuario;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/cursos")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping("/cadastrarCursos")
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroCurso dados, UriComponentsBuilder uriComponentsBuilder){
        System.out.println("Salvando no banco...");
        Curso curso = new Curso(dados);
        var uri = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();
        repository.save(curso);
        return ResponseEntity.created(uri).body(new DadosListagemCurso(curso));
    }


    @GetMapping
    public ResponseEntity<Page<DadosListagemCurso>> listar (@PageableDefault(size = 10,sort = {"nome"}) Pageable paginacao){
        var page =  repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemCurso::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/listarTodos")
    public Page<DadosListagemCurso> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        return repository.findAll(paginacao)
                .map(DadosListagemCurso::new);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoCurso dados){
        Curso curso = repository.getReferenceById(dados.id());
        curso.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosListagemCurso(curso));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        Curso curso = repository.getReferenceById(id);
        curso.excluir();
        return ResponseEntity.noContent().build();
    }
}
