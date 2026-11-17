package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import org.springframework.data.domain.Page;

public interface ContatoService {

    Contato save(Contato contato);

    void delete(Long id);

    Contato update(Long id, Contato update);

    Contato findById(Long id);

    Contato findFirstByIdosoId(Long idosoId);

    Page<Contato> findAll(Pageable pageable);
    Page<Contato> findByOtherFilter(Long idosoId, Long usuarioId, Pageable pageable);
}
