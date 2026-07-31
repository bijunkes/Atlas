package atlas.service;

import atlas.dto.AuthResponseDTO;
import atlas.dto.LoginDTO;
import atlas.dto.RegisterDTO;
import atlas.dto.ResetarSenhaDTO;
import atlas.entity.TokenRecuperacaoSenha;
import atlas.entity.Usuario;
import atlas.enums.AuthProvider;
import atlas.exception.*;
import atlas.repository.TokenRecuperacaoSenhaRepository;
import atlas.repository.UsuarioRepository;

import atlas.security.TokenService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UsuarioRepository usuarioRepository;
        private final PasswordEncoder passwordEncoder;
        private final TokenRecuperacaoSenhaRepository tokenRecuperacaoSenhaRepository;
        private final EmailService emailService;

        private final TokenService tokenService;

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
                                .provider(AuthProvider.LOCAL)
                                .build();

                usuarioRepository.save(usuario);

                return tokenService.gerarResposta(usuario);
        }

        public AuthResponseDTO login(LoginDTO dados) {

                Usuario usuario = buscarPorEmail(
                                normalizarEmail(dados.email()));

                if (usuario.getSenha() == null) {
                        throw new SocialLoginException(
                                        "Esta conta foi criada com Google. Acesse pelo Google ou defina uma senha nas configurações da conta.");
                }

                if (!passwordEncoder.matches(
                                dados.senha(),
                                usuario.getSenha())) {
                        throw new InvalidCredentialsException();
                }

                return tokenService.gerarResposta(usuario);
        }

        private Usuario buscarPorEmail(String email) {
                return usuarioRepository.findByEmail(email)
                                .orElseThrow(InvalidCredentialsException::new);
        }

        private String normalizarEmail(String email) {
                return email.trim().toLowerCase();
        }

        @Transactional
        public void solicitarRecuperacaoSenha(String email) {

                String emailNormalizado = normalizarEmail(email);

                usuarioRepository.findByEmail(emailNormalizado)
                                .ifPresent(usuario -> {

                                        tokenRecuperacaoSenhaRepository.deleteByUsuarioId(usuario.getId());

                                        String token = UUID.randomUUID().toString();

                                        TokenRecuperacaoSenha tokenRecuperacao = new TokenRecuperacaoSenha(
                                                        token,
                                                        LocalDateTime.now().plusMinutes(15),
                                                        usuario);

                                        tokenRecuperacaoSenhaRepository.save(tokenRecuperacao);

                                        String link = "http://localhost:4200/resetar-senha?token=" + token;

                                        emailService.enviarEmail(
                                                        usuario.getEmail(),
                                                        "Recuperação de senha - Atlas",
                                                        """
                                                                        Olá!

                                                                        Recebemos uma solicitação para redefinir sua senha.

                                                                        Acesse o link:

                                                                        %s

                                                                        Esse link expira em 15 minutos.
                                                                        """
                                                                        .formatted(link));
                                });
        }

        @Transactional
        public void resetarSenha(ResetarSenhaDTO dados) {

                TokenRecuperacaoSenha token = tokenRecuperacaoSenhaRepository.findByToken(dados.token())
                                .orElseThrow(() -> new ResourceNotFoundException("Token inválido"));

                if (token.getExpiracao().isBefore(LocalDateTime.now())) {

                        tokenRecuperacaoSenhaRepository.delete(token);

                        throw new ExpiredTokenException();
                }

                Usuario usuario = token.getUsuario();

                usuario.setSenha(
                                passwordEncoder.encode(dados.novaSenha()));

                if (usuario.getProvider() == AuthProvider.GOOGLE) {
                        usuario.setProvider(AuthProvider.GOOGLE_AND_LOCAL);
                }

                tokenRecuperacaoSenhaRepository.delete(token);
        }

}
