package atlas.controller;

import atlas.dto.CriarSenhaDTO;
import atlas.dto.usuario.AtualizarUsuarioDTO;
import atlas.dto.usuario.UsuarioResponseDTO;
import atlas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

        private final UsuarioService usuarioService;

        @PutMapping("/criar-senha")
        public ResponseEntity<Void> criarSenha(
                        @RequestBody @Valid CriarSenhaDTO dto) {

                usuarioService.criarSenha(dto.senha());

                return ResponseEntity.noContent().build();
        }

        @GetMapping("/me")
        public ResponseEntity<UsuarioResponseDTO> me() {

                return ResponseEntity.ok(
                                usuarioService.buscarPerfil());
        }

        @PutMapping("/me")
        public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(
                        @Valid @RequestBody AtualizarUsuarioDTO dto) {

                return ResponseEntity.ok(
                                usuarioService.atualizar(dto));

        }

        @PutMapping("/me/imagem")
        public ResponseEntity<UsuarioResponseDTO> atualizarImagem(
                        @RequestParam("file") MultipartFile file) {

                return ResponseEntity.ok(
                                usuarioService.atualizarImagemPerfil(file));
        }

        @DeleteMapping("/me/imagem")
        public ResponseEntity<UsuarioResponseDTO> removerImagemPerfil() {

                return ResponseEntity.ok(
                                usuarioService.removerImagemPerfil());

        }

}