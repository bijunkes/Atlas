package atlas.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CookieService {

    private static final String ACCESS_COOKIE = "accessToken";
    private static final String REFRESH_COOKIE = "refreshToken";

    // Cria o cookie do Access Token
    public void criarAccessToken(HttpServletResponse response, String token) {

        ResponseCookie cookie = ResponseCookie.from(ACCESS_COOKIE, token)
                .httpOnly(true)
                .secure(false) // true em produção (HTTPS)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMinutes(15))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // Cria o cookie do Refresh Token
    public void criarRefreshToken(HttpServletResponse response, String token) {

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE, token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }


    // Remove o Access Token
    public void limparAccessToken(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from(ACCESS_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void limparRefreshToken(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from(REFRESH_COOKIE, "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

}