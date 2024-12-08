package com.polarplus.dto.filters.cr;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PagamentosDTO(
        @JsonProperty("data_vencimento") LocalDate dataVencimento,
        BigDecimal valor, @JsonProperty("valor_pago") BigDecimal valorPago) {
}
