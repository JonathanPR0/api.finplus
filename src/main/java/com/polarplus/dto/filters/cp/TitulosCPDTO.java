package com.polarplus.dto.filters.cp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TitulosCPDTO(@JsonProperty("id_status") Long idStatus,
                @JsonProperty("id_fornecedor") Long idFornecedor,
                @JsonProperty("id_cartao") Long idCartao,
                @JsonProperty("id_categoria") Long idCategoria,
                @JsonProperty("id_forma_pagamento") Long idFormaPagamento,
                @JsonProperty("id_conta_bancaria") Long idContaBancaria, BigDecimal valor,
                @JsonProperty("data_pagamento") LocalDate dataPagamento,
                String descricao, List<VencimentosDTO> vencimentos) {
}
