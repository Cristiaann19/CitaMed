package com.app.CitaMed.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();
        // Only skip JWT validation for truly public endpoints
        if (path.startsWith("/api/auth/")
                || path.startsWith("/api/reniec/")
                || path.startsWith("/api/lading")
                || path.startsWith("/api/landing")
                || path.startsWith("/api/contacto")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token requerido\"}");
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validarToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
            return;
        }

        String username = jwtUtil.extraerUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            java.util.List<String> roles = jwtUtil.extraerRoles(token);
            // Log extracted username and roles for debugging
            logger.info("[JwtFilter] user=\"{}\" roles={}", username, roles);
            java.util.List<GrantedAuthority> authorities = new java.util.ArrayList<>();
            for (String r : roles) {
                // Normalize roles to Spring Security convention ROLE_<NAME>
                String roleName = r.startsWith("ROLE_") ? r : "ROLE_" + r;
                authorities.add(new SimpleGrantedAuthority(roleName));
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
