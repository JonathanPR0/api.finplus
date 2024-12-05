package com.polarplus.dto.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequestDTO(String name, String email, String password,
                @JsonProperty("id_empresa") Long idEmpresa) {
}
