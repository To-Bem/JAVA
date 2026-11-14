package br.com.fiap_tdsps.to_bem_api.mapper;

import br.com.fiap_tdsps.to_bem_api.dto.EventoResponse;
import br.com.fiap_tdsps.to_bem_api.model.Evento;

public final class EventoMapper {

    private EventoMapper(){}

    public static EventoResponse toResponse(Evento evento){
        return new EventoResponse(
                evento.getId(),
                evento.getTipo(),
                evento.getValor(),
                evento.getDataHora(),
                evento.getDispositivo().getId()
        );
    }
}
