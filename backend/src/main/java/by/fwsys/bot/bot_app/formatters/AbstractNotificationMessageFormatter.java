package by.fwsys.bot.bot_app.formatters;

import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.InformationLevel;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractNotificationMessageFormatter<T> implements NotificationMessageFormatter<T> {

    public String getBaseFormattedText(SendNotificationMessageDto messageDto) {
        log.debug("Formatting Telegram notification message: title='{}', level='{}'",
                messageDto.getTittle(), messageDto.getInformationLevel());

        StringBuilder messageBuilder = new StringBuilder();

        String levelIcon = getLevelIcon(messageDto.getInformationLevel());

        // Title
        if (hasContent(messageDto.getTittle())) {
            messageBuilder.append("*%s %s*".formatted(levelIcon, escapeMarkdownV2(messageDto.getTittle())));
            messageBuilder.append("\n\n");
        }

        // Description
        if (hasContent(messageDto.getDescription())) {
            messageBuilder.append("*📝 Сообщение:*\n\n");
            messageBuilder.append(escapeMarkdownV2(messageDto.getDescription()));
            messageBuilder.append("\n\n");
        }

        // From user
        if (hasContent(messageDto.getFromUsername())) {
            messageBuilder.append("*👤 От:* ").append(escapeMarkdownV2(messageDto.getFromUsername()));
            messageBuilder.append("\n\n");
        }

        // Tags
        Set<String> tags = messageDto.getTags();
        if (tags != null && !tags.isEmpty()) {
            messageBuilder.append("*🏷️ Теги:* ");
            String tagString = tags.stream()
                    .map(String::trim)
                    .map(text -> text.replaceAll("\\s+", "_"))
                    .map(tag -> "#" + tag)
                    .map(this::escapeMarkdownV2)
                    .collect(Collectors.joining(" "));
            messageBuilder.append(tagString);
        }

        log.trace("Formatted message content: {}", messageBuilder);

        return messageBuilder.toString();
    }

    protected String getBaseFormattedText(SendCustomerMessageDto messageDto) {
        log.debug("Formatting Telegram notification message: title='{}', level='{}'",
                messageDto.getTittle(), messageDto.getInformationLevel());

        StringBuilder messageBuilder = new StringBuilder();

        String levelIcon = getLevelIcon(messageDto.getInformationLevel());

        // Title
        if (hasContent(messageDto.getTittle())) {
            messageBuilder.append("*%s %s*".formatted(levelIcon, escapeMarkdownV2(messageDto.getTittle())));
            messageBuilder.append("\n\n");
        }

        // Description
        if (hasContent(messageDto.getDescription())) {
            messageBuilder.append("*📝 Сообщение:*\n\n");
            messageBuilder.append(escapeMarkdownV2(messageDto.getDescription()));
            messageBuilder.append("\n\n");
        }

        // From user
        if (hasContent(messageDto.getFromUsername())) {
            messageBuilder.append("*👤 От:* ").append(escapeMarkdownV2(messageDto.getFromUsername()));
            messageBuilder.append("\n\n");
        }

        // Tags
        Set<String> links = messageDto.getLinks();
        if (links != null && !links.isEmpty()) {
            messageBuilder.append("*📁 Файлы:*\n");
            String tagString = links.stream()
                    .map(String::trim)
                    .map(this::escapeMarkdownV2)
                    .collect(Collectors.joining("\n"));
            messageBuilder.append(tagString).append("\n\n");
        }

        // Tags
        Set<String> tags = messageDto.getTags();
        if (tags != null && !tags.isEmpty()) {
            messageBuilder.append("*🏷️ Теги:* ");
            String tagString = tags.stream()
                    .map(String::trim)
                    .map(text -> text.replaceAll("\\s+", ""))
                    .map(tag -> "#" + tag)
                    .map(this::escapeMarkdownV2)
                    .collect(Collectors.joining(" "));
            messageBuilder.append(tagString);
        }

        log.trace("Formatted message content: {}", messageBuilder);

        return messageBuilder.toString();
    }

    protected String getLevelIcon(Object informationLevel) {
        if (informationLevel == null) {
            return InformationLevel.INFO.getIcon();
        }

        String level = informationLevel.toString().toUpperCase();

        try {
            InformationLevel infoLevel = InformationLevel.valueOf(level);
            return infoLevel.getIcon();
        } catch (IllegalArgumentException e) {
            log.warn("Unknown information level: {}, using default INFO", informationLevel);
            return InformationLevel.INFO.getIcon();
        }
    }

    protected boolean hasContent(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public String escapeMarkdownV2(String text) {
        if (text == null) {
            return "";
        }
        // List of characters to escape: _ * [ ] ( ) ~ ` > # + - = | { } . ! \
        return text.replaceAll("([_*\\[\\]()~`>#+\\-=|{}.!\\\\])", "\\\\$1");
    }
}
