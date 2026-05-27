package com.app.CitaMed.Controller.Medico;
import com.app.CitaMed.Model.Medico.Especialidad;
import com.app.CitaMed.Service.Medico.EspecialidadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/especialidad")
@RequiredArgsConstructor

public class EspecialidadController {
    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<Especialidad>> findAll() {
        return ResponseEntity.ok(especialidadService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid Especialidad especialidad) {
        String resultado = especialidadService.save(especialidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid Especialidad dto) {
        String resultado = especialidadService.update(id, dto);
        if (resultado.equals("Especialidad no encontrada"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String resultado = especialidadService.delete(id);
        if (resultado.equals("Especialidad no encontrada"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }
}
