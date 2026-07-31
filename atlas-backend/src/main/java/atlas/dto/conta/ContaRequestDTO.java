package atlas.dto.conta;

import atlas.enums.TipoConta;

import java.math.BigDecimal;

public record ContaRequestDTO(

        Long instituicaoId,
        String nome,
        TipoConta tipo,
        BigDecimal saldoInicial,
        String numeroAgencia,
        String numeroConta
        
) {}
