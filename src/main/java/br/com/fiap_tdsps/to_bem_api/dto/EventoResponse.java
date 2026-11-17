package br.com.fiap_tdsps.to_bem_api.dto;

public record EventoResponse(
        Long id,
        String tipo,
        Integer valor,
        String dataHora,
        Long dispositivoId
) {
}
