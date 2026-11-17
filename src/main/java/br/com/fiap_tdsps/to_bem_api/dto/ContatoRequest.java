package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ContatoRequest(
        @NotNull(message = "parentesco é obrigatorio")
        @NotBlank(message = "parentesco não pode ser vazio")
        @Schema(description = "Vínculo com o idoso.", example = "Filho")
        String parentesco,
        @NotNull(message = "ordemEscalonamento é obrigatorio")
        @Positive(message = "A ordem deve ser positiva")
        @Schema(description = "Ordem de contato por idoso.", example = "1")
        Integer ordemEscalonamento
) {
}
