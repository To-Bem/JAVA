package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.mapper.AlertaMapper;
import br.com.fiap_tdsps.to_bem_api.mapper.EventoMapper;
import br.com.fiap_tdsps.to_bem_api.model.*;
import br.com.fiap_tdsps.to_bem_api.service.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @GetMapping
    public PageResponse<EventoResponse> findAll(@RequestParam(required = false,defaultValue = "0") @PositiveOrZero Integer page,
                                                @RequestParam(required = false,defaultValue = "10") @Positive Integer size){
        Page<Evento> pageResult = this.eventoService.findAll(page,size);
        return PageResponse.from(pageResult.map(EventoMapper::toResponse));
    }

    @GetMapping("/byOtherFilter")
    public PageResponse<EventoResponse> findByOtherFilter(@RequestParam @Positive Long dispositivoId,
                                                          @RequestParam(required = false) String tipo,
                                                          @RequestParam(required = false,defaultValue = "0") @PositiveOrZero Integer page,
                                                          @RequestParam(required = false,defaultValue = "10") @Positive Integer size){
        Page<Evento> pageResult = tipo != null ?
                this.eventoService.findByTipoAndDispositivoId(tipo,dispositivoId,page,size) :
                this.eventoService.findByDispositivoId(dispositivoId,page,size);
        return  PageResponse.from(pageResult.map(EventoMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponse> findById(@PathVariable @Positive Long id){
        Evento evento = this.eventoService.findById(id);

        return ResponseEntity.ok(EventoMapper.toResponse(evento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponse> update(@PathVariable @Positive Long id,
                                                 @RequestBody EventoRequest eventoRequest) {
        Evento evento = this.eventoService.findById(id);
        evento.setValor(eventoRequest.valor());
        evento.setTipo(eventoRequest.tipo());
        evento.setDataHora(eventoRequest.dataHora().format(CommonVariables.dateTimeFormatter));
        Evento updated = this.eventoService.update(id, evento);
        return ResponseEntity.ok(EventoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        this.eventoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/alerta")
    public ResponseEntity<AlertaResponse> saveAlerta(@PathVariable @Positive Long id,
                                                     @RequestBody AlertaRequest alertaRequest){
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

        Alerta save = this.alertaService.save(alerta);
        return ResponseEntity.created(URI.create("/api/alertas/" + save.getId())).body(AlertaMapper.toResponse(save));
    }
}