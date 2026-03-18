package by.fwsys.bot.bot_app.services.notifications;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.models.entities.BotNotificationCodeEntity;
import by.fwsys.bot.bot_app.models.repository.BotNotificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationRegistrationDispatcher {

    private final List<NotificationRegistrationService> registrators;
    private final BotNotificationCodeRepository botNotificationCodeRepository;

    @Transactional
    public boolean register(TargetMessengerType type, String code, String chatId) {
        Optional<BotNotificationCodeEntity> byCode = botNotificationCodeRepository.findByCode(code);
        if (byCode.isEmpty()) {
            return false;
        }

        Long userId = byCode.get().getId();
        NotificationRegistrationService registrationService = registrators.stream()
                .filter(r -> r.getTargetMessenger().equals(type))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No registrator found for type " + type));

        registrationService.register(userId, chatId);

        return true;
    }
}
