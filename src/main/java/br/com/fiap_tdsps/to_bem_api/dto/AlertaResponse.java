package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;

public record AlertaResponse(
        Long id,
        String tipo,
        AlertaNivel nivel,
        AlertaStatus status,
        Integer nivelEscalonamento,
        String criadoEm,
        Long usuarioId,
        Long idosoId
) {
}
