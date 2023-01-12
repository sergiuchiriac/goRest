package com.rest.goRest.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class ApiKeyAuthToken implements Authentication {
    private String apiKey;
    private Collection<? extends GrantedAuthority> authorities;

    public ApiKeyAuthToken(String apiKey) {
        this(apiKey, Collections.emptyList());
    }

    public ApiKeyAuthToken(String apiKey, Collection<? extends GrantedAuthority> authorities) {
        this.apiKey = apiKey;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return apiKey;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        return null;
    }
}
