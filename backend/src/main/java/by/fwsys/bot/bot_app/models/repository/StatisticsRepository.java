package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.SendNotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatisticsRepository extends JpaRepository<SendNotificationHistoryEntity, Long> {

    @Query(nativeQuery = true, value = """
            SELECT tag, COUNT(DISTINCT h.id) AS tagCount
            FROM forward_system.bot_notification_history h,
                 jsonb_array_elements_text(h.message->'tags') AS tag
            WHERE jsonb_typeof(h.message->'tags') = 'array'
              AND (:userId is null or h.target_user_id = :userId)
            GROUP BY tag
            ORDER BY tagCount DESC limit 40;
            """)
    List<TagStatProjection> calcMessageStat(Long userId);

    record TagStatProjection(String tag, Long tagCount) {
    }
}
