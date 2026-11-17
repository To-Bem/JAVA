package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.service.Paginacao;

import org.springframework.data.domain.Sort;

import br.com.fiap_tdsps.to_bem_api.dto.AlertaRequest;
import br.com.fiap_tdsps.to_bem_api.dto.AlertaResponse;
import br.com.fiap_tdsps.to_bem_api.dto.PageResponse;
import br.com.fiap_tdsps.to_bem_api.exceptions.InvalidAlertaResend;
import br.com.fiap_tdsps.to_bem_api.mapper.AlertaMapper;
import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import br.com.fiap_tdsps.to_bem_api.model.CommonVariables;
import br.com.fiap_tdsps.to_bem_api.service.AlertaService;
import br.com.fiap_tdsps.to_bem_api.service.AlertaNotificacaoService;
import br.com.fiap_tdsps.to_bem_api.dto.AlertaNotificacaoResponse;
import br.com.fiap_tdsps.to_bem_api.service.EventoService;
import br.com.fiap_tdsps.to_bem_api.service.IdosoService;
import br.com.fiap_tdsps.to_bem_api.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/alertas")
@Validated
public class AlertaController {

    private final AlertaNotificacaoService alertaNotificacaoService;
    private final AlertaService alertaService;
    private final IdosoService idosoService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;

    public AlertaController(AlertaService alertaService, IdosoService idosoService,
                            UsuarioService usuarioService, EventoService eventoService,
                            AlertaNotificacaoService alertaNotificacaoService){
        this.alertaNotificacaoService = alertaNotificacaoService;
        this.alertaService = alertaService;
        this.idosoService = idosoService;
        this.usuarioService = usuarioService;
        this.eventoService = eventoService;
    }

    @GetMapping
    public PageResponse<AlertaResponse> findAll(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction){

        Page<Alerta> pageResult = this.alertaService.findAll(Paginacao.criar(page, size, sort, direction, Alerta.class));
        return PageResponse.from(pageResult.map(AlertaMapper::toResponse));
    }

    @GetMapping("/byOtherFilter")
    public PageResponse<AlertaResponse> findAllByOtherFilter(@RequestParam(required = false) @Positive Long idosoId,
                                                             @RequestParam(required = false) @Positive Long usuarioId,
                                                             @RequestParam(required = false) AlertaStatus status,
                                                             @RequestParam(required = false) AlertaNivel nivel,
                                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                             @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction){

        Page<Alerta> pageResult = this.alertaService.findByOtherFilter(idosoId, usuarioId, status, nivel,
                Paginacao.criar(page, size, sort, direction, Alerta.class));
        return PageResponse.from(pageResult.map(AlertaMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> findById(@PathVariable @Positive Long id){
        Alerta alerta = this.alertaService.findById(id);
        return ResponseEntity.ok(AlertaMapper.toResponse(alerta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponse> update(@PathVariable @Positive Long id,
                                                 @Valid @RequestBody AlertaRequest request){
        Alerta alerta = this.alertaService.findById(id);
        alerta.setTipo(request.tipo());
        alerta.setNivel(request.nivel());
        alerta.setStatus(request.status());

        Alerta updated = this.alertaService.update(id, alerta);
        return ResponseEntity.ok(AlertaMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        this.alertaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/resend")
    public ResponseEntity<AlertaNotificacaoResponse> reSendAlerta(
            @PathVariable @Positive Long id, @RequestParam @Positive Integer etapa) {
        return ResponseEntity.ok(this.alertaNotificacaoService.escalonar(id, etapa));
    }
}
