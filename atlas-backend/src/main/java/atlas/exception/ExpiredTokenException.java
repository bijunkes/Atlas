package atlas.exception;

public class ExpiredTokenException extends RuntimeException {

    public ExpiredTokenException() {
        super("Token expirado");
    }

}
