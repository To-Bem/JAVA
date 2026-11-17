package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AlertaRequest(
        @NotBlank(message = "O tipo não pode ser vazio")
        @NotNull(message = "o tipo é obrigatorio")
        @Schema(description = "Tipo do alerta.", example = "INATIVIDADE")
        String tipo,
        @NotNull(message = "O nivel é obrigatorio")
        @Schema(description = "Gravidade do alerta.", example = "ALERTA")
        AlertaNivel nivel,
        @NotNull(message = "O status é obrigatio")
        @Schema(description = "Status do alerta.", example = "ABERTO")
        AlertaStatus status,
        @NotNull(message = "O nivel de escalonamento é obrigatorio")
        @Positive(message = "O nivel deve ser positivo")
        @Schema(description = "Obrigatório no contrato; o servidor define e mantém a etapa do destinatário.", example = "1")
        Integer nivelEscalonamento
) {
}
