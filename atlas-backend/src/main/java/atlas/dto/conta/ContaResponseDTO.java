package atlas.dto.conta;

import atlas.enums.TipoConta;

import java.math.BigDecimal;

public record ContaResponseDTO(
        Long id,
        String nome,
        TipoConta tipo,
        BigDecimal saldoAtual,
        String instituicaoNome,
        Boolean ativo
) {}