package br.com.fiap_tdsps.to_bem_api.service;

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
        return repository.save(idoso);
    }

    @Override
    public void delete(Long id) {
        repository.delete(findById(id));
    }

    @Override
    public Idoso update(Long id, Idoso update) {
        findById(id);
        update.setId(id);
        return repository.save(update);
    }

    @Override
    public Page<Idoso> findAll(Integer page, Integer size) {
        return repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Idoso findById(Long id) {
        return repository.findById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Idoso findWithAllById(Long id) {
        return repository.findWithAllById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Contato findWithContatosById(Long id) {
        return repository.findWithContatosById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Contato findWithAlertasById(Long id) {
        return repository.findWithAlertasById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Contato findWithRotinasById(Long id) {
        return repository.findWithRotinasById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Contato findWithDispositivosById(Long id) {
        return repository.findWithDispositivosById(id).orElseThrow(/* () -> erro */);
    }
}