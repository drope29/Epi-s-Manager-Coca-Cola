package com.epis.security.jwt;

import com.epis.entities.Usuario;
import com.epis.services.FuncaoService;
import com.epis.services.FuncionarioService;
import com.epis.services.UserDetailServiceImpl;
import com.epis.services.UsuarioService;
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

    @Autowired
    private UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if (path.startsWith("/auth/login") || path.startsWith("/api/usuarios/")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = getToken(request);

            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtUtils.validateJwtToken(jwt)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido ou expirado");
                return;
            }

            String username = jwtUtils.getUsernameToken(jwt);
            String role = jwtUtils.getRoleFromToken(jwt);
            Integer jwtTokenVersion = jwtUtils.getTokenVersionFromToken(jwt);

            Usuario usuario = usuarioService.getByLogin(username);

            if (!jwtTokenVersion.equals(usuario.getTokenVersion())) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        "Token não é mais válido. Faça login novamente.");
                return;
            }

            UsernamePasswordAuthenticationToken auth =
                    getUsernamePasswordAuthenticationToken(role, username);

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

        } catch (Exception e) {
            logger.error("Erro ao processar token", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
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
