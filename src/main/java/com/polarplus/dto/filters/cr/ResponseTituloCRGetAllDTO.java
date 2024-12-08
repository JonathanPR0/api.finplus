package com.polarplus.dto.filters.cr;

import java.math.BigDecimal;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseTituloCRGetAllDTO(
        String descricao,
        String categoria,
        String status,
        BigDecimal valor,
        String cor,
        @JsonProperty("data_servico") Date dataServico) {
}
