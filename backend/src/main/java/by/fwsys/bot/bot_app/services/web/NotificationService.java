package by.fwsys.bot.bot_app.services.web;

import by.fwsys.bot.bot_app.dto.NotificationHistoryDto;
import by.fwsys.bot.bot_app.mappers.SendNotificationHistoryMapper;
import by.fwsys.bot.bot_app.models.repository.SendNotificationHistoryRepository;
import by.fwsys.bot.bot_app.models.repository.StatisticsRepository;
import by.fwsys.bot.bot_app.models.specifications.NotificationHistorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SendNotificationHistoryRepository repository;
    private final SendNotificationHistoryMapper sendNotificationHistoryMapper;
    private final StatisticsRepository statisticsRepository;

    @Transactional(readOnly = true)
    public Page<NotificationHistoryDto> list(Long userId, Pageable pageable) {
        return repository.findAll(NotificationHistorySpecification.listByUserId(userId), pageable)
                .map(sendNotificationHistoryMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<StatisticsRepository.TagStatProjection> getStat(Long userId) {
        return statisticsRepository.calcMessageStat(userId);
    }
}
