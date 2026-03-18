package by.fwsys.bot.bot_app.utils;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.message.Message;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TgSendHelper {

    @SneakyThrows
    public static Message send(TelegramClient client, SendMessage sendMessage) {
        return client.execute(sendMessage);
    }
}
