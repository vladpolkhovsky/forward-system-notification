package by.fwsys.bot.bot_app.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;


@Getter
@Entity
@Immutable
@Table(name = "forward_order", schema = "forward_system")
public class ForwardOrderEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_chat_id", nullable = false)
    private ChatEntity adminChat;

    @Column(name = "code", length = 8, nullable = false)
    private String code;

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
