package com.polarplus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContaBancariaDTO(
        @JsonProperty("id_banco") Long idBanco, String agencia, @JsonProperty("dv_agencia") String dvAgencia,
        String conta, @JsonProperty("dv_conta") String dvConta,
        String descricao) {
}
