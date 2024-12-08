package com.polarplus.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarplus.domain.Cartao;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.services.CartaoService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cartoes")
@RequiredArgsConstructor

public class CartaoController {
    private final CartaoService cartaoCRService;

    @GetMapping
    public ResponseEntity<?> getCartaoCR(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @ModelAttribute FilterTermoDTO filters) {
        try {
            PaginationUtil.PaginatedResponse<Cartao> cartaoCRs = cartaoCRService
                    .getAll(pagination, filters);
            return ResponseEntity.ok(cartaoCRs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCartaoCRById(@PathVariable Long id, Authentication authentication) {
        try {
            Cartao cartaoCR = cartaoCRService.getOne(id);
            return ResponseEntity.ok(cartaoCR);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createCartaoCR(@RequestBody Cartao cartaoCR,
            Authentication authentication) {
        try {

            Cartao novaCartaoCR = cartaoCRService.insertOne(cartaoCR);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCartaoCR);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar cartão"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCartaoCR(@PathVariable Long id,
            @RequestBody Cartao cartaoCRAtualizado,
            Authentication authentication) {
        try {
            Cartao cartaoCR = cartaoCRService.update(id,
                    cartaoCRAtualizado);
            return ResponseEntity.ok(cartaoCR);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar cartão"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
            Authentication authentication) {
        try {
            cartaoCRService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Success"));
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao remover cartão"));
        }
    }
}
