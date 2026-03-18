package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.BotNotificationCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotNotificationCodeRepository extends JpaRepository<BotNotificationCodeEntity, Long> {
    Optional<BotNotificationCodeEntity> findByCode(String code);
}
