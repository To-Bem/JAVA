package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Role;
import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import br.com.fiap_tdsps.to_bem_api.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository repository, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    @Override
    public Usuario save(Usuario usuario) {
        validarEmail(usuario);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        if (usuario.getRole() == null) {
            usuario.setRole(Role.MEMBER);
        }
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
        update.setPassword(passwordEncoder.encode(update.getPassword()));
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
    public boolean existsByEmail(String email) {
        return this.repository.existsByEmailIgnoreCase(email.trim());
    }

    @Override
    public Usuario register(String name, String email, String rawPassword) {
        Usuario usuario = new Usuario();
        usuario.setNome(name);
        usuario.setEmail(email);
        usuario.setPassword(rawPassword);
        usuario.setRole(Role.MEMBER);
        return this.save(usuario);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Usuario registerPrivilegiado(String name, String email, String rawPassword,
                                        UsuarioPapel papel, Role role) {
        if (role != Role.ADMIN && role != Role.MANAGER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Este cadastro permite apenas ADMIN ou MANAGER.");
        }
        if (papel == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Papel e obrigatorio.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(name);
        usuario.setEmail(email);
        this.validarEmail(usuario);
        usuario.setPassword(this.passwordEncoder.encode(rawPassword));
        usuario.setPapel(papel);
        usuario.setRole(role);
        return this.repository.save(usuario);
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
