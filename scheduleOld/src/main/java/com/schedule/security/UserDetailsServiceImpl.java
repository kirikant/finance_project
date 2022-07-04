package com.schedule.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${jwt.secret}")
    String secret;

    @Override
    public UserDetails loadUserByUsername(String token) throws EntityNotFoundException {
        return SecurityUser.transform(token,secret);
    }
}