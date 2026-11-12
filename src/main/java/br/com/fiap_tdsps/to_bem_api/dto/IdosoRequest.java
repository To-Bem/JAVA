package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.IdosoModo;

public record IdosoRequest(
         String nome,
         String dataNascimento,
         String telefone,
         String endereco,
         IdosoModo modo
) {
}
