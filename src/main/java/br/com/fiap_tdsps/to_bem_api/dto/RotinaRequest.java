package br.com.fiap_tdsps.to_bem_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record RotinaRequest(
        @NotBlank(message = "diaSemana não pode ser vazio")
        @NotNull(message = "diaSemana é obrigatorio")
        String diaSemana,
        @NotNull(message = "horaPrimeiraAtividade é obrigatorio")
        LocalTime horaPrimeiraAtividade,
        @NotNull(message = "mediaLevantadasPorNoite é obrigatorio")
        Integer mediaLevantadasPorNoite
) {
}
