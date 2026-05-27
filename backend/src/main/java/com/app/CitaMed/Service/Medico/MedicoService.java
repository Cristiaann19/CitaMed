package com.app.CitaMed.Service.Medico;
import com.app.CitaMed.DTO.MedicoDTO;
import com.app.CitaMed.DTO.MedicoPerfilDTO;
import com.app.CitaMed.Enums.Rol;
import com.app.CitaMed.Model.Medico.Especialidad;
import com.app.CitaMed.Model.Medico.Medico;
import com.app.CitaMed.Model.Administrativo.Consultorio;
import com.app.CitaMed.Model.Administrativo.Usuario;
import com.app.CitaMed.Repository.Administrativo.ConsultorioRepository;
import com.app.CitaMed.Repository.Medico.EspecialidadRepository;
import com.app.CitaMed.Repository.Medico.MedicoRepository;
import com.app.CitaMed.Repository.Administrativo.UsuarioRepository;
import com.app.CitaMed.Util.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor

public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final ConsultorioRepository consultorioRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Medico> findAll() {
        return medicoRepository.findAll();
    }

    public Medico findById(Long id) {
        return medicoRepository.findById(id).orElse(null);
    }

    public List<Medico> findByEspecialidad(Long especialidadId) {
        return medicoRepository.findByEspecialidadIdAndActivoTrue(especialidadId);
    }

    @Transactional
    public String save(MedicoDTO dto) {
        DniValidator.validar(dto.getDni());
        NombreValidator.validar(dto.getNombre(), "nombre");
        NombreValidator.validar(dto.getApellidoPaterno(), "apellido paterno");
        if (dto.getApellidoMaterno() != null && !dto.getApellidoMaterno().isBlank())
            NombreValidator.validar(dto.getApellidoMaterno(), "apellido materno");
        TelefonoValidator.validar(dto.getTelefono());
        EmailValidator.validar(dto.getEmail());
        UserNameValidator.validar(dto.getUserName());
        PasswordValidator.validar(dto.getPassword());

        if (medicoRepository.existsByDni(dto.getDni()))
            return "Ya existe un médico con ese DNI";

        if (usuarioRepository.existsByUserName(dto.getUserName()))
            return "El nombre de usuario ya está en uso";

        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId()).orElse(null);
        if (especialidad == null) return "Especialidad no encontrada";

        Consultorio consultorio = null;
        if (dto.getConsultorioId() != null) {
            consultorio = consultorioRepository.findById(dto.getConsultorioId()).orElse(null);
        }

        Usuario usuario = new Usuario();
        usuario.setUserName(dto.getUserName());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(Rol.MEDICO);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        Medico medico = new Medico();
        medico.setNombre(dto.getNombre().toUpperCase());
        medico.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
        medico.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
        medico.setDni(dto.getDni());
        medico.setTelefono(dto.getTelefono());
        medico.setDireccion(dto.getDireccion());
        medico.setEmail(dto.getEmail());
        medico.setFechaNacimiento(dto.getFechaNacimiento());
        medico.setGenero(dto.getGenero());
        medico.setNumeroColegiatura(dto.getNumeroColegiatura());
        medico.setEspecialidad(especialidad);
        medico.setConsultorio(consultorio);
        medico.setUsuario(usuario);
        medico.setActivo(true);
        medicoRepository.save(medico);
        return "Médico registrado correctamente";
    }

    public String updateEspecialidad(Long medicoId, Long especialidadId) {
        Medico medico = medicoRepository.findById(medicoId).orElse(null);
        if (medico == null) return "Médico no encontrado";
        Especialidad especialidad = especialidadRepository.findById(especialidadId).orElse(null);
        if (especialidad == null) return "Especialidad no encontrada";
        medico.setEspecialidad(especialidad);
        medicoRepository.save(medico);
        return "Especialidad actualizada correctamente";
    }

    public Medico medicoAlAzar() {
        List<Medico> medicos = medicoRepository.findAll().stream().filter(Medico :: isActivo).toList();

        if (medicos.isEmpty()) {
            return null;
        }

        int indice = ThreadLocalRandom.current().nextInt(medicos.size());
        return medicos.get(indice);
    }

    public MedicoPerfilDTO medicoAlAzarPerfil() {
        Medico medico = medicoAlAzar();
        if (medico == null) {
            return null;
        }

        return new MedicoPerfilDTO(
                medico.getNombre(),
                medico.getApellidoPaterno(),
                medico.getApellidoMaterno(),
                medico.getTelefono(),
                medico.getEmail(),
                medico.getGenero(),
                medico.getNumeroColegiatura(),
                medico.getEspecialidad().getNombre()
        );
    }

    @Transactional
    public String update(Long id, MedicoDTO dto) {
        NombreValidator.validar(dto.getNombre(), "nombre");
        NombreValidator.validar(dto.getApellidoPaterno(), "apellido paterno");
        if (dto.getApellidoMaterno() != null && !dto.getApellidoMaterno().isBlank())
            NombreValidator.validar(dto.getApellidoMaterno(), "apellido materno");
        TelefonoValidator.validar(dto.getTelefono());
        EmailValidator.validar(dto.getEmail());
        UserNameValidator.validar(dto.getUserName());
        if (dto.getPassword() != null && !dto.getPassword().isBlank())
            PasswordValidator.validar(dto.getPassword());

        Medico medico = medicoRepository.findById(id).orElse(null);
        if (medico == null) return "Médico no encontrado";

        Especialidad especialidad = especialidadRepository.findById(dto.getEspecialidadId()).orElse(null);
        if (especialidad == null) return "Especialidad no encontrada";

        medico.setNombre(dto.getNombre().toUpperCase());
        medico.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
        medico.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
        medico.setTelefono(dto.getTelefono());
        medico.setDireccion(dto.getDireccion());
        medico.setEmail(dto.getEmail());
        medico.setFechaNacimiento(dto.getFechaNacimiento());
        medico.setGenero(dto.getGenero());
        medico.setNumeroColegiatura(dto.getNumeroColegiatura());
        medico.setEspecialidad(especialidad);

        if (dto.getConsultorioId() != null) {
            Consultorio consultorio = consultorioRepository.findById(dto.getConsultorioId()).orElse(null);
            medico.setConsultorio(consultorio);
        }

        if (medico.getUsuario() != null) {
            Usuario usuario = medico.getUsuario();
            usuario.setUserName(dto.getUserName());
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                usuario.setPassword(dto.getPassword());
            }
            usuarioRepository.save(usuario);
        }

        medicoRepository.save(medico);
        return "Médico actualizado correctamente";
    }

    @Transactional
    public String cambiarEstado(Long id) {

        Optional<Medico> medicoOpt = medicoRepository.findById(id);

        if (medicoOpt.isEmpty()) {
            return "Médico no encontrado";
        }

        Medico medico = medicoOpt.get();
        boolean nuevoEstado = !medico.isActivo();
        medico.setActivo(nuevoEstado);

        if (medico.getUsuario() != null) {
            medico.getUsuario().setActivo(nuevoEstado);
        }

        medicoRepository.save(medico);

        return nuevoEstado
                ? "Médico activado correctamente"
                : "Médico desactivado correctamente";
    }
}
