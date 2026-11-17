package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import org.springframework.data.domain.Page;

public interface UsuarioService {

    Usuario save(Usuario usuario);

    void delete(Long id);

    Usuario update(Long id, Usuario update);

    Usuario findById(Long id);

    Usuario findWithAlertasById(Long id);

    Usuario findWithContatosById(Long id);

    Usuario findWithAllById(Long id);

    Page<Usuario> findAll(Pageable pageable);
    boolean existsByEmail(String email);

    Usuario register(String name, String email, String rawPassword);

    Usuario registerPrivilegiado(String name, String email, String rawPassword,
                                 UsuarioPapel papel, br.com.fiap_tdsps.to_bem_api.model.Role role);
}
