package by.fwsys.bot.bot_app.models.specifications;

import by.fwsys.bot.bot_app.models.entities.SendNotificationHistoryEntity;
import org.springframework.data.jpa.domain.Specification;

public class NotificationHistorySpecification {

    public static Specification<SendNotificationHistoryEntity> listByUserId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id != null) {
                return criteriaBuilder.equal(root.get(SendNotificationHistoryEntity.Fields.targetUserId), id);
            }
            return criteriaBuilder.conjunction();
        };
    }

}
