package com.app.CitaMed.Model.Medico;
import com.app.CitaMed.Model.Agenda.Cita;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "diagnosticos")

public class Diagnostico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Debe indicar la enfermedad")
    @Size(max = 100, message = "El nombre de la enfermedad no puede superar los 100 caracteres")
    private String enfermedad;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @Size(max = 500, message = "La receta no puede superar los 500 caracteres")
    private String receta;

    @Size(max = 500, message = "Las indicaciones no pueden superar los 500 caracteres")
    private String indicaciones;

    @NotNull(message = "Debe existir una cita asociada")
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "cita_id", nullable = false, unique = true)
    private Cita cita;
}
