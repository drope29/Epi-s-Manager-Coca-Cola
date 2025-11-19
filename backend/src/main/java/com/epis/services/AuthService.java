package com.epis.services;

import com.epis.dtos.AccessDto;
import com.epis.dtos.AuthenticationDto;
import com.epis.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public AccessDto login(AuthenticationDto authDto) {

        try {

            UsernamePasswordAuthenticationToken userAuth =
                    new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(userAuth);

            UserDetailsImpl userAuthenticate = (UserDetailsImpl)authentication.getPrincipal();

            String token = jwtUtils.generetaTokenFromUserDetailsImpl(userAuthenticate);

            return new AccessDto(token);

        } catch (BadCredentialsException e) {
            System.out.println("Credenciais Incoretas. " + e.getMessage());
        }

        return new AccessDto("Acesso negado");

    }

}
