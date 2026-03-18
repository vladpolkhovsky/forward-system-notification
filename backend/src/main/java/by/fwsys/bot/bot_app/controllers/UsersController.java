package by.fwsys.bot.bot_app.controllers;

import by.fwsys.bot.bot_app.dto.UserDto;
import by.fwsys.bot.bot_app.mappers.UserMapper;
import by.fwsys.bot.bot_app.models.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserDto>> list() {
        log.debug("Fetching all users");
        
        List<UserDto> users = userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
        
        log.info("Retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }
}
