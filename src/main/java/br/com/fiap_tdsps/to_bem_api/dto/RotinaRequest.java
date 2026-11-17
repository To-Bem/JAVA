package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalTime;

public record RotinaRequest(
        @NotBlank(message = "diaSemana não pode ser vazio")
        @NotNull(message = "diaSemana é obrigatorio")
        @Schema(description = "Dia da semana.", example = "SEGUNDA")
        String diaSemana,
        @NotNull(message = "horaPrimeiraAtividade é obrigatorio")
        @Schema(description = "Horário da primeira atividade.", example = "08:00:00")
        LocalTime horaPrimeiraAtividade,
        @NotNull(message = "mediaLevantadasPorNoite é obrigatorio")
        @PositiveOrZero(message = "A media nao pode ser negativa")
        @Schema(description = "Média de levantadas por noite.", example = "2")
        Integer mediaLevantadasPorNoite
) {
}
