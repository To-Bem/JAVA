package br.com.fiap_tdsps.to_bem_api.dto;

public record ContatoResponse(
        Long id,
        String parentesco,
        Integer ordemEscalonamento,
        Long usuarioId,
        Long idosoId) {
}