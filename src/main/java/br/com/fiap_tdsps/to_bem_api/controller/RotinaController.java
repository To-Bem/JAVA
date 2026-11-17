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
import br.com.fiap_tdsps.to_bem_api.mapper.RotinaMapper;
import br.com.fiap_tdsps.to_bem_api.model.*;
import br.com.fiap_tdsps.to_bem_api.service.RotinaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rotinas")
@RestController
@RequestMapping("/api/rotinas")
@Validated
public class RotinaController {
    private final RotinaService rotinaService;
    public RotinaController(RotinaService rotinaService) { this.rotinaService = rotinaService; }

    @Operation(summary = "Lista rotinas", description = "Exclusivo para ADMIN ou MANAGER.")
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
    public PageResponse<RotinaResponse> findAll(@Parameter(description = "Página, iniciando em 0.") @RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                @Parameter(description = "Quantidade de registros por página.") @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                @Parameter(description = "ID do idoso.") @RequestParam(required = false) @Positive Long idosoId,
            @Parameter(description = "Campo simples usado na ordenação.") @RequestParam(defaultValue = "id") String sort,
            @Parameter(description = "Direção da ordenação.") @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Page<Rotina> pageResult = idosoId == null ? this.rotinaService.findAll(Paginacao.criar(page, size, sort, direction, Rotina.class)) : this.rotinaService.findByIdosoId(idosoId, Paginacao.criar(page, size, sort, direction, Rotina.class));
        return PageResponse.from(pageResult.map(RotinaMapper::toResponse));
    }

    @Operation(summary = "Consulta rotina por ID")
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
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER') or @permissaoService.podeAcessarRotina(#id)")
    @GetMapping("/{id}")
    public RotinaResponse findById(@Parameter(description = "ID do rotina.") @PathVariable @Positive Long id) {
        return RotinaMapper.toResponse(this.rotinaService.findById(id));
    }

    @Operation(summary = "Atualiza rotina")
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
    public RotinaResponse update(@Parameter(description = "ID do rotina.") @PathVariable @Positive Long id,
                                 @RequestBody @Valid RotinaRequest rotinaRequest) {
        Rotina rotina = this.rotinaService.findById(id);
        rotina.setDiaSemana(rotinaRequest.diaSemana());
        rotina.setHoraPrimeiraAtividade(rotinaRequest.horaPrimeiraAtividade().format(CommonVariables.timeFormatter));
        rotina.setMediaLevantadasPorNoite(rotinaRequest.mediaLevantadasPorNoite());
        return RotinaMapper.toResponse(this.rotinaService.update(id, rotina));
    }

    @Operation(summary = "Exclui rotina")
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
    public ResponseEntity<Void> delete(@Parameter(description = "ID do rotina.") @PathVariable @Positive Long id) {
        this.rotinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
