package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AlertaRequest(
        @NotBlank(message = "O tipo não pode ser vazio")
        @NotNull(message = "o tipo é obrigatorio")
        String tipo,
        @NotNull(message = "O nivel é obrigatorio")
        AlertaNivel nivel,
        @NotNull(message = "O status é obrigatio")
        AlertaStatus status,
        @NotNull(message = "O nivel de escalonamento é obrigatorio")
        Integer nivelEscalonamento
) {
}
