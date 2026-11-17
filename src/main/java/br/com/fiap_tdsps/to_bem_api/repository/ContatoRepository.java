package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Contato> {

    boolean existsByIdosoIdAndOrdemEscalonamentoAndIdNot(Long idosoId, Integer ordemEscalonamento, Long id);

    boolean existsByIdosoIdAndOrdemEscalonamento(Long idosoId, Integer ordemEscalonamento);

    Page<Contato> findByIdosoId(Long idosoId, Pageable pageable);

    Page<Contato> findByUsuarioId(Long usuarioId, Pageable pageable);

    Optional<Contato> findFirstByIdosoIdOrderByOrdemEscalonamentoAsc(Long idosoId);

    Optional<Contato> findFirstByIdosoIdAndOrdemEscalonamentoGreaterThanOrderByOrdemEscalonamentoAsc(Long idosoId,Integer ordemAtual);
}
