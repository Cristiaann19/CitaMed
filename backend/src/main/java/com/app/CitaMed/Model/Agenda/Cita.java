package com.app.CitaMed.Model.Agenda;
import com.app.CitaMed.Enums.EstadoCita;
import com.app.CitaMed.Model.Administrativo.Consultorio;
import com.app.CitaMed.Model.Administrativo.Pago;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Model.Medico.Diagnostico;
import com.app.CitaMed.Model.Paciente.Paciente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "citas")

public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Debe seleccionar un paciente")
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @NotNull(message = "Debe seleccionar un médico")
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @NotNull(message = "Debe seleccionar un consultorio")
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "consultorio_id", nullable = false)
    private Consultorio consultorio;

    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    private LocalDateTime fechaHora;

    @NotBlank(message = "Debe indicar el motivo de la consulta")
    @Size(max = 200, message = "El motivo no puede superar los 200 caracteres")
    private String motivoConsulta;

    @NotNull(message = "Debe indicar el estado de la cita")
    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    @JsonIgnore
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL)
    private Pago pago;

    @JsonIgnore
    @OneToOne(mappedBy = "cita", cascade = CascadeType.ALL)
    private Diagnostico diagnostico;
}
