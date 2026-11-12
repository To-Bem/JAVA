package br.com.fiap_tdsps.to_bem_api.dto;

import java.time.LocalDateTime;

public record EventoRequest(
        String tipo,
        Integer valor,
        LocalDateTime dataHora
) {
}
