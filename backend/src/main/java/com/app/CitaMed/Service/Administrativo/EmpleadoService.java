package com.app.CitaMed.Service.Administrativo;
import com.app.CitaMed.DTO.EmpleadoDTO;
import com.app.CitaMed.Model.Administrativo.Empleado;
import com.app.CitaMed.Model.Administrativo.Usuario;
import com.app.CitaMed.Repository.Administrativo.EmpleadoRepository;
import com.app.CitaMed.Repository.Administrativo.UsuarioRepository;
import com.app.CitaMed.Util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor

public class EmpleadoService {
    private final EmpleadoRepository empleadoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }

    public Empleado findById(Long id) {
        return empleadoRepository.findById(id).orElse(null);
    }

    public List<Empleado> findActivos() {
        return empleadoRepository.findByActivoTrue();
    }

    @Transactional
    public String save(EmpleadoDTO dto) {
        DniValidator.validar(dto.getDni());
        NombreValidator.validar(dto.getNombre(), "nombre");
        NombreValidator.validar(dto.getApellidoPaterno(), "apellido paterno");
        if (dto.getApellidoMaterno() != null && !dto.getApellidoMaterno().isBlank())
            NombreValidator.validar(dto.getApellidoMaterno(), "apellido materno");
        TelefonoValidator.validar(dto.getTelefono());
        EmailValidator.validar(dto.getEmail());
        UserNameValidator.validar(dto.getUserName());
        PasswordValidator.validar(dto.getPassword());

        if (empleadoRepository.existsByDni(dto.getDni()))
            return "Ya existe un empleado con ese DNI";

        if (usuarioRepository.existsByUserName(dto.getUserName()))
            return "El nombre de usuario ya está en uso";

        Usuario usuario = new Usuario();
        usuario.setUserName(dto.getUserName());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(dto.getRol());
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        Empleado empleado = new Empleado();
        empleado.setNombre(dto.getNombre().toUpperCase());
        empleado.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
        empleado.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
        empleado.setDni(dto.getDni());
        empleado.setTelefono(dto.getTelefono());
        empleado.setDireccion(dto.getDireccion());
        empleado.setEmail(dto.getEmail());
        empleado.setFechaNacimiento(dto.getFechaNacimiento());
        empleado.setGenero(dto.getGenero());
        empleado.setSalario(dto.getSalario());
        empleado.setFechaIngreso(dto.getFechaIngreso());
        empleado.setUsuario(usuario);
        empleado.setActivo(true);
        empleadoRepository.save(empleado);
        return "Empleado registrado correctamente";
    }

    @Transactional
    public String update(Long id, EmpleadoDTO dto) {
        NombreValidator.validar(dto.getNombre(), "nombre");
        NombreValidator.validar(dto.getApellidoPaterno(), "apellido paterno");
        if (dto.getApellidoMaterno() != null && !dto.getApellidoMaterno().isBlank())
            NombreValidator.validar(dto.getApellidoMaterno(), "apellido materno");
        TelefonoValidator.validar(dto.getTelefono());
        EmailValidator.validar(dto.getEmail());

        Empleado empleado = empleadoRepository.findById(id).orElse(null);
        if (empleado == null) return "Empleado no encontrado";

        empleado.setNombre(dto.getNombre().toUpperCase());
        empleado.setApellidoPaterno(dto.getApellidoPaterno().toUpperCase());
        empleado.setApellidoMaterno(dto.getApellidoMaterno().toUpperCase());
        empleado.setTelefono(dto.getTelefono());
        empleado.setDireccion(dto.getDireccion());
        empleado.setEmail(dto.getEmail());
        empleado.setSalario(dto.getSalario());
        empleadoRepository.save(empleado);
        return "Empleado actualizado correctamente";
    }

    @Transactional
    public String toggleActivo(Long id) {
        Empleado empleado = empleadoRepository.findById(id).orElse(null);
        if (empleado == null) return "Empleado no encontrado";
        empleado.setActivo(!empleado.isActivo());
        empleadoRepository.save(empleado);
        return empleado.isActivo() ? "Empleado activado" : "Empleado desactivado";
    }
}
