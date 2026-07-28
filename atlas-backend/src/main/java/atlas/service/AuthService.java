package atlas.service;

import atlas.dto.AuthResponseDTO;
import atlas.dto.LoginDTO;
import atlas.dto.RegisterDTO;
import atlas.entity.RefreshToken;
import atlas.entity.Usuario;
import atlas.exception.EmailAlreadyExistsException;
import atlas.exception.InvalidCredentialsException;
import atlas.repository.UsuarioRepository;

import atlas.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Transactional // Impedir caso salvar usuário funcionar e refresh token falhar
    public AuthResponseDTO register(RegisterDTO dados) {

        String email = normalizarEmail(dados.email());

        if (usuarioRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        Usuario usuario = Usuario.builder()
                .nome(dados.nome())
                .email(email)
                .senha(passwordEncoder.encode(dados.senha()))
                .build();

        usuarioRepository.save(usuario);

        return gerarResposta(usuario);
    }

    public AuthResponseDTO login(LoginDTO dados) {

        Usuario usuario = buscarPorEmail(
                normalizarEmail(dados.email())
        );

        if (!passwordEncoder.matches(
                dados.senha(),
                usuario.getSenha()
        )) {
            throw new InvalidCredentialsException();
        }

        return gerarResposta(usuario);
    }

    private Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);
    }

    private String normalizarEmail(String email) {
        return email.trim().toLowerCase();
    }

    private AuthResponseDTO gerarResposta(Usuario usuario) {

        String accessToken = jwtService.gerarToken(usuario);

        RefreshToken refreshToken =
                refreshTokenService.criar(usuario);

        return new AuthResponseDTO(
                accessToken,
                refreshToken.getToken(),
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }

}
