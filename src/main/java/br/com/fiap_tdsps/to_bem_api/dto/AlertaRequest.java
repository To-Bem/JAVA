package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;

import java.time.LocalDateTime;

public record AlertaRequest(
        String tipo,
        AlertaNivel nivel,
        AlertaStatus status,
        Integer nivelEscalonamento
) {
}
