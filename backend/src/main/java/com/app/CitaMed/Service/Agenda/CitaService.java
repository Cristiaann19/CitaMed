package com.app.CitaMed.Service.Agenda;
import com.app.CitaMed.DTO.CitaDetalleDTO;
import com.app.CitaMed.DTO.CitaDTO;
import com.app.CitaMed.Enums.DiaSemana;
import com.app.CitaMed.Enums.EstadoCita;
import com.app.CitaMed.Enums.EstadoPago;
import com.app.CitaMed.Enums.MetodoPago;
import com.app.CitaMed.Model.Administrativo.Pago;
import com.app.CitaMed.Model.Agenda.Cita;
import com.app.CitaMed.Model.Agenda.HorarioMedico;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Model.Paciente.Paciente;
import com.app.CitaMed.Repository.Administrativo.PagoRepository;
import com.app.CitaMed.Repository.Agenda.CitaRepository;
import com.app.CitaMed.Repository.Agenda.HorarioMedicoRepository;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Repository.Paciente.PacienteRepository;
import com.app.CitaMed.Service.MicroServicios.EmailService;
import com.app.CitaMed.Util.HorarioValidator;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final EmailService emailService;
    private final PagoRepository pagoRepository;
    private final HorarioMedicoRepository horarioMedicoRepository;

    public List<CitaDetalleDTO> findAllDetalle() {
        return citaRepository.findAllDetalle();
    }

    public List<CitaDetalleDTO> findDetalleByMedico(Long medicoId) {
        return citaRepository.findDetalleByMedicoId(medicoId);
    }

    public List<CitaDetalleDTO> findDetalleByPaciente(Long pacienteId) {
        return citaRepository.findDetalleByPacienteId(pacienteId);
    }

    public List<CitaDetalleDTO> findDetalleByMedicoAndPaciente(Long medicoId, Long pacienteId) {
        return citaRepository.findDetalleByMedicoIdAndPacienteId(medicoId, pacienteId);
    }

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    public Cita findById(Long id) {
        return citaRepository.findById(id).orElse(null);
    }

    public List<Cita> findByMedico(Long medicoId) {
        return citaRepository.findByMedicoId(medicoId);
    }

    public List<Cita> findByPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    private DiaSemana toDiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.LUNES;
            case TUESDAY -> DiaSemana.MARTES;
            case WEDNESDAY -> DiaSemana.MIERCOLES;
            case THURSDAY -> DiaSemana.JUEVES;
            case FRIDAY -> DiaSemana.VIERNES;
            case SATURDAY -> DiaSemana.SABADO;
            case SUNDAY -> DiaSemana.DOMINGO;
        };
    }

    private String validarHorarioMedico(Long medicoId, LocalDateTime fechaHora) {
        DiaSemana diaCita = toDiaSemana(fechaHora.getDayOfWeek());
        LocalTime horaCita = fechaHora.toLocalTime();

        List<HorarioMedico> horarios = horarioMedicoRepository.findByMedicoIdAndActivoTrue(medicoId);

        if (horarios.isEmpty()) {
            return "El médico no tiene horarios configurados";
        }

        boolean horarioValido = horarios.stream().anyMatch(h ->
            h.getDia() == diaCita
            && !horaCita.isBefore(h.getHoraInicio())
            && !horaCita.isAfter(h.getHoraFin())
        );

        if (!horarioValido) {
            return "El médico no atiende en la fecha y hora seleccionadas";
        }

        return null;
    }

    @Transactional
    public String save(CitaDTO dto) {
        Paciente paciente = pacienteRepository.findById(dto.getPacienteId()).orElse(null);
        if (paciente == null) return "Paciente no encontrado";

        Medico medico = medicoRepository.findById(dto.getMedicoId()).orElse(null);
        if (medico == null) return "Médico no encontrado";

        if (!medico.isActivo()) return "El médico no está activo";

        if (medico.getConsultorio() == null) return "El médico no tiene un consultorio asignado";

        try { HorarioValidator.validar(dto.getFechaHora()); } catch (IllegalArgumentException e) { return e.getMessage(); }

        String errorHorario = validarHorarioMedico(dto.getMedicoId(), dto.getFechaHora());
        if (errorHorario != null) return errorHorario;

        LocalDateTime inicio = dto.getFechaHora();
        LocalDateTime fin = inicio.plusHours(1);
        if (citaRepository.countOverlapByMedico(dto.getMedicoId(), EstadoCita.CANCELADA, inicio.minusHours(1), fin) > 0)
            return "El médico ya tiene una cita en ese horario";

        if (citaRepository.countOverlapByPaciente(dto.getPacienteId(), EstadoCita.CANCELADA, inicio.minusHours(1), fin) > 0)
            return "El paciente ya tiene una cita en ese horario";

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setConsultorio(medico.getConsultorio());
        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstado(EstadoCita.PROGRAMADA);
        citaRepository.save(cita);

        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMonto(80.00);
        pago.setMetodoPago(MetodoPago.EFECTIVO);
        pago.setEstado(EstadoPago.PENDIENTE);
        pago.setFechaPago(LocalDateTime.now());
        pagoRepository.save(pago);

        try { emailService.enviarConfirmacion(cita); } catch (Exception ignored) {}

        return "Cita registrada correctamente";
    }

    @Transactional
    public String cancelar(Long id) {
        Cita cita = citaRepository.findById(id).orElse(null);
        if (cita == null) return "Cita no encontrada";

        if (cita.getEstado() == EstadoCita.ATENDIDA)
            return "No se puede cancelar una cita ya atendida";

        if (cita.getEstado() == EstadoCita.CANCELADA)
            return "La cita ya está cancelada";

        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);

        Optional<Pago> pagoOpt = pagoRepository.findByCitaId(id);
        pagoOpt.ifPresent(p -> {
            if (p.getEstado() == EstadoPago.PENDIENTE) {
                p.setEstado(EstadoPago.ANULADO);
                pagoRepository.save(p);
            }
        });

        try { emailService.enviarCancelacion(cita); } catch (Exception ignored) {}
        return "Cita cancelada correctamente";
    }

    @Transactional
    public String completar(Long id) {
        Cita cita = citaRepository.findById(id).orElse(null);
        if (cita == null) return "Cita no encontrada";
        if (cita.getEstado() == EstadoCita.CANCELADA)
            return "No se puede completar una cita cancelada";
        cita.setEstado(EstadoCita.ATENDIDA);
        citaRepository.save(cita);
        return "Cita completada correctamente";
    }

    @Transactional
    public String noAsistio(Long id) {
        Cita cita = citaRepository.findById(id).orElse(null);
        if (cita == null) return "Cita no encontrada";
        cita.setEstado(EstadoCita.NO_ASISTIO);
        citaRepository.save(cita);
        return "Cita marcada como no asistida";
    }

    @Transactional
    public String reprogramar(Long id, LocalDateTime nuevaFechaHora) {
        Cita cita = citaRepository.findById(id).orElse(null);
        if (cita == null) return "Cita no encontrada";

        if (cita.getEstado() != EstadoCita.PROGRAMADA)
            return "Solo se pueden reprogramar citas en estado PROGRAMADA";

        try { HorarioValidator.validar(nuevaFechaHora); } catch (IllegalArgumentException e) { return e.getMessage(); }

        String errorHorario = validarHorarioMedico(cita.getMedico().getId(), nuevaFechaHora);
        if (errorHorario != null) return errorHorario;

        LocalDateTime fin = nuevaFechaHora.plusHours(1);
        if (citaRepository.countOverlapByMedico(cita.getMedico().getId(), EstadoCita.CANCELADA, nuevaFechaHora.minusHours(1), fin) > 0)
            return "El médico ya tiene una cita en ese horario";

        LocalDateTime fechaAnterior = cita.getFechaHora();
        cita.setFechaHora(nuevaFechaHora);
        citaRepository.save(cita);

        try { emailService.enviarReprogramacion(cita, fechaAnterior); } catch (Exception ignored) {}
        return "Cita reprogramada correctamente";
    }

    @Transactional
    public String actualizar(Long id, CitaDTO dto) {
        Cita cita = citaRepository.findById(id).orElse(null);
        if (cita == null) return "Cita no encontrada";

        if (cita.getEstado() != EstadoCita.PROGRAMADA)
            return "Solo se pueden editar citas en estado PROGRAMADA";

        if (dto.getFechaHora() != null && !dto.getFechaHora().equals(cita.getFechaHora())) {
            try { HorarioValidator.validar(dto.getFechaHora()); } catch (IllegalArgumentException e) { return e.getMessage(); }
            String errorHorario = validarHorarioMedico(cita.getMedico().getId(), dto.getFechaHora());
            if (errorHorario != null) return errorHorario;
            LocalDateTime inicio = dto.getFechaHora();
            LocalDateTime fin = inicio.plusHours(1);
            if (citaRepository.countOverlapByMedico(cita.getMedico().getId(), EstadoCita.CANCELADA, inicio.minusHours(1), fin) > 0)
                return "El médico ya tiene una cita en ese horario";
            cita.setFechaHora(dto.getFechaHora());
        }

        if (dto.getMotivoConsulta() != null && !dto.getMotivoConsulta().isBlank())
            cita.setMotivoConsulta(dto.getMotivoConsulta());

        citaRepository.save(cita);
        return "Cita actualizada correctamente";
    }

    @Transactional
    public String eliminar(Long id) {
        Cita cita = citaRepository.findById(id).orElse(null);
        if (cita == null) return "Cita no encontrada";

        if (cita.getPago() != null) {
            pagoRepository.delete(cita.getPago());
        }

        citaRepository.delete(cita);
        return "Cita eliminada correctamente";
    }
}
