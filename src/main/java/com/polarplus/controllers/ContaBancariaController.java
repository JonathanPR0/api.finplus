package com.polarplus.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.polarplus.domain.ContaBancaria;
import com.polarplus.dto.ContaBancariaDTO;
import com.polarplus.services.ContaBancariaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contas-bancarias")
@RequiredArgsConstructor

public class ContaBancariaController {
    private final ContaBancariaService contaBancariaService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<List<ContaBancaria>> getContaBancaria(Authentication authentication) {
        List<ContaBancaria> contaBancarias = contaBancariaService.getAll();
        return ResponseEntity.ok(contaBancarias);

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getContaBancariaById(@PathVariable Long id, Authentication authentication) {
        try {
            ContaBancaria contaBancaria = contaBancariaService.getOne(id);
            return ResponseEntity.ok(contaBancaria);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createContaBancaria(@RequestBody ContaBancariaDTO contaBancaria,
            Authentication authentication) {
        try {

            ContaBancaria novoContaBancaria = contaBancariaService.insertOne(contaBancaria);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoContaBancaria);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar contaBancaria"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateContaBancaria(@PathVariable Long id,
            @RequestBody ContaBancaria contaBancariaAtualizado,
            Authentication authentication) {
        try {
            ContaBancaria contaBancaria = contaBancariaService.update(id, contaBancariaAtualizado);
            return ResponseEntity.ok(contaBancaria);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar contaBancaria"));
        }
    }

}
