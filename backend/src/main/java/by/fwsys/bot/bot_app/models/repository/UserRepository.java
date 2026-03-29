package by.fwsys.bot.bot_app.models.repository;

import by.fwsys.bot.bot_app.models.entities.UserEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query(value = "from UserEntity where roles not like '%BANNDED%' and roles not like '%DELETED%'")
    List<UserEntity> findAllNotBanned(Sort sort);

    @Query(value = "from UserEntity where roles not like '%BANNDED%' and roles like concat('%', :role, '%')")
    List<UserEntity> findByRole(String role);
}
