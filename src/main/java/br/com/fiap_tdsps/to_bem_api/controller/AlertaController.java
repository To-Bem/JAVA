package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.ErrorCustomResponse;
import br.com.fiap_tdsps.to_bem_api.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import br.com.fiap_tdsps.to_bem_api.service.Paginacao;

import org.springframework.data.domain.Sort;

import br.com.fiap_tdsps.to_bem_api.dto.AlertaRequest;
import br.com.fiap_tdsps.to_bem_api.dto.AlertaResponse;
import br.com.fiap_tdsps.to_bem_api.dto.PageResponse;
import br.com.fiap_tdsps.to_bem_api.mapper.AlertaMapper;
import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import br.com.fiap_tdsps.to_bem_api.service.AlertaService;
import br.com.fiap_tdsps.to_bem_api.service.AlertaNotificacaoService;
import br.com.fiap_tdsps.to_bem_api.dto.AlertaNotificacaoResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Alertas")
@RestController
@RequestMapping("/api/alertas")
@Validated
public class AlertaController {

    private final AlertaNotificacaoService alertaNotificacaoService;
    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService,
                            AlertaNotificacaoService alertaNotificacaoService){
        this.alertaNotificacaoService = alertaNotificacaoService;
        this.alertaService = alertaService;

    }

    @Operation(summary = "Lista alertas", description = "Exclusivo para ADMIN ou MANAGER.")
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
    @GetMapping
    public PageResponse<AlertaResponse> findAll(@Parameter(description = "Página, iniciando em 0.") @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                @Parameter(description = "Quantidade de registros por página.") @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction){

        Page<Alerta> pageResult = this.alertaService.findAll(Paginacao.criar(page, size, sort, direction, Alerta.class));
        return PageResponse.from(pageResult.map(AlertaMapper::toResponse));
    }

    @Operation(summary = "Filtra alertas", description = "Combina os filtros informados.")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeFiltrar(#idosoId, #usuarioId)")
    @GetMapping("/byOtherFilter")
    public PageResponse<AlertaResponse> findAllByOtherFilter(@Parameter(description = "ID do idoso.") @RequestParam(required = false) @Positive Long idosoId,
                                                             @Parameter(description = "ID do usuário.") @RequestParam(required = false) @Positive Long usuarioId,
                                                             @Parameter(description = "Status para filtrar.") @RequestParam(required = false) AlertaStatus status,
                                                             @Parameter(description = "Nível para filtrar.") @RequestParam(required = false) AlertaNivel nivel,
                                                             @Parameter(description = "Página, iniciando em 0.") @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                             @Parameter(description = "Quantidade de registros por página.") @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction){

        Page<Alerta> pageResult = this.alertaService.findByOtherFilter(idosoId, usuarioId, status, nivel,
                Paginacao.criar(page, size, sort, direction, Alerta.class));
        return PageResponse.from(pageResult.map(AlertaMapper::toResponse));
    }

    @Operation(summary = "Consulta alerta por ID")
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
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarAlerta(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> findById(@Parameter(description = "ID do alerta.") @PathVariable @Positive Long id){
        Alerta alerta = this.alertaService.findById(id);
        return ResponseEntity.ok(AlertaMapper.toResponse(alerta));
    }

    @Operation(summary = "Atualiza alerta")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarAlerta(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponse> update(@Parameter(description = "ID do alerta.") @PathVariable @Positive Long id,
                                                 @Valid @RequestBody AlertaRequest request){
        Alerta alerta = this.alertaService.findById(id);
        alerta.setTipo(request.tipo());
        alerta.setNivel(request.nivel());
        alerta.setStatus(request.status());

        Alerta updated = this.alertaService.update(id, alerta);
        return ResponseEntity.ok(AlertaMapper.toResponse(updated));
    }

    @Operation(summary = "Exclui alerta")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Excluído.", content = @Content),
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "ID do alerta.") @PathVariable @Positive Long id){
        this.alertaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Escalona alerta", description = "Equivale ao escalonamento de notificações. Exige prazo vencido e etapa atual.")
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
    @PostMapping("/{id}/resend")
    public ResponseEntity<AlertaNotificacaoResponse> reSendAlerta(
            @Parameter(description = "ID do alerta.") @PathVariable @Positive Long id, @Parameter(description = "Ordem atual do destinatário. Consulte o valor no alerta.") @RequestParam @Positive Integer etapa) {
        return ResponseEntity.ok(this.alertaNotificacaoService.escalonar(id, etapa));
    }
}
