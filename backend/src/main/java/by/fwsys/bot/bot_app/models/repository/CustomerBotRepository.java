package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.CustomerBotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerBotRepository extends JpaRepository<CustomerBotEntity, Long> {
    Optional<CustomerBotEntity> findByForwardOrderIdAndTgChatId(Long forwardOrderId, String tgChatId);
    Optional<CustomerBotEntity> findByForwardOrderId(Long forwardOrderId);
}
