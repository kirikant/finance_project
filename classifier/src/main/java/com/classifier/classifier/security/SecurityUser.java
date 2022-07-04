package com.classifier.classifier.security;


import com.classifier.classifier.entity.Role;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


public class SecurityUser implements UserDetails {



    private String username;
    private String password;
    private List<SimpleGrantedAuthority> authorities;
    private boolean isActive;


    public SecurityUser(String username, String password, List<SimpleGrantedAuthority> authorities, boolean isActive) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public static UserDetails transform(String token,String secret){

        String subject = Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                .getBody().getSubject();

        Role role = Role.valueOf(Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
                .getBody().get("role", String.class));

        return   new org.springframework.security.core.userdetails.User(
                subject,
                "1",
                true,
              true,
                true,
                true,
                role.getProperties()
        );
    }

}