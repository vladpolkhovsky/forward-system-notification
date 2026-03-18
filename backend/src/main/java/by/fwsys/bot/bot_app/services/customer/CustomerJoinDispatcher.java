package by.fwsys.bot.bot_app.services.customer;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerJoinDispatcher {

    private final List<CustomerRegistrationService> registrators;
    private final TelegramClient tgCustomerClient;

    @Transactional
    public boolean register(TargetMessengerType type, String code, String chatId) {
        CustomerRegistrationService registrationService = registrators.stream()
                .filter(r -> r.getTargetMessenger().equals(type))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No registrator found for type " + type));

        registrationService.register(code, chatId);

        return true;
    }
}
