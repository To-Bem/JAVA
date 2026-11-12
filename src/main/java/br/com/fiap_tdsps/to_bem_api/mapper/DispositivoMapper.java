package br.com.fiap_tdsps.to_bem_api.mapper;

import br.com.fiap_tdsps.to_bem_api.dto.DispositivoResponse;
import br.com.fiap_tdsps.to_bem_api.dto.DispositivoWithEventosResponse;
import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;

public final class DispositivoMapper {

    private DispositivoMapper(){}

    public static DispositivoResponse toResponse(Dispositivo dispositivo){
        return new DispositivoResponse(
                dispositivo.getId(),
                dispositivo.getTipo(),
                dispositivo.getComodo(),
                dispositivo.getStatus(),
                dispositivo.getUltimoHeartbeat(),
                dispositivo.getIdoso().getId()
        );
    }

    public static DispositivoWithEventosResponse toResponseWithEventos(Dispositivo dispositivo){
        return new DispositivoWithEventosResponse(
                dispositivo.getId(),
                dispositivo.getTipo(),
                dispositivo.getComodo(),
                dispositivo.getStatus(),
                dispositivo.getUltimoHeartbeat(),
                dispositivo.getIdoso().getId(),
                dispositivo.getEventos().stream().map(EventoMapper::toResponse).toList()
        );
    }
}
