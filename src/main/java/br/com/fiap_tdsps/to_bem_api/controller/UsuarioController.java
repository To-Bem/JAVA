package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.service.Paginacao;

import org.springframework.data.domain.Sort;

import br.com.fiap_tdsps.to_bem_api.dto.*;
import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotCreated;
import br.com.fiap_tdsps.to_bem_api.exceptions.RoleNotPermitted;
import br.com.fiap_tdsps.to_bem_api.mapper.IdosoMapper;
import br.com.fiap_tdsps.to_bem_api.mapper.UsuarioMapper;
import br.com.fiap_tdsps.to_bem_api.model.*;
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
import java.util.Optional;

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
       if(request.papel() == UsuarioPapel.ADMIN) {
           throw new RoleNotPermitted("Usuario", "ADMIN");
       }
        usuario.setPapel(request.papel());

        Usuario save = this.usuarioService.save(usuario);

        return ResponseEntity.created(URI.create("/api/usuarios/" + save.getId())).body(UsuarioMapper.toResponse(save));
    }

    @GetMapping
    public PageResponse<UsuarioResponse> findAll(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
                                                 @RequestParam(required = false,defaultValue = "10") @Positive Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction){
        Page<Usuario> pageResult = this.usuarioService.findAll(Paginacao.criar(page, size, sort, direction, Usuario.class));

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
        if(request.papel() != UsuarioPapel.ADMIN) {
            usuario.setPapel(request.papel());
        }
        else {
            throw new RoleNotPermitted("Usuario", "ADMIN");
        }

        Usuario updated = this.usuarioService.update(id,usuario);
        return ResponseEntity.ok(UsuarioMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id){
        this.usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{usuarioId}/idoso")
    public ResponseEntity<IdosoResponse> saveIdoso(@PathVariable @Positive Long usuarioId,
                                                   @RequestBody @Valid IdosoRequest idosoRequest,
                                                   @RequestParam String parentesco,
                                                   @RequestParam @Positive Integer ordemEscalonamento){
        this.usuarioService.findById(usuarioId);
        Idoso idoso = new Idoso();
        idoso.setNome(idosoRequest.nome());
        idoso.setDataNascimento(
                idosoRequest.dataNascimento().format(CommonVariables.dateFormatter)
        );
        idoso.setTelefone(idosoRequest.telefone());
        idoso.setEndereco(idosoRequest.endereco());
        idoso.setModo(idosoRequest.modo());

        Idoso save = this.idosoService.cadastrarComContato(usuarioId,idoso,parentesco,ordemEscalonamento);

        return ResponseEntity.created(URI.create("/api/idosos/" + save.getId())).body(IdosoMapper.toResponse(idoso));
    }

    @GetMapping("/{id}/withAlertas")
    public ResponseEntity<UsuarioWithAlertasResponse> findWithAlertasById(@PathVariable @Positive Long id){
        Usuario usuario = this.usuarioService.findWithAlertasById(id);
        return ResponseEntity.ok(UsuarioMapper.toResponseWithAlertas(usuario));
    }

    @GetMapping("/{id}/withAll")
    public ResponseEntity<UsuarioWithAllResponse> findWithAllById(@PathVariable @Positive Long id){
        Usuario usuario = this.usuarioService.findWithAllById(id);
        return ResponseEntity.ok(UsuarioMapper.toResponseWithAll(usuario));
    }

    @GetMapping("/{id}/withContatos")
    public ResponseEntity<UsuarioWithContatosResponse> findWithContatosById(@PathVariable @Positive Long id){
        Usuario usuario = this.usuarioService.findWithContatosById(id);
        return ResponseEntity.ok(UsuarioMapper.toResponseWithContatos(usuario));
    }
}
