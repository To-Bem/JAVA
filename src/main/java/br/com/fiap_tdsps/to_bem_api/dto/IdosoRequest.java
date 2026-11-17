package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import br.com.fiap_tdsps.to_bem_api.model.IdosoModo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record IdosoRequest(
        @NotNull(message = "nome é obrigatorio")
        @NotBlank(message = "nome não pode ser vazio")
        @Schema(description = "Nome.", example = "Maria Silva")
        String nome,
        @NotNull(message = "dataNascimento é obrigatorio")
        @Past(message = "A data de nascimento deve estar no passado")
        @Schema(description = "Data de nascimento.", example = "1950-01-01")
        LocalDate dataNascimento,
        @NotNull(message = "telefone é obrigatorio")
        @NotBlank(message = "telefone não pode ser vazio")
        @Schema(description = "Telefone com DDD.", example = "11999999999")
        String telefone,
        @NotNull(message = "endereco é obrigatorio")
        @NotBlank(message = "endereco não pode ser vazio")
        @Schema(description = "Endereço.", example = "Rua Exemplo, 100")
        String endereco,
        @NotNull(message = "modo é obrigatorio")
        @Schema(description = "Modo de monitoramento.", example = "NORMAL")
        IdosoModo modo
) {
}
