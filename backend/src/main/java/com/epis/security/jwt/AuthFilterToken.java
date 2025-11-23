package com.epis.security.jwt;

import com.epis.services.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class AuthFilterToken extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/auth/") || path.startsWith("/api/usuarios/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            String jwt = getToken(request);

            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                
                String username = jwtUtils.getUsernameToken(jwt);
                String role = jwtUtils.getRoleFromToken(jwt);

                UsernamePasswordAuthenticationToken auth = getUsernamePasswordAuthenticationToken(role, username);

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Setar no contexto
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (Exception e) {
            logger.error("Erro ao processar token", e);
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String role, String username) {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + role);

        UserDetails userDetails =
                new org.springframework.security.core.userdetails.User(
                        username,
                        "",
                        List.of(authority)
                );

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
        return auth;
    }


    private String getToken(HttpServletRequest request) {

        String headerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(headerToken) && headerToken.startsWith("Bearer")) {

            return headerToken.replace("Bearer ", "");

        }

        return null;

    }
}
