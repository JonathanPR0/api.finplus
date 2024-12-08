package com.polarplus.dto.filters.cp;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VencimentosDTO(
        @JsonProperty("data_vencimento") LocalDate dataVencimento,
        BigDecimal valor, @JsonProperty("valor_pago") BigDecimal valorPago) {
}
