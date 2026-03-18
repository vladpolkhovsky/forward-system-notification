package by.fwsys.bot.bot_app.configs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import by.fwsys.bot.bot_app.resolvers.JwtUserArgumentResolver;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtUserArgumentResolver jwtUserArgumentResolver;

    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter,
                          @Lazy JwtUserArgumentResolver jwtUserArgumentResolver) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtUserArgumentResolver = jwtUserArgumentResolver;
        
        log.info("SecurityConfig initialized");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/auth/**").permitAll()
                            .requestMatchers("/**").hasAuthority("ADMIN")
                            .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

            log.info("Security filter chain configured successfully");
            return http.build();
        } catch (Exception e) {
            log.error("Failed to configure security filter chain", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtUserArgumentResolver);
        log.debug("Added JWT argument resolver");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Password encoder bean created");
        return new BCryptPasswordEncoder();
    }
}
