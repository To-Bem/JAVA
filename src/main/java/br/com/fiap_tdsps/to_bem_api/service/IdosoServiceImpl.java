package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.exceptions.ResourceNotFoundException;
import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import br.com.fiap_tdsps.to_bem_api.repository.IdosoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IdosoServiceImpl implements  IdosoService{

    private final IdosoRepository repository;

    public IdosoServiceImpl(IdosoRepository repository){
        this.repository = repository;
    }

    @Override
    public Idoso save(Idoso idoso) {
        return this.repository.save(idoso);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Idoso update(Long id, Idoso update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Idoso> findAll(Integer page, Integer size) {
        return this.repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Idoso findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithAllById(Long id) {
        return this.repository.findWithAllById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithContatosById(Long id) {
        return this.repository.findWithContatosById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithAlertasById(Long id) {
        return this.repository.findWithAlertasById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithRotinasById(Long id) {
        return this.repository.findWithRotinasById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }

    @Override
    public Idoso findWithDispositivosById(Long id) {
        return this.repository.findWithDispositivosById(id).orElseThrow(() -> new ResourceNotFoundException("idoso", id));
    }
}