package com.epis.services;

import com.epis.entities.Funcao;
import com.epis.entities.Funcionario;
import com.epis.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Autowired
    private FuncaoService funcaoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioService.getByLogin(username);

        Funcionario funcionario = funcionarioService.getById(usuario.getFuncionarioId());
        Funcao funcao = funcaoService.getById(funcionario.getFuncaoId());

        String role = funcao.getNome();

        return UserDetailsImpl.build(usuario, role);
    }

}
