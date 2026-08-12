package atlas.dto.conta;

import atlas.enums.TipoConta;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ContaRequestDTO(

        Long instituicaoId,

        @NotBlank(message = "O nome da conta é obrigatório.")
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String nome,

        @NotNull(message = "O tipo da conta é obrigatório.")
        TipoConta tipo,

        @NotNull(message = "O saldo inicial é obrigatório.")
        @DecimalMin(
                value = "0.00",
                message = "O saldo inicial não pode ser negativo."
        )
        @Digits(
                integer = 10,
                fraction = 2,
                message = "O saldo deve ter no máximo 2 casas decimais."
        )
        BigDecimal saldoInicial,

        String numeroAgencia,

        String numeroConta

) {}