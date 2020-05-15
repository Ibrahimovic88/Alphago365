package com.alphago365.octopus.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserPrincipal extends User {

    private Long id;

    public UserPrincipal(String username, String password, Collection<? extends GrantedAuthority> authorities
            , Long id) {
        super(username, password, authorities);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
