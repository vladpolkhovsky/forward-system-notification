package by.fwsys.bot.bot_app.models.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
@Table(name = "customer_bot", schema = "forward_system")
public class CustomerBotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forward_system.id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tg_chat_id", length = 50)
    private String tgChatId;

    @Column(name = "max_chat_id", length = 50)
    private String maxChatId;

    @Column(name = "vk_chat_id", length = 50)
    private String vkChatId;

    @Column(name = "forward_order_id")
    private Long forwardOrderId;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}