package by.fwsys.bot.bot_app.bots.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramCustomerBot implements LongPollingSingleThreadUpdateConsumer {

    @Override
    public void consume(Update update) {

    }
}
