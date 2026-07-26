package atlas.dto;

import atlas.exception.ErrorCode;
import java.time.LocalDateTime;

public record ErrorResponseDTO(

        String message,
        ErrorCode code,
        int status,
        LocalDateTime timestamp

){}