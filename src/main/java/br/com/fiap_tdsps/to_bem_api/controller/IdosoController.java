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
import br.com.fiap_tdsps.to_bem_api.mapper.IdosoMapper;
import br.com.fiap_tdsps.to_bem_api.mapper.RotinaMapper;
import br.com.fiap_tdsps.to_bem_api.model.CommonVariables;
import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import br.com.fiap_tdsps.to_bem_api.model.Rotina;
import br.com.fiap_tdsps.to_bem_api.service.DispositivoService;
import br.com.fiap_tdsps.to_bem_api.service.IdosoService;
import br.com.fiap_tdsps.to_bem_api.service.RotinaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@Tag(name = "Idosos")
@RestController
@RequestMapping("/api/idosos")
@Validated
public class IdosoController {

    private final IdosoService idosoService;
    private final RotinaService rotinaService;
    private final DispositivoService dispositivoService;

    public IdosoController(IdosoService idosoService, RotinaService rotinaService,
                           DispositivoService dispositivoService){
        this.idosoService = idosoService;
        this.rotinaService = rotinaService;
        this.dispositivoService = dispositivoService;
    }

    @Operation(summary = "Lista idosos", description = "Exclusivo para ADMIN ou MANAGER.")
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
    public PageResponse<IdosoResponse> findAll(@Parameter(description = "Página, iniciando em 0.") @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                               @Parameter(description = "Quantidade de registros por página.") @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction){

        Page<Idoso> pageResult = this.idosoService.findAll(Paginacao.criar(page, size, sort, direction, Idoso.class));
        return PageResponse.from(pageResult.map(IdosoMapper::toResponse));
    }

    @Operation(summary = "Consulta idoso por ID")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#id)")
    @GetMapping("/{id}")
    public ResponseEntity<IdosoResponse> findById(@Positive @Parameter(description = "ID do idoso.") @PathVariable Long id){
        Idoso idoso = this.idosoService.findById(id);
        return ResponseEntity.ok(IdosoMapper.toResponse(idoso));
    }

    @Operation(summary = "Atualiza idoso")
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
    public ResponseEntity<IdosoResponse> update(@Positive @Parameter(description = "ID do idoso.") @PathVariable Long id,
                                                @RequestBody @Valid IdosoRequest idosoRequest){
        Idoso idoso = this.idosoService.findById(id);

        idoso.setNome( idosoRequest.nome());
        idoso.setDataNascimento(idosoRequest.dataNascimento().format(CommonVariables.dateFormatter));
        idoso.setTelefone(idosoRequest.telefone());
        idoso.setEndereco(idosoRequest.endereco());
        idoso.setModo(idosoRequest.modo());

        Idoso updated = this.idosoService.update(id,idoso);
        return ResponseEntity.ok(IdosoMapper.toResponse(updated));
    }

    @Operation(summary = "Exclui idoso")
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
    public ResponseEntity<Void> delete(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long id){
        this.idosoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cadastra rotina do idoso")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#idosoId)")
    @PostMapping("/{idosoId}/rotina")
    public ResponseEntity<RotinaResponse> saveRotina(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long idosoId,
                                                     @RequestBody @Valid RotinaRequest rotinaRequest){
        Rotina rotina = new Rotina();

        rotina.setDiaSemana(rotinaRequest.diaSemana());
        rotina.setHoraPrimeiraAtividade(rotinaRequest.horaPrimeiraAtividade().format(CommonVariables.timeFormatter));
        rotina.setMediaLevantadasPorNoite(rotinaRequest.mediaLevantadasPorNoite());
        rotina.setIdoso(this.idosoService.findById(idosoId));

        Rotina save = this.rotinaService.save(rotina);
        return ResponseEntity.created(URI.create("/api/rotinas/" + save.getId())).body(RotinaMapper.toResponse(save));
    }

    @Operation(summary = "Cadastra dispositivo do idoso")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#idosoId)")
    @PostMapping("/{idosoId}/dispositivo")
    public ResponseEntity<DispositivoResponse> saveDispositivo(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long idosoId,
                                                               @RequestBody @Valid DispositivoRequest dispositivoRequest){
        Dispositivo dispositivo = new Dispositivo();

        dispositivo.setUltimoHeartbeat(LocalDateTime.now().format(CommonVariables.dateTimeFormatter));
        dispositivo.setStatus(dispositivoRequest.status());
        dispositivo.setComodo(dispositivoRequest.comodo());
        dispositivo.setTipo(dispositivoRequest.tipo());
        dispositivo.setIdoso(this.idosoService.findById(idosoId));

        Dispositivo save = this.dispositivoService.save(dispositivo);
        return ResponseEntity.created(URI.create("/api/dispositivos/" + save.getId())).body(DispositivoMapper.toResponse(save));
    }

    @Operation(summary = "Consulta idoso com alertas")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#id)")
    @GetMapping("/{id}/withAlertas")
    public ResponseEntity<IdosoWithAlertasResponse> findWithAlertasById(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long id){
        Idoso idoso = this.idosoService.findWithAlertasById(id);
        return ResponseEntity.ok(IdosoMapper.toResponseWithAlertas(idoso));
    }

    @Operation(summary = "Consulta idoso com todos os relacionamentos")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#id)")
    @GetMapping("/{id}/withAll")
    public ResponseEntity<IdosoWithAllResponse> findWithAllById(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long id){
        Idoso idoso = this.idosoService.findWithAllById(id);
        return ResponseEntity.ok(IdosoMapper.toResponseWithAll(idoso));
    }

    @Operation(summary = "Consulta idoso com contatos")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#id)")
    @GetMapping("/{id}/withContatos")
    public ResponseEntity<IdosoWithContatosResponse> findWithContatosById(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long id){
        Idoso idoso = this.idosoService.findWithContatosById(id);
        return ResponseEntity.ok(IdosoMapper.toResponseWithContatos(idoso));
    }

    @Operation(summary = "Consulta idoso com dispositivos")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#id)")
    @GetMapping("/{id}/withDispositivos")
    public ResponseEntity<IdosoWithDispositivosResponse> findWithDispositivosById(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long id){
        Idoso idoso = this.idosoService.findWithDispositivosById(id);
        return ResponseEntity.ok(IdosoMapper.toResponseWithDispositivos(idoso));
    }

    @Operation(summary = "Consulta idoso com rotinas")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarIdoso(#id)")
    @GetMapping("/{id}/withRotinas")
    public ResponseEntity<IdosoWithRotinasResponse> findWithRotinasById(@Parameter(description = "ID do idoso.") @PathVariable @Positive Long id){
        Idoso idoso = this.idosoService.findWithRotinasById(id);
        return ResponseEntity.ok(IdosoMapper.toResponseWithRotinas(idoso));
    }
}
