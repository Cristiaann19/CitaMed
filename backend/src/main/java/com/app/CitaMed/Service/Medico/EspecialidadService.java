package com.app.CitaMed.Service.Medico;
import com.app.CitaMed.Model.Medico.Especialidad;
import com.app.CitaMed.Repository.Medico.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class EspecialidadService {
    private final EspecialidadRepository especialidadRepository;

    public List<Especialidad> findAll() {
        return especialidadRepository.findAll();
    }

    @Transactional
    public String save(Especialidad especialidad) {
        especialidadRepository.save(especialidad);
        return "Especialidad registrada correctamente";
    }

    @Transactional
    public String update(Long id, Especialidad dto) {
        Especialidad especialidad = especialidadRepository.findById(id).orElse(null);
        if (especialidad == null) return "Especialidad no encontrada";
        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());
        especialidadRepository.save(especialidad);
        return "Especialidad actualizada correctamente";
    }

    @Transactional
    public String delete(Long id) {
        if (!especialidadRepository.existsById(id))
            return "Especialidad no encontrada";
        especialidadRepository.deleteById(id);
        return "Especialidad eliminada correctamente";
    }
}
