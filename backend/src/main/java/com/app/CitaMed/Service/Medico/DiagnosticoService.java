package com.app.CitaMed.Service.Medico;
import com.app.CitaMed.DTO.DiagnosticoDTO;
import com.app.CitaMed.Model.Agenda.Cita;
import com.app.CitaMed.Model.Medico.Diagnostico;
import com.app.CitaMed.Repository.Agenda.CitaRepository;
import com.app.CitaMed.Repository.Medico.DiagnosticoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiagnosticoService {
    private final DiagnosticoRepository diagnosticoRepository;
    private final CitaRepository citaRepository;

    public List<Diagnostico> findAll() {
        return diagnosticoRepository.findAll();
    }

    public Diagnostico findByCita(Long citaId) {
        return diagnosticoRepository.findByCitaId(citaId).orElse(null);
    }

    public String save(DiagnosticoDTO dto) {
        Cita cita = citaRepository.findById(dto.getCitaId()).orElse(null);
        if (cita == null) return "Cita no encontrada";

        if (diagnosticoRepository.findByCitaId(dto.getCitaId()).isPresent())
            return "La cita ya tiene un diagnóstico registrado";

        Diagnostico diagnostico = new Diagnostico();
        diagnostico.setCita(cita);
        diagnostico.setEnfermedad(dto.getEnfermedad().toUpperCase());
        diagnostico.setDescripcion(dto.getDescripcion());
        diagnostico.setReceta(dto.getReceta());
        diagnostico.setIndicaciones(dto.getIndicaciones());
        diagnosticoRepository.save(diagnostico);
        return "Diagnóstico registrado correctamente";
    }

    public String update(Long citaId, DiagnosticoDTO dto) {
        Diagnostico diagnostico = diagnosticoRepository.findByCitaId(citaId).orElse(null);
        if (diagnostico == null) return "Diagnóstico no encontrado";

        if (dto.getEnfermedad() != null) diagnostico.setEnfermedad(dto.getEnfermedad().toUpperCase());
        if (dto.getDescripcion() != null) diagnostico.setDescripcion(dto.getDescripcion());
        if (dto.getReceta() != null) diagnostico.setReceta(dto.getReceta());
        if (dto.getIndicaciones() != null) diagnostico.setIndicaciones(dto.getIndicaciones());
        diagnosticoRepository.save(diagnostico);
        return "Diagnóstico actualizado correctamente";
    }

    public String delete(Long id) {
        if (!diagnosticoRepository.existsById(id))
            return "Diagnóstico no encontrado";
        diagnosticoRepository.deleteById(id);
        return "Diagnóstico eliminado correctamente";
    }
}
