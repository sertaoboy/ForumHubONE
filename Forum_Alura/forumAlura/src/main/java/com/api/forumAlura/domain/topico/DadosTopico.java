//package com.api.forumAlura.domain.topico;
//
//import com.api.forumAlura.domain.curso.DadosCurso;
//import com.api.forumAlura.domain.resposta.DadosResposta;
//import com.api.forumAlura.domain.usuario.DadosUsuario;
//
//import javax.swing.text.StyledEditorKit;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public record DadosTopico(@NotBlank String titulo,
//                         @NotBlank String mensagem,
//                          @NotNull LocalDateTime dataCriacao,
//                          @NotNull Boolean status,
//                          @NotNull @Valid DadosUsuario usuario,
//                          @NotNull @Valid DadosCurso curso,
//                          @Valid List<DadosResposta> respostas){
//}
