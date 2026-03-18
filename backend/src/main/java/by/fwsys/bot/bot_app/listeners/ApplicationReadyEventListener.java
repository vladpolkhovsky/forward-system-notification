package by.fwsys.bot.bot_app.listeners;

import by.fwsys.bot.bot_app.bots.notifications.TelegramNotificationBot;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationReadyEventListener {

    @Value("${app.telegram.notification.token}")
    private String tgNotificationClientToken;

    private final TelegramNotificationBot tgNotificationBot;

    @EventListener
    @SneakyThrows
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        TelegramBotsLongPollingApplication tgLoongPooling = new TelegramBotsLongPollingApplication();
        tgLoongPooling.registerBot(tgNotificationClientToken, tgNotificationBot);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> stopAll(tgLoongPooling)));
    }

    @SneakyThrows
    private void stopAll(TelegramBotsLongPollingApplication tgApp) {
        tgApp.stop();
    }
}
