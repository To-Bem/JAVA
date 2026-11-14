package br.com.fiap_tdsps.to_bem_api.controller;

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
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @GetMapping
    public PageResponse<IdosoResponse> findAll(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                               @RequestParam(required = false,defaultValue = "10") @Positive Integer size){

        Page<Idoso> pageResult = this.idosoService.findAll(page,size);
        return PageResponse.from(pageResult.map(IdosoMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IdosoResponse> findById(@Positive @PathVariable Long id){
        Idoso idoso = this.idosoService.findById(id);
        return ResponseEntity.ok(IdosoMapper.toResponse(idoso));
    }

    @PutMapping("/{id}")
    public ResponseEntity<IdosoResponse> update(@Positive @PathVariable Long id,
                                                @RequestBody @Valid IdosoRequest idosoRequest){
        Idoso idoso = this.idosoService.findById(id);

        idoso.setNome( idosoRequest.nome());
        idoso.setDataNascimento(idosoRequest.dataNascimento().format(CommonVariables.dateTimeFormatter));
        idoso.setTelefone(idosoRequest.telefone());
        idoso.setEndereco(idosoRequest.endereco());
        idoso.setModo(idosoRequest.modo());

        Idoso updated = this.idosoService.update(id,idoso);
        return ResponseEntity.ok(IdosoMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        this.idosoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idosoId}/rotina")
    public ResponseEntity<RotinaResponse> saveRotina(@PathVariable @Positive Long idosoId,
                                                     @RequestBody @Valid RotinaRequest rotinaRequest){
        Rotina rotina = new Rotina();

        rotina.setDiaSemana(rotinaRequest.diaSemana());
        rotina.setHoraPrimeiraAtividade(rotinaRequest.horaPrimeiraAtividade().format(CommonVariables.timeFormatter));
        rotina.setMediaLevantadasPorNoite(rotinaRequest.mediaLevantadasPorNoite());
        rotina.setIdoso(this.idosoService.findById(idosoId));

        Rotina save = this.rotinaService.save(rotina);
        return ResponseEntity.created(URI.create("api/rotinas/" + save.getId())).body(RotinaMapper.toResponse(save));
    }

    @PostMapping("/{idosoId}/dispositivo")
    public ResponseEntity<DispositivoResponse> saveDispositivo(@PathVariable @Positive Long idosoId,
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
}
