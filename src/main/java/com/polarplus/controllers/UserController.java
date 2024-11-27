package com.polarplus.controllers;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarplus.domain.user.User;
import com.polarplus.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;

    // ! DESCOBRIR COMO RESOLVER BUG DE VALIDAÇÃO DE PERMISSÃO
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getUser(Authentication authentication) {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable UUID id, Authentication authentication) {
        try {
            User user = userService.getOne(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}
