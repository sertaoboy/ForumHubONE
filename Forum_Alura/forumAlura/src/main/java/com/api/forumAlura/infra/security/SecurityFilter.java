package com.api.forumAlura.infra.security;

import com.api.forumAlura.domain.usuario.Usuario;
import com.api.forumAlura.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("CHAMANDO FILTER");
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            UserDetails usuario = usuarioRepository.findByNome(subject); // Supondo que 'email' seja único
            if (usuario != null) {
                var authorities = usuario.getAuthorities(); // Recupera as authorities do usuário

                // Cria a autenticação com as authorities
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("LOGADO NA REQUISICAO");
            }
        }
        filterChain.doFilter(request, response);
    }



    private boolean shouldBypassAuthentication(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.startsWith("/usuarios") || uri.startsWith("/login");  // Permite que as rotas /usuarios e /login sejam acessadas sem autenticação
    }


    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }


    private UserDetails getUsuarioFromToken(String tokenJWT) {
        var subject = tokenService.getSubject(tokenJWT);
        return usuarioRepository.findByNome(subject);  // Supondo que o email seja único
    }
}
