package com.app.CitaMed.Controller.Medico;
import com.app.CitaMed.DTO.DiagnosticoDTO;
import com.app.CitaMed.Model.Medico.Diagnostico;
import com.app.CitaMed.Service.Medico.DiagnosticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/diagnostico")
@RequiredArgsConstructor
public class DiagnosticoController {
    private final DiagnosticoService diagnosticoService;

    @GetMapping
    public ResponseEntity<List<Diagnostico>> findAll() {
        return ResponseEntity.ok(diagnosticoService.findAll());
    }

    @GetMapping("/cita/{citaId}")
    public ResponseEntity<Diagnostico> findByCita(@PathVariable Long citaId) {
        Diagnostico d = diagnosticoService.findByCita(citaId);
        if (d == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(d);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody DiagnosticoDTO dto) {
        String resultado = diagnosticoService.save(dto);
        if (!resultado.equals("Diagnóstico registrado correctamente"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PutMapping("/cita/{citaId}")
    public ResponseEntity<String> update(@PathVariable Long citaId, @RequestBody DiagnosticoDTO dto) {
        String resultado = diagnosticoService.update(citaId, dto);
        if (resultado.equals("Diagnóstico no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String resultado = diagnosticoService.delete(id);
        if (resultado.equals("Diagnóstico no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }
}
