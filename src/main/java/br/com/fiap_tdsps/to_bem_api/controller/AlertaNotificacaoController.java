package br.com.fiap_tdsps.to_bem_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import br.com.fiap_tdsps.to_bem_api.dto.ErrorCustomResponse;
import br.com.fiap_tdsps.to_bem_api.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.service.AlertaNotificacaoService;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Notificações")
@RestController
@RequestMapping("/api/alertas/notificacoes")
@Validated
public class AlertaNotificacaoController {

    private final AlertaNotificacaoService alertaNotificacaoService;
    public AlertaNotificacaoController(AlertaNotificacaoService alertaNotificacaoService) { this.alertaNotificacaoService = alertaNotificacaoService; }

    @Operation(summary = "Lista notificações pendentes", description = "Retorna alertas não resolvidos que aguardam envio.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/pendentes")
    public PageResponse<AlertaNotificacaoResponse> findPendentesEnvio(@Parameter(description = "Página, iniciando em 0.") @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                      @Parameter(description = "Quantidade de registros por página.") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return PageResponse.from(this.alertaNotificacaoService.findPendentesEnvio(page, size));
    }

    @Operation(summary = "Lista alertas com prazo vencido", description = "Retorna alertas não resolvidos cujo prazo expirou.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/vencidos")
    public PageResponse<AlertaNotificacaoResponse> findVencidos(@Parameter(description = "Página, iniciando em 0.") @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                                @Parameter(description = "Quantidade de registros por página.") @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return PageResponse.from(this.alertaNotificacaoService.findVencidos(page, size));
    }

    @Operation(summary = "Confirma envio da notificação", description = "Inicia o prazo de resposta. Repetir a confirmação não renova o prazo.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "409", description = "Conflito com os dados ou com o estado atual.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("/{id}/confirmar-envio")
    public AlertaNotificacaoResponse confirmarEnvio(@Parameter(description = "ID do notificação.") @PathVariable @Positive Long id,
                                                    @Parameter(description = "Ordem atual do destinatário. Consulte o valor no alerta.") @RequestParam @Positive Integer etapa) {
        return this.alertaNotificacaoService.confirmarEnvio(id, etapa);
    }

    @Operation(summary = "Escalona alerta", description = "Após o prazo, seleciona o próximo contato. Sem próximo contato, encerra o escalonamento.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "409", description = "Conflito com os dados ou com o estado atual.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("/{id}/escalonar")
    public AlertaNotificacaoResponse escalonar(@Parameter(description = "ID do notificação.") @PathVariable @Positive Long id,
                                               @Parameter(description = "Ordem atual do destinatário. Consulte o valor no alerta.") @RequestParam @Positive Integer etapa) {
        return this.alertaNotificacaoService.escalonar(id, etapa);
    }
}
