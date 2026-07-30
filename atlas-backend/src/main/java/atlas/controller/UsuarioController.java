package atlas.controller;

import atlas.dto.usuario.AtualizarUsuarioDTO;
import atlas.dto.usuario.UsuarioResponseDTO;
import atlas.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> me(){

        return ResponseEntity.ok(
                usuarioService.buscarPerfil()
        );
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(
            @Valid @RequestBody AtualizarUsuarioDTO dto
    ){

        return ResponseEntity.ok(
                usuarioService.atualizar(dto)
        );

    }
}