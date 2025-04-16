package com.polarplus.dto.filters.cp;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseTituloCPGetAllDTO(
        Long id,
        String descricao,
        String categoria,
        String status,
        BigDecimal valor,
        String cor,
        @JsonProperty("data_titulo") Timestamp dataTitulo) {
}
