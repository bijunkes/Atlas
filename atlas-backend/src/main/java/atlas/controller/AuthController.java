package atlas.controller;

import atlas.dto.AuthResponseDTO;
import atlas.dto.LoginDTO;
import atlas.dto.RegisterDTO;
import atlas.dto.MessageResponseDTO;
import atlas.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

     private final UsuarioService usuarioService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterDTO dados
    ){

        usuarioService.register(dados);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(new MessageResponseDTO("Usuário criado."));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginDTO dados
    ){

        return ResponseEntity.ok(
                usuarioService.login(dados)
        );

    }

}