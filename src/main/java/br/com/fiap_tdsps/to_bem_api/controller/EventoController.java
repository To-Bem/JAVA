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
import org.springframework.security.access.prepost.PreAuthorize;

import br.com.fiap_tdsps.to_bem_api.service.Paginacao;

import org.springframework.data.domain.Sort;

import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.mapper.AlertaMapper;
import br.com.fiap_tdsps.to_bem_api.mapper.EventoMapper;
import br.com.fiap_tdsps.to_bem_api.model.*;
import br.com.fiap_tdsps.to_bem_api.service.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@Tag(name = "Eventos")
@RestController
@RequestMapping("/api/eventos")
@Validated
public class EventoController {

    private final EventoService eventoService;
    private final AlertaService alertaService;
    private final IdosoService idosoService;
    private final ContatoService contatoService;
    private final DispositivoService dispositivoService;

    public EventoController(EventoService eventoService, AlertaService alertaService, IdosoService idosoService,
                            ContatoService contatoService, DispositivoService dispositivoService){
        this.eventoService = eventoService;
        this.alertaService = alertaService;
        this.idosoService = idosoService;
        this.contatoService = contatoService;
        this. dispositivoService = dispositivoService;
    }

    @Operation(summary = "Lista eventos", description = "Exclusivo para ADMIN ou MANAGER.")
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
    public PageResponse<EventoResponse> findAll(@Parameter(description = "Página, iniciando em 0.") @RequestParam(required = false,defaultValue = "0") @PositiveOrZero Integer page,
                                                @Parameter(description = "Quantidade de registros por página.") @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        Page<Evento> pageResult = this.eventoService.findAll(Paginacao.criar(page, size, sort, direction, Evento.class));
        return PageResponse.from(pageResult.map(EventoMapper::toResponse));
    }

    @Operation(summary = "Filtra eventos", description = "Combina os filtros informados.")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarDispositivo(#dispositivoId)")
    @GetMapping("/byOtherFilter")
    public PageResponse<EventoResponse> findByOtherFilter(@Parameter(description = "ID do dispositivo.") @RequestParam @Positive Long dispositivoId,
                                                          @Parameter(description = "Tipo para filtrar.") @RequestParam(required = false) String tipo,
                                                          @Parameter(description = "Página, iniciando em 0.") @RequestParam(required = false,defaultValue = "0") @PositiveOrZero Integer page,
                                                          @Parameter(description = "Quantidade de registros por página.") @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        Page<Evento> pageResult = this.eventoService.findByOtherFilter(dispositivoId, tipo,
                Paginacao.criar(page, size, sort, direction, Evento.class));
        return  PageResponse.from(pageResult.map(EventoMapper::toResponse));
    }

    @Operation(summary = "Consulta evento por ID")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarEvento(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> findById(@Parameter(description = "ID do evento.") @PathVariable @Positive Long id){
        Evento evento = this.eventoService.findById(id);

        return ResponseEntity.ok(EventoMapper.toResponse(evento));
    }

    @Operation(summary = "Atualiza evento")
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
    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> update(@Parameter(description = "ID do evento.") @PathVariable @Positive Long id,
                                                 @RequestBody @Valid EventoRequest eventoRequest) {
        Evento evento = this.eventoService.findById(id);
        evento.setValor(eventoRequest.valor());
        evento.setTipo(eventoRequest.tipo());
        evento.setDataHora(eventoRequest.dataHora().format(CommonVariables.dateTimeFormatter));
        Evento updated = this.eventoService.update(id, evento);
        return ResponseEntity.ok(EventoMapper.toResponse(updated));
    }

    @Operation(summary = "Consulta evento com alertas")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarEvento(#id)")
    @GetMapping("/{id}/withAlertas")
    public ResponseEntity<EventoWithAlertasResponse> findWithAlertasById(@Parameter(description = "ID do evento.") @PathVariable @Positive Long id){
        Evento evento = this.eventoService.findWithAlertasById(id);
        return ResponseEntity.ok(EventoMapper.toResponseWithAlertas(evento));
    }

    @Operation(summary = "Exclui evento")
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
    public ResponseEntity<Void> delete(@Parameter(description = "ID do evento.") @PathVariable @Positive Long id){
        this.eventoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cria alerta do evento", description = "Seleciona o primeiro contato do idoso.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Criado.", useReturnTypeSchema = true,
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
    @PostMapping("/{id}/alerta")
    public ResponseEntity<AlertaResponse> saveAlerta(@Parameter(description = "ID do evento.") @PathVariable @Positive Long id,
                                                     @RequestBody @Valid AlertaRequest alertaRequest){
        Alerta alerta = new Alerta();

        alerta.setCriadoEm(LocalDateTime.now().format(CommonVariables.dateTimeFormatter));
        alerta.setStatus(alertaRequest.status());
        alerta.setTipo(alertaRequest.tipo());
        alerta.setNivel(alertaRequest.nivel());
        alerta.setNivelEscalonamento(alertaRequest.nivelEscalonamento());

        Evento evento = this.eventoService.findById(id);
        alerta.setEvento(evento);

        Dispositivo dispositivo = this.dispositivoService.findById(evento.getDispositivo().getId());
        Idoso idoso = this.idosoService.findById(dispositivo.getIdoso().getId());
        Contato contato = this.contatoService.findFirstByIdosoId(idoso.getId());

        alerta.setIdoso(idoso);
        alerta.setUsuario(contato.getUsuario());
        alerta.setNivelEscalonamento(contato.getOrdemEscalonamento());

        Alerta save = this.alertaService.save(alerta);
        return ResponseEntity.created(URI.create("/api/alertas/" + save.getId())).body(AlertaMapper.toResponse(save));
    }
}