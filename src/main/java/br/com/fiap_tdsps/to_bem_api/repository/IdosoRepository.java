package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Idoso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdosoRepository extends JpaRepository<Idoso, Long> {
}
