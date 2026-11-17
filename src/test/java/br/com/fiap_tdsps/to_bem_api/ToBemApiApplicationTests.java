package br.com.fiap_tdsps.to_bem_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "app.jwt.secret=MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE=",
    "app.jwt.expiration-ms=3600000",
    "app.admin.email=admin-teste@example.com",
    "app.admin.password=senha-apenas-teste",
    "spring.datasource.url=jdbc:h2:mem:contexto;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.show-sql=false"
})
@org.springframework.test.context.TestExecutionListeners(
    listeners = org.springframework.test.context.support.DependencyInjectionTestExecutionListener.class,
    mergeMode = org.springframework.test.context.TestExecutionListeners.MergeMode.REPLACE_DEFAULTS)
class ToBemApiApplicationTests {

    @org.springframework.beans.factory.annotation.Autowired
    br.com.fiap_tdsps.to_bem_api.service.UsuarioService usuarioService;
    @org.springframework.beans.factory.annotation.Autowired
    br.com.fiap_tdsps.to_bem_api.repository.UsuarioRepository usuarioRepository;
    @org.springframework.beans.factory.annotation.Autowired
    org.springframework.security.authentication.AuthenticationManager authenticationManager;
    @org.springframework.beans.factory.annotation.Autowired
    br.com.fiap_tdsps.to_bem_api.security.JwtService jwtService;

    @Test
    void autenticaAdministradorInicialEValidaJwt() {
        var authentication = authenticationManager.authenticate(
            new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                " ADMIN-TESTE@example.com ", "senha-apenas-teste"));
        var usuario = (br.com.fiap_tdsps.to_bem_api.model.Usuario) authentication.getPrincipal();
        org.junit.jupiter.api.Assertions.assertEquals(
            br.com.fiap_tdsps.to_bem_api.model.Role.ADMIN, usuario.getRole());
        String token = jwtService.generateToken(usuario);
        org.junit.jupiter.api.Assertions.assertTrue(jwtService.isTokenValid(token, usuario));
        org.junit.jupiter.api.Assertions.assertThrows(
            org.springframework.security.authentication.BadCredentialsException.class,
            () -> authenticationManager.authenticate(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                    usuario.getEmail(), "senha-incorreta")));
    }

    @Test
    void normalizaCadastroPublicoERejeitaEmailDuplicado() {
        String email = java.util.UUID.randomUUID() + "@example.com";
        var usuario = usuarioService.register("Teste", " " + email.toUpperCase(java.util.Locale.ROOT) + " ", "senha-teste");
        org.junit.jupiter.api.Assertions.assertEquals(email, usuario.getEmail());
        org.junit.jupiter.api.Assertions.assertEquals(
            br.com.fiap_tdsps.to_bem_api.model.Role.MEMBER, usuario.getRole());
        org.junit.jupiter.api.Assertions.assertThrows(
            org.springframework.web.server.ResponseStatusException.class,
            () -> usuarioService.register("Duplicado", email, "senha-teste"));
    }

    @Test
    void contextLoads() {
	}

}
