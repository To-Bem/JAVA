package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import br.com.fiap_tdsps.to_bem_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;

    public UsuarioServiceImpl(UsuarioRepository repository){
        this.repository = repository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        return this.repository.save(usuario);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Usuario update(Long id, Usuario update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Usuario> findAll(Integer page, Integer size) {
        return this.repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Usuario findById(Long id) {
        return this.repository.findById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Page<Usuario> findByPapel(UsuarioPapel papel, Integer page, Integer size) {
        return this.repository.findByPapel(papel, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Usuario findWithAlertasById(Long id) {
        return this.repository.findWithAlertasById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Usuario findWithContatosById(Long id) {
        return this.repository.findWithContatosById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Usuario findWithAllById(Long id) {
        return this.repository.findWithAllById(id).orElseThrow(/* () -> erro */);
    }
}
