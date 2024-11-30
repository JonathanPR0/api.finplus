package com.polarplus.controllers;

import java.util.Map;

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

import com.polarplus.domain.Categoria;
import com.polarplus.dto.CategoriaDTO;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.FiltersCategoriaDTO;
import com.polarplus.services.CategoriaService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor

public class CategoriaController {
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<?> getCategoria(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @ModelAttribute FiltersCategoriaDTO filters) {
        try {
            PaginationUtil.PaginatedResponse<Categoria> categorias = categoriaService.getAll(pagination, filters);
            return ResponseEntity.ok(categorias);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoriaById(@PathVariable Long id, Authentication authentication) {
        try {
            Categoria categoria = categoriaService.getOne(id);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCategoria(@RequestBody CategoriaDTO categoria,
            Authentication authentication) {
        try {

            Categoria novaCategoria = categoriaService.insertOne(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoria);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar categoria"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoria(@PathVariable Long id,
            @RequestBody CategoriaDTO categoriaAtualizado,
            Authentication authentication) {
        try {
            Categoria categoria = categoriaService.update(id, categoriaAtualizado);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar categoria"));
        }
    }

}
