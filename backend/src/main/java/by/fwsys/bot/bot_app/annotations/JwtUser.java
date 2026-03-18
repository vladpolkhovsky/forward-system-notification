package by.fwsys.bot.bot_app.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для извлечения UserDto из заголовка Authorization.
 * Применяется к параметрам контроллера.
 *
 * Пример использования:
 * <pre>
 * {@code @GetMapping("/profile")}
 * public ResponseEntity<UserDto> getProfile(@JwtUser UserDto user) {
 *     return ResponseEntity.ok(user);
 * }
 * </pre>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtUser {
}
