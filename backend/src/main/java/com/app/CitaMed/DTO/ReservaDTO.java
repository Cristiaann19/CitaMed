package com.app.CitaMed.DTO;
import lombok.Data;
import java.time.LocalDate;

@Data
public class ReservaDTO {
    private String dni;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String telefono;
    private String email;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String genero;
    private String grupoSanguineo;
    private Long medicoId;
    private Long consultorioId;
    private String fechaHora;
    private String motivoConsulta;
}
