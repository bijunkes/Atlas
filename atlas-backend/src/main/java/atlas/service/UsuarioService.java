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

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public Usuario register(RegisterDTO dados) {

        String email = dados.email()
                .trim()
                .toLowerCase();

        if(usuarioRepository.existsByEmail(email)){
            throw new EmailAlreadyExistsException();
        }

        Usuario usuario = Usuario.builder()
                .nome(dados.nome())
                .email(email)
                .senha(passwordEncoder.encode(dados.senha()))
                .build();

        return usuarioRepository.save(usuario);
    }

    public AuthResponseDTO login(LoginDTO dados){

        Usuario usuario = usuarioRepository
                .findByEmail(
                        dados.email()
                                .trim()
                                .toLowerCase()
                )
                .orElseThrow(
                        InvalidCredentialsException::new
                );

        if(!passwordEncoder.matches(
                dados.senha(),
                usuario.getSenha()
        )){
            throw new InvalidCredentialsException();
        }

        return gerarResposta(usuario);
    }

    private AuthResponseDTO gerarResposta(Usuario usuario){

        String accessToken =
                jwtService.gerarToken(usuario);

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
