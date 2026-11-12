package br.com.fiap_tdsps.to_bem_api.mapper;

import br.com.fiap_tdsps.to_bem_api.dto.AlertaResponse;
import br.com.fiap_tdsps.to_bem_api.model.Alerta;

public final class AlertaMapper {

    private AlertaMapper(){}

    public static AlertaResponse toResponse(Alerta alerta){
        return new AlertaResponse(
                alerta.getId(),
                alerta.getTipo(),
                alerta.getNivel(),
                alerta.getStatus(),
                alerta.getNivelEscalonamento(),
                alerta.getCriadoEm(),
                alerta.getUsuario().getId(),
                alerta.getIdoso().getId()
        );
    }
}
