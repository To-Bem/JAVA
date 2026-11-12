package br.com.fiap_tdsps.to_bem_api.mapper;

import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.model.Idoso;

public final class IdosoMapper {

    private IdosoMapper(){}

    public static IdosoResponse toResponse(Idoso idoso){
        return new IdosoResponse(
                idoso.getId(),
                idoso.getNome(),
                idoso.getDataNascimento(),
                idoso.getTelefone(),
                idoso.getEndereco(),
                idoso.getModo()
        );
    }

    public static IdosoWithAlertasResponse toResponseWithAlertas(Idoso idoso){
        return new IdosoWithAlertasResponse(
                idoso.getId(),
                idoso.getNome(),
                idoso.getDataNascimento(),
                idoso.getTelefone(),
                idoso.getEndereco(),
                idoso.getModo(),
                idoso.getAlertas().stream().map(AlertaMapper::toResponse).toList()
        );
    }

    public static IdosoWithAllResponse toResponseWithAll(Idoso idoso){
        return new IdosoWithAllResponse(
                idoso.getId(),
                idoso.getNome(),
                idoso.getDataNascimento(),
                idoso.getTelefone(),
                idoso.getEndereco(),
                idoso.getModo(),
                idoso.getContatos().stream().map(ContatoMapper::toResponse).toList(),
                idoso.getAlertas().stream().map(AlertaMapper::toResponse).toList(),
                idoso.getRotinas().stream().map(RotinaMapper::toResponse).toList(),
                idoso.getDispositivos().stream().map(DispositivoMapper::toResponse).toList()
        );
    }

    public static IdosoWithContatosResponse toResponseWithContatos(Idoso idoso){
        return new IdosoWithContatosResponse(
                idoso.getId(),
                idoso.getNome(),
                idoso.getDataNascimento(),
                idoso.getTelefone(),
                idoso.getEndereco(),
                idoso.getModo(),
                idoso.getContatos().stream().map(ContatoMapper::toResponse).toList()
        );
    }

    public static IdosoWithDispositivosResponse toResponseWithDispositivos(Idoso idoso){
        return new IdosoWithDispositivosResponse(
                idoso.getId(),
                idoso.getNome(),
                idoso.getDataNascimento(),
                idoso.getTelefone(),
                idoso.getEndereco(),
                idoso.getModo(),
                idoso.getDispositivos().stream().map(DispositivoMapper::toResponse).toList()
        );
    }

    public static IdosoWithRotinasResponse toResponseWithRotinas(Idoso idoso){
        return new IdosoWithRotinasResponse(
                idoso.getId(),
                idoso.getNome(),
                idoso.getDataNascimento(),
                idoso.getTelefone(),
                idoso.getEndereco(),
                idoso.getModo(),
                idoso.getRotinas().stream().map(RotinaMapper::toResponse).toList()
        );
    }
}
