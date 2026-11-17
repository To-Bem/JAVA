package br.com.fiap_tdsps.to_bem_api.security;

import br.com.fiap_tdsps.to_bem_api.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey key;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}")String base64Secret, @Value("${app.jwt.expiration-ms}")long expirationMs) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.expirationMs = expirationMs;
    }

    public String generateToken(Usuario usuario) {
        Date now = new Date();
        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("role",usuario.getRole().name())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs)) //exp
                .signWith(key)
                .compact();
    }

    public long getExpirationSeconds() {
        return this.expirationMs/1000;
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = parseToken(token);
        return claims.getSubject().equals(userDetails.getUsername())
                && claims.getExpiration().after(new Date());
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build().parseSignedClaims(token).getPayload();
    }

}
