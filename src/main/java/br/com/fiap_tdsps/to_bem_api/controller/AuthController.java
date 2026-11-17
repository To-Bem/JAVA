package br.com.fiap_tdsps.to_bem_api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import br.com.fiap_tdsps.to_bem_api.dto.ErrorCustomResponse;
import br.com.fiap_tdsps.to_bem_api.dto.ValidationErrorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Autenticação")
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
    @Operation(summary = "Cadastra usuário", description = "Cria um usuário MEMBER. Acesso público.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Criado.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "409", description = "Conflito com os dados ou com o estado atual.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponse> register(@RequestBody @Valid RegisterRequest request) {
        Usuario created = usuarioService.register(request.name(), request.email(), request.password());
        return ResponseEntity.created(URI.create("/api/usuarios/" + created.getId()))
                .body(UsuarioMapper.toResponse(created));
    }

    @Operation(summary = "Cadastra administrador ou gerente", description = "Exclusivo para ADMIN. Aceita ADMIN ou MANAGER.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Criado.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "409", description = "Conflito com os dados ou com o estado atual.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/privilegiado")
    public ResponseEntity<UsuarioResponse> registerPrivilegiado(
            @RequestBody @Valid RegisterPrivilegiadoRequest request) {
        Usuario usuario = this.usuarioService.registerPrivilegiado(
                request.name(), request.email(), request.password(), request.papel(), request.role());
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.getId()))
                .body(UsuarioMapper.toResponse(usuario));
    }

    @SecurityRequirements
    @Operation(summary = "Autentica usuário", description = "Retorna um JWT para uso no botão Authorize.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
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

    @Operation(summary = "Consulta usuário autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sucesso.", useReturnTypeSchema = true,
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados ou parâmetros inválidos.",
                content = @Content(mediaType = "application/json", schema = @Schema(oneOf = {ErrorCustomResponse.class, ValidationErrorResponse.class}))),
        @ApiResponse(responseCode = "403", description = "Usuário sem permissão.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "401", description = "Autenticação ausente ou inválida.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorCustomResponse.class)))
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me() {
        return ResponseEntity.ok(UsuarioMapper.toResponse(SecurityUtils.currentUsuario()));
    }
}
