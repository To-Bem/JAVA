package br.com.fiap_tdsps.to_bem_api.dto;

public record RotinaResponse(
        Long id,
        String diaSemana,
        String horaPrimeiraAtividade,
        Integer mediaLevantadasPorNoite,
        Long idosoId
) {
}
