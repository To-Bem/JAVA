package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import br.com.fiap_tdsps.to_bem_api.repository.AlertaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.fiap_tdsps.to_bem_api.model.*;

@Service
public class AlertaServiceImpl implements AlertaService{

    private final AlertaRepository repository;

    public AlertaServiceImpl(AlertaRepository repository){
        this.repository = repository;
    }

    @Override
    public Alerta save(Alerta alerta) {
        return this.repository.save(alerta);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Alerta update(Long id, Alerta update) {
        Alerta alerta = this.repository.findForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("alerta", id));

        if (alerta.getStatus() == AlertaStatus.RESOLVIDO && update.getStatus() != AlertaStatus.RESOLVIDO) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.CONFLICT, "Um alerta resolvido nao pode ser reaberto. Crie outro alerta.");
        }
        alerta.setTipo(update.getTipo());
        alerta.setNivel(update.getNivel());

        alerta.setStatus(update.getStatus());
        if (alerta.getStatus() == AlertaStatus.RESOLVIDO)
            alerta.setPrazoRespostaEm(null);

        return this.repository.save(alerta);
    }

    @Override
    public Page<Alerta> findAll(Integer page, Integer size) {
        return this.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Alerta> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Alerta findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("alerta", id));
    }

    @Override
    public Page<Alerta> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return this.findByIdosoId(idosoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Alerta> findByIdosoId(Long idosoId, Pageable pageable) {
        return this.repository.findByIdosoId(idosoId, pageable);
    }

    @Override
    public Page<Alerta> findByStatusAndOtherId(AlertaStatus status,Long idosoId,Long usuarioId, Integer page, Integer size) {
        return this.findByStatusAndOtherId(status, idosoId, usuarioId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Alerta> findByStatusAndOtherId(AlertaStatus status,Long idosoId,Long usuarioId, Pageable pageable) {
        return this.repository.findByStatusAndOtherId(status, idosoId, usuarioId, pageable);
    }

    @Override
    public Page<Alerta> findByNivelAndOtherId(AlertaNivel nivel, Long idosoId,Long usuarioId , Integer page, Integer size) {
        return this.findByNivelAndOtherId(nivel, idosoId, usuarioId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Alerta> findByNivelAndOtherId(AlertaNivel nivel, Long idosoId,Long usuarioId , Pageable pageable) {
        return this.repository.findByNivelAndOtherId(nivel, idosoId, usuarioId,pageable);
    }

    @Override
    public Page<Alerta> findByUsuarioId(Long usuarioId, Integer page, Integer size) {
        return this.findByUsuarioId(usuarioId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Alerta> findByUsuarioId(Long usuarioId, Pageable pageable) {
        return this.repository.findByUsuarioId(usuarioId,pageable);
    }
    @Override
    public Page<Alerta> findByOtherFilter(Long idosoId, Long usuarioId, AlertaStatus status, AlertaNivel nivel, Pageable pageable) {
        Specification<Alerta> filtros = (root, query, builder) -> builder.conjunction();
        if (idosoId != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("idoso").get("id"), idosoId));
        if (usuarioId != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("usuario").get("id"), usuarioId));
        if (status != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("status"), status));
        if (nivel != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("nivel"), nivel));
        return this.repository.findAll(filtros, pageable);
    }
}
