package br.com.fiap_tdsps.to_bem_api.security;

import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Usuario currentUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Usuario usuario)) {
            throw new IllegalStateException("Nenhum colaborador autenticado no contexto de segurança");
        }
        return usuario;
    }
}
