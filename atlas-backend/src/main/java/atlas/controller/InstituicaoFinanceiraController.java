package atlas.controller;

import atlas.entity.InstituicaoFinanceira;
import atlas.service.InstituicaoFinanceiraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instituicoes-financeiras")
@RequiredArgsConstructor
public class InstituicaoFinanceiraController {

    private final InstituicaoFinanceiraService instituicaoFinanceiraService;

    @GetMapping
    public ResponseEntity<List<InstituicaoFinanceira>> listar() {

        return ResponseEntity.ok(
                instituicaoFinanceiraService.listarAtivas()
        );
    }
}