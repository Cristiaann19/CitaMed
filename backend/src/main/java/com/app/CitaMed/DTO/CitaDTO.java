package com.app.CitaMed.DTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CitaDTO {
    private Long pacienteId;
    private Long medicoId;
    private LocalDateTime fechaHora;
    private String motivoConsulta;
}
