package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import br.com.fiap_tdsps.to_bem_api.model.UsuarioPapel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByEmailIgnoreCase(String email);

    Page<Usuario> findByPapel(UsuarioPapel papel, Pageable pageable);

    @EntityGraph(attributePaths = "alertas")
    Optional<Usuario> findWithAlertasById(Long id);

    @EntityGraph(attributePaths = "contatos")
    Optional<Usuario> findWithContatosById(Long id);

    Optional<Usuario> findWithAllById(Long id);
}
