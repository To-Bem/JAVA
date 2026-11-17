package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import br.com.fiap_tdsps.to_bem_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository repository, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        validarEmail(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return this.repository.save(usuario);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Usuario update(Long id, Usuario update) {
        update.setId(id);
        validarEmail(update);
        update.setSenha(passwordEncoder.encode(update.getSenha()));
        return this.repository.save(update);
    }

    @Override
    public Page<Usuario> findAll(Integer page, Integer size) {
        return this.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Usuario> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Usuario findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("usuario", id));
    }

    @Override
    public Page<Usuario> findByPapel(UsuarioPapel papel, Integer page, Integer size) {
        return this.findByPapel(papel, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Usuario> findByPapel(UsuarioPapel papel, Pageable pageable) {
        return this.repository.findByPapel(papel, pageable);
    }

    @Override
    public Usuario findWithAlertasById(Long id) {
        return this.repository.findWithAlertasById(id).orElseThrow(() -> new ResourceNotFoundException("usuario", id));
    }

    @Override
    public Usuario findWithContatosById(Long id) {
        return this.repository.findWithContatosById(id).orElseThrow(() -> new ResourceNotFoundException("usuario", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findWithAllById(Long id) {
        Usuario usuario = this.findById(id);
        usuario.getContatos().size();
        usuario.getAlertas().size();
        return usuario;
    }
    private void validarEmail(Usuario usuario) {
        usuario.setEmail(usuario.getEmail().trim().toLowerCase(java.util.Locale.ROOT));
        boolean existe = usuario.getId() == null ? this.repository.existsByEmailIgnoreCase(usuario.getEmail())
                : this.repository.existsByEmailIgnoreCaseAndIdNot(usuario.getEmail(), usuario.getId());
        if (existe) throw new ResponseStatusException(HttpStatus.CONFLICT, "Este email ja esta cadastrado.");
    }
}
