package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.ContatoRequest;
import br.com.fiap_tdsps.to_bem_api.dto.ContatoResponse;
import br.com.fiap_tdsps.to_bem_api.dto.PageResponse;
import br.com.fiap_tdsps.to_bem_api.mapper.ContatoMapper;
import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.service.ContatoService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contatos")
@Validated
public class ContatoController {

    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService){
        this.contatoService = contatoService;
    }

    @GetMapping
    public PageResponse<ContatoResponse> findAll(@RequestParam(required = false,defaultValue = "0") @PositiveOrZero Integer page,
                                                 @RequestParam(required = false,defaultValue = "10") @Positive Integer size){

        Page<Contato> pageResult = this.contatoService.findAll(page,size);

        return PageResponse.from(pageResult.map(ContatoMapper::toResponse));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> update(@RequestBody ContatoRequest contatoRequest,
                                                  @PathVariable @Positive Long id){
        Contato contato = this.contatoService.findById(id);
        contato.setOrdemEscalonamento(contatoRequest.ordemEscalonamento());
        contato.setParentesco(contatoRequest.parentesco());

        Contato updated = this.contatoService.update(id,contato);
        return ResponseEntity.ok(ContatoMapper.toResponse(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContatoResponse> findById(@PathVariable @Positive Long id){
        Contato contato = this.contatoService.findById(id);
        return ResponseEntity.ok(ContatoMapper.toResponse(contato));
    }

    @GetMapping("/byOtherId")
    public ResponseEntity<ContatoResponse> findByOtherId(@RequestParam(required = false) @Positive Long idosoId,
                                                         @RequestParam(required = false) @Positive Long usarioId){
        Contato contato = idosoId != null && usarioId == null ?
                this.contatoService.findByIdosoId(idosoId) :
                idosoId == null && usarioId != null ?
                        this.contatoService.findByUsuarioId(usarioId) :
                        null ; // lançar MoreThanOneFilter

        return ResponseEntity.ok(ContatoMapper.toResponse(contato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable @Positive Long id){
        this.contatoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
