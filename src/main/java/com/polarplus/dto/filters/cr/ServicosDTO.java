package com.polarplus.dto.filters.cr;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ServicosDTO(@JsonProperty("id_categoria") Long idCategoria,
        @JsonProperty("data_servico") LocalDate dataServico,
        @JsonProperty("data_servico_futuro") LocalDate dataServicoFuturo,
        @JsonProperty("data_expiracao_garantia") LocalDate dataExpiracaoGarantia,
        String descricao) {
}
