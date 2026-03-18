package by.fwsys.bot.bot_app.models.entities;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Entity
@Immutable
@Table(name = "users", schema = "forward_system")
public class UserEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "firstname", nullable = false, length = 255)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 255)
    private String lastname;

    @Column(name = "roles", nullable = false, length = 512)
    private String roles;

    public Set<String> getAuthorities() {
        String roles = Optional.ofNullable(getRoles()).orElse("");
        return Arrays.stream(roles.split(","))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
