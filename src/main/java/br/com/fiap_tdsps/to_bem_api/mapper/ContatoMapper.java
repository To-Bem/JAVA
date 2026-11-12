package br.com.fiap_tdsps.to_bem_api.mapper;

import br.com.fiap_tdsps.to_bem_api.dto.ContatoResponse;
import br.com.fiap_tdsps.to_bem_api.model.Contato;

public final class ContatoMapper {

    private ContatoMapper(){}

    public static ContatoResponse toResponse(Contato contato){
        return new ContatoResponse(
                contato.getId(),
                contato.getParentesco(),
                contato.getOrdemEscalonamento(),
                contato.getUsuario().getId(),
                contato.getIdoso().getId()
        );
    }
}
