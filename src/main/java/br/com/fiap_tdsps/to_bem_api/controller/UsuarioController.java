package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.mapper.IdosoMapper;
import br.com.fiap_tdsps.to_bem_api.mapper.UsuarioMapper;
import br.com.fiap_tdsps.to_bem_api.model.CommonVariables;
import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.service.ContatoService;
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
import java.time.LocalDate;

@RestController
@RequestMapping("/api/usuarios")
@Validated
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final IdosoService idosoService;
    private final ContatoService contatoService;

    public UsuarioController(UsuarioService usuarioService, IdosoService idosoService,
                             ContatoService contatoService){
        this.usuarioService = usuarioService;
        this.contatoService = contatoService;
        this.idosoService = idosoService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> saveUsuario(@RequestBody @Valid UsuarioRequest request){
        Usuario usuario = new Usuario();

        usuario.setEmail(request.email());
        usuario.setNome(request.nome());
        usuario.setSenha(request.senha());
        usuario.setPapel(request.papel());

        Usuario save = this.usuarioService.save(usuario);

        return ResponseEntity.created(URI.create("api/usuarios/" + save.getId())).body(UsuarioMapper.toResponse(save));
    }

    @GetMapping
    public PageResponse<UsuarioResponse> findAll(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                 @RequestParam(required = false,defaultValue = "10") @Positive Integer size){
        Page<Usuario> pageResult = this.usuarioService.findAll(page,size);

        return PageResponse.from(pageResult.map(UsuarioMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable @Positive Long id){
        Usuario usuario = this.usuarioService.findById(id);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> update(@PathVariable @Positive Long id,
                                                  @RequestBody @Valid UsuarioRequest request){
        Usuario usuario = this.usuarioService.findById(id);

        usuario.setEmail(request.email());
        usuario.setNome(request.nome());
        usuario.setSenha(request.senha());
        usuario.setPapel(request.papel());

        Usuario updated = this.usuarioService.update(id,usuario);
        return ResponseEntity.ok(UsuarioMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        this.usuarioService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{usuarioId}/idoso")
    public ResponseEntity<IdosoResponse> saveIdoso(@PathVariable @Positive Long usuarioId,
                                                   @RequestBody @Valid IdosoRequest idosoRequest,
                                                   @RequestParam String parentesco,
                                                   @RequestParam @Positive Integer ordemEscalonamento){
        Idoso idoso = new Idoso();

        idoso.setModo(idosoRequest.modo());
        idoso.setEndereco(idosoRequest.endereco());
        idoso.setTelefone(idosoRequest.telefone());
        idoso.setNome(idosoRequest.nome());
        idoso.setDataNascimento(idosoRequest.dataNascimento().format(CommonVariables.dateFormatter));

        Idoso save = this.idosoService.save(idoso);

        Contato contato = new Contato();

        contato.setParentesco(parentesco);
        contato.setOrdemEscalonamento(ordemEscalonamento);
        contato.setIdoso(save);
        contato.setUsuario(this.usuarioService.findById(usuarioId));

        this.contatoService.save(contato);

        return ResponseEntity.created(URI.create("/api/idosos/" + save.getId())).body(IdosoMapper.toResponse(idoso));
    }
}
