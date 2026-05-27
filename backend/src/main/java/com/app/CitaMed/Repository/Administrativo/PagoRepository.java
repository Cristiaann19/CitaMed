package com.app.CitaMed.Repository.Administrativo;
import com.app.CitaMed.DTO.UltimoPagoDTO;
import com.app.CitaMed.Model.Administrativo.Pago;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface PagoRepository extends JpaRepository<Pago, Long> {
    boolean existsByCitaId(Long citaId);
    Optional<Pago> findByCitaId(Long citaId);
    @Query("SELECT COALESCE(SUM(p.monto),0) FROM Pago p WHERE p.fechaPago BETWEEN :inicio AND :fin")
    Double ingresos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    @Query("SELECT COALESCE(SUM(p.monto),0) FROM Pago p WHERE p.cita.medico.id = :medicoId AND p.fechaPago BETWEEN :inicio AND :fin")
    Double ingresosPorMedico(@Param("medicoId") Long medicoId, @Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    @Query("SELECT new com.app.CitaMed.DTO.UltimoPagoDTO(CONCAT(p.cita.paciente.nombre,' '," +
    "p.cita.paciente.apellidoPaterno), p.metodoPago, p.cita.id, p.monto, p.estado) " +
    "FROM Pago p ORDER BY p.fechaPago DESC limit 4")
    List<UltimoPagoDTO> ultimosPagos(Pageable pageable);
}
