package br.com.fiap_tdsps.to_bem_api.exceptions;

public class RoleNotPermitted extends RuntimeException {
    public RoleNotPermitted(String resource, String role) {

        super("A role " + role + " não pode ser colocada em " + resource);
    }
}
