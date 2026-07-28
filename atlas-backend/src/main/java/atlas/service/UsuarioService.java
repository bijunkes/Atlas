package atlas.service;

import atlas.dto.usuario.UsuarioResponseDTO;
import atlas.entity.Usuario;
import atlas.exception.ResourceNotFoundException;
import atlas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public UsuarioResponseDTO buscarPerfil() {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        return UsuarioResponseDTO.fromEntity(usuario);
    }

}
