package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import br.com.fiap_tdsps.to_bem_api.repository.AlertaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlertaServiceImpl implements AlertaService{

    private final AlertaRepository repository;

    public AlertaServiceImpl(AlertaRepository repository){
        this.repository = repository;
    }

    @Override
    public Alerta save(Alerta alerta) {
        return repository.save(alerta);
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public Alerta update(Long id, Alerta update) {
        findById(id);
        update.setId(id);
        return repository.save(update);
    }

    @Override
    public Page<Alerta> findAll(Integer page, Integer size) {
        return repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Alerta findById(Long id) {
        return repository.findById(id).orElseThrow(/*() -> erro */);
    }

    @Override
    public Page<Alerta> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return repository.findByIdosoId(idosoId, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Alerta> findByStatus(AlertaStatus status, Integer page, Integer size) {
        return repository.findByStatus(status, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Alerta> findByNivel(AlertaNivel nivel, Integer page, Integer size) {
        return repository.findByNivel(nivel,Pageable.ofSize(size).withPage(page));
    }
}
