package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.IdosoModo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record IdosoRequest(
        @NotNull(message = "nome é obrigatorio")
        @NotBlank(message = "nome não pode ser vazio")
        String nome,
        @NotNull(message = "dataNascimento é obrigatorio")
        @Past(message = "A data de nascimento deve estar no passado")
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
