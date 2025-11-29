package com.epis.services;

import com.epis.entities.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private Usuario usuario;
    private String role;
    private Integer tokenVersion;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    public UserDetailsImpl(Usuario usuario, String role) {
        this.usuario = usuario;
        this.role = role;
        this.tokenVersion = usuario.getTokenVersion();
    }

    public static UserDetailsImpl build(Usuario usuario, String role) {

        return new UserDetailsImpl(usuario, role);

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public Integer getTokenVersion() { return tokenVersion; }

    public void setTokenVersion(Integer tokenVersion) { this.tokenVersion = tokenVersion; }
}
