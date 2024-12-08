package com.polarplus.controllers.cp;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.polarplus.domain.cp.TituloCP;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.cp.FiltersTitulosCPDTO;
import com.polarplus.dto.filters.cp.ResponseTituloCPGetAllDTO;
import com.polarplus.dto.filters.cp.TitulosCPDTO;
import com.polarplus.services.cp.TituloCPService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contas-pagar/titulos")
@RequiredArgsConstructor

public class TituloCPController {
    private final TituloCPService tituloCPService;

    @GetMapping
    public ResponseEntity<?> getTituloCP(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @RequestParam(name = "id_status", required = false) Long idStatus,
            @RequestParam(name = "id_fornecedor", required = false) Long idFornecedor,
            @RequestParam(name = "id_cartao", required = false) Long idCartao,
            @RequestParam(name = "id_forma_pagamento", required = false) Long idFormaPagamento,
            @RequestParam(name = "id_conta_bancaria", required = false) Long idContaBancaria,
            @RequestParam(name = "id_categoria", required = false) Long idCategoria,
            @RequestParam(required = false) String descricao) {

        FiltersTitulosCPDTO filters = new FiltersTitulosCPDTO(
                idStatus, idFornecedor, idCartao, idFormaPagamento, idContaBancaria, idCategoria,
                descricao);

        try {
            PaginationUtil.PaginatedResponse<ResponseTituloCPGetAllDTO> tituloCPs = tituloCPService
                    .getAll(pagination, filters);
            return ResponseEntity.ok(tituloCPs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTituloCPById(@PathVariable Long id,
            Authentication authentication) {
        try {
            TituloCP tituloCP = tituloCPService.getOne(id);
            return ResponseEntity.ok(tituloCP);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createTituloCP(@RequestBody TitulosCPDTO tituloCP,
            Authentication authentication) {
        try {

            TituloCP novaTituloCP = tituloCPService.insertOne(tituloCP);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTituloCP);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateTituloCP(@PathVariable Long id,
    // @RequestBody TituloCP tituloCPAtualizado,
    // Authentication authentication) {
    // try {
    // TituloCP tituloCP = tituloCPService.update(id,
    // tituloCPAtualizado);
    // return ResponseEntity.ok(tituloCP);
    // } catch (RuntimeException e) {
    // // Retorna um body com a mensagem de erro
    // return ResponseEntity.badRequest().body(Map.of("message",
    // e.getMessage()));
    // } catch (Exception e) {
    // // Tratamento genérico para outros erros
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(Map.of("message", "Erro ao atualizar tituloCP"));
    // }
    // }

}
