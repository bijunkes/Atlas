package atlas.dto.transferencia;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferenciaRequestDTO(

        Long contaOrigemId,
        Long contaDestinoId,
        BigDecimal valor,
        LocalDate dataTransferencia,
        String observacao
        
) {}
