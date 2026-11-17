package br.com.fiap_tdsps.to_bem_api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email é obrigatório")
        String email,

        @NotBlank(message = "password é obrigatório")
        String password
) {
}
