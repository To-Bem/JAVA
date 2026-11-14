package br.com.fiap_tdsps.to_bem_api.dto;

import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DispositivoRequest(
        @NotNull(message = "tipo é obrigatorio")
        DispositivoTipo tipo,
        @NotNull(message = "comodo é obrigatorio")
        @NotBlank(message = "comodo não pode ser vazio")
        String comodo,
        @NotNull(message = "status é obrigatorio")
        DispositivoStatus status,
        LocalDateTime ultimoHeartbeat
) {
}
