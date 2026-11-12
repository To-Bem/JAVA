package br.com.fiap_tdsps.to_bem_api.dto;

import java.time.LocalTime;

public record RotinaRequest(
         String diaSemana,
         LocalTime horaPrimeiraAtividade,
         Integer mediaLevantadasPorNoite
) {
}
