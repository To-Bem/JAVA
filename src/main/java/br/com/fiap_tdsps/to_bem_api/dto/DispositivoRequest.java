package br.com.fiap_tdsps.to_bem_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DispositivoRequest(
        @NotNull(message = "tipo é obrigatorio")
        @Schema(description = "Tipo do dispositivo.", example = "BASE")
        DispositivoTipo tipo,
        @NotNull(message = "comodo é obrigatorio")
        @NotBlank(message = "comodo não pode ser vazio")
        @Schema(description = "Cômodo do dispositivo.", example = "Quarto")
        String comodo,
        @NotNull(message = "status é obrigatorio")
        @Schema(description = "Status do dispositivo.", example = "ONLINE")
        DispositivoStatus status
) {
}
