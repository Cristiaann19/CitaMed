package com.app.CitaMed.Controller.Agenda;
import com.app.CitaMed.DTO.CitaDTO;
import com.app.CitaMed.DTO.CitaDetalleDTO;
import com.app.CitaMed.Model.Agenda.Cita;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Service.Agenda.CitaService;
import com.app.CitaMed.Util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/cita")
@RequiredArgsConstructor

public class CitaController {

    private final CitaService citaService;
    private final MedicoRepository medicoRepository;

    @GetMapping
    public ResponseEntity<List<CitaDetalleDTO>> findAll() {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null) return ResponseEntity.status(403).build();
            return ResponseEntity.ok(citaService.findDetalleByMedico(medico.getId()));
        }
        return ResponseEntity.ok(citaService.findAllDetalle());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> findById(@PathVariable Long id) {
        Cita cita = citaService.findById(id);
        if (cita == null) return ResponseEntity.notFound().build();
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !cita.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(cita);
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<CitaDetalleDTO>> findByMedico(@PathVariable Long medicoId) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !medico.getId().equals(medicoId))
                return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(citaService.findDetalleByMedico(medicoId));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaDetalleDTO>> findByPaciente(@PathVariable Long pacienteId) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null) return ResponseEntity.status(403).build();
            return ResponseEntity.ok(citaService.findDetalleByMedicoAndPaciente(medico.getId(), pacienteId));
        }
        return ResponseEntity.ok(citaService.findDetalleByPaciente(pacienteId));
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid CitaDTO dto) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null) return ResponseEntity.status(403).build();
            dto.setMedicoId(medico.getId());
        }
        String resultado = citaService.save(dto);
        if (resultado.contains("no encontrado") || resultado.contains("no está") || resultado.contains("ya tiene"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizar(@PathVariable Long id, @RequestBody @Valid CitaDTO dto) {
        if (SecurityUtil.isMedico()) {
            Cita cita = citaService.findById(id);
            if (cita == null) return ResponseEntity.notFound().build();
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !cita.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = citaService.actualizar(id, dto);
        if (resultado.equals("Cita no encontrada")) return ResponseEntity.notFound().build();
        if (resultado.contains("Solo se pueden") || resultado.contains("ya tiene") || resultado.contains("no encontrado") || resultado.contains("no está"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/{id}/reprogramar")
    public ResponseEntity<String> reprogramar(
            @PathVariable Long id,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime nuevaFecha) {
        if (SecurityUtil.isMedico()) {
            Cita cita = citaService.findById(id);
            if (cita == null) return ResponseEntity.notFound().build();
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !cita.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = citaService.reprogramar(id, nuevaFecha);
        if (resultado.equals("Cita no encontrada")) return ResponseEntity.notFound().build();
        if (resultado.contains("Solo se pueden") || resultado.contains("ya tiene"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelar(@PathVariable Long id) {
        if (SecurityUtil.isMedico()) {
            Cita cita = citaService.findById(id);
            if (cita == null) return ResponseEntity.notFound().build();
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !cita.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = citaService.cancelar(id);
        if (resultado.equals("Cita no encontrada")) return ResponseEntity.notFound().build();
        if (resultado.contains("No se puede") || resultado.contains("ya está"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/{id}/completar")
    public ResponseEntity<String> completar(@PathVariable Long id) {
        if (SecurityUtil.isMedico()) {
            Cita cita = citaService.findById(id);
            if (cita == null) return ResponseEntity.notFound().build();
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !cita.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = citaService.completar(id);
        if (resultado.equals("Cita no encontrada")) return ResponseEntity.notFound().build();
        if (resultado.contains("No se puede")) return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/{id}/no-asistio")
    public ResponseEntity<String> noAsistio(@PathVariable Long id) {
        if (SecurityUtil.isMedico()) {
            Cita cita = citaService.findById(id);
            if (cita == null) return ResponseEntity.notFound().build();
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !cita.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = citaService.noAsistio(id);
        if (resultado.equals("Cita no encontrada")) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        if (SecurityUtil.isMedico()) {
            Cita cita = citaService.findById(id);
            if (cita == null) return ResponseEntity.notFound().build();
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !cita.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = citaService.eliminar(id);
        if (resultado.equals("Cita no encontrada"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("mensaje", resultado));
    }
}
