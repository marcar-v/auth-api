package marcar_v.auth_api.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-minutes}")
    private String expirationMinutes;

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + Long.parseLong(expirationMinutes) * 60 * 1000);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public LocalDateTime calculateExpiration() {
        return LocalDateTime.now().plusMinutes(Long.parseLong(expirationMinutes));
    }
}
