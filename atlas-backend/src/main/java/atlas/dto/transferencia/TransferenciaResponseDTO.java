package atlas.dto.transferencia;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransferenciaResponseDTO(
        
        Long id,
        BigDecimal valor,
        String contaOrigem,
        String contaDestino,
        LocalDate dataTransferencia

) {}