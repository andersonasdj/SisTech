package br.com.techgol.app.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TwoFactorRedirectFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuthenticated = auth != null && auth.isAuthenticated();
        boolean isPre2FA = false;

        if (isAuthenticated) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            isPre2FA = authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_PRE_2FA"));
        }

        String requestURI = request.getRequestURI();

        if (isPre2FA && !requestURI.equals("/2fa") && !requestURI.equals("/verify-2fa") && !requestURI.startsWith("/logout")) {
        	
        	if ("GET".equalsIgnoreCase(request.getMethod())) {
        		response.sendRedirect(request.getContextPath() + "/2fa");
        	}else {
        	    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        	    response.getWriter().write("2FA required");
        	}
            return;
        }
        filterChain.doFilter(request, response);
    }
}
