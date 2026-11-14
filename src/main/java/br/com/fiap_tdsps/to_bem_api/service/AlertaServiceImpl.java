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
        return this.repository.save(alerta);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Alerta update(Long id, Alerta update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Alerta> findAll(Integer page, Integer size) {
        return this.repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Alerta findById(Long id) {
        return this.repository.findById(id).orElseThrow(/*() -> erro */);
    }

    @Override
    public Page<Alerta> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return this.repository.findByIdosoId(idosoId, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Alerta> findByStatusAndOtherId(AlertaStatus status,Long idosoId,Long usuarioId, Integer page, Integer size) {
        return this.repository.findByStatusAndOtherId(status, idosoId, usuarioId, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Alerta> findByNivelAndOtherId(AlertaNivel nivel, Long idosoId,Long usuarioId , Integer page, Integer size) {
        return this.repository.findByNivelAndOtherId(nivel, idosoId, usuarioId,Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Alerta> findByUsuarioId(Long usuarioId, Integer page, Integer size) {
        return this.repository.findByUsuarioId(usuarioId,Pageable.ofSize(size).withPage(page));
    }
}
