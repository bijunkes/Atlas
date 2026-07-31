package atlas.dto.transacao;

import atlas.enums.StatusTransacao;
import atlas.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequestDTO(

        Long contaId,
        Long categoriaId,
        BigDecimal valor,
        String descricao,
        TipoTransacao tipo,
        StatusTransacao status,
        LocalDate dataTransacao,
        String observacao
        
) {}
