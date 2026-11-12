package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import org.springframework.data.domain.Page;

public interface UsuarioService {

    Usuario save(Usuario usuario);

    void delete(Long id);

    Usuario update(Long id, Usuario update);

    Page<Usuario> findAll(Integer page, Integer size);

    Usuario findById(Long id);

    Page<Usuario> findByPapel(UsuarioPapel papel, Integer page, Integer size);

    Usuario findWithAlertasById(Long id);

    Usuario findWithContatosById(Long id);

    Usuario findWithAllById(Long id);
}
