package by.fwsys.bot.bot_app.controllers;


import by.fwsys.bot.bot_app.dto.NotificationHistoryDto;
import by.fwsys.bot.bot_app.dto.SendCustomerMessageDto;
import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.models.entities.SendNotificationHistoryEntity;
import by.fwsys.bot.bot_app.services.customer.CustomerNotificationDispatcher;
import by.fwsys.bot.bot_app.services.notifications.NotificationDispatcher;
import by.fwsys.bot.bot_app.services.web.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final NotificationDispatcher notificationDispatcher;
    private final CustomerNotificationDispatcher customerNotificationDispatcher;
    private final NotificationService notificationService;

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

    @GetMapping
    public ResponseEntity<Page<NotificationHistoryDto>> list(@RequestParam(value = "userId", required = false)
                                                             Long userId,
                                                             @PageableDefault(size = 100)
                                                             @SortDefault(sort = SendNotificationHistoryEntity.Fields.createdAt, direction = Sort.Direction.DESC)
                                                             Pageable pageable) {
        return ResponseEntity.ok(notificationService.list(userId, pageable));
    }
}
