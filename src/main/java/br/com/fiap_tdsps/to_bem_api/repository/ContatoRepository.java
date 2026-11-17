package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Contato> {

    boolean existsByIdosoIdAndUsuarioId(Long idosoId, Long usuarioId);

    boolean existsByIdosoIdAndOrdemEscalonamentoAndIdNot(Long idosoId, Integer ordemEscalonamento, Long id);

    boolean existsByIdosoIdAndOrdemEscalonamento(Long idosoId, Integer ordemEscalonamento);

    Optional<Contato> findFirstByIdosoIdOrderByOrdemEscalonamentoAsc(Long idosoId);

    Optional<Contato> findFirstByIdosoIdAndOrdemEscalonamentoGreaterThanOrderByOrdemEscalonamentoAsc(Long idosoId,Integer ordemAtual);
}
