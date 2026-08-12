package atlas.controller;

import atlas.dto.conta.ContaRequestDTO;
import atlas.dto.conta.ContaResponseDTO;
import atlas.service.ContaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@RequiredArgsConstructor
public class ContaController {

    private final ContaService contaService;

    @PostMapping
    public ResponseEntity<ContaResponseDTO> criar(
            @Valid @RequestBody ContaRequestDTO dados
    ) {

        ContaResponseDTO conta =
                contaService.criar(dados);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(conta);
    }

    @GetMapping
    public ResponseEntity<List<ContaResponseDTO>> listar() {

        return ResponseEntity.ok(
                contaService.listarMinhasContas()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> buscarPorId(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                contaService.buscarPorId(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ContaRequestDTO dados
    ) {

        return ResponseEntity.ok(
                contaService.atualizar(id, dados)
        );
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(
            @PathVariable Long id
    ) {

        contaService.desativar(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(
            @PathVariable Long id
    ) {

        contaService.reativar(id);

        return ResponseEntity.noContent().build();
    }
}