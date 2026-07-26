package atlas.security;

import atlas.entity.Usuario;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {

        this.key = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expiration = expiration;
    }

    public String gerarToken(Usuario usuario) {

        return Jwts.builder()
                .subject(usuario.getEmail())

                .claim("id", usuario.getId())
                .claim("role", usuario.getRole().name())

                .issuedAt(new Date())

                .expiration(
                        new Date(System.currentTimeMillis() + expiration)
                )

                .signWith(key)

                .compact();
    }

    public String extrairEmail(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

}