package com.app.CitaMed.Controller.Administrativo;
import com.app.CitaMed.DTO.ConsultorioDTO;
import com.app.CitaMed.Model.Administrativo.Consultorio;
import com.app.CitaMed.Service.Administrativo.ConsultorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/consultorio")
@RequiredArgsConstructor

public class ConsultorioController {
    private final ConsultorioService consultorioService;

    @GetMapping
    public ResponseEntity<List<Consultorio>> findAll() {
        return ResponseEntity.ok(consultorioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consultorio> findById(@PathVariable Long id) {
        Consultorio c = consultorioService.findById(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(c);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Consultorio>> findDisponibles() {
        return ResponseEntity.ok(consultorioService.findDisponibles());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid ConsultorioDTO dto) {
        String resultado = consultorioService.save(dto);
        if (!resultado.equals("Consultorio registrado correctamente"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid ConsultorioDTO dto) {
        String resultado = consultorioService.update(id, dto);
        if (resultado.equals("Consultorio no encontrado") || resultado.equals("Área no encontrada"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/{id}/disponibilidad")
    public ResponseEntity<String> toggleDisponible(@PathVariable Long id) {
        String resultado = consultorioService.toggleDisponible(id);
        if (resultado.equals("Consultorio no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }
}
