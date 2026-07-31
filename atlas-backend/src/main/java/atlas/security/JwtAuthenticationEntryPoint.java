package atlas.security;

import atlas.dto.ErrorResponseDTO;
import atlas.exception.ErrorCode;

import tools.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component // Crie uma instância da classe automaticamente
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint { // Essa interface pertence ao Spring
                                                                               // Security

        private final ObjectMapper objectMapper;

        public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
                this.objectMapper = objectMapper;
        }

        @Override
        public void commence( // Possui apenas um método obrigatório
                        HttpServletRequest request,
                        HttpServletResponse response, // Resposta a enviar para o front-end
                        AuthenticationException authException // Contém o motivo da falha
        ) throws IOException, ServletException {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Usuário não está autenticado

                response.setContentType("application/json"); // Resposta em formato JSON

                response.setCharacterEncoding("UTF-8"); // Resposta ser erros ortográficos

                // Criando erro
                ErrorResponseDTO error = new ErrorResponseDTO(
                                "Não autorizado. Faça login novamente.",
                                ErrorCode.UNAUTHORIZED,
                                HttpServletResponse.SC_UNAUTHORIZED,
                                LocalDateTime.now());

                // Convertendo erro em JSON
                response.getWriter()
                                .write(
                                                objectMapper.writeValueAsString(error));
        }

}