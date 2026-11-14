package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.IdosoModo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record IdosoRequest(
        @NotNull(message = "nome é obrigatorio")
        @NotBlank(message = "nome não pode ser vazio")
        String nome,
        @NotNull(message = "dataNascimento é obrigatorio")
        LocalDate dataNascimento,
        @NotNull(message = "telefone é obrigatorio")
        @NotBlank(message = "telefone não pode ser vazio")
        String telefone,
        @NotNull(message = "endereco é obrigatorio")
        @NotBlank(message = "endereco não pode ser vazio")
        String endereco,
        @NotNull(message = "modo é obrigatorio")
        IdosoModo modo
) {
}
