package user.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import user.entity.UserEntity;

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

    public static UserDetails transform(UserEntity user){
      return   new org.springframework.security.core.userdetails.User(
                user.getLogin(),
              user.getPassword(),
                true,
              true,
                true,
                true,
                user.getRole().getProperties()
        );
    }

}