package com.app.CitaMed.Repository.Agenda;

import com.app.CitaMed.DTO.EspecialidadDTO;
import com.app.CitaMed.DTO.UltimaCitaDTO;
import com.app.CitaMed.Enums.EstadoCita;
import com.app.CitaMed.Model.Agenda.Cita;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByMedicoId(Long medicoId);

    List<Cita> findByPacienteId(Long pacienteId);

    boolean existsByMedicoIdAndFechaHoraAndEstadoNot(Long medicoId, LocalDateTime fechaHora, EstadoCita estado);

    boolean existsByPacienteIdAndFechaHoraAndEstadoNot(Long pacienteId, LocalDateTime fechaHora, EstadoCita estado);

    Long countByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);

    Long countByFechaHoraBetweenAndEstado(LocalDateTime inicio, LocalDateTime fin, EstadoCita estado);

    @Query("SELECT COUNT(DISTINCT c.paciente.id) FROM Cita c WHERE c.fechaHora BETWEEN :inicio AND :fin")
    Long pacientesActivos(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);

    @Query("SELECT new com.app.CitaMed.DTO.UltimaCitaDTO(CONCAT(c.paciente.nombre,' ', c.paciente.apellidoPaterno), " +
            "CONCAT(c.medico.nombre,' ', c.medico.apellidoPaterno), c.medico.especialidad.nombre, c.fechaHora, c.estado) FROM Cita c ORDER BY c.fechaHora DESC")
    List<UltimaCitaDTO> ultimasCitas(Pageable pageable);

    @Query("SELECT new com.app.CitaMed.DTO.EspecialidadDTO(c.medico.especialidad.nombre, COUNT(c)) FROM Cita c GROUP BY " +
            "c.medico.especialidad.nombre ORDER BY COUNT(c) DESC")
    List<EspecialidadDTO> citasPorEspecialidad();

    @Query(value = "SELECT DATE_FORMAT(citas.fecha_hora,'%h:%i %p') AS hora, " +
            "CONCAT(pacientes.nombre,' ', pacientes.apellido_paterno,' ', pacientes.apellido_materno) AS paciente, " +
            "CONCAT(especialidades.nombre,' - ', medicos.nombre,' ', medicos.apellido_paterno) AS detalle " +
            "FROM citas " +
            "INNER JOIN medicos ON citas.medico_id = medicos.id " +
            "INNER JOIN pacientes ON citas.paciente_id = pacientes.id " +
            "INNER JOIN especialidades ON medicos.especialidad_id = especialidades.id " +
            "WHERE YEAR(citas.fecha_hora) = :anio " +
            "AND MONTH(citas.fecha_hora) = :mes " +
            "AND DAY(citas.fecha_hora) = :dia " +
            "ORDER BY citas.fecha_hora", nativeQuery = true)
    List<Object[]> agendaHoyNative(@Param("anio") int anio, @Param("mes") int mes, @Param("dia") int dia);
}
