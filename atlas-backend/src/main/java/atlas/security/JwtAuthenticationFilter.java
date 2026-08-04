package atlas.security;

import atlas.dto.ErrorResponseDTO;
import atlas.exception.ErrorCode;
import atlas.repository.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import io.jsonwebtoken.ExpiredJwtException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final UsuarioRepository usuarioRepository;
        private final ObjectMapper objectMapper;

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain) throws ServletException, IOException {

                String token = extrairToken(request);

                if (token == null) {
                        filterChain.doFilter(request, response);
                        return;
                }

                try {

                        String email = jwtService.extrairEmail(token);

                        autenticarUsuario(email, request);

                } catch (ExpiredJwtException e) {

                        enviarErro(
                                        response,
                                        "Sua sessão expirou.",
                                        ErrorCode.TOKEN_EXPIRED);

                        return;

                } catch (Exception e) {

                        enviarErro(
                                        response,
                                        "Token inválido.",
                                        ErrorCode.TOKEN_INVALID);

                        return;
                }

                filterChain.doFilter(request, response);

        }

        private void enviarErro(
                        HttpServletResponse response,
                        String mensagem,
                        ErrorCode code

        ) throws IOException {

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                ErrorResponseDTO error = new ErrorResponseDTO(
                                mensagem,
                                code,
                                401,
                                LocalDateTime.now());

                response.getWriter()
                                .write(
                                                objectMapper.writeValueAsString(error));
        }

        private String extrairToken(HttpServletRequest request) {

                if (request.getCookies() == null) {
                        return null;
                }

                for (var cookie : request.getCookies()) {

                        if (cookie.getName().equals("accessToken")) {
                                return cookie.getValue();
                        }
                }

                return null;
        }

        private void autenticarUsuario(
                        String email,
                        HttpServletRequest request) {

                if (SecurityContextHolder.getContext().getAuthentication() != null) {
                        return;
                }

                var usuario = usuarioRepository
                                .findByEmail(email)
                                .orElse(null);

                if (usuario == null) {
                        return;
                }

                User userDetails = (User) User
                                .withUsername(usuario.getEmail())
                                .password(usuario.getSenha())
                                .roles(usuario.getRole().name())
                                .build();

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(
                                new WebAuthenticationDetailsSource()
                                                .buildDetails(request));

                SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);
        }
}