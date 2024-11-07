package com.api.forumAlura.controllers;


import com.api.forumAlura.domain.perfil.DadosCadastroPerfil;
import com.api.forumAlura.domain.perfil.Perfil;
import com.api.forumAlura.domain.usuario.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriComponentsBuilder) {
        System.out.println("Salvando no banco...");
        if(repository.findByNome(dados.nome())!= null) {
            return ResponseEntity.badRequest().body("Nome de usuario ja existe.");
        }
        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        Usuario usuario = new Usuario(dados);
        usuario.setPassword(senhaCriptografada);
        var uri = uriComponentsBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        System.out.println("Usuario a ser salvo: " + usuario);
        repository.save(usuario);
        return ResponseEntity.created(uri).body(new DadosListagemUsuario(usuario));
    }

    @PostMapping("/{id}/adicionarPerfil")
    @Transactional
    public ResponseEntity adicionarPerfil(@PathVariable Long id, @RequestBody @Valid DadosCadastroPerfil dadosPerfil) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        Perfil novoPerfil = new Perfil(dadosPerfil);
        novoPerfil.setUsuario(usuario);
        usuario.getPerfis().add(novoPerfil);
        repository.save(usuario);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemUsuario>> listar (@PageableDefault(size = 10,sort = {"id"}) Pageable paginacao){
        var page =  repository.findAllByAtivoTrue(paginacao)
                .map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<Page<DadosListagemUsuario>> listarTodos(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        var page = repository.findAll(paginacao)
                .map(DadosListagemUsuario::new);
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados){
        Usuario usuario = repository.getReferenceById(dados.id());
        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        usuario.atualizarInformacoes(dados);
        usuario.setPassword(senhaCriptografada);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        Usuario usuario = repository.getReferenceById(id);
        usuario.excluir();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        Usuario usuario = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosListagemUsuario(usuario));
    }















}
