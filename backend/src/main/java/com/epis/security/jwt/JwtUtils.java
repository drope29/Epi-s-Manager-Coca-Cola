package com.epis.security.jwt;

import com.epis.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
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

        return Jwts.builder()
                .subject(userDetail.getUsername())
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
            System.out.println("Token inválido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token não suportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Token argumento inválido: " + e.getMessage());
        }

        return false;
    }

}
