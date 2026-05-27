package com.app.CitaMed.Controller.Administrativo;
import com.app.CitaMed.DTO.EmpleadoDTO;
import com.app.CitaMed.Model.Administrativo.Empleado;
import com.app.CitaMed.Service.Administrativo.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/empleado")
@RequiredArgsConstructor

public class EmpleadoController {
    private final EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<Empleado>> findAll() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> findById(@PathVariable Long id) {
        Empleado e = empleadoService.findById(id);
        if (e == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(e);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Empleado>> findActivos() {
        return ResponseEntity.ok(empleadoService.findActivos());
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid EmpleadoDTO dto) {
        String resultado = empleadoService.save(dto);
        if (!resultado.equals("Empleado registrado correctamente"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid EmpleadoDTO dto) {
        String resultado = empleadoService.update(id, dto);
        if (resultado.equals("Empleado no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<String> toggleActivo(@PathVariable Long id) {
        String resultado = empleadoService.toggleActivo(id);
        if (resultado.equals("Empleado no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }
}
