package com.kttkpm.FoodOrder.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        log.info("CustomAuthenticationSuccessHandler");
        log.info(authorities.toString());
        for (GrantedAuthority authority : authorities) {
            log.info(authority.getAuthority());
<<<<<<< HEAD
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
=======
            if (authority.getAuthority().equals("ADMIN")) {
>>>>>>> origin/tai-dev
                redirectUrl = "/admin";
                break;
            } else if (authority.getAuthority().equals("USER")) {
                redirectUrl = "/users";
                break;
            }
        }

        if (redirectUrl == null) {
            throw new IllegalStateException();
        }
        response.sendRedirect(redirectUrl);
    }
}