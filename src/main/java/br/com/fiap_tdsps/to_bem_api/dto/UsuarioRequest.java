package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;

public record UsuarioRequest(
         String nome,
         String email,
         String senha,
         UsuarioPapel papel
) {
}
