package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    boolean existsByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = "alertas")
    Optional<Usuario> findWithAlertasById(Long id);

    @EntityGraph(attributePaths = "contatos")
    Optional<Usuario> findWithContatosById(Long id);

    Optional<Usuario> findByEmailIgnoreCase(String email);
}
