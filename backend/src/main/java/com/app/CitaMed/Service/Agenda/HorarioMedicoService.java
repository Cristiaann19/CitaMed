package com.app.CitaMed.Service.Agenda;
import com.app.CitaMed.DTO.HorarioMedicoDTO;
import com.app.CitaMed.Model.Administrativo.Consultorio;
import com.app.CitaMed.Model.Agenda.HorarioMedico;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Repository.Administrativo.ConsultorioRepository;
import com.app.CitaMed.Repository.Agenda.HorarioMedicoRepository;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class HorarioMedicoService {
    private final HorarioMedicoRepository horarioMedicoRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultorioRepository consultorioRepository;

    public List<HorarioMedico> findByMedico(Long medicoId) {
        return horarioMedicoRepository.findByMedicoIdAndActivoTrue(medicoId);
    }

    @Transactional
    public String save(HorarioMedicoDTO dto) {
        Medico medico = medicoRepository.findById(dto.getMedicoId()).orElse(null);
        if (medico == null) return "Médico no encontrado";

        Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId()).orElse(null);
        if (consultorio == null) return "Consultorio no encontrado";

        boolean cruce = horarioMedicoRepository
                .existsByMedicoIdAndDiaAndActivoTrueAndHoraInicioLessThanAndHoraFinGreaterThan(
                        dto.getMedicoId(), dto.getDia(), dto.getHoraFin(), dto.getHoraInicio());
        if (cruce) return "El médico ya tiene un horario que se cruza en ese día y hora";

        HorarioMedico horario = new HorarioMedico();
        horario.setMedico(medico);
        horario.setConsultorio(consultorio);
        horario.setDia(dto.getDia());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());
        horario.setActivo(true);
        horarioMedicoRepository.save(horario);
        return "Horario registrado correctamente";
    }

    @Transactional
    public String toggleActivo(Long id) {
        HorarioMedico horario = horarioMedicoRepository.findById(id).orElse(null);
        if (horario == null) return "Horario no encontrado";
        horario.setActivo(!horario.isActivo());
        horarioMedicoRepository.save(horario);
        return horario.isActivo() ? "Horario activado" : "Horario desactivado";
    }

    @Transactional
    public String update(Long id, HorarioMedicoDTO dto) {
        HorarioMedico horario = horarioMedicoRepository.findById(id).orElse(null);
        if (horario == null) return "Horario no encontrado";

        Medico medico = medicoRepository.findById(dto.getMedicoId()).orElse(null);
        if (medico == null) return "Médico no encontrado";

        Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId()).orElse(null);
        if (consultorio == null) return "Consultorio no encontrado";

        horario.setMedico(medico);
        horario.setConsultorio(consultorio);
        horario.setDia(dto.getDia());
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFin(dto.getHoraFin());

        horarioMedicoRepository.save(horario);
        return "Horario actualizado correctamente";
    }

    public List<HorarioMedico> findAll() {
        return horarioMedicoRepository.findAll();
    }
}
