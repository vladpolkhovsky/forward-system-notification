package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.SendNotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SendNotificationHistoryRepository extends JpaRepository<SendNotificationHistoryEntity, Long> {
}
