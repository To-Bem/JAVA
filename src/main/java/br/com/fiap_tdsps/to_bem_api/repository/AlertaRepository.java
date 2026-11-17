package br.com.fiap_tdsps.to_bem_api.repository;

import br.com.fiap_tdsps.to_bem_api.model.Alerta;
import br.com.fiap_tdsps.to_bem_api.model.AlertaNivel;
import br.com.fiap_tdsps.to_bem_api.model.AlertaStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

public interface AlertaRepository extends JpaRepository<Alerta, Long>, org.springframework.data.jpa.repository.JpaSpecificationExecutor<Alerta> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Alerta a where a.id = :id")
    Optional<Alerta> findForUpdate(@Param("id") Long id);

    @Query("select a from Alerta a where a.status <> br.com.fiap_tdsps.to_bem_api.model.AlertaStatus.RESOLVIDO and a.enviadoEm is null order by a.id")
    Page<Alerta> findPendentesEnvio(Pageable pageable);

    @Query("select a from Alerta a where a.status <> br.com.fiap_tdsps.to_bem_api.model.AlertaStatus.RESOLVIDO and a.prazoRespostaEm <= :agora order by a.id")
    Page<Alerta> findVencidos(@Param("agora") LocalDateTime agora, Pageable pageable);

    Page<Alerta> findByIdosoId(Long idosoId, Pageable pageable);

    @Query("""
            SELECT a FROM Alerta a
            WHERE a.status = :status
            AND (a.idoso.id = :idosoId OR a.usuario.id = :usuarioId)
            """)
    Page<Alerta> findByStatusAndOtherId(@Param("status") AlertaStatus status,
                                        @Param("idosoId") Long idosoId,
                                        @Param("usuarioId") Long usuarioId,
                                        Pageable pageable);

    @Query("""
            SELECT a FROM Alerta a
            WHERE a.nivel = :nivel
            AND (a.idoso.id = :idosoId OR a.usuario.id = :usuarioId)
            """)
    Page<Alerta> findByNivelAndOtherId(@Param("nivel") AlertaNivel nivel,
                                       @Param("idosoId") Long idosoId,
                                       @Param("usuarioId") Long usuarioId,
                                       Pageable pageable);

    Page<Alerta> findByUsuarioId(Long usuarioId, Pageable pageable);
}
