package br.com.fiap_tdsps.to_bem_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventoRequest(
        @NotNull(message = "tipo é obrigatorio")
        @NotBlank(message = "tipo não pode ser vazio")
        String tipo,
        @NotNull(message = "valor é obrigatorio")
        Integer valor,
        @NotNull(message = "dataHora é obrigatorio")
        LocalDateTime dataHora
) {
}
