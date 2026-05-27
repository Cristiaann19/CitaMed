package com.app.CitaMed.Controller.Paciente;
import com.app.CitaMed.DTO.PacienteDTO;
import com.app.CitaMed.Model.Medico.Medico;
import java.util.List;
import com.app.CitaMed.Model.Paciente.Paciente;
import com.app.CitaMed.Repository.Agenda.CitaRepository;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Service.Paciente.PacienteService;
import com.app.CitaMed.Util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/paciente")
@RequiredArgsConstructor

public class PacienteController {
    private final PacienteService pacienteService;
    private final MedicoRepository medicoRepository;
    private final CitaRepository citaRepository;

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false, defaultValue = "") String termino,
            @RequestParam(required = false, defaultValue = "false") boolean incluirInactivos
    ) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico != null) {
                List<Long> pacienteIds = citaRepository.findPacienteIdsByMedicoId(medico.getId());
                return ResponseEntity.ok(pacienteService.findAllByIds(pacienteIds));
            }
            return ResponseEntity.ok(List.of());
        }
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
            if (!termino.isEmpty()) {
                return ResponseEntity.ok(pacienteService.buscar(termino, incluirInactivos, pageable));
            }
            return ResponseEntity.ok(pacienteService.findAll(incluirInactivos, pageable));
        }
        return ResponseEntity.ok(pacienteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> findById(@PathVariable Long id) {
        Paciente p = pacienteService.findById(id);
        if (p == null) return ResponseEntity.notFound().build();
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !citaRepository.existsByMedicoIdAndPacienteId(medico.getId(), id)) {
                return ResponseEntity.status(403).build();
            }
        }
        return ResponseEntity.ok(p);
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<Paciente> findByDni(@PathVariable String dni) {
        Paciente p = pacienteService.findByDni(dni);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    @PostMapping
    public ResponseEntity<java.util.Map<String, String>> save(@RequestBody @Valid PacienteDTO dto) {
        String resultado = pacienteService.save(dto);
        if (!resultado.equals("Paciente registrado correctamente"))
            return ResponseEntity.badRequest().body(java.util.Map.of("mensaje", resultado));
        return ResponseEntity.status(HttpStatus.CREATED).body(java.util.Map.of("mensaje", resultado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<java.util.Map<String, String>> update(@PathVariable Long id, @RequestBody @Valid PacienteDTO dto) {
        String resultado = pacienteService.update(id, dto);
        if (resultado.equals("Paciente no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(java.util.Map.of("mensaje", resultado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<java.util.Map<String, String>> delete(@PathVariable Long id) {
        String resultado = pacienteService.delete(id);
        if (resultado.equals("Paciente no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(java.util.Map.of("mensaje", resultado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<java.util.Map<String, String>> toggleEstado(@PathVariable Long id) {
        String resultado = pacienteService.toggleEstado(id);
        if (resultado.equals("Paciente no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(java.util.Map.of("mensaje", resultado));
    }
}
