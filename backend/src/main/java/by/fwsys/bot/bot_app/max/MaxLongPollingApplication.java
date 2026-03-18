package by.fwsys.bot.bot_app.max;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

public class MaxLongPollingApplication extends Thread {

    private static final Map<String, MaxUpdateConsumer> consumers = new HashMap<>();
    private static final Map<String, Long> lastUpdateDate = new HashMap<>();

    private static final String MAX_API_URL = "https://platform-api.max.ru/updates";

    public void register(String botToken, MaxUpdateConsumer bot) {
        consumers.put(botToken, bot);
    }

    @Override
    public void start() {
        while (isAlive()) {

        }
    }

    private static WebClient getLongPollingClient() {
        return WebClient.builder().baseUrl(MAX_API_URL).build();
    }
}
