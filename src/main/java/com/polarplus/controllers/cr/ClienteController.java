package com.polarplus.controllers.cr;

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

import com.polarplus.domain.cr.Cliente;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.cr.FiltersClientesDTO;
import com.polarplus.services.cr.ClienteService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor

public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<?> getCliente(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @ModelAttribute FiltersClientesDTO filters) {
        try {
            PaginationUtil.PaginatedResponse<Cliente> clientes = clienteService.getAll(pagination, filters);
            return ResponseEntity.ok(clientes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id, Authentication authentication) {
        try {
            Cliente cliente = clienteService.getOne(id);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<?> createCliente(@RequestBody Cliente cliente,
            Authentication authentication) {
        try {

            Cliente novaCliente = clienteService.insertOne(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaCliente);
        } catch (IllegalArgumentException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            System.err.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao criar cliente"));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id,
            @RequestBody Cliente clienteAtualizado,
            Authentication authentication) {
        try {
            Cliente cliente = clienteService.update(id, clienteAtualizado);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // Tratamento genérico para outros erros
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Erro ao atualizar cliente"));
        }
    }

}
