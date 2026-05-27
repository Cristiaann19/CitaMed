package com.app.CitaMed.DTO;
import com.app.CitaMed.Enums.DiaSemana;
import lombok.Data;
import java.time.LocalTime;

@Data
public class HorarioMedicoDTO {
    private Long medicoId;
    private Long consultorioId;
    private DiaSemana dia;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
