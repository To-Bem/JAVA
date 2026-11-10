package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoStatus;
import br.com.fiap_tdsps.to_bem_api.model.DispositivoTipo;
import br.com.fiap_tdsps.to_bem_api.repository.DispositivoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DispositivoServiceImpl implements DispositivoService{

    private final DispositivoRepository repository;

    public DispositivoServiceImpl(DispositivoRepository repository){
        this.repository = repository;
    }

    @Override
    public Dispositivo save(Dispositivo dispositivo) {
        return repository.save(dispositivo);
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public Dispositivo update(Long id, Dispositivo update) {
        findById(id);
        update.setId(id);
        return repository.save(update);
    }

    @Override
    public Page<Dispositivo> findAll(Integer page, Integer size) {
        return repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Dispositivo findById(Long id) {
        return repository.findById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Page<Dispositivo> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return repository.findByIdosoId(idosoId, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Dispositivo> findByTipo(DispositivoTipo tipo, Integer page, Integer size) {
        return repository.findByTipo(tipo,Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Dispositivo> findByComodo(String comodo, Integer page, Integer size) {
        return repository.findByComodo(comodo, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Dispositivo> findByStatus(DispositivoStatus status, Integer page, Integer size) {
        return repository.findByStatus(status,Pageable.ofSize(size).withPage(page));
    }
}
