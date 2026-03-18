package by.fwsys.bot.bot_app.services;

import by.fwsys.bot.bot_app.dto.UserDto;
import by.fwsys.bot.bot_app.mappers.UserMapper;
import by.fwsys.bot.bot_app.models.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class JwtService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${app.jwt.expiration}")
    private long expiration;

    public JwtService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String login) {
        log.debug("Generating JWT token for user: {}", login);
        
        String token = userRepository.findByUsername(login)
                .map(userMapper::toClaims)
                .map(claims -> Jwts.builder()
                        .subject(login)
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + expiration))
                        .signWith(getSigningKey())
                        .claims(claims)
                        .compact())
                .orElse(null);
        
        if (token != null) {
            log.info("JWT token generated successfully for user: {}", login);
        } else {
            log.warn("Failed to generate JWT token - user not found: {}", login);
        }
        
        return token;
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaimsInternal(token);
            boolean isValid = !claims.getExpiration().before(new Date());
            log.debug("Token validation result: {}", isValid);
            return isValid;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public UserDto extractUser(String token) {
        log.debug("Extracting user from token");
        Claims claims = extractClaimsInternal(token);
        return userMapper.fromClaims(claims);
    }

    public boolean checkCredentials(String login, String password) {
        log.debug("Checking credentials for user: {}", login);
        
        boolean isValid = userRepository.findByUsername(login)
                .map(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(false);
        
        log.debug("Credentials check result for user {}: {}", login, isValid ? "success" : "failure");
        return isValid;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> extractAllClaims(String token) {
        log.debug("Extracting all claims from token");
        
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        e -> String.valueOf(e.getValue())
                ));
    }

    private Claims extractClaimsInternal(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
