package atlas.dto.transacao;

import atlas.enums.StatusTransacao;
import atlas.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponseDTO(

        Long id,
        BigDecimal valor,
        String descricao,
        TipoTransacao tipo,
        StatusTransacao status,
        LocalDate dataTransacao,
        String categoriaNome,
        String contaNome
        
) {}
