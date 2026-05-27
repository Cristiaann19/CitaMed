package com.app.CitaMed.Controller.Util;
import com.app.CitaMed.DTO.MedicoPerfilDTO;
import com.app.CitaMed.DTO.ReservaDTO;
import com.app.CitaMed.DTO.SlotDisponibleDTO;
import com.app.CitaMed.Model.Medico.Especialidad;
import com.app.CitaMed.Service.Administrativo.ReservaService;
import com.app.CitaMed.Service.Medico.EspecialidadService;
import com.app.CitaMed.Service.Medico.MedicoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lading")
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class LadingPageController {

    private final MedicoService medicoService;
    private final EspecialidadService especialidadService;
    private final ReservaService reservaService;

    @GetMapping("/random")
    public ResponseEntity<?> obtenerMedicoRandom() {
        MedicoPerfilDTO medico = medicoService.medicoAlAzarPerfil();
        if (medico == null)
            return ResponseEntity.status(404).body("No hay médicos disponibles");
        return ResponseEntity.ok(medico);
    }

    @GetMapping("/especialidades")
    public ResponseEntity<List<Especialidad>> listarEspecialidades() {
        return ResponseEntity.ok(especialidadService.findAll());
    }

    @GetMapping("/slots")
    public ResponseEntity<List<SlotDisponibleDTO>> obtenerSlots(
            @RequestParam Long especialidadId,
            @RequestParam String fecha) {
        return ResponseEntity.ok(reservaService.obtenerSlotsDisponibles(especialidadId, fecha));
    }

    @PostMapping("/reserva")
    public ResponseEntity<String> procesarReserva(@RequestBody @Valid ReservaDTO dto) {
        try {
            String resultado = reservaService.procesarReserva(dto);
            return ResponseEntity.ok(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
