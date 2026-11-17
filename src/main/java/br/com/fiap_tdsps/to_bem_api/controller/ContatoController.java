package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.service.Paginacao;

import org.springframework.data.domain.Sort;

import br.com.fiap_tdsps.to_bem_api.dto.ContatoRequest;
import br.com.fiap_tdsps.to_bem_api.dto.ContatoResponse;
import br.com.fiap_tdsps.to_bem_api.dto.PageResponse;
import br.com.fiap_tdsps.to_bem_api.mapper.ContatoMapper;
import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.service.ContatoService;
import jakarta.validation.Valid;
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
                                                 @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction){

        Page<Contato> pageResult = this.contatoService.findAll(Paginacao.criar(page, size, sort, direction, Contato.class));

        return PageResponse.from(pageResult.map(ContatoMapper::toResponse));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ContatoResponse> update(@RequestBody @Valid ContatoRequest contatoRequest,
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
    public PageResponse<ContatoResponse> findByOtherId(@RequestParam(required = false) @Positive Long idosoId,
                                                       @RequestParam(required = false) @Positive Long usuarioId,
                                                       @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                       @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        Page<Contato> pageResult = this.contatoService.findByOtherFilter(idosoId, usuarioId,
                Paginacao.criar(page, size, sort, direction, Contato.class));
        return PageResponse.from(pageResult.map(ContatoMapper::toResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable @Positive Long id){
        this.contatoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
