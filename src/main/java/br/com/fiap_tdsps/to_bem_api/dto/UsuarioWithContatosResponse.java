package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;

import java.util.List;

public record UsuarioWithContatosResponse(
        Long id,
        String nome,
        String email,
        UsuarioPapel papel,
        List<ContatoResponse> contatos
) {
}
