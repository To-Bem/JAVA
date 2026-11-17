package br.com.fiap_tdsps.to_bem_api.dto;


import br.com.fiap_tdsps.to_bem_api.model.Role;

public record AuthResponse(String token, String tokenType, long expiresInSeconds, String email, Role role) {
}
