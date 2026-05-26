package com.app.CitaMed.DTO;
import com.app.CitaMed.Enums.EstadoCita;
import lombok.Data;
import java.time.LocalDateTime;

@Data

public class CitaDetalleDTO {
    private Long id;
    private Long pacienteId;
    private String pacienteNombre;
    private String pacienteApellidoPaterno;
    private String pacienteApellidoMaterno;
    private String pacienteDni;
    private Long medicoId;
    private String medicoNombre;
    private String medicoApellidoPaterno;
    private String medicoApellidoMaterno;
    private String medicoEspecialidad;
    private Long consultorioId;
    private String consultorioNumero;
    private String consultorioDescripcion;
    private LocalDateTime fechaHora;
    private String motivoConsulta;
    private EstadoCita estado;
}
