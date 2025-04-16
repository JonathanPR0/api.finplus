package com.polarplus.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContaBancariaDTO(
                @JsonProperty("id_banco") Long idBanco,
                String descricao) {
}
