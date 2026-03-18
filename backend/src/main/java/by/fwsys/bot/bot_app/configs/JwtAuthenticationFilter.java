package by.fwsys.bot.bot_app.configs;

import by.fwsys.bot.bot_app.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String requestUri = request.getRequestURI();

        log.debug("Processing authentication filter for URI: {}", requestUri);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtService.validateToken(token)) {
                Map<String, String> claims = jwtService.extractAllClaims(token);
                String username = claims.get("username");
                String authoritiesStr = claims.get("authorities");

                List<SimpleGrantedAuthority> authorities = Collections.emptyList();
                if (authoritiesStr != null && !authoritiesStr.isBlank()) {
                    authorities = Arrays.stream(authoritiesStr.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                }

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("User '{}' authenticated successfully for URI: {}", username, requestUri);
            } else {
                log.warn("Invalid JWT token for URI: {}", requestUri);
            }
        }

        filterChain.doFilter(request, response);
    }
}
