package com.epis.services;

import com.epis.dtos.AccessDto;
import com.epis.dtos.AuthenticationDto;
import com.epis.dtos.UsuarioUpdateDto;
import com.epis.entities.Funcao;
import com.epis.entities.Funcionario;
import com.epis.entities.Usuario;
import com.epis.security.jwt.JwtUtils;
import com.epis.services.exception.InvalidJwtTokenException;
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
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncaoService funcaoService;

    public AccessDto login(AuthenticationDto authDto) {

        try {
            UsernamePasswordAuthenticationToken userAuth =
                    new UsernamePasswordAuthenticationToken(authDto.getUsername(), authDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(userAuth);

            Usuario usuario = usuarioService.getByLogin(authDto.getUsername());

            UsuarioUpdateDto usuarioUpd =
                    new UsuarioUpdateDto(usuario.getPassword(), usuario.getTokenVersion() + 1);

            usuarioService.update(usuario.getUsuarioId(), usuarioUpd);

            Usuario usuarioAtualizado = usuarioService.getByLogin(authDto.getUsername());

            String role = loadRoleForUsuario(usuarioAtualizado);

            UserDetailsImpl userDetails = UserDetailsImpl.build(usuarioAtualizado, role);

            String token = jwtUtils.generetaTokenFromUserDetailsImpl(userDetails);

            return new AccessDto(token);

        } catch (BadCredentialsException e) {
            throw new InvalidJwtTokenException("Credenciais incorretas. Acesso negado: " + e.getMessage());
        }
    }

    private String loadRoleForUsuario(Usuario usuario) {

        Funcionario funcionario = funcionarioService.getById(usuario.getFuncionarioId());

        Funcao funcao = funcaoService.getById(funcionario.getFuncaoId());

        return funcao.getNome();
    }

}
