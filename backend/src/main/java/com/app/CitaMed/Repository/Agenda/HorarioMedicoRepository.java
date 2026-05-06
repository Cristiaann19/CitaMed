package com.app.CitaMed.Repository.Agenda;

import com.app.CitaMed.Enums.DiaSemana;
import com.app.CitaMed.Model.Agenda.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository

public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Long> {
    List<HorarioMedico> findByMedicoIdAndActivoTrue(Long medicoId);

    boolean existsByMedicoIdAndDiaAndActivoTrueAndHoraInicioLessThanAndHoraFinGreaterThan(Long medicoId, DiaSemana dia, LocalTime horaFin, LocalTime horaInicio);
}
