package br.com.fiap_tdsps.to_bem_api.controller;

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

@RestController
@RequestMapping("/api/rotinas")
@Validated
public class RotinaController {
    private final RotinaService rotinaService;
    public RotinaController(RotinaService rotinaService) { this.rotinaService = rotinaService; }

    @GetMapping
    public PageResponse<RotinaResponse> findAll(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
                                                @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                                @RequestParam(required = false) @Positive Long idosoId,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        Page<Rotina> pageResult = idosoId == null ? this.rotinaService.findAll(Paginacao.criar(page, size, sort, direction, Rotina.class)) : this.rotinaService.findByIdosoId(idosoId, Paginacao.criar(page, size, sort, direction, Rotina.class));
        return PageResponse.from(pageResult.map(RotinaMapper::toResponse));
    }

    @GetMapping("/{id}")
    public RotinaResponse findById(@PathVariable @Positive Long id) {
        return RotinaMapper.toResponse(this.rotinaService.findById(id));
    }

    @PutMapping("/{id}")
    public RotinaResponse update(@PathVariable @Positive Long id,
                                 @RequestBody @Valid RotinaRequest rotinaRequest) {
        Rotina rotina = this.rotinaService.findById(id);
        rotina.setDiaSemana(rotinaRequest.diaSemana());
        rotina.setHoraPrimeiraAtividade(rotinaRequest.horaPrimeiraAtividade().format(CommonVariables.timeFormatter));
        rotina.setMediaLevantadasPorNoite(rotinaRequest.mediaLevantadasPorNoite());
        return RotinaMapper.toResponse(this.rotinaService.update(id, rotina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        this.rotinaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
