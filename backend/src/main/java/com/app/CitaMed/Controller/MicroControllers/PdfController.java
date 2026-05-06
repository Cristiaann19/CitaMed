package com.app.CitaMed.Controller.MicroControllers;
import com.app.CitaMed.DTO.RecetaDTO;
import com.app.CitaMed.DTO.TicketCitaDTO;
import com.app.CitaMed.Service.MicroServicios.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor

public class PdfController {
    @Autowired
    private PdfService service;

    @PostMapping("/ticket")
    public ResponseEntity<byte[]> ticket(@RequestBody TicketCitaDTO dto) {

        byte[] pdf = service.ticketCita(dto);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }

    @PostMapping("/receta")
    public ResponseEntity<byte[]> receta(@RequestBody RecetaDTO dto) {

        byte[] pdf = service.receta(dto);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .body(pdf);
    }
}
