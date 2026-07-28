package atlas.controller;

import atlas.dto.usuario.UsuarioResponseDTO;
import atlas.service.UsuarioService;
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
}