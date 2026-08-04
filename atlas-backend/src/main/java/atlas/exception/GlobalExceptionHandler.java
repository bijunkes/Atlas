package atlas.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import atlas.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(InvalidCredentialsException.class)
        public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(
                        InvalidCredentialsException e) {

                return buildError(
                                HttpStatus.UNAUTHORIZED,
                                ErrorCode.INVALID_CREDENTIALS,
                                e.getMessage());
        }

        @ExceptionHandler(SocialLoginException.class)
        public ResponseEntity<ErrorResponseDTO> handleSocialLogin(
                        SocialLoginException e) {

                return buildError(
                                HttpStatus.CONFLICT,
                                ErrorCode.SOCIAL_LOGIN,
                                e.getMessage());
        }

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExists(
                        EmailAlreadyExistsException e) {

                return buildError(
                                HttpStatus.CONFLICT,
                                ErrorCode.EMAIL_ALREADY_EXISTS,
                                e.getMessage());
        }

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDTO> handleNotFound(
                        ResourceNotFoundException e) {

                return buildError(
                                HttpStatus.NOT_FOUND,
                                ErrorCode.RESOURCE_NOT_FOUND,
                                e.getMessage());
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<ErrorResponseDTO> handleUnauthorized(
                        UnauthorizedException e) {

                return buildError(
                                HttpStatus.UNAUTHORIZED,
                                ErrorCode.UNAUTHORIZED,
                                e.getMessage());
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponseDTO> handleValidation(
                        MethodArgumentNotValidException e) {

                String message = e.getBindingResult()
                                .getFieldErrors()
                                .get(0)
                                .getDefaultMessage();

                return buildError(
                                HttpStatus.BAD_REQUEST,
                                ErrorCode.VALIDATION_ERROR,
                                message);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDTO> handleGeneric(
                        Exception e) {
                return buildError(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ErrorCode.INTERNAL_SERVER_ERROR,
                                "Erro interno do servidor");
        }

        private ResponseEntity<ErrorResponseDTO> buildError(
                        HttpStatus status,
                        ErrorCode code,
                        String message) {

                return ResponseEntity
                                .status(status)
                                .body(
                                                new ErrorResponseDTO(
                                                                message,
                                                                code,
                                                                status.value(),
                                                                LocalDateTime.now()));
        }

}