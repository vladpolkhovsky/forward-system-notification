package by.fwsys.bot.bot_app.configs;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VkMessengerConfig {

    @Value("${app.vk.notification.token}")
    private String tgNotificationClientToken;

    @Value("${app.vk.notification.groupId}")
    private Long tgNotificationClientGroupId;

    @Bean
    @Qualifier("vkNotificationClient")
    public VkApiClient vkNotificationClient() {
        return new VkApiClient(new HttpTransportClient());
    }

    @Bean
    @Qualifier("vkNotificationGroupActor")
    public GroupActor vkNotificationGroupActor() {
        return new GroupActor(tgNotificationClientGroupId, tgNotificationClientToken);
    }
}
