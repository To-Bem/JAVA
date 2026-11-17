package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventoRequest(
        @NotNull(message = "tipo é obrigatorio")
        @NotBlank(message = "tipo não pode ser vazio")
        @Schema(description = "Tipo do evento.", example = "MOVIMENTO")
        String tipo,
        @NotNull(message = "valor é obrigatorio")
        @Schema(description = "Valor medido.", example = "1")
        Integer valor,
        @NotNull(message = "dataHora é obrigatorio")
        @Schema(description = "Data e hora do evento.", example = "2026-11-17T10:00:00")
        LocalDateTime dataHora
) {
}
