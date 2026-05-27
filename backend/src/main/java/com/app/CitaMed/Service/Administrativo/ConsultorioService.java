package com.app.CitaMed.Service.Administrativo;
import com.app.CitaMed.DTO.ConsultorioDTO;
import com.app.CitaMed.Model.Administrativo.Consultorio;
import com.app.CitaMed.Repository.Administrativo.ConsultorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ConsultorioService {
    private final ConsultorioRepository consultorioRepository;

    public List<Consultorio> findAll() {
        return consultorioRepository.findAll();
    }

    public List<Consultorio> findDisponibles() {
        return consultorioRepository.findByDisponibleTrue();
    }

    public Consultorio findById(Long id) {
        return consultorioRepository.findById(id).orElse(null);
    }

    @Transactional
    public String save(ConsultorioDTO dto) {
        if (consultorioRepository.existsByNumero(dto.getNumero()))
            return "Ya existe un consultorio con ese número";
        Consultorio consultorio = new Consultorio();
        consultorio.setNumero(dto.getNumero());
        consultorio.setDescripcion(dto.getDescripcion());
        consultorio.setDisponible(true);
        consultorioRepository.save(consultorio);
        return "Consultorio registrado correctamente";
    }

    @Transactional
    public String update(Long id, ConsultorioDTO dto) {
        Consultorio consultorio = consultorioRepository.findById(id).orElse(null);
        if (consultorio == null) return "Consultorio no encontrado";
        consultorio.setNumero(dto.getNumero());
        consultorio.setDescripcion(dto.getDescripcion());
        consultorioRepository.save(consultorio);
        return "Consultorio actualizado correctamente";
    }

    @Transactional
    public String toggleDisponible(Long id) {
        Consultorio consultorio = consultorioRepository.findById(id).orElse(null);
        if (consultorio == null) return "Consultorio no encontrado";
        consultorio.setDisponible(!consultorio.isDisponible());
        consultorioRepository.save(consultorio);
        return consultorio.isDisponible() ? "Consultorio disponible" : "Consultorio no disponible";
    }
}
