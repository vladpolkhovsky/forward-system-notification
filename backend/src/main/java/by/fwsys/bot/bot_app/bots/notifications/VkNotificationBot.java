package by.fwsys.bot.bot_app.bots.notifications;

import by.fwsys.bot.bot_app.bots.commands.notifications.NotificationCommandResolver;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.events.longpoll.GroupLongPollApi;
import com.vk.api.sdk.objects.callback.MessageNew;
import com.vk.api.sdk.objects.callback.MessageObject;
import com.vk.api.sdk.objects.messages.Message;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class VkNotificationBot extends GroupLongPollApi {

    private final VkApiClient vk;
    private final GroupActor actor;

    @Autowired
    private List<NotificationCommandResolver> resolvers;

    public VkNotificationBot(VkApiClient client, GroupActor actor, @Value("${app.vk.notification.waitTime}") int waitTime) {
        super(client, actor, waitTime);
        this.actor = actor;
        this.vk = client;
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        vk.groups().setLongPollSettings(actor, actor.getGroupId())
                .enabled(true)
                .messageNew(true)
                .messageReply(false)
                .execute();
        run();
    }

    @Override
    public void messageNew(Integer groupId, MessageNew message) {
        try {
            Optional<NotificationCommandResolver> optResolver = resolvers.stream().filter(resolver -> resolver.isCommand(getText(message))).findAny();
            optResolver.ifPresent(notificationCommandResolver -> {
                boolean processed = notificationCommandResolver.processCommand(TargetMessengerType.VK, getText(message), getChatId(message).toString());
                sendCommandResult(getChatId(message), processed);
            });

            if (optResolver.isEmpty()) {
                sendIllegalCommand(getChatId(message));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

    private String getText(MessageNew messageNew) {
        return Optional.ofNullable(messageNew)
                .map(MessageNew::getObject)
                .map(MessageObject::getMessage)
                .map(Message::getText)
                .orElse(null);
    }

    private Long getChatId(MessageNew messageNew) {
        return Optional.ofNullable(messageNew)
                .map(MessageNew::getObject)
                .map(MessageObject::getMessage)
                .map(Message::getFromId)
                .orElse(null);
    }

    @SneakyThrows
    private void sendCommandResult(Long chatId, boolean isSuccess) {
        vk.messages().sendUserIds(actor)
                .userId(chatId)
                .randomId(0)
                .message(isSuccess ? "Команда выполнена" : "Команда не выполнена (Что-то пошло не так)")
                .executeAsString();
    }

    @SneakyThrows
    private void sendIllegalCommand(Long chatId) {
        vk.messages().sendUserIds(actor)
                .userId(chatId)
                .randomId(0)
                .message("Неправильная команда")
                .executeAsString();

    }
}
