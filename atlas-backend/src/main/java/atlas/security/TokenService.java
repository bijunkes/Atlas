package atlas.security;

import atlas.dto.AuthResponseDTO;
import atlas.entity.RefreshToken;
import atlas.entity.Usuario;

import atlas.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

        private final JwtService jwtService;
        private final RefreshTokenService refreshTokenService;

        public AuthResponseDTO gerarResposta(Usuario usuario) {

                String accessToken = jwtService.gerarToken(usuario);

                RefreshToken refreshToken = refreshTokenService.criar(usuario);

                return new AuthResponseDTO(
                                accessToken,
                                refreshToken.getToken(),
                                usuario.getId(),
                                usuario.getNome(),
                                usuario.getEmail(),
                                usuario.getRole().name());
        }
}