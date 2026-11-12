package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        UsuarioPapel papel
) {
}
