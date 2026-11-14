package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.AlertaRequest;
import br.com.fiap_tdsps.to_bem_api.dto.AlertaResponse;
import br.com.fiap_tdsps.to_bem_api.dto.PageResponse;
import br.com.fiap_tdsps.to_bem_api.mapper.AlertaMapper;
import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import br.com.fiap_tdsps.to_bem_api.model.CommonVariables;
import br.com.fiap_tdsps.to_bem_api.service.AlertaService;
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

    private final AlertaService alertaService;
    private final IdosoService idosoService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;

    public AlertaController(AlertaService alertaService, IdosoService idosoService,
                            UsuarioService usuarioService, EventoService eventoService){
        this.alertaService = alertaService;
        this.idosoService = idosoService;
        this.usuarioService = usuarioService;
        this.eventoService = eventoService;
    }

    @GetMapping
    public PageResponse<AlertaResponse> findAll(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                @RequestParam(required = false,defaultValue = "10") @Positive Integer size){

        Page<Alerta> pageResult = this.alertaService.findAll(page,size);
        return PageResponse.from(pageResult.map(AlertaMapper::toResponse));
    }

    @GetMapping("/byOtherFilter")
    public PageResponse<AlertaResponse> findAllByOtherFilter(@RequestParam(required = false) @Positive Long idosoId,
                                                             @RequestParam(required = false) @Positive Long usuarioId,
                                                             @RequestParam(required = false) AlertaStatus status,
                                                             @RequestParam(required = false) AlertaNivel nivel,
                                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                             @RequestParam(required = false,defaultValue = "10") @Positive Integer size){

        Page<Alerta> pageResult = status != null && nivel == null && (idosoId != null || usuarioId != null) ?
                this.alertaService.findByStatusAndOtherId(status,idosoId,usuarioId,page,size) :
                status == null && nivel != null && (idosoId != null || usuarioId != null) ?
                        this.alertaService.findByNivelAndOtherId(nivel,idosoId,usuarioId,page,size) :
                        status == null && nivel == null && idosoId != null && usuarioId == null ?
                                this.alertaService.findByIdosoId(idosoId,page,size) :
                                status == null && nivel == null && idosoId == null && usuarioId != null ?
                                        this.alertaService.findByUsuarioId(usuarioId,page, size) :
                                        null ; //lançar MoreThanOneFilter

        return PageResponse.from(pageResult.map(AlertaMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponse> findById(@PathVariable @Positive Long id){
        Alerta alerta = this.alertaService.findById(id);
        return ResponseEntity.ok(AlertaMapper.toResponse(alerta));
    }

    @PostMapping("/{id}")
    public ResponseEntity<AlertaResponse> update(@PathVariable @Positive Long id,
                                                 @Valid @RequestBody AlertaRequest request){
        Alerta alerta = this.alertaService.findById(id);
        alerta.setTipo(request.tipo());
        alerta.setNivelEscalonamento(request.nivelEscalonamento());
        alerta.setNivel(request.nivel());
        alerta.setStatus(request.status());

        Alerta updated = this.alertaService.update(id, alerta);
        return ResponseEntity.ok(AlertaMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        this.alertaService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reSend")
    public ResponseEntity<AlertaResponse> reSendAlerta(@PathVariable @Positive Long id,
                                                       @RequestParam @Positive Integer oredemAtual){
        Alerta alerta = this.alertaService.findById(id);
        if(alerta.getStatus() != AlertaStatus.RESOLVIDO) {
            Alerta reSend = new Alerta();
            reSend.setUsuario(this.usuarioService.findById(alerta.getUsuario().getId()));
            reSend.setIdoso(this.idosoService.findById(alerta.getIdoso().getId()));
            reSend.setEvento(this.eventoService.findById(alerta.getEvento().getId()));
            reSend.setCriadoEm(LocalDateTime.now().format(CommonVariables.dateTimeFormatter));
            reSend.setNivelEscalonamento(oredemAtual + 1);
            reSend.setNivel(alerta.getNivel());
            reSend.setStatus(alerta.getStatus());
            reSend.setTipo(alerta.getTipo());
            Alerta save = this.alertaService.save(reSend);
            return ResponseEntity.created(URI.create("/api/alertas/" + save.getId())).body(AlertaMapper.toResponse(save));
        }
        //InvalidAlertaResend
        return null;
    }
}
