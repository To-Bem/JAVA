package br.com.fiap_tdsps.to_bem_api.dto;

public record ContatoRequest(
        String parentesco,
        Integer ordemEscalonamento
) {
}
