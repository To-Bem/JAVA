package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import br.com.fiap_tdsps.to_bem_api.model.Dispositivo;
import br.com.fiap_tdsps.to_bem_api.repository.ContatoRepository;
import org.springframework.boot.data.metrics.DefaultRepositoryTagsProvider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ContatoServiceImpl implements ContatoService{

    private final ContatoRepository repository;

    public ContatoServiceImpl(ContatoRepository repository){
        this.repository = repository;
    }


    @Override
    public Contato save(Contato contato) {
        return this.repository.save(contato);
    }

    @Override
    public void delete(Long id) {
        this.repository.delete(findById(id));
    }

    @Override
    public Contato update(Long id, Contato update) {
        update.setId(id);
        return this.repository.save(update);
    }

    @Override
    public Page<Contato> findAll(Integer page, Integer size) {
        return this.repository.findAll(Pageable.ofSize(size).withPage(page));
    }

    @Override
    public Contato findById(Long id) {
        return this.repository.findById(id).orElseThrow(/* () -> erro */);
    }

    @Override
    public Contato findByIdosoId(Long idosoId) {
        return this.repository.findByIdosoId(idosoId).orElseThrow(/* () -> erro */);
    }

    @Override
    public Contato findByUsuarioId(Long usuarioId) {
        return this.repository.findByUsuarioId(usuarioId).orElseThrow(/* () -> erro */);
    }

    public Contato findFirstByIdosoId(Long idosoId) {
        return this.repository.findFirstByIdoso_IdOrderByOrdemEscalonamentoAsc(idosoId)
                .orElseThrow(/* () -> erro */);
    }

    @Override
    public Contato findFirstByIdosoIdAndOrdemEscalonamentoGreaterThanOrderByOrdemEscalonamentoAsc(Long idosoId, Integer ordemAtual) {
        return this.repository.findFirstByIdosoIdAndOrdemEscalonamentoGreaterThanOrderByOrdemEscalonamentoAsc(idosoId, ordemAtual).
                orElseThrow(/* () -> erro */);
    }
}
