package by.fwsys.bot.bot_app.bots.notifications;

import by.fwsys.bot.bot_app.bots.commands.notifications.NotificationCommandResolver;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramNotificationBot implements LongPollingSingleThreadUpdateConsumer {

    private final List<NotificationCommandResolver> resolvers;
    private final TelegramClient tgNotificationClient;

    @Override
    public void consume(Update update) {
        try {
            if (!update.hasMessage() || !update.getMessage().hasText()) {
                sendIllegalCommand(getChatId(update));
                return;
            }

            if (Objects.equals(getText(update), "/start")) {
                sendHelloMessage(getChatId(update));
                return;
            }

            Optional<NotificationCommandResolver> optResolver = resolvers.stream().filter(resolver -> resolver.isCommand(getText(update))).findAny();
            optResolver.ifPresent(notificationCommandResolver -> {
                boolean processed = notificationCommandResolver.processCommand(TargetMessengerType.TELEGRAM, getText(update), getChatId(update));
                sendCommandResult(getChatId(update), processed);
            });

            if (optResolver.isEmpty()) {
                sendIllegalCommand(getChatId(update));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    @SneakyThrows
    private void sendHelloMessage(String chatId) {
        tgNotificationClient.execute(SendMessage.builder()
                .text("""
                        Зарегистрируйтесь используя команду `/register <code>`
                        Где `<code>` указан на сайте во вкладке "Уведомления".
                        """)
                .chatId(chatId)
                .build());
    }

    @SneakyThrows
    private void sendIllegalCommand(String chatId) {
        tgNotificationClient.execute(SendMessage.builder()
                .text("Неправильная команда")
                .chatId(chatId)
                .build());
    }

    @SneakyThrows
    private void sendCommandResult(String chatId, boolean isSuccess) {
        tgNotificationClient.execute(SendMessage.builder()
                .text(isSuccess ? "Команда выполнена" : "Команда не выполнена (Что-то пошло не так)")
                .chatId(chatId)
                .build());
    }

    private String getChatId(Update update) {
        Long chatId = update.getMessage().getChatId();
        return chatId.toString();
    }

    private String getText(Update update) {
        return update.getMessage().getText();
    }
}
