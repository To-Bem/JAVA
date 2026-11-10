package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Rotina;
import br.com.fiap_tdsps.to_bem_api.repository.RotinaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RotinaServiceImpl implements RotinaService{

    private final RotinaRepository repository;

    public RotinaServiceImpl(RotinaRepository repository){
        this.repository = repository;
    }

    @Override
    public Rotina save(Rotina rotina) {
        return repository.save(rotina);
    }

    @Override
    public void delte(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public Rotina update(Long id, Rotina update) {
        findById(id);
        update.setId(id);
        return repository.save(update);
    }

    @Override
    public Page<Rotina> findAll(Integer page, Integer size) {
        return repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Rotina findById(Long id) {
        return repository.findById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Page<Rotina> findByIdosoId(Long idosoId, Integer page, Integer size) {
        return repository.findByIdosoId(idosoId,Pageable.ofSize(size).withPage(page));
    }
}
