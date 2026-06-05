package com.app.CitaMed.Model.Agenda;
import com.app.CitaMed.Enums.DiaSemana;
import com.app.CitaMed.Model.Administrativo.Consultorio;
import com.app.CitaMed.Model.Medico.Medico;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "horarios_medicos")

public class HorarioMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe indicar el día de atención")
    @Enumerated(EnumType.STRING)
    private DiaSemana dia;

    @NotNull(message = "Debe indicar la hora de inicio")
    private LocalTime horaInicio;

    @NotNull(message = "Debe indicar la hora de fin")
    private LocalTime horaFin;

    private boolean activo;

    @NotNull(message = "Debe seleccionar un médico")
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotNull(message = "Debe seleccionar un consultorio")
    @ManyToOne
    @JoinColumn(name = "consultorio_id", nullable = false)
    private Consultorio consultorio;
}
