package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.IdosoModo;

import java.time.LocalDate;

public record IdosoRequest(
         String nome,
         LocalDate dataNascimento,
         String telefone,
         String endereco,
         IdosoModo modo
) {
}
