package br.com.fiap_tdsps.to_bem_api.service;

import org.springframework.data.domain.Pageable;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import org.springframework.data.domain.Page;

public interface ContatoService {

    Contato save(Contato contato);

    void delete(Long id);

    Contato update(Long id, Contato update);

    Page<Contato> findAll(Integer page, Integer size);

    Contato findById(Long id);

    Page<Contato> findByIdosoId(Long idosoId, Integer page, Integer size);

    Page<Contato> findByUsuarioId(Long usuarioId,Integer page, Integer size);

    Contato findFirstByIdosoId(Long idosoId);



    Page<Contato> findAll(Pageable pageable);
    Page<Contato> findByIdosoId(Long idosoId, Pageable pageable);
    Page<Contato> findByUsuarioId(Long usuarioId, Pageable pageable);
    Page<Contato> findByOtherFilter(Long idosoId, Long usuarioId, Pageable pageable);
}
