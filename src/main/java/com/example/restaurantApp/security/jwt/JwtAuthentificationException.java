package com.example.restaurantApp.security.jwt;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthentificationException extends AuthenticationException {

    public JwtAuthentificationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthentificationException(String msg) {
        super(msg);
    }
}
