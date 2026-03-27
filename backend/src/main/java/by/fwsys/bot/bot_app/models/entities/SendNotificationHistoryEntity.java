package by.fwsys.bot.bot_app.models.entities;

import by.fwsys.bot.bot_app.dto.SendNotificationMessageDto;
import by.fwsys.bot.bot_app.enums.TargetMessengerType;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
@FieldNameConstants
@Table(name = "bot_notification_history", schema = "forward_system")
public class SendNotificationHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forward_system.id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "target")
    @Enumerated(EnumType.STRING)
    private TargetMessengerType messenger;

    @Column(name = "target_user_id")
    private Long targetUserId;

    @Type(JsonType.class)
    @Column(name = "message", nullable = false)
    private SendNotificationMessageDto messageDto;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
