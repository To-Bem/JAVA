package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;

import java.time.LocalDateTime;
import java.util.List;

public record DispositivoWithEventosResponse(
        Long id,
        DispositivoTipo tipo,
        String comodo,
        DispositivoStatus status,
        String ultimoHeartbeat,
        Long idosoId,
        List<EventoResponse> eventos
) {
}
