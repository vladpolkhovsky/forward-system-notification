package by.fwsys.bot.bot_app.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

@Getter
@Entity
@Immutable
@Table(name = "chats", schema = "forward_system")
public class ChatEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "chat_name", nullable = false, length = 1024)
    private String chatName;
}
