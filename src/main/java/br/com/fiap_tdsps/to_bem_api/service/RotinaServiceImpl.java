package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
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
        return this.repository.save(rotina);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Rotina update(Long id, Rotina update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Rotina> findAll(Pageable pageable) {
        return this.repository.findAll(pageable);
    }

    @Override
    public Rotina findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("rotina", id));
    }

    @Override
    public Page<Rotina> findByIdosoId(Long idosoId, Pageable pageable) {
        return this.repository.findByIdosoId(idosoId,pageable);
    }
}
