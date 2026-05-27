package com.app.CitaMed.Controller.Agenda;

import com.app.CitaMed.DTO.HorarioMedicoDTO;
import com.app.CitaMed.Model.Agenda.HorarioMedico;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Repository.Agenda.HorarioMedicoRepository;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Service.Agenda.HorarioMedicoService;
import com.app.CitaMed.Util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/horarioMedico")
@RequiredArgsConstructor

public class HorarioMedicoController {
    private final HorarioMedicoService horarioMedicoService;
    private final MedicoRepository medicoRepository;
    private final HorarioMedicoRepository horarioMedicoRepository;

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<HorarioMedico>> findByMedico(@PathVariable Long medicoId) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !medico.getId().equals(medicoId))
                return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(horarioMedicoService.findByMedico(medicoId));
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid HorarioMedicoDTO dto) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !medico.getId().equals(dto.getMedicoId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = horarioMedicoService.save(dto);
        if (!resultado.equals("Horario registrado correctamente"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            HorarioMedico horario = horarioMedicoRepository.findById(id).orElse(null);
            if (medico == null || horario == null || !horario.getMedico().getId().equals(medico.getId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = horarioMedicoService.toggleActivo(id);
        if (resultado.equals("Horario no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid HorarioMedicoDTO dto) {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null || !medico.getId().equals(dto.getMedicoId()))
                return ResponseEntity.status(403).build();
        }
        String resultado = horarioMedicoService.update(id, dto);
        if (resultado.equals("Horario no encontrado"))
            return ResponseEntity.notFound().build();
        if (!resultado.equals("Horario actualizado correctamente"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping
    public ResponseEntity<List<HorarioMedico>> findAll() {
        if (SecurityUtil.isMedico()) {
            Medico medico = medicoRepository.findByUsuarioUserName(SecurityUtil.getCurrentUsername()).orElse(null);
            if (medico == null) return ResponseEntity.status(403).build();
            return ResponseEntity.ok(horarioMedicoService.findByMedico(medico.getId()));
        }
        return ResponseEntity.ok(horarioMedicoService.findAll());
    }
}
