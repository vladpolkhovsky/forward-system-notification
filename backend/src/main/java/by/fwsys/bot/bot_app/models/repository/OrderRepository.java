package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query("from OrderEntity o order by cast(o.techNumber as integer) DESC")
    Stream<OrderEntity> fetchAllOrdersForSend();
}
