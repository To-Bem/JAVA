package br.com.fiap_tdsps.to_bem_api.service;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import org.springframework.data.domain.Page;

public interface ContatoService {

    Contato save(Contato contato);

    void delete(Long id);

    Contato update(Long id, Contato update);

    Page<Contato> findAll(Integer page, Integer size);

    Contato findById(Long id);

    Contato findByIdosoId(Long idosoId);

    Contato findByUsuarioId(Long usuarioId);

    Contato findFirstByIdosoId(Long idosoId);

    Contato findFirstByIdosoIdAndOrdemEscalonamentoGreaterThanOrderByOrdemEscalonamentoAsc(
            Long idosoId, Integer ordemAtual);
}
