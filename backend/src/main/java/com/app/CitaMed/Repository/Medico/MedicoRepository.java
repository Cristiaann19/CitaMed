package com.app.CitaMed.Repository.Medico;
import com.app.CitaMed.DTO.MedicoActivoDTO;
import com.app.CitaMed.Model.Medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    long count();
    boolean existsByDni(String dni);

    List<Medico> findByEspecialidadIdAndActivoTrue(Long especialidadId);

    @Query("""
            SELECT new com.app.CitaMed.DTO.MedicoActivoDTO(
            SUBSTRING(c.medico.nombre,1,1),
            CONCAT(c.medico.nombre,' ',c.medico.apellidoPaterno),
            c.medico.numeroColegiatura,
            c.medico.especialidad.nombre,
            COUNT(c)
            )
            FROM Cita c
            GROUP BY c.medico.id,
            c.medico.nombre,
            c.medico.apellidoPaterno,
            c.medico.numeroColegiatura,
            c.medico.especialidad.nombre
            ORDER BY COUNT(c) DESC
            """)
    List<MedicoActivoDTO> medicosActivos();

    Optional<Medico> findByUsuarioUserName(String userName);
}
