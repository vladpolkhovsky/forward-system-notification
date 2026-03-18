package by.fwsys.bot.bot_app.services.customer;

import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.models.repository.CustomerBotRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerNotificationDispatcher {

    private final CustomerBotRepository customerBotRepository;
    private final List<CustomerNotificationMessageSenderService> senders;

    @SneakyThrows
    @Transactional
    public void dispatchAll(SendCustomerMessageDto messageDto) {
        customerBotRepository.findByForwardOrderId(messageDto.getTargetForwardOrderId()).ifPresent(cBot -> {
            if (cBot.getTgChatId() != null) send(TargetMessengerType.TELEGRAM, messageDto);
            if (cBot.getMaxChatId() != null) send(TargetMessengerType.MAX, messageDto);
            if (cBot.getVkChatId() != null) send(TargetMessengerType.VK, messageDto);
        });
    }

    private void send(TargetMessengerType type, SendCustomerMessageDto messageDto) {
        try {
            senders.stream().filter(sender -> Objects.equals(type, sender.getTargetMessenger())).findAny()
                    .ifPresent(sender -> sender.send(messageDto));
        } catch (Exception ex) {
            log.error("Cant send message to {} for order {}", type, messageDto.getTargetForwardOrderId(), ex);
        }
    }
}
