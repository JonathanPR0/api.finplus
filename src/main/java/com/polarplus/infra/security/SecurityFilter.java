package com.polarplus.infra.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarplus.configuration.SecurityConfig;
import com.polarplus.domain.user.User;
import com.polarplus.repositories.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getServletPath();

        // Verifica se a rota é pública
        if (SecurityConfig.getPublicRoutes().contains(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        var token = this.recoverToken(request);

        // Se não houver token, retorna 401 Unauthorized
        if (token == null) {
            sendErrorResponse(response, "Token não fornecido.");
            return;
        }

        var login = tokenService.validateToken(token);

        if (!login.isBlank() && !login.isEmpty()) {
            sendErrorResponse(response, "Token inválido.");
            return;
        }

        User user = userRepository.findByEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }

    // Método para enviar uma resposta de erro personalizada com código e mensagem
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Cria o corpo da resposta com a mensagem de erro
        var errorResponse = Map.of("message", message);

        // Envia a resposta como JSON
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}