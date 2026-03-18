package by.fwsys.bot.bot_app.controllers;

import by.fwsys.bot.bot_app.dto.LoginRequest;
import by.fwsys.bot.bot_app.dto.UserDto;
import by.fwsys.bot.bot_app.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        log.info("Login attempt for user: {}", request.login());
        
        if (!jwtService.checkCredentials(request.login(), request.password())) {
            log.warn("Failed login attempt for user: {}", request.login());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid credentials"));
        }

        String token = jwtService.generateToken(request.login());
        if (token == null) {
            log.error("Failed to generate token for user: {}", request.login());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid credentials"));
        }

        log.info("Successful login for user: {}", request.login());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/iam")
    public ResponseEntity<?> iam(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("IAM request with missing or invalid authorization header");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Missing or invalid token"));
        }

        String token = authHeader.substring(7);
        if (jwtService.validateToken(token)) {
            UserDto user = jwtService.extractUser(token);
            log.debug("IAM request validated for user: {}", user);
            return ResponseEntity.ok(Map.of("login", user));
        }

        log.warn("IAM request with invalid or expired token");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Invalid or expired token"));
    }
}
