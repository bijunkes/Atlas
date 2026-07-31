package atlas.security;

import atlas.dto.ErrorResponseDTO;
import atlas.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

        private final ObjectMapper objectMapper;

        @Override
        public void handle(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        AccessDeniedException accessDeniedException) throws IOException {

                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                response.setContentType("application/json");

                response.setCharacterEncoding("UTF-8");

                ErrorResponseDTO error = new ErrorResponseDTO(
                                "Você não possui permissão para acessar este recurso.",
                                ErrorCode.ACCESS_DENIED,
                                HttpServletResponse.SC_FORBIDDEN,
                                LocalDateTime.now());

                response.getWriter()
                                .write(
                                                objectMapper.writeValueAsString(error));

                response.getWriter().flush();
        }
}