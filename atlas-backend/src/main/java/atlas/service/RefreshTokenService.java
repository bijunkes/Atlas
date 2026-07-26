package atlas.service;

import atlas.entity.RefreshToken;
import atlas.entity.Usuario;
import atlas.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken criar(Usuario usuario){

        RefreshToken refreshToken = refreshTokenRepository
                .findByUsuarioId(usuario.getId())
                .orElse(new RefreshToken());


        refreshToken.setToken(
                UUID.randomUUID().toString()
        );


        refreshToken.setUsuario(usuario);


        refreshToken.setExpiracao(
                LocalDateTime.now().plusDays(7)
        );

        return refreshTokenRepository.save(refreshToken);
    }

}