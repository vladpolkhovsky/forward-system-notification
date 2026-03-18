package by.fwsys.bot.bot_app.mappers;

import by.fwsys.bot.bot_app.dto.UserDto;
import by.fwsys.bot.bot_app.models.entities.UserEntity;
import io.jsonwebtoken.Claims;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toDto(UserEntity entity);

    default Map<String, String> toClaims(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        HashMap<String, String> claims = new HashMap<>();
        claims.put(UserDto.Fields.username, entity.getUsername());
        claims.put(UserDto.Fields.id, entity.getId().toString());
        claims.put(UserDto.Fields.authorities, String.join(",", entity.getAuthorities()));
        claims.put(UserDto.Fields.firstname, entity.getFirstname());
        claims.put(UserDto.Fields.lastname, entity.getLastname());

        return claims;
    }

    default UserDto fromClaims(Claims claims) {
        UserDto userDto = new UserDto();

        String authoritiesStr = claims.get(UserDto.Fields.authorities, String.class);
        Set<String> authorities = Arrays.stream(authoritiesStr.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        userDto.setId(Long.valueOf(claims.get(UserDto.Fields.id, String.class)));
        userDto.setUsername(claims.get(UserDto.Fields.username, String.class));
        userDto.setLastname(claims.get(UserDto.Fields.lastname, String.class));
        userDto.setFirstname(claims.get(UserDto.Fields.firstname, String.class));
        userDto.setAuthorities(authorities);

        return userDto;
    }
}
