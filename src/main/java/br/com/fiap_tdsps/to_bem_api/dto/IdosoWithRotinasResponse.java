package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.IdosoModo;

import java.util.List;

public record IdosoWithRotinasResponse(
        Long id,
        String nome,
        String dataNascimento,
        String telefone,
        String endereco,
        IdosoModo modo,
        List<RotinaResponse> rotinas
) {
}
