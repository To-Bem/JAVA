package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.IdosoModo;

import java.time.LocalDate;
import java.util.List;

public record IdosoWithContatosResponse(
        Long id,
        String nome,
        String dataNascimento,
        String telefone,
        String endereco,
        IdosoModo modo,
        List<ContatoResponse> contatos
) {
}
