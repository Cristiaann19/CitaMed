package com.app.CitaMed.Service.Administrativo;
import com.app.CitaMed.DTO.ReservaDTO;
import com.app.CitaMed.DTO.SlotDisponibleDTO;
import com.app.CitaMed.Enums.*;
import com.app.CitaMed.Model.Administrativo.Consultorio;
import com.app.CitaMed.Model.Administrativo.Pago;
import com.app.CitaMed.Model.Agenda.Cita;
import com.app.CitaMed.Model.Agenda.HorarioMedico;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Model.Paciente.HistorialMedico;
import com.app.CitaMed.Model.Paciente.Paciente;
import com.app.CitaMed.Repository.Administrativo.ConsultorioRepository;
import com.app.CitaMed.Repository.Administrativo.PagoRepository;
import com.app.CitaMed.Repository.Agenda.CitaRepository;
import com.app.CitaMed.Repository.Agenda.HorarioMedicoRepository;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Repository.Paciente.HistorialMedicoRepository;
import com.app.CitaMed.Repository.Paciente.PacienteRepository;
import com.app.CitaMed.Service.MicroServicios.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ReservaService {
    private final CitaRepository citaRepository;
    private final PagoRepository pagoRepository;
    private final PacienteRepository pacienteRepository;
    private final HistorialMedicoRepository historialMedicoRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultorioRepository consultorioRepository;
    private final HorarioMedicoRepository horarioMedicoRepository;
    private final EmailService emailService;

    public List<SlotDisponibleDTO> obtenerSlotsDisponibles(Long especialidadId, String fechaStr) {
        LocalDateTime fechaBase = LocalDateTime.parse(fechaStr + "T12:00:00");
        int diaSemana = fechaBase.getDayOfWeek().getValue();
        DiaSemana dia = mapearDia(diaSemana);
        if (dia == null) return Collections.emptyList();

        List<Medico> medicos = medicoRepository.findByEspecialidadIdAndActivoTrue(especialidadId);
        if (medicos.isEmpty()) return Collections.emptyList();

        List<Long> medicoIds = medicos.stream().map(Medico::getId).collect(Collectors.toList());

        List<HorarioMedico> todosHorarios = horarioMedicoRepository
                .findByMedicoIdInAndDiaAndActivoTrue(medicoIds, dia);

        if (todosHorarios.isEmpty()) return Collections.emptyList();

        Map<Long, List<HorarioMedico>> horariosPorMedico = todosHorarios.stream()
                .collect(Collectors.groupingBy(h -> h.getMedico().getId()));

        LocalDateTime inicioDia = fechaBase.toLocalDate().atStartOfDay();
        LocalDateTime finDia = inicioDia.plusDays(1);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        List<SlotDisponibleDTO> resultado = new ArrayList<>();

        for (Medico medico : medicos) {
            List<HorarioMedico> horarios = horariosPorMedico.get(medico.getId());
            if (horarios == null) continue;

            List<Cita> citasDelDia = citaRepository
                    .findByMedicoIdAndFechaHoraBetweenAndEstadoNot(
                            medico.getId(), inicioDia, finDia, EstadoCita.CANCELADA);

            Set<String> horasOcupadas = citasDelDia.stream()
                    .map(c -> c.getFechaHora().format(fmt))
                    .collect(Collectors.toSet());

            for (HorarioMedico horario : horarios) {
                List<String> slots = generarSlots(horario.getHoraInicio(), horario.getHoraFin(), fechaStr);
                List<String> slotsLibres = slots.stream()
                        .filter(s -> !horasOcupadas.contains(s.substring(0, 16)))
                        .collect(Collectors.toList());

                if (!slotsLibres.isEmpty()) {
                    resultado.add(new SlotDisponibleDTO(
                            medico.getId(),
                            medico.getNombre(),
                            medico.getApellidoPaterno(),
                            medico.getGenero().name(),
                            medico.getEspecialidad().getNombre(),
                            horario.getConsultorio().getId(),
                            slotsLibres
                    ));
                }
            }
        }
        return resultado;
    }

    @Transactional
    public String procesarReserva(ReservaDTO dto) {
        Paciente paciente = pacienteRepository.findByDniAndActivoTrue(dto.getDni());
        if (paciente == null) {
            // check if there's an inactive patient with same DNI
            Paciente posibleInactivo = pacienteRepository.findByDni(dto.getDni());
            if (posibleInactivo != null) {
                // reactivate and update
                posibleInactivo.setActivo(true);
                posibleInactivo.setNombre(dto.getNombre().toUpperCase());
                posibleInactivo.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
                posibleInactivo.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
                posibleInactivo.setTelefono(dto.getTelefono());
                posibleInactivo.setEmail(dto.getEmail());
                posibleInactivo.setDireccion(dto.getDireccion());
                posibleInactivo.setFechaNacimiento(dto.getFechaNacimiento());
                posibleInactivo.setGenero(Genero.valueOf(dto.getGenero()));
                posibleInactivo.setGrupoSanguineo(GrupoSanguineo.valueOf(dto.getGrupoSanguineo()));
                paciente = pacienteRepository.save(posibleInactivo);
            } else {
                paciente = new Paciente();
                paciente.setNombre(dto.getNombre().toUpperCase());
                paciente.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
                paciente.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
                paciente.setDni(dto.getDni());
                paciente.setTelefono(dto.getTelefono());
                paciente.setEmail(dto.getEmail());
                paciente.setDireccion(dto.getDireccion());
                paciente.setFechaNacimiento(dto.getFechaNacimiento());
                paciente.setGenero(Genero.valueOf(dto.getGenero()));
                paciente.setGrupoSanguineo(GrupoSanguineo.valueOf(dto.getGrupoSanguineo()));
                pacienteRepository.save(paciente);
                HistorialMedico historial = new HistorialMedico();
                historial.setPaciente(paciente);
                historialMedicoRepository.save(historial);
            }
        }

        Medico medico = medicoRepository.findById(dto.getMedicoId()).orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (!medico.isActivo()) throw new RuntimeException("El médico no está activo");

        if (medico.getConsultorio() == null) throw new RuntimeException("El médico no tiene un consultorio asignado");

        Consultorio consultorio = medico.getConsultorio();

        LocalDateTime fechaHora = LocalDateTime.parse(dto.getFechaHora(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

        if (citaRepository.existsByMedicoIdAndFechaHoraAndEstadoNot(medico.getId(), fechaHora, EstadoCita.CANCELADA)) {
            throw new RuntimeException("El horario seleccionado ya no está disponible");
        }

        Cita cita = new Cita();
        cita.setPaciente(paciente);
        cita.setMedico(medico);
        cita.setConsultorio(consultorio);
        cita.setFechaHora(fechaHora);
        cita.setMotivoConsulta(dto.getMotivoConsulta());
        cita.setEstado(EstadoCita.PROGRAMADA);
        citaRepository.save(cita);

        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMonto(80.00);
        pago.setMetodoPago(MetodoPago.EFECTIVO);
        pago.setEstado(EstadoPago.PAGADO);
        pago.setFechaPago(LocalDateTime.now());
        pagoRepository.save(pago);

        emailService.enviarConfirmacion(cita);
        return "Reserva registrada correctamente";
    }

    private DiaSemana mapearDia(int dayOfWeek) {
        return switch (dayOfWeek) {
            case 1 -> DiaSemana.LUNES;
            case 2 -> DiaSemana.MARTES;
            case 3 -> DiaSemana.MIERCOLES;
            case 4 -> DiaSemana.JUEVES;
            case 5 -> DiaSemana.VIERNES;
            case 6 -> DiaSemana.SABADO;
            case 7 -> DiaSemana.DOMINGO;
            default -> null;
        };
    }

    private List<String> generarSlots(LocalTime inicio, LocalTime fin, String fecha) {
        List<String> slots = new ArrayList<>();
        int current = inicio.getHour() * 60 + inicio.getMinute();
        int end = fin.getHour() * 60 + fin.getMinute();
        while (current + 30 <= end) {
            int h = current / 60;
            int m = current % 60;
            slots.add(String.format("%sT%02d:%02d:00", fecha, h, m));
            current += 30;
        }
        return slots;
    }
}
