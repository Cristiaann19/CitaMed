package com.app.CitaMed.Model.Paciente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "HistorialesMedicos")

public class HistorialMedico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El historial debe pertenecer a un paciente")
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

}
