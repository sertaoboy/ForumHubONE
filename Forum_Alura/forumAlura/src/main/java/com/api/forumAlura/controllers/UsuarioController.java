package com.api.forumAlura.controllers;


import com.api.forumAlura.domain.perfil.DadosCadastroPerfil;
import com.api.forumAlura.domain.perfil.Perfil;
import com.api.forumAlura.domain.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroUsuario dados) {
        System.out.println("Salvando no banco...");
        Usuario usuario = new Usuario(dados);
        System.out.println("Usuario a ser salvo: " + usuario);
        repository.save(usuario);
    }

    @PostMapping("/{id}/adicionarPerfil")
    @Transactional
    public void adicionarPerfil(@PathVariable Long id, @RequestBody @Valid DadosCadastroPerfil dadosPerfil) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        Perfil novoPerfil = new Perfil(dadosPerfil);
        novoPerfil.setUsuario(usuario); // Vincula o perfil ao usuário
        usuario.getPerfis().add(novoPerfil); // Adiciona o perfil à lista
        repository.save(usuario); // Salva as alterações
    }

    @GetMapping
    public Page<DadosListagemUsuario> listar (@PageableDefault(size = 10,sort = {"id"}) Pageable paginacao){
        return repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemUsuario::new);
    }

    @GetMapping("/listarTodos")
    public Page<DadosListagemUsuario> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        return repository.findAll(paginacao)
                .map(DadosListagemUsuario::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados){
        Usuario usuario = repository.getReferenceById(dados.id());
        usuario.atualizarInformacoes(dados);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
        Usuario usuario = repository.getReferenceById(id);
        usuario.excluir();
    }















}
