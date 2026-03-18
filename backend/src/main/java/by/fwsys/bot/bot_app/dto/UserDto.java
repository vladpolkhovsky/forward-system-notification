package by.fwsys.bot.bot_app.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@FieldNameConstants
public class UserDto {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private Set<String> authorities;

    public String getFio() {
        return Stream.of(getFirstname(), getLastname())
                .filter(Objects::nonNull)
                .filter(Predicate.not(String::isBlank))
                .collect(Collectors.joining(" "));
    }
}
