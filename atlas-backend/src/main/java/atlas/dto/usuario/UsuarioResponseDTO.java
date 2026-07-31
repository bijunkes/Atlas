package atlas.dto.usuario;

import atlas.entity.Usuario;
import atlas.enums.AuthProvider;
import atlas.enums.Role;

import java.time.LocalDateTime;

public record UsuarioResponseDTO(

        Long id,
        String nome,
        String email,
        Role role,
        AuthProvider provider,
        LocalDateTime criadoEm,
        String imagemPerfil
        
) {

    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getProvider(),
                usuario.getCriadoEm(),
                usuario.getImagemPerfil()
        );
    }
}