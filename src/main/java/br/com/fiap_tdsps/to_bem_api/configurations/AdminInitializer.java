package br.com.fiap_tdsps.to_bem_api.configurations;

import br.com.fiap_tdsps.to_bem_api.model.Role;
import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements ApplicationRunner {

    private final UsuarioService usuarioService;
    private final String adminEmail;
    private final String adminPassword;

    public AdminInitializer(UsuarioService usuarioService,
                            @Value("${app.admin.email}") String adminEmail,
                            @Value("${app.admin.password}") String adminPassword) {
        this.usuarioService = usuarioService;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioService.existsByEmail(adminEmail)) {
            return;
        }
        Usuario admin = new Usuario();
        admin.setNome("Administrador");
        admin.setEmail(adminEmail);
        admin.setPassword(adminPassword);
        admin.setRole(Role.ADMIN);
        usuarioService.save(admin);
    }
}
