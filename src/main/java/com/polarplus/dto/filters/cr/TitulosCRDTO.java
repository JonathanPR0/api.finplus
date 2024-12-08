package com.polarplus.dto.filters.cr;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TitulosCRDTO(@JsonProperty("id_status") Long idStatus, @JsonProperty("id_cliente") Long idCliente,
        @JsonProperty("id_forma_recebimento") Long idFormaRecebimento,
        @JsonProperty("id_conta_bancaria") Long idContaBancaria, BigDecimal valor,
        @JsonProperty("data_pagamento") LocalDate dataPagamento,
        String descricao, ServicosDTO servico, List<PagamentosDTO> pagamentos) {
}
