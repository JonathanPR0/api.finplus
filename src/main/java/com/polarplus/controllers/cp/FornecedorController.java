package com.polarplus.controllers.cp;

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

import com.polarplus.domain.cp.Fornecedor;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FilterTermoDTO;
import com.polarplus.services.cp.FornecedorService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contas-pagar/fornecedores")
@RequiredArgsConstructor

public class FornecedorController {
    private final FornecedorService fornecedorService;

    @GetMapping
    public ResponseEntity<?> getFornecedor(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @ModelAttribute FilterTermoDTO filters) {
        try {
            PaginationUtil.PaginatedResponse<Fornecedor> fornecedores = fornecedorService.getAll(pagination, filters);
            return ResponseEntity.ok(fornecedores);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFornecedorById(@PathVariable Long id, Authentication authentication) {
        try {
            Fornecedor fornecedor = fornecedorService.getOne(id);
            return ResponseEntity.ok(fornecedor);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createFornecedor(@RequestBody Fornecedor fornecedor,
            Authentication authentication) {
        try {

            Fornecedor novaFornecedor = fornecedorService.insertOne(fornecedor);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaFornecedor);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar fornecedor"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFornecedor(@PathVariable Long id,
            @RequestBody Fornecedor fornecedorAtualizado,
            Authentication authentication) {
        try {
            Fornecedor fornecedor = fornecedorService.update(id, fornecedorAtualizado);
            return ResponseEntity.ok(fornecedor);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar fornecedor"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
            Authentication authentication) {
        try {
            fornecedorService.delete(id);
            return ResponseEntity.ok(Map.of("message", "Success"));
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar fornecedor"));
        }
    }
}
