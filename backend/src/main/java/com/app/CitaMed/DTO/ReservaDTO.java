package com.app.CitaMed.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaDTO {
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo puede contener letras")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50)
    private String apellidoPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 50)
    private String apellidoMaterno;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El teléfono debe tener 9 dígitos")
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un email válido")
    private String email;

    @Size(max = 150)
    private String direccion;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser pasada")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El género es obligatorio")
    private String genero;

    @NotNull(message = "Debe seleccionar un grupo sanguíneo")
    private String grupoSanguineo;

    @NotNull(message = "Debe seleccionar un médico")
    private Long medicoId;

    @NotNull(message = "Debe seleccionar un consultorio")
    private Long consultorioId;

    @NotBlank(message = "La fecha y hora de la cita es obligatoria")
    private String fechaHora;

    @NotBlank(message = "Debe indicar el motivo de la consulta")
    @Size(max = 200, message = "El motivo no puede superar los 200 caracteres")
    private String motivoConsulta;
}
