package br.com.fiap_tdsps.to_bem_api.controller;

import br.com.fiap_tdsps.to_bem_api.dto.AuthResponse;
import br.com.fiap_tdsps.to_bem_api.dto.LoginRequest;
import br.com.fiap_tdsps.to_bem_api.dto.RegisterRequest;
import br.com.fiap_tdsps.to_bem_api.dto.RegisterPrivilegiadoRequest;
import br.com.fiap_tdsps.to_bem_api.dto.UsuarioResponse;
import br.com.fiap_tdsps.to_bem_api.mapper.UsuarioMapper;
import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.security.JwtService;
import br.com.fiap_tdsps.to_bem_api.security.SecurityUtils;
import br.com.fiap_tdsps.to_bem_api.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UsuarioService usuarioService, AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @SecurityRequirements
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@RequestBody @Valid RegisterRequest request) {
        Usuario created = usuarioService.register(request.name(), request.email(), request.password());
        return ResponseEntity.created(URI.create("/api/usuarios/" + created.getId()))
                .body(UsuarioMapper.toResponse(created));
    }

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/privilegiado")
    public ResponseEntity<UsuarioResponse> registerPrivilegiado(
            @RequestBody @Valid RegisterPrivilegiadoRequest request) {
        Usuario usuario = this.usuarioService.registerPrivilegiado(
                request.name(), request.email(), request.password(), request.papel(), request.role());
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.getId()))
                .body(UsuarioMapper.toResponse(usuario));
    }

    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        Usuario usuario = (Usuario) authentication.getPrincipal();
        String token = jwtService.generateToken(usuario);

        return ResponseEntity.ok(
                new AuthResponse(token, "Bearer", jwtService.getExpirationSeconds(),
                usuario.getEmail(), usuario.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me() {
        return ResponseEntity.ok(UsuarioMapper.toResponse(SecurityUtils.currentUsuario()));
    }
}
