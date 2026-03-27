package by.fwsys.bot.bot_app.controllers;

import by.fwsys.bot.bot_app.dto.NotificationStatusDto;
import by.fwsys.bot.bot_app.dto.UserDto;
import by.fwsys.bot.bot_app.dto.UserShortDto;
import by.fwsys.bot.bot_app.dto.UserWithStatusDto;
import by.fwsys.bot.bot_app.mappers.NotificationBotMapper;
import by.fwsys.bot.bot_app.mappers.UserMapper;
import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import by.fwsys.bot.bot_app.models.entities.UserEntity;
import by.fwsys.bot.bot_app.models.repository.NotificationBotRepository;
import by.fwsys.bot.bot_app.models.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final NotificationBotRepository notificationBotRepository;
    private final NotificationBotMapper notificationBotMapper;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<Page<UserDto>> list() {
        log.debug("Fetching all users");

        Page<UserDto> users = userRepository.findAll(Pageable.unpaged(Sort.by(UserEntity.Fields.username)))
                .map(userMapper::toDto);

        log.info("Retrieved {} users", users.getTotalElements());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/status")
    @Transactional(readOnly = true)
    public ResponseEntity<Page<UserWithStatusDto>> listWithStatus() {
        log.debug("Fetching all users with status");

        Page<UserDto> users = userRepository.findAll(Pageable.unpaged(Sort.by(UserEntity.Fields.username)))
                .map(userMapper::toDto);

        Map<Long, NotificationStatusDto> userIdToStatus = notificationBotRepository.findAll().stream()
                .collect(Collectors.toMap(NotificationBotEntity::getId, notificationBotMapper::toDto));

        Page<UserWithStatusDto> mapped = users.map(u -> UserWithStatusDto.builder()
                .id(u.getId())
                .user(u)
                .status(userIdToStatus.getOrDefault(u.getId(), NotificationStatusDto.allOff(u.getId())))
                .build());

        log.info("Retrieved {} users with status", users.getTotalElements());
        return ResponseEntity.ok(mapped);
    }

    @GetMapping("/short")
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserShortDto>> listShortUsers() {
        log.debug("Fetching all users short");

        List<UserShortDto> shortDtos = userRepository.findAllNotBanned(Sort.by(UserEntity.Fields.username)).stream()
                .map(userMapper::toShortDto)
                .toList();

        log.info("Retrieved {} short users", shortDtos.size());
        return ResponseEntity.ok(shortDtos);
    }
}
