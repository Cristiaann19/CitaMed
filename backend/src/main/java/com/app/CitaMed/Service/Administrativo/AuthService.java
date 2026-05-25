package com.app.CitaMed.Service.Administrativo;
import com.app.CitaMed.Config.JwtUtil;
import com.app.CitaMed.Model.Administrativo.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class AuthService {
    private final UsuarioService usuarioService;
    private final JwtUtil jwtUtil;

    public ResponseEntity<?> login(String username, String password){

        Optional<Usuario> usuarioOpt = usuarioService.findByUsuario(username);

        if(usuarioOpt.isEmpty()){
            return ResponseEntity.status(401)
                    .body(Map.of("error","Usuario no encontrado"));
        }

        Usuario user = usuarioOpt.get();

        if(!user.isActivo()){
            return ResponseEntity.status(403)
                    .body(Map.of("error","Usuario inactivo"));
        }

        if(!usuarioService.verificarContrasena(password,user.getPassword())){
            return ResponseEntity.status(401)
                    .body(Map.of("error","Contraseña incorrecta"));
        }

        // Include roles in the token so SecurityContext can be populated by JwtFilter
        java.util.List<String> roles = java.util.List.of(user.getRol().name());
        String token = jwtUtil.generarToken(user.getUserName(), roles);

        Map<String,Object> response = new HashMap<>();
        response.put("nombre", user.getUserName());
        response.put("token", token);
        response.put("usuario", user.getUserName());
        response.put("perfil", user.getRol().name());

        return ResponseEntity.ok(response);
    }
}
