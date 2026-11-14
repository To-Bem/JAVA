package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import br.com.fiap_tdsps.to_bem_api.repository.DispositivoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return this.repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Dispositivo findById(Long id) {
        return this.repository.findById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Page<Dispositivo> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return this.repository.findByIdosoId(idosoId, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Dispositivo> findByTipoAndIdosoId(DispositivoTipo tipo,Long idosoId, Integer page, Integer size) {
        return this.repository.findByTipoAndIdosoId(tipo, idosoId,Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Dispositivo> findByComodoAndIdosoId(String comodo, Long idosoId, Integer page, Integer size) {
        return this.repository.findByComodoAndIdosoId(comodo, idosoId, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Dispositivo> findByStatusAndIdosoId(DispositivoStatus status, Long idosoId, Integer page, Integer size) {
        return this.repository.findByStatusAndIdosoId(status, idosoId,Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Dispositivo findWithEventosById(Long id) {
        return this.repository.findWithEventosById(id).orElseThrow(/* () -> erro */);
    }
}
