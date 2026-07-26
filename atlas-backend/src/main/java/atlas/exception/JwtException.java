package atlas.exception;

import lombok.Getter;

@Getter
public class JwtException extends RuntimeException {

    private final ErrorCode code;

    public JwtException(
            ErrorCode code,
            String message
    ){
        super(message);
        this.code = code;
    }

}