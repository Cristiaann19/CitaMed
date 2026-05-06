package com.app.CitaMed.Service.MicroServicios;
import com.app.CitaMed.DTO.RecetaDTO;
import com.app.CitaMed.DTO.TicketCitaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Component

public class PdfService {
    private final RestTemplate rest = new RestTemplate();

    public byte[] ticketCita(TicketCitaDTO dto) {
        return rest.postForObject(
                "http://localhost:3005/generar-ticket-cita",
                dto,
                byte[].class
        );
    }

    public byte[] receta(RecetaDTO dto) {
        return rest.postForObject(
                "http://localhost:3005/generar-receta",
                dto,
                byte[].class
        );
    }
}
