package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Evento;
import br.com.fiap_tdsps.to_bem_api.repository.EventoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class EventoServiceImpl implements EventoService{

    private final EventoRepository repository;

    public EventoServiceImpl(EventoRepository repository){
        this.repository = repository;
    }

    @Override
    public Evento save(Evento evento) {
        return repository.save(evento);
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public Evento update(Long id, Evento update) {
        findById(id);
        update.setId(id);
        return repository.save(update);
    }

    @Override
    public Page<Evento> findAll(Integer page, Integer size) {
        return repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Evento findById(Long id) {
        return repository.findById(id).orElseThrow( /* () -> erro */);
    }

    @Override
    public Page<Evento> findByDispositivoId(Long dispositivoId, Integer page, Integer size) {
        return repository.findByDispositivoId(dispositivoId, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Page<Evento> findByTipo(String tipo, Integer page, Integer size) {
        return repository.findByTipo(tipo, Pageable.ofSize(size).withPage(page));
    }
}
