package com.app.CitaMed.Controller.Medico;
import com.app.CitaMed.DTO.MedicoDTO;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Service.Medico.MedicoService;
import com.app.CitaMed.Util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medico")
@RequiredArgsConstructor

public class MedicoController {
    private final MedicoService medicoService;
    private final MedicoRepository medicoRepository;

    @GetMapping
    public ResponseEntity<List<Medico>> findAll() {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null) return ResponseEntity.status(403).build();
            return ResponseEntity.ok(List.of(medico));
        }
        return ResponseEntity.ok(medicoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> findById(@PathVariable Long id) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !medico.getId().equals(id))
                return ResponseEntity.status(403).build();
        }
        Medico medico = medicoService.findById(id);
        if (medico == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(medico);
    }

    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<Medico>> findByEspecialidad(@PathVariable Long especialidadId) {
        return ResponseEntity.ok(medicoService.findByEspecialidad(especialidadId));
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid MedicoDTO dto) {
        String resultado = medicoService.save(dto);
        if (!resultado.equals("Médico registrado correctamente"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PatchMapping("/{medicoId}/especialidad/{especialidadId}")
    public ResponseEntity<String> updateEspecialidad(
            @PathVariable Long medicoId,
            @PathVariable Long especialidadId) {
        String resultado = medicoService.updateEspecialidad(medicoId, especialidadId);
        if (resultado.equals("Médico no encontrado") || resultado.equals("Especialidad no encontrada"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid MedicoDTO dto) {
        String resultado = medicoService.update(id, dto);

        if (resultado.equals("Médico no encontrado") || resultado.equals("Especialidad no encontrada")) {
            return ResponseEntity.notFound().build();
        }
        if (resultado.equals("Ya existe un médico con ese DNI")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", resultado);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id) {

        String resultado = medicoService.cambiarEstado(id);

        if (resultado.equals("Médico no encontrado")) {
            return ResponseEntity.notFound().build();
        }

        Map<String, String> response = new HashMap<>();
        response.put("mensaje", resultado);

        return ResponseEntity.ok(response);
    }
}
