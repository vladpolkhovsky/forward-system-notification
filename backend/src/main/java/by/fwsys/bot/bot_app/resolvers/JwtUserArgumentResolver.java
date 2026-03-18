package by.fwsys.bot.bot_app.resolvers;

import by.fwsys.bot.bot_app.annotations.JwtUser;
import by.fwsys.bot.bot_app.dto.UserDto;
import by.fwsys.bot.bot_app.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Resolver для обработки параметров с аннотацией @JwtUser.
 * Извлекает UserDto из заголовка Authorization.
 */
@Component
public class JwtUserArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    public JwtUserArgumentResolver(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtUser.class)
                && parameter.getParameterType().equals(UserDto.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {
        HttpServletRequest httpRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (httpRequest == null) {
            throw new UnauthorizedException("Unable to get HTTP request");
        }

        String authHeader = httpRequest.getHeader(AUTHORIZATION_HEADER);

        if (authHeader == null || authHeader.isBlank()) {
            throw new UnauthorizedException("Authorization header is missing");
        }

        if (!authHeader.startsWith(BEARER_PREFIX)) {
            throw new UnauthorizedException("Invalid authorization header format. Expected: Bearer <token>");
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        if (!jwtService.validateToken(token)) {
            throw new UnauthorizedException("Invalid or expired token");
        }

        return jwtService.extractUser(token);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
