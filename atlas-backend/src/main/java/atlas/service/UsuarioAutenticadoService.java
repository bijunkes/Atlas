package atlas.service;

import atlas.entity.Usuario;
import atlas.exception.ResourceNotFoundException;
import atlas.exception.UnauthorizedException;
import atlas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;

    public Usuario getUsuarioLogado() {

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado");
        }

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuário não encontrado"));
    }
}