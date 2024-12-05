package com.polarplus.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarplus.domain.Banco;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.services.BancoService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bancos")
@RequiredArgsConstructor

public class BancoController {
    private final BancoService bancoService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @GetMapping
    public ResponseEntity<?> getBancos(Authentication authentication, @ModelAttribute PaginationDTO pagination,
            @ModelAttribute FilterTermoDTO filters) {
        try {

            PaginationUtil.PaginatedResponse<Banco> bancos = bancoService.getAll(pagination, filters);
            return ResponseEntity.ok(bancos);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBancoById(@PathVariable Long id, Authentication authentication) {
        try {
            Banco banco = bancoService.getOne(id);
            return ResponseEntity.ok(banco);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createBanco(@RequestBody Banco banco, Authentication authentication) {
        try {

            Banco novoBanco = bancoService.insertOne(banco);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoBanco);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar banco"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBanco(@PathVariable Long id, @RequestBody Banco bancoAtualizado,
            Authentication authentication) {
        try {
            Banco banco = bancoService.update(id, bancoAtualizado);
            return ResponseEntity.ok(banco);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            logger.error(e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar banco"));
        }
    }

}
