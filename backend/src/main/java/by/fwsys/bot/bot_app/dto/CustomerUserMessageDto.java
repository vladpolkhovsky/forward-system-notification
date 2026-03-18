package by.fwsys.bot.bot_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerUserMessageDto {
    private String text;
    private String callback;
    private String createdByMessageId;
    private String orderId;
    private List<Long> attachments;
    private String chatId;
}
