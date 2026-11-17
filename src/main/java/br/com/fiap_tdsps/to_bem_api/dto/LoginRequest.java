package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email é obrigatório")
        @Schema(description = "Email.", example = "maria@example.com")
        String email,

        @NotBlank(message = "password é obrigatório")
        @Schema(description = "Senha.", example = "Senha12345", accessMode = Schema.AccessMode.WRITE_ONLY)
        String password
) {
}
