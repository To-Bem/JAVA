package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Rotina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RotinaRepository extends JpaRepository<Rotina,Long> {

    Page<Rotina> findByIdosoId(Long idosoId, Pageable pageable);
}
