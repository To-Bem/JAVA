package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
        @NotNull(message = "nome é obrigatorio")
        @NotBlank(message = "nome não pode ser vazio")
        @Schema(description = "Nome.", example = "Maria Silva")
        String nome,
        @NotNull(message = "email é obrigatorio")
        @NotBlank(message = "email não pode ser vazio")
        @Email(message = "Email invalido")
        @Schema(description = "Email.", example = "maria@example.com")
        String email,
        @NotNull(message = "senha é obrigatorio")
        @NotBlank(message = "senha não pode ser vazio")
        @Schema(description = "Senha.", example = "Senha12345", accessMode = Schema.AccessMode.WRITE_ONLY)
        String senha,
        @NotNull(message = "papel é obrigatorio")
        @Schema(description = "Vínculo do usuário.", example = "FAMILIAR")
        UsuarioPapel papel
) {
}
