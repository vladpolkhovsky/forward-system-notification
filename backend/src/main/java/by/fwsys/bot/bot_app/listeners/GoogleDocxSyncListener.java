package by.fwsys.bot.bot_app.listeners;

import by.fwsys.bot.bot_app.dto.GoogleDocxSyncDto;
import by.fwsys.bot.bot_app.events.GDocxSyncEvent;
import by.fwsys.bot.bot_app.mappers.GoogleDocxSyncMapper;
import by.fwsys.bot.bot_app.models.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleDocxSyncListener {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final RestClient gdocxRestClient = RestClient.create();
    private final OrderRepository orderRepository;
    private final GoogleDocxSyncMapper googleDocxSyncMapper;

    @Value("${app.gsync.url}")
    private String gDocxUrl;

    @EventListener
    @Transactional(readOnly = true)
    public void googleDocxSyncProcessor(GDocxSyncEvent event) {

        List<GoogleDocxSyncDto> dtos = orderRepository.fetchAllOrdersForSend()
                .map(googleDocxSyncMapper::toDto)
                .toList();

        ResponseEntity<Void> bodilessEntity = gdocxRestClient.post()
                .uri(gDocxUrl + "/api/gsync/apply")
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtos)
                .retrieve()
                .toBodilessEntity();

        log.info("google docx updated, status={}", bodilessEntity.getStatusCode().value());
    }

    @SneakyThrows
    @Scheduled(cron = "0 */5 * * * *")
    public void tryUpdate() {
        applicationEventPublisher.publishEvent(new GDocxSyncEvent());
    }
}
