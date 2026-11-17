package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.repository.ContatoRepository;
import org.springframework.boot.data.metrics.DefaultRepositoryTagsProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.fiap_tdsps.to_bem_api.model.*;

@Service
public class ContatoServiceImpl implements ContatoService{

    private final ContatoRepository repository;

    public ContatoServiceImpl(ContatoRepository repository){
        this.repository = repository;
    }


    @Override
    public Contato save(Contato contato) {
        validarOrdem(contato);
        return this.repository.save(contato);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Contato update(Long id, Contato update) {
        update.setId(id);
        validarOrdem(update);
        return this.repository.save(update);
    }

    @Override
    public Page<Contato> findAll(Integer page, Integer size) {
        return this.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Contato> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Contato findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("contato", id));
    }

    @Override
    public Page<Contato> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return this.findByIdosoId(idosoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Contato> findByIdosoId(Long idosoId, Pageable pageable) {
        return this.repository.findByIdosoId(idosoId, pageable);
    }

    @Override
    public Page<Contato> findByUsuarioId(Long usuarioId, Integer page, Integer size) {
        return this.findByUsuarioId(usuarioId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Contato> findByUsuarioId(Long usuarioId, Pageable pageable) {
        return this.repository.findByUsuarioId(usuarioId, pageable);
    }

    public Contato findFirstByIdosoId(Long idosoId) {
        return this.repository.findFirstByIdosoIdOrderByOrdemEscalonamentoAsc(idosoId)
                .orElseThrow(() -> new ResourceNotFoundException("contato", idosoId));
    }
    private void validarOrdem(Contato contato) {
        if (contato.getOrdemEscalonamento() == null || contato.getOrdemEscalonamento() <= 0) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,
                    "A ordem de escalonamento deve ser positiva.");
        }
        boolean existe = contato.getId() == null
                ? this.repository.existsByIdosoIdAndOrdemEscalonamento(contato.getIdoso().getId(), contato.getOrdemEscalonamento())
                : this.repository.existsByIdosoIdAndOrdemEscalonamentoAndIdNot(contato.getIdoso().getId(), contato.getOrdemEscalonamento(), contato.getId());
        if (existe) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,
                "Ja existe um contato nessa ordem para o idoso.");
    }
    @Override
    public Page<Contato> findByOtherFilter(Long idosoId, Long usuarioId, Pageable pageable) {
        Specification<Contato> filtros = (root, query, builder) -> builder.conjunction();
        if (idosoId != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("idoso").get("id"), idosoId));
        if (usuarioId != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("usuario").get("id"), usuarioId));
        return this.repository.findAll(filtros, pageable);
    }
}
