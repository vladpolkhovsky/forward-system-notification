package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.ForwardOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForwardOrderRepository extends JpaRepository<ForwardOrderEntity, Long> {
    Optional<ForwardOrderEntity> findByCode(String code);
}
