package com.app.CitaMed.Service.Administrativo;
import com.app.CitaMed.Enums.Rol;
import com.app.CitaMed.Model.Administrativo.Usuario;
import com.app.CitaMed.Repository.Administrativo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<Usuario> findAllUsers() {
        return usuarioRepository.findAll();
    }

    @Transactional
    public String saveUser(Usuario usuario) {
        if (usuarioRepository.existsByUserName(usuario.getUserName())) {
            return "El usuario ya está registrado. Intente de nuevo";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioRepository.save(usuario);
        return "Usuario guardado correctamente";
    }

    public Optional<Usuario> findByUsuario(String userName) {
        return usuarioRepository.findByUserName(userName);
    }

    @Transactional
    public String updateStatus(Long id) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setActivo(!usuario.isActivo());
            usuarioRepository.save(usuario);
            return usuario.isActivo() ? "Usuario activado correctamente" : "Usuario desactivado correctamente";
        }).orElse("Usuario no encontrado");
    }

    @Transactional
    public String changeRol(Long id, Rol nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) return "Usuario no encontrado";

        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
        return "Rol actualizado a " + nuevoRol;
    }

    public boolean verificarContrasena(String passwordPlana, String passwordHash) {
        try {
            return passwordEncoder.matches(passwordPlana, passwordHash);
        } catch (Exception e) {
            return passwordPlana.equals(passwordHash);
        }
    }
}
