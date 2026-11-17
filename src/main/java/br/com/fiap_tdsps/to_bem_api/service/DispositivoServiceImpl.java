package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import br.com.fiap_tdsps.to_bem_api.repository.DispositivoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.fiap_tdsps.to_bem_api.model.*;

import java.util.Optional;

@Service
public class DispositivoServiceImpl implements DispositivoService{

    private final DispositivoRepository repository;

    public DispositivoServiceImpl(DispositivoRepository repository){
        this.repository = repository;
    }

    @Override
    public Dispositivo save(Dispositivo dispositivo) {
        return this.repository.save(dispositivo);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Dispositivo update(Long id, Dispositivo update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Dispositivo> findAll(Integer page, Integer size) {
        return this.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Dispositivo> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Dispositivo findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("dispositivo", id));
    }

    @Override
    public Page<Dispositivo> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return this.findByIdosoId(idosoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Dispositivo> findByIdosoId(Long idosoId, Pageable pageable) {
        return this.repository.findByIdosoId(idosoId, pageable);
    }

    @Override
    public Page<Dispositivo> findByTipoAndIdosoId(DispositivoTipo tipo,Long idosoId, Integer page, Integer size) {
        return this.findByTipoAndIdosoId(tipo, idosoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Dispositivo> findByTipoAndIdosoId(DispositivoTipo tipo,Long idosoId, Pageable pageable) {
        return this.repository.findByTipoAndIdosoId(tipo, idosoId,pageable);
    }

    @Override
    public Page<Dispositivo> findByComodoAndIdosoId(String comodo, Long idosoId, Integer page, Integer size) {
        return this.findByComodoAndIdosoId(comodo, idosoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Dispositivo> findByComodoAndIdosoId(String comodo, Long idosoId, Pageable pageable) {
        return this.repository.findByComodoAndIdosoId(comodo, idosoId, pageable);
    }

    @Override
    public Page<Dispositivo> findByStatusAndIdosoId(DispositivoStatus status, Long idosoId, Integer page, Integer size) {
        return this.findByStatusAndIdosoId(status, idosoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Dispositivo> findByStatusAndIdosoId(DispositivoStatus status, Long idosoId, Pageable pageable) {
        return this.repository.findByStatusAndIdosoId(status, idosoId,pageable);
    }

    @Override
    public Dispositivo findWithEventosById(Long id) {
        return this.repository.findWithEventosById(id).orElseThrow(() -> new ResourceNotFoundException("dispositivo", id));
    }

    @Override
    public Dispositivo updateHeartBeatById(Long id, String updateHeartbeat) {
        Dispositivo dispositivo = this.findById(id);
        dispositivo.setUltimoHeartbeat(updateHeartbeat);
        return this.repository.save(dispositivo);
    }

    @Override
    public Page<Dispositivo> findByOtherFilter(Long idosoId, DispositivoTipo tipo, String comodo, DispositivoStatus status, Pageable pageable) {
        Specification<Dispositivo> filtros = (root, query, builder) -> builder.conjunction();
        if (idosoId != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("idoso").get("id"), idosoId));
        if (tipo != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("tipo"), tipo));
        if (comodo != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("comodo"), comodo));
        if (status != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("status"), status));
        return this.repository.findAll(filtros, pageable);
    }
}
