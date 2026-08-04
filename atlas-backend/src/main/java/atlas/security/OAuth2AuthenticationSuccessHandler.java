package atlas.security;

import atlas.dto.AuthResponseDTO;
import atlas.entity.Usuario;
import atlas.enums.AuthProvider;
import atlas.enums.Role;
import atlas.repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler
                implements AuthenticationSuccessHandler {

        private final UsuarioRepository usuarioRepository;
        private final TokenService tokenService;
        private final CookieService cookieService;

        @Override
        public void onAuthenticationSuccess(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

            String email = oauthUser.getAttribute("email");

            String nome = oauthUser.getAttribute("name");

            String googleId = oauthUser.getAttribute("sub");

            Usuario usuario = usuarioRepository
                    .findByEmail(email)
                    .map(usuarioExistente -> {

                        usuarioExistente.setGoogleId(googleId);

                        if (usuarioExistente.getProvider() == AuthProvider.LOCAL) {
                            usuarioExistente.setProvider(AuthProvider.GOOGLE_AND_LOCAL);
                        }

                        return usuarioRepository.save(usuarioExistente);

                    })
                    .orElseGet(() -> {

                        Usuario novoUsuario = Usuario.builder()
                                .nome(nome)
                                .email(email)
                                .googleId(googleId)
                                .provider(AuthProvider.GOOGLE)
                                .role(Role.USER)
                                .build();

                        return usuarioRepository.save(novoUsuario);
                    });

            AuthResponseDTO resposta = tokenService.gerarResposta(usuario);

            cookieService.criarAccessToken(
                    response,
                    resposta.getAccessToken()
            );

            cookieService.criarRefreshToken(
                    response,
                    resposta.getRefreshToken()
            );

            response.sendRedirect(
                    "http://localhost:4200/dashboard"
            );
        }
}