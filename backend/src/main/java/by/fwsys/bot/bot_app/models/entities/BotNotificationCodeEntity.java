package by.fwsys.bot.bot_app.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "bot_notification_code", schema = "forward_system")
public class BotNotificationCodeEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private UserEntity user;

    @Column(name = "code", nullable = false, length = 32)
    private String code;

}
