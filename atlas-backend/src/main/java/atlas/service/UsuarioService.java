package atlas.service;

import atlas.dto.usuario.UsuarioResponseDTO;
import atlas.dto.usuario.AtualizarUsuarioDTO;
import atlas.entity.Usuario;
import atlas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import atlas.exception.EmailAlreadyExistsException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioAutenticadoService usuarioAutenticadoService;
    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO buscarPerfil() {

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();

        return UsuarioResponseDTO.fromEntity(usuario);
    }

    public UsuarioResponseDTO atualizar(
            AtualizarUsuarioDTO dto
    ){

        Usuario usuario =
                usuarioAutenticadoService.getUsuarioLogado();


        if (!usuario.getEmail().equals(dto.email())
                && usuarioRepository.existsByEmail(dto.email())) {

            throw new EmailAlreadyExistsException();
        }


        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());


        Usuario atualizado =
                usuarioRepository.save(usuario);


        return UsuarioResponseDTO.fromEntity(atualizado);
    }

}
