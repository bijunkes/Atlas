package atlas.controller;

import atlas.dto.*;
import atlas.dto.usuario.UsuarioResponseDTO;
import atlas.entity.Usuario;
import atlas.exception.ResourceNotFoundException;
import atlas.security.CookieService;
import atlas.service.AuthService;

import atlas.service.EmailService;
import atlas.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

        private final AuthService authService;
        private final CookieService cookieService;
        private final UsuarioService usuarioService;

        @PostMapping("/register")
        public ResponseEntity<?> register(
                        @Valid @RequestBody RegisterDTO dados) {

                authService.register(dados);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new MessageResponseDTO("Usuário criado."));

        }

        @PostMapping("/login")
        public ResponseEntity<UsuarioResponseDTO> login(
                @Valid @RequestBody LoginDTO dados,
                HttpServletResponse response) {

                AuthResponseDTO auth = authService.login(dados);

                cookieService.criarAccessToken(
                        response,
                        auth.getAccessToken()
                );

                cookieService.criarRefreshToken(
                        response,
                        auth.getRefreshToken()
                );

                return ResponseEntity.ok(
                        UsuarioResponseDTO.fromEntity(auth.getUsuario())
                );
        }

        @PostMapping("/recuperar-senha")
        public ResponseEntity<MessageResponseDTO> solicitarRecuperacao(
                        @RequestBody @Valid RecuperarSenhaDTO dados) {

                authService.solicitarRecuperacaoSenha(dados.email());

                return ResponseEntity.ok(
                                new MessageResponseDTO("Se o e-mail existir, você receberá as instruções."));
        }

        @PostMapping("/resetar-senha")
        public ResponseEntity<MessageResponseDTO> resetarSenha(
                        @Valid @RequestBody ResetarSenhaDTO dados) {

                authService.resetarSenha(dados);

                return ResponseEntity.ok(
                                new MessageResponseDTO("Senha alterada com sucesso."));
        }

}