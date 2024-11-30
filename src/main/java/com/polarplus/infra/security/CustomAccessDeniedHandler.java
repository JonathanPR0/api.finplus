package com.polarplus.infra.security;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.polarplus.controllers.AuthController;
import com.polarplus.domain.user.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Usando SecurityContextHolder para obter o nome do usuário
        String username = "desconhecido";
        Principal principal = request.getUserPrincipal();
        if (principal instanceof User) {
            username = ((User) principal).getName(); // Se for uma instância de uma classe User
        } else if (principal != null) {
            username = principal.getName(); // Usar o método getName() padrão
        }

        System.err.println(username);
        logger.warn("Acesso negado para o usuário: " +
                username +
                ", URL: " + request.getRequestURI());
        // Corpo da resposta JSON
        var errorResponse = Map.of(
                "error", accessDeniedException.getMessage(),
                "message", "Acesso negado",
                "path", request.getRequestURI());

        // Converte para JSON e escreve no corpo da resposta
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
