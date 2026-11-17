package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.PageRequest;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.Evento;
import br.com.fiap_tdsps.to_bem_api.repository.EventoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.com.fiap_tdsps.to_bem_api.model.*;

@Service
public class EventoServiceImpl implements EventoService{

    private final EventoRepository repository;

    public EventoServiceImpl(EventoRepository repository){
        this.repository = repository;
    }

    @Override
    public Evento save(Evento evento) {
        return this.repository.save(evento);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Evento update(Long id, Evento update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Evento> findAll(Integer page, Integer size) {
        return this.findAll(PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Evento> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Evento findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("evento", id));
    }

    @Override
    public Page<Evento> findByDispositivoId(Long dispositivoId, Integer page, Integer size) {
        return this.findByDispositivoId(dispositivoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Evento> findByDispositivoId(Long dispositivoId, Pageable pageable) {
        return this.repository.findByDispositivoId(dispositivoId, pageable);
    }

    @Override
    public Page<Evento> findByTipoAndDispositivoId(String tipo,Long dispositivoId, Integer page, Integer size) {
        return this.findByTipoAndDispositivoId(tipo, dispositivoId, PageRequest.of(page, size, Sort.by("id")));
    }

    @Override
    public Page<Evento> findByTipoAndDispositivoId(String tipo,Long dispositivoId, Pageable pageable) {
        return this.repository.findByTipoAndDispositivoId(tipo, dispositivoId, pageable);
    }

    @Override
    public Evento findWithAlertasById(Long id) {
        return this.repository.findWithAlertasById(id).orElseThrow(() -> new ResourceNotFoundException("evento", id));
    }

    @Override
    public Page<Evento> findByOtherFilter(Long dispositivoId, String tipo, Pageable pageable) {
        Specification<Evento> filtros = (root, query, builder) -> builder.conjunction();
        if (dispositivoId != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("dispositivo").get("id"), dispositivoId));
        if (tipo != null) filtros = filtros.and((root, query, builder) -> builder.equal(root.get("tipo"), tipo));
        return this.repository.findAll(filtros, pageable);
    }
}
