package br.com.fiap_tdsps.to_bem_api.dto;

import java.time.LocalTime;

public record RotinaResponse(
        Long id,
        String diaSemana,
        String horaPrimeiraAtividade,
        Integer mediaLevantadasPorNoite,
        Long idosoId
) {
}
