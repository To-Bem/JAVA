package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import br.com.fiap_tdsps.to_bem_api.model.Role;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import jakarta.validation.constraints.*;

public record RegisterPrivilegiadoRequest(
        @Schema(description = "Nome.", example = "Maria Silva")
        @NotBlank @Size(max = 100) String name,
        @Schema(description = "Email.", example = "maria@example.com")
        @NotBlank @Email String email,
        @Schema(description = "Senha.", example = "Senha12345", accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank @Size(min = 8, max = 72) String password,
        @Schema(description = "Vínculo do usuário.", example = "FAMILIAR")
        @NotNull UsuarioPapel papel,
        @Schema(description = "Permissão do usuário.", example = "MANAGER", allowableValues = {"ADMIN", "MANAGER"})
        @NotNull Role role
) { }
