package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.NotificationBotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationBotRepository extends JpaRepository<NotificationBotEntity, Long> {
}
