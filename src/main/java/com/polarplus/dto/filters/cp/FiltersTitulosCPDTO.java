package com.polarplus.dto.filters.cp;

import org.springframework.web.bind.annotation.RequestParam;

public record FiltersTitulosCPDTO(
                @RequestParam(name = "id_status") Long idStatus,
                @RequestParam(name = "id_fornecedor") Long idFornecedor,
                @RequestParam(name = "id_cartao") Long idCartao,
                @RequestParam(name = "id_forma_pagamento") Long idFormaPagamento,
                @RequestParam(name = "id_conta_bancaria") Long idContaBancaria,
                @RequestParam(name = "id_categoria") Long idCategoria,
                String descricao) {
}
