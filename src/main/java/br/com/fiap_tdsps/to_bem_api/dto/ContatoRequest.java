package br.com.fiap_tdsps.to_bem_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoRequest(
        @NotNull(message = "parentesco é obrigatorio")
        @NotBlank(message = "parentesco não pode ser vazio")
        String parentesco,
        @NotNull(message = "ordemEscalonamento é obrigatorio")
        Integer ordemEscalonamento
) {
}
