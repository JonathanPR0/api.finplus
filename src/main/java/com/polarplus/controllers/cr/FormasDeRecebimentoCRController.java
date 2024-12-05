package com.polarplus.controllers.cr;

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

import com.polarplus.domain.cr.FormaDeRecebimentoCR;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.services.cr.FormaDeRecebimentoCRService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/formas-recebimento")
@RequiredArgsConstructor

public class FormasDeRecebimentoCRController {
    private final FormaDeRecebimentoCRService formaDeRecebimentoCRService;

    @GetMapping
    public ResponseEntity<?> getFormaDeRecebimentoCR(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @ModelAttribute FilterTermoDTO filters) {
        try {
            PaginationUtil.PaginatedResponse<FormaDeRecebimentoCR> formaDeRecebimentoCRs = formaDeRecebimentoCRService
                    .getAll(pagination, filters);
            return ResponseEntity.ok(formaDeRecebimentoCRs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFormaDeRecebimentoCRById(@PathVariable Long id, Authentication authentication) {
        try {
            FormaDeRecebimentoCR formaDeRecebimentoCR = formaDeRecebimentoCRService.getOne(id);
            return ResponseEntity.ok(formaDeRecebimentoCR);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createFormaDeRecebimentoCR(@RequestBody FormaDeRecebimentoCR formaDeRecebimentoCR,
            Authentication authentication) {
        try {

            FormaDeRecebimentoCR novaFormaDeRecebimentoCR = formaDeRecebimentoCRService.insertOne(formaDeRecebimentoCR);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaFormaDeRecebimentoCR);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar formaDeRecebimentoCR"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFormaDeRecebimentoCR(@PathVariable Long id,
            @RequestBody FormaDeRecebimentoCR formaDeRecebimentoCRAtualizado,
            Authentication authentication) {
        try {
            FormaDeRecebimentoCR formaDeRecebimentoCR = formaDeRecebimentoCRService.update(id,
                    formaDeRecebimentoCRAtualizado);
            return ResponseEntity.ok(formaDeRecebimentoCR);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar formaDeRecebimentoCR"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
            Authentication authentication) {
        try {
            formaDeRecebimentoCRService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Success"));
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar formaDeRecebimentoCR"));
        }
    }
}
