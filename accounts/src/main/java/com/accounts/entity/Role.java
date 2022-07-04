package com.accounts.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Property.READ,Property.WRITE,Property.REFACTOR)),
    ADMIN(Set.of(Property.READ,Property.WRITE,Property.REFACTOR,Property.SPECIAL));

    private Set<Property> properties;

    Role(Set<Property> properties) {
        this.properties = properties;
    }

    public Set<SimpleGrantedAuthority> getProperties() {
        return properties.stream()
                .map(x->new SimpleGrantedAuthority(x.getProperty()))
                .collect(Collectors.toSet());
    }
}