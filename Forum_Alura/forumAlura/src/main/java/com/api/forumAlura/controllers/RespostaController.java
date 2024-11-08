package com.api.forumAlura.controllers;

import com.api.forumAlura.domain.resposta.DadosCadastroResposta;
import com.api.forumAlura.domain.resposta.DadosListagemResposta;
import com.api.forumAlura.domain.resposta.Resposta;
import com.api.forumAlura.domain.resposta.RespostaRepository;
import com.api.forumAlura.domain.topico.DadosListagemTopico;
import com.api.forumAlura.domain.topico.Topico;
import com.api.forumAlura.domain.topico.TopicoRepository;
import com.api.forumAlura.domain.usuario.Usuario;
import com.api.forumAlura.domain.usuario.UsuarioRepository;
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

import java.util.List;

@RestController
@RequestMapping("respostas")
public class RespostaController {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Void> cadastrar(@RequestBody @Valid DadosCadastroResposta dados) {
        Topico topico = topicoRepository.findById(dados.topicoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tópico não encontrado"));

        Usuario autor = usuarioRepository.findById(dados.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        Resposta resposta = new Resposta();
        resposta.setMensagem(dados.mensagem());
        resposta.setTopico(topico);
        resposta.setAutor(autor);
        resposta.setSolucao(false);
        try{
            respostaRepository.save(resposta);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Erro ao salvar a resposta"+ e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemResposta>> listar(@PageableDefault(size = 30,sort = {"id"}) Pageable paginacao){
        var page = respostaRepository.findAll(paginacao)
                .map(DadosListagemResposta::new);
        return ResponseEntity.ok(page);
    }
}