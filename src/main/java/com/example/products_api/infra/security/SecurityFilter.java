package com.example.products_api.infra.security;

import com.example.products_api.repositories.UserRepository;
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

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token do header Authorization
        String token = recoverToken(request);

        if (token != null) {
            // Valida o token e extrai o login do usuário
            String login = tokenService.validateToken(token);

            if (login != null) {
                // Busca o usuário no repositório
                UserDetails user = userRepository.findByLogin(login);

                if (user != null) {
                    // Cria um objeto de autenticação e define no contexto de segurança
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // Continua a execução do filtro
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        // Extrai o token do header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        return authHeader.replace("Bearer ", "").trim(); // Remove o prefixo "Bearer" e espaços adicionais
    }
}