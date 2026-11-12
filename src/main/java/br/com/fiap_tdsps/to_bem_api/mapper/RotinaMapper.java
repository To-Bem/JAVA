package br.com.fiap_tdsps.to_bem_api.mapper;

import br.com.fiap_tdsps.to_bem_api.dto.RotinaResponse;
import br.com.fiap_tdsps.to_bem_api.model.Rotina;

public final class RotinaMapper {

    private RotinaMapper(){}

    public static RotinaResponse toResponse(Rotina rotina){
        return new RotinaResponse(
                rotina.getId(),
                rotina.getDiaSemana(),
                rotina.getHoraPrimeiraAtividade(),
                rotina.getMediaLevantadasPorNoite(),
                rotina.getIdoso().getId()
        );
    }
}
