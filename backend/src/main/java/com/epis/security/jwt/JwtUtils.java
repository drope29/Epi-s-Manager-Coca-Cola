package com.epis.security.jwt;

import com.epis.services.UserDetailsImpl;
import com.epis.services.exception.InvalidJwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${epi.jwtSecret}")
    private String jwtSecret;

    @Value("${epi.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generetaTokenFromUserDetailsImpl(UserDetailsImpl userDetail) {

        Key key = getSigninKey();
        long now = System.currentTimeMillis();
        String role = userDetail.getUsuario().getFuncionario().getFuncao().getNome().toUpperCase();

        return Jwts.builder()
                .subject(userDetail.getUsername())
                .claim("role", role)
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public Key getSigninKey() {

            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

    }

    public String getUsernameToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    public boolean validateJwtToken(String authToken) {

        SecretKey key = (SecretKey) getSigninKey();

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);

            return true;

        } catch (MalformedJwtException e) {
            throw  new InvalidJwtTokenException("Token inválido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw  new InvalidJwtTokenException("Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw  new InvalidJwtTokenException("Token não suportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw  new InvalidJwtTokenException("Token argumento inválido: " + e.getMessage());
        }

    }

    public String getRoleFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
