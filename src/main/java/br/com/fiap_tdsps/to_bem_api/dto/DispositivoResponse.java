package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;

import java.time.LocalDateTime;

public record DispositivoResponse(
        Long id,
        DispositivoTipo tipo,
        String comodo,
        DispositivoStatus status,
        LocalDateTime ultimoHeartbeat,
        Long idosoId
) {
}
