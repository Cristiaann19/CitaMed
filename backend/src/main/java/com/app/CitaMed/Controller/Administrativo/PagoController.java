package com.app.CitaMed.Controller.Administrativo;
import com.app.CitaMed.DTO.PagoDTO;
import com.app.CitaMed.Model.Administrativo.Pago;
import com.app.CitaMed.Service.Administrativo.PagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pago")
@RequiredArgsConstructor

public class PagoController {
    private final PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<Pago>> findAll() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> findById(@PathVariable Long id) {
        Pago pago = pagoService.findById(id);
        if (pago == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(pago);
    }

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid PagoDTO dto) {
        String resultado = pagoService.save(dto);
        if (!resultado.equals("Pago registrado correctamente"))
            return ResponseEntity.badRequest().body(resultado);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<String> anular(@PathVariable Long id) {
        String resultado = pagoService.anular(id);
        if (resultado.equals("Pago no encontrado"))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(resultado);
    }
}
