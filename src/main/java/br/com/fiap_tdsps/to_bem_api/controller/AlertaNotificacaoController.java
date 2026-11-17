package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.service.AlertaNotificacaoService;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alertas/notificacoes")
@Validated
public class AlertaNotificacaoController {

    private final AlertaNotificacaoService alertaNotificacaoService;
    public AlertaNotificacaoController(AlertaNotificacaoService alertaNotificacaoService) { this.alertaNotificacaoService = alertaNotificacaoService; }

    @GetMapping("/pendentes")
    public PageResponse<AlertaNotificacaoResponse> findPendentesEnvio(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return PageResponse.from(this.alertaNotificacaoService.findPendentesEnvio(page, size));
    }

    @GetMapping("/vencidos")
    public PageResponse<AlertaNotificacaoResponse> findVencidos(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return PageResponse.from(this.alertaNotificacaoService.findVencidos(page, size));
    }

    @PostMapping("/{id}/confirmar-envio")
    public AlertaNotificacaoResponse confirmarEnvio(@PathVariable @Positive Long id,
                                                    @RequestParam @Positive Integer etapa) {
        return this.alertaNotificacaoService.confirmarEnvio(id, etapa);
    }

    @PostMapping("/{id}/escalonar")
    public AlertaNotificacaoResponse escalonar(@PathVariable @Positive Long id,
                                               @RequestParam @Positive Integer etapa) {
        return this.alertaNotificacaoService.escalonar(id, etapa);
    }
}
