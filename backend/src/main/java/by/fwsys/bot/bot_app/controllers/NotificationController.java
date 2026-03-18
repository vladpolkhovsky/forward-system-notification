package by.fwsys.bot.bot_app.controllers;


import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.services.customer.CustomerNotificationDispatcher;
import by.fwsys.bot.bot_app.services.notifications.NotificationDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationDispatcher notificationDispatcher;
    private final CustomerNotificationDispatcher customerNotificationDispatcher;

    @PostMapping
    public ResponseEntity<?> send(@RequestBody SendNotificationMessageDto messageDto) {
        log.info("Received notification request: title='{}', level='{}', targetUserId={}", 
                messageDto.getTittle(), 
                messageDto.getInformationLevel(),
                messageDto.getTargetUserId());
        
        notificationDispatcher.dispatch(messageDto);
        
        log.info("Notification dispatched successfully");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/any")
    public ResponseEntity<?> sendAny(@RequestBody SendNotificationMessageDto messageDto) {
        log.info("Received broadcast notification request: title='{}', level='{}'", 
                messageDto.getTittle(), 
                messageDto.getInformationLevel());
        
        notificationDispatcher.dispatchAll(messageDto);
        
        log.info("Broadcast notification dispatched successfully");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/customer/all")
    public ResponseEntity<?> sendCustomerAll(@RequestBody SendCustomerMessageDto messageDto) {
        log.info("Received broadcast notification request: title='{}', level='{}', targetForwardOrder={}",
                messageDto.getTittle(),
                messageDto.getInformationLevel(),
                messageDto.getTargetForwardOrderId());

        customerNotificationDispatcher.dispatchAll(messageDto);

        log.info("Broadcast notification dispatched successfully");
        return ResponseEntity.ok().build();
    }
}
