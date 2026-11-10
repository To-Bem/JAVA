package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    Optional<Contato> findByIdosoId(Long idosoId);

    Optional<Contato> findByUsuarioId(Long usuarioId);

    Page<Contato> findByParentesco(String parentesco, Pageable pageable);
}
