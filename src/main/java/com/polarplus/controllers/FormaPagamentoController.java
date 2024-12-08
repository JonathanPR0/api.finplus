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

import com.polarplus.domain.FormaPagamento;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.services.FormaPagamentoService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/formas-pagamento")
@RequiredArgsConstructor

public class FormaPagamentoController {
    private final FormaPagamentoService FormaPagamentoCRService;

    @GetMapping
    public ResponseEntity<?> getFormaPagamentoCR(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @ModelAttribute FilterTermoDTO filters) {
        try {
            PaginationUtil.PaginatedResponse<FormaPagamento> FormaPagamentoCRs = FormaPagamentoCRService
                    .getAll(pagination, filters);
            return ResponseEntity.ok(FormaPagamentoCRs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFormaPagamentoCRById(@PathVariable Long id, Authentication authentication) {
        try {
            FormaPagamento FormaPagamentoCR = FormaPagamentoCRService.getOne(id);
            return ResponseEntity.ok(FormaPagamentoCR);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createFormaPagamentoCR(@RequestBody FormaPagamento FormaPagamentoCR,
            Authentication authentication) {
        try {

            FormaPagamento novaFormaPagamentoCR = FormaPagamentoCRService.insertOne(FormaPagamentoCR);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaFormaPagamentoCR);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar forma de pagamento"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFormaPagamentoCR(@PathVariable Long id,
            @RequestBody FormaPagamento FormaPagamentoCRAtualizado,
            Authentication authentication) {
        try {
            FormaPagamento FormaPagamentoCR = FormaPagamentoCRService.update(id,
                    FormaPagamentoCRAtualizado);
            return ResponseEntity.ok(FormaPagamentoCR);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar forma de pagamento"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
            Authentication authentication) {
        try {
            FormaPagamentoCRService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Success"));
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao remover forma de pagamento"));
        }
    }
}
