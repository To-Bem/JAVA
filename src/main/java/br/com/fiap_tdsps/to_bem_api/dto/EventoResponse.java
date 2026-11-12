package br.com.fiap_tdsps.to_bem_api.dto;

import java.time.LocalDateTime;

public record EventoResponse(
        Long id,
        String tipo,
        Integer valor,
        LocalDateTime dataHora,
        Long dispositivoId
) {
}
