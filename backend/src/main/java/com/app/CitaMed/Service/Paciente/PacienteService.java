package com.app.CitaMed.Service.Paciente;
import com.app.CitaMed.DTO.PacienteDTO;
import com.app.CitaMed.Model.Paciente.HistorialMedico;
import com.app.CitaMed.Model.Paciente.Paciente;
import com.app.CitaMed.Repository.Paciente.HistorialMedicoRepository;
import com.app.CitaMed.Repository.Paciente.PacienteRepository;
import com.app.CitaMed.Util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final HistorialMedicoRepository historialMedicoRepository;

    public List<Paciente> findAll() {
        return pacienteRepository.findByActivoTrue();
    }

    public List<Paciente> findAllByIds(List<Long> ids) {
        return pacienteRepository.findAllById(ids).stream()
                .filter(p -> p.getActivo() == null || p.getActivo())
                .toList();
    }

    public org.springframework.data.domain.Page<Paciente> findAll(org.springframework.data.domain.Pageable pageable) {
        return pacienteRepository.findByActivoTrue(pageable);
    }

    public org.springframework.data.domain.Page<Paciente> findAll(boolean incluirInactivos, org.springframework.data.domain.Pageable pageable) {
        if (incluirInactivos) {
            return pacienteRepository.findAll(pageable);
        }
        return pacienteRepository.findByActivoTrue(pageable);
    }

    public org.springframework.data.domain.Page<Paciente> buscar(String termino, boolean incluirInactivos, org.springframework.data.domain.Pageable pageable) {
        return pacienteRepository.buscarConFiltros(termino, incluirInactivos, pageable);
    }

    public Paciente findById(Long id) {
        return pacienteRepository.findById(id).orElse(null);
    }

    public Paciente findByDni(String dni) {
        return pacienteRepository.findByDniAndActivoTrue(dni);
    }

    @Transactional
    public String save(PacienteDTO dto) {
        DniValidator.validar(dto.getDni());
        NombreValidator.validar(dto.getNombre(), "nombre");
        NombreValidator.validar(dto.getApellidoPaterno(), "apellido paterno");
        if (dto.getApellidoMaterno() != null && !dto.getApellidoMaterno().isBlank())
            NombreValidator.validar(dto.getApellidoMaterno(), "apellido materno");
        TelefonoValidator.validar(dto.getTelefono());
        EmailValidator.validar(dto.getEmail());

        if (pacienteRepository.existsByDni(dto.getDni()))
            return "Ya existe un paciente con ese DNI";

        Paciente paciente = new Paciente();
        paciente.setNombre(dto.getNombre().toUpperCase());
        paciente.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
        paciente.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
        paciente.setDni(dto.getDni());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setEmail(dto.getEmail());
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setGenero(dto.getGenero());
        paciente.setGrupoSanguineo(dto.getGrupoSanguineo());
        pacienteRepository.save(paciente);

        HistorialMedico historial = new HistorialMedico();
        historial.setPaciente(paciente);
        historialMedicoRepository.save(historial);

        return "Paciente registrado correctamente";
    }

    @Transactional
    public String update(Long id, PacienteDTO dto) {
        NombreValidator.validar(dto.getNombre(), "nombre");
        NombreValidator.validar(dto.getApellidoPaterno(), "apellido paterno");
        if (dto.getApellidoMaterno() != null && !dto.getApellidoMaterno().isBlank())
            NombreValidator.validar(dto.getApellidoMaterno(), "apellido materno");
        TelefonoValidator.validar(dto.getTelefono());
        EmailValidator.validar(dto.getEmail());

        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente == null) return "Paciente no encontrado";
        paciente.setNombre(dto.getNombre().toUpperCase());
        paciente.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
        paciente.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
        paciente.setTelefono(dto.getTelefono());
        paciente.setDireccion(dto.getDireccion());
        paciente.setEmail(dto.getEmail());
        pacienteRepository.save(paciente);
        return "Paciente actualizado correctamente";
    }

    @Transactional
    public String delete(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente == null) {
            return "Paciente no encontrado";
        }
        paciente.setActivo(false);
        pacienteRepository.save(paciente);
        return "Paciente eliminado correctamente";
    }

    @Transactional
    public String toggleEstado(Long id) {
        Paciente paciente = pacienteRepository.findById(id).orElse(null);
        if (paciente == null) return "Paciente no encontrado";
        paciente.setActivo(paciente.getActivo() == null ? false : !paciente.getActivo());
        pacienteRepository.save(paciente);
        return paciente.getActivo() ? "Paciente activado" : "Paciente desactivado";
    }
}
