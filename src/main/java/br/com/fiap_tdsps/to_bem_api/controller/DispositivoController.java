package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.mapper.DispositivoMapper;
import br.com.fiap_tdsps.to_bem_api.mapper.EventoMapper;
import br.com.fiap_tdsps.to_bem_api.model.*;
import br.com.fiap_tdsps.to_bem_api.service.DispositivoService;
import br.com.fiap_tdsps.to_bem_api.service.EventoService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

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

    @GetMapping
    public PageResponse<DispositivoResponse> findAll(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                    @RequestParam(required = false,defaultValue = "10") @Positive Integer size){
        Page<Dispositivo> pageResult = this.dispositivoService.findAll(page,size);
        return PageResponse.from(pageResult.map(DispositivoMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DispositivoResponse> findById(@PathVariable @Positive Long id){
        Dispositivo dispositivo = this.dispositivoService.findById(id);
        return ResponseEntity.ok(DispositivoMapper.toResponse(dispositivo));
    }

    @GetMapping("/byOtherFilter")
    public PageResponse<DispositivoResponse> findByOtherFilter(@Positive Long idosoId,
                                                               @RequestParam(required = false) DispositivoTipo tipo,
                                                               @RequestParam(required = false) String comodo,
                                                               @RequestParam(required = false) DispositivoStatus status,
                                                               @RequestParam(required = false,defaultValue = "0") @PositiveOrZero Integer page,
                                                               @RequestParam(required = false,defaultValue = "10") @Positive Integer size){
        Page<Dispositivo> pageResult = tipo != null && comodo == null && status == null ?
                this.dispositivoService.findByTipoAndIdosoId(tipo,idosoId,page,size) :
                tipo == null && comodo != null && status == null ?
                        this.dispositivoService.findByComodoAndIdosoId(comodo,idosoId,page,size) :
                        tipo == null && comodo == null && status != null ?
                                this.dispositivoService.findByStatusAndIdosoId(status,idosoId,page,size) :
                                tipo == null && comodo == null && status == null ?
                                        this.dispositivoService.findByIdosoId(idosoId,page,size) :
                                        null ; //lançar MoreThanOneFilter

        return PageResponse.from(pageResult.map(DispositivoMapper::toResponse));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<DispositivoResponse> update(@PathVariable @Positive Long id,
                                               @RequestBody DispositivoRequest dispositivoRequest){
        Dispositivo dispositivo = this.dispositivoService.findById(id);

        dispositivo.setTipo(dispositivoRequest.tipo());
        dispositivo.setComodo(dispositivoRequest.comodo());
        dispositivo.setStatus(dispositivoRequest.status());
        dispositivo.setUltimoHeartbeat(dispositivoRequest.ultimoHeartbeat().format(CommonVariables.dateTimeFormatter));

        Dispositivo updated = this.dispositivoService.update(id,dispositivo);
        return ResponseEntity.ok(DispositivoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        this.dispositivoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/evento")
    public ResponseEntity<EventoResponse> saveEvento(@PathVariable @Positive Long id,
                                                     @RequestBody EventoRequest eventoRequest){
        Evento evento = new Evento();
        evento.setDataHora(eventoRequest.dataHora().format(CommonVariables.dateTimeFormatter));
        evento.setDispositivo(this.dispositivoService.findById(id));
        evento.setTipo(eventoRequest.tipo());
        evento.setValor(eventoRequest.valor());

        Evento save = this.eventoService.save(evento);
        return ResponseEntity.created(URI.create("/api/eventos/" + save.getId())).body(EventoMapper.toResponse(save));
    }
}
