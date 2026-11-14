package br.com.fiap_tdsps.to_bem_api.dto;

import java.util.List;

public record EventoWithAlertasResponse(
        Long id,
        String tipo,
        Integer valor,
        String dataHora,
        Long dispositivoId,
        List<AlertaResponse> alertas
) {
}
