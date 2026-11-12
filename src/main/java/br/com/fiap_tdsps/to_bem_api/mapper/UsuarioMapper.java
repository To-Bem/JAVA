package br.com.fiap_tdsps.to_bem_api.mapper;

import br.com.fiap_tdsps.to_bem_api.dto.UsuarioResponse;
import br.com.fiap_tdsps.to_bem_api.dto.UsuarioWithAlertasResponse;
import br.com.fiap_tdsps.to_bem_api.dto.UsuarioWithAllResponse;
import br.com.fiap_tdsps.to_bem_api.dto.UsuarioWithContatosResponse;
import br.com.fiap_tdsps.to_bem_api.model.Usuario;

public final class UsuarioMapper {

    private UsuarioMapper() {}

    public static UsuarioResponse toResponse(Usuario usuario){
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel()
        );
    }

    public static UsuarioWithAlertasResponse toResponseWithAlertas(Usuario usuario){
        return new UsuarioWithAlertasResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel(),
                usuario.getAlertas().stream().map(AlertaMapper::toResponse).toList()
        );
    }

    public static UsuarioWithAllResponse toResponseWithAll(Usuario usuario){
        return new UsuarioWithAllResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel(),
                usuario.getContatos().stream().map(ContatoMapper::toResponse).toList(),
                usuario.getAlertas().stream().map(AlertaMapper::toResponse).toList()
        );
    }

    public static UsuarioWithContatosResponse toResponseWithContatos(Usuario usuario){
        return new UsuarioWithContatosResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPapel(),
                usuario.getContatos().stream().map(ContatoMapper::toResponse).toList()
        );
    }
}
