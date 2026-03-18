package by.fwsys.bot.bot_app.services.customer;

import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import by.fwsys.bot.bot_app.models.entities.CustomerBotEntity;
import by.fwsys.bot.bot_app.models.entities.ForwardOrderEntity;
import by.fwsys.bot.bot_app.models.repository.CustomerBotRepository;
import by.fwsys.bot.bot_app.models.repository.ForwardOrderRepository;
import by.fwsys.bot.bot_app.utils.TgSendHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Component
@RequiredArgsConstructor
public class TelegramCustomerRegistrationService implements CustomerRegistrationService {

    private final ForwardOrderRepository forwardOrderRepository;
    private final CustomerBotRepository customerBotRepository;
    private final TelegramClient tgCustomerClient;

    @Override
    public TargetMessengerType getTargetMessenger() {
        return TargetMessengerType.TELEGRAM;
    }

    @Override
    public void register(String code, String chatId) {
        forwardOrderRepository.findByCode(code).ifPresent(order -> {
            CustomerBotEntity customerBot = customerBotRepository.findByForwardOrderIdAndTgChatId(order.getId(), chatId).orElse(new CustomerBotEntity());
            customerBot.setTgChatId(chatId);
            customerBot.setForwardOrderId(order.getId());

            customerBotRepository.save(customerBot);

            setupSync(order.getChat().getChatName(), chatId);
        });
    }

    private void setupSync(String chatName, String chatId) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                SendMessage sendMessage = SendMessage.builder().text("""
                                Вы успешно присоединились к чату заказа %s
                                """.formatted(chatName))
                        .chatId(chatId)
                        .build();
                TgSendHelper.send(tgCustomerClient, sendMessage);
            }
        });
    }
}
