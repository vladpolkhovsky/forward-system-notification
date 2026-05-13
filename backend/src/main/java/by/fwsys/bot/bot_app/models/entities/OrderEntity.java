package by.fwsys.bot.bot_app.models.entities;

import by.fwsys.bot.bot_app.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Getter
@Entity
@Immutable
@Table(name = "orders", schema = "forward_system")
public class OrderEntity {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 2048)
    private String name;

    @Column(name = "tech_number", nullable = false)
    private String techNumber;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "intermediate_deadline")
    private LocalDateTime intermediateDeadline;

    @Column(name = "deadline", nullable = false)
    private LocalDateTime deadline;
}