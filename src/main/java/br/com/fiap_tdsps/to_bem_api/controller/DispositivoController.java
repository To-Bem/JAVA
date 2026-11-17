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
import br.com.fiap_tdsps.to_bem_api.mapper.DispositivoMapper;
import br.com.fiap_tdsps.to_bem_api.mapper.EventoMapper;
import br.com.fiap_tdsps.to_bem_api.model.*;
import br.com.fiap_tdsps.to_bem_api.service.DispositivoService;
import br.com.fiap_tdsps.to_bem_api.service.EventoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@Tag(name = "Dispositivos")
@RestController
@RequestMapping("/api/dispositivos")
@Validated
public class DispositivoController {

    private final DispositivoService dispositivoService;
    private final EventoService eventoService;

    public DispositivoController(DispositivoService dispositivoService, EventoService eventoService){
        this.dispositivoService = dispositivoService;
        this.eventoService = eventoService;
    }

    @Operation(summary = "Lista dispositivos", description = "Exclusivo para ADMIN ou MANAGER.")
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
    public PageResponse<DispositivoResponse> findAll(@Parameter(description = "Página, iniciando em 0.") @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                     @Parameter(description = "Quantidade de registros por página.") @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        Page<Dispositivo> pageResult = this.dispositivoService.findAll(Paginacao.criar(page, size, sort, direction, Dispositivo.class));
        return PageResponse.from(pageResult.map(DispositivoMapper::toResponse));
    }

    @Operation(summary = "Consulta dispositivo por ID")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarDispositivo(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<DispositivoResponse> findById(@Parameter(description = "ID do dispositivo.") @PathVariable @Positive Long id){
        Dispositivo dispositivo = this.dispositivoService.findById(id);
        return ResponseEntity.ok(DispositivoMapper.toResponse(dispositivo));
    }

    @Operation(summary = "Filtra dispositivos", description = "Combina os filtros informados.")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#idosoId)")
    @GetMapping("/byOtherFilter")
    public PageResponse<DispositivoResponse> findByOtherFilter(@Parameter(description = "ID do idoso.") @RequestParam @Positive Long idosoId,
                                                               @Parameter(description = "Tipo para filtrar.") @RequestParam(required = false) DispositivoTipo tipo,
                                                               @Parameter(description = "Cômodo para filtrar.") @RequestParam(required = false) String comodo,
                                                               @Parameter(description = "Status para filtrar.") @RequestParam(required = false) DispositivoStatus status,
                                                               @Parameter(description = "Página, iniciando em 0.") @RequestParam(required = false,defaultValue = "0") @PositiveOrZero Integer page,
                                                               @Parameter(description = "Quantidade de registros por página.") @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        Page<Dispositivo> pageResult = this.dispositivoService.findByOtherFilter(idosoId, tipo, comodo, status,
                Paginacao.criar(page, size, sort, direction, Dispositivo.class));
        return PageResponse.from(pageResult.map(DispositivoMapper::toResponse));
    }

    @Operation(summary = "Atualiza dispositivo")
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
    public  ResponseEntity<DispositivoResponse> update(@Parameter(description = "ID do dispositivo.") @PathVariable @Positive Long id,
                                                       @RequestBody @Valid DispositivoRequest dispositivoRequest){
        Dispositivo dispositivo = this.dispositivoService.findById(id);

        dispositivo.setTipo(dispositivoRequest.tipo());
        dispositivo.setComodo(dispositivoRequest.comodo());
        dispositivo.setStatus(dispositivoRequest.status());

        Dispositivo updated = this.dispositivoService.update(id,dispositivo);
        return ResponseEntity.ok(DispositivoMapper.toResponse(updated));
    }

    @Operation(summary = "Atualiza heartbeat", description = "Registra o horário atual do dispositivo.")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @PostMapping("/{id}/heartbeat")
    public ResponseEntity<DispositivoResponse> updateHeartBeatById(@Parameter(description = "ID do dispositivo.") @PathVariable @Positive Long id){
        Dispositivo updated =this.dispositivoService.updateHeartBeatById(id, LocalDateTime.now().format(CommonVariables.dateTimeFormatter));
        return ResponseEntity.ok(DispositivoMapper.toResponse(updated));
    }

    @Operation(summary = "Exclui dispositivo")
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
    public ResponseEntity<Void> delete(@Parameter(description = "ID do dispositivo.") @PathVariable @Positive Long id){
        this.dispositivoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Registra evento do dispositivo")
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
    @PostMapping("/{id}/evento")
    public ResponseEntity<EventoResponse> saveEvento(@Parameter(description = "ID do dispositivo.") @PathVariable @Positive Long id,
                                                     @RequestBody @Valid EventoRequest eventoRequest){
        Evento evento = new Evento();
        evento.setDataHora(eventoRequest.dataHora().format(CommonVariables.dateTimeFormatter));
        evento.setDispositivo(this.dispositivoService.findById(id));
        evento.setTipo(eventoRequest.tipo());
        evento.setValor(eventoRequest.valor());

        Evento save = this.eventoService.save(evento);
        return ResponseEntity.created(URI.create("/api/eventos/" + save.getId())).body(EventoMapper.toResponse(save));
    }

    @Operation(summary = "Consulta dispositivo com eventos")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarDispositivo(#id)")
    @GetMapping("/{id}/withEventos")
    public ResponseEntity<DispositivoWithEventosResponse> findWithEventosById(@Parameter(description = "ID do dispositivo.") @PathVariable @Positive Long id){
        Dispositivo dispositivo = this.dispositivoService.findWithEventosById(id);
        return ResponseEntity.ok(DispositivoMapper.toResponseWithEventos(dispositivo));
    }
}
