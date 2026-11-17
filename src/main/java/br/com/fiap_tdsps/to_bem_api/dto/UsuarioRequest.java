package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioRequest(
        @NotNull(message = "nome é obrigatorio")
        @NotBlank(message = "nome não pode ser vazio")
        String nome,
        @NotNull(message = "email é obrigatorio")
        @NotBlank(message = "email não pode ser vazio")
        @Email(message = "Email invalido")
        String email,
        @NotNull(message = "senha é obrigatorio")
        @NotBlank(message = "senha não pode ser vazio")
        String senha,
        @NotNull(message = "papel é obrigatorio")
        UsuarioPapel papel
) {
}
