package by.fwsys.bot.bot_app.formatters;

import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VkNotificationMessageFormatter extends AbstractNotificationMessageFormatter<String> {

    @Override
    public String format(SendNotificationMessageDto messageDto) {
        return this.getBaseFormattedText(messageDto);
    }

    @Override
    public String format(SendCustomerMessageDto messageDto) {
        return this.getBaseFormattedText(messageDto);
    }

    @Override
    public String getBaseFormattedText(SendNotificationMessageDto messageDto) {
        log.debug("Formatting Vk notification message: title='{}', level='{}'",
                messageDto.getTittle(), messageDto.getInformationLevel());

        StringBuilder messageBuilder = new StringBuilder();

        String levelIcon = getLevelIcon(messageDto.getInformationLevel());

        // Title
        if (hasContent(messageDto.getTittle())) {
            messageBuilder.append("%s %s".formatted(levelIcon, messageDto.getTittle()));
            messageBuilder.append("\n\n");
        }

        // Description
        if (hasContent(messageDto.getDescription())) {
            messageBuilder.append("📝 Сообщение:\n\n");
            messageBuilder.append(messageDto.getDescription());
            messageBuilder.append("\n\n");
        }

        // From user
        if (hasContent(messageDto.getFromUsername())) {
            messageBuilder.append("👤 От: ").append(messageDto.getFromUsername());
            messageBuilder.append("\n\n");
        }

        // Tags
        Set<String> tags = messageDto.getTags();
        if (tags != null && !tags.isEmpty()) {
            messageBuilder.append("🏷️ Теги: ");
            String tagString = tags.stream()
                    .map(String::trim)
                    .map(text -> text.replaceAll("\\s+", "_"))
                    .map(tag -> "#" + tag)
                    .collect(Collectors.joining(" "));
            messageBuilder.append(tagString);
        }

        log.trace("Formatted message content: {}", messageBuilder);

        return messageBuilder.toString();
    }

    @Override
    public TargetMessengerType getType() {
        return TargetMessengerType.VK;
    }
}
