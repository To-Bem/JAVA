package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.Role;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import jakarta.validation.constraints.*;

public record RegisterPrivilegiadoRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 72) String password,
        @NotNull UsuarioPapel papel,
        @NotNull Role role
) { }
