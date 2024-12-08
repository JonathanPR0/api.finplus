package com.polarplus.controllers.cr;

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

import com.polarplus.domain.cr.TituloCR;
import com.polarplus.dto.PaginationDTO;
import com.polarplus.dto.filters.cr.FiltersTitulosCRDTO;
import com.polarplus.dto.filters.cr.ResponseTituloCRGetAllDTO;
import com.polarplus.dto.filters.cr.TitulosCRDTO;
import com.polarplus.services.cr.TituloCRService;
import com.polarplus.utils.PaginationUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contas-receber/titulos")
@RequiredArgsConstructor

public class TituloCRController {
    private final TituloCRService tituloCRService;

    @GetMapping
    public ResponseEntity<?> getTituloCR(Authentication authentication,
            @ModelAttribute PaginationDTO pagination,
            @RequestParam(name = "id_status", required = false) Long idStatus,
            @RequestParam(name = "id_cliente", required = false) Long idCliente,
            @RequestParam(name = "id_forma_recebimento", required = false) Long idFormaRecebimento,
            @RequestParam(name = "id_conta_bancaria", required = false) Long idContaBancaria,
            @RequestParam(name = "id_categoria", required = false) Long idCategoria,
            @RequestParam(required = false) String descricao) {

        FiltersTitulosCRDTO filters = new FiltersTitulosCRDTO(
                idStatus, idCliente, idFormaRecebimento, idContaBancaria, idCategoria, descricao);

        try {
            PaginationUtil.PaginatedResponse<ResponseTituloCRGetAllDTO> tituloCRs = tituloCRService
                    .getAll(pagination, filters);
            return ResponseEntity.ok(tituloCRs);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTituloCRById(@PathVariable Long id,
            Authentication authentication) {
        try {
            TituloCR tituloCR = tituloCRService.getOne(id);
            return ResponseEntity.ok(tituloCR);
        } catch (RuntimeException e) {
            // Retorna um body com a mensagem de erro
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createTituloCR(@RequestBody TitulosCRDTO tituloCR,
            Authentication authentication) {
        try {

            TituloCR novaTituloCR = tituloCRService.insertOne(tituloCR);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaTituloCR);
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
    // public ResponseEntity<?> updateTituloCR(@PathVariable Long id,
    // @RequestBody TituloCR tituloCRAtualizado,
    // Authentication authentication) {
    // try {
    // TituloCR tituloCR = tituloCRService.update(id,
    // tituloCRAtualizado);
    // return ResponseEntity.ok(tituloCR);
    // } catch (RuntimeException e) {
    // // Retorna um body com a mensagem de erro
    // return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    // } catch (Exception e) {
    // // Tratamento genérico para outros erros
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(Map.of("message", "Erro ao atualizar tituloCR"));
    // }
    // }

}
