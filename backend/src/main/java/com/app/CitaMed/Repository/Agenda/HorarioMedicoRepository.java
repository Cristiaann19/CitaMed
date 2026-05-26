package com.app.CitaMed.Repository.Agenda;

import com.app.CitaMed.Enums.DiaSemana;
import com.app.CitaMed.Model.Agenda.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {
    List<HorarioMedico> findByMedicoIdAndActivoTrue(Long medicoId);

    @Query("SELECT h FROM HorarioMedico h JOIN FETCH h.consultorio WHERE h.medico.id IN :medicoIds AND h.dia = :dia AND h.activo = true")
    List<HorarioMedico> findByMedicoIdInAndDiaAndActivoTrue(@Param("medicoIds") List<Long> medicoIds, @Param("dia") DiaSemana dia);

    boolean existsByMedicoIdAndDiaAndActivoTrueAndHoraInicioLessThanAndHoraFinGreaterThan(Long medicoId, DiaSemana dia, LocalTime horaFin, LocalTime horaInicio);
}
