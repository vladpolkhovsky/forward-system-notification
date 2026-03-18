package by.fwsys.bot.bot_app.configs;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class TelegramMessengerConfig {

    @Value("${app.telegram.notification.token}")
    private String tgNotificationClientToken;

    @Value("${app.telegram.customer.token}")
    private String tgCustomerClientToken;

    @Bean
    @Qualifier("tgNotificationClient")
    public TelegramClient tgNotificationClient() {
        return new OkHttpTelegramClient(tgNotificationClientToken);
    }

    @Bean
    @Qualifier("tgCustomerClient")
    public TelegramClient tgCustomerClient() {
        return new OkHttpTelegramClient(tgCustomerClientToken);
    }
}
