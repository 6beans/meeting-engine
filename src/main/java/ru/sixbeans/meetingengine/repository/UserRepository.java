package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sixbeans.meetingengine.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByIdAndSubscribers_Id(String userId, String subscriberId);

    boolean existsByIdAndSubscriptions_Id(String userId, String subscriberId);

    @Query("SELECT u FROM User u JOIN u.tags t WHERE t.id IN :tags GROUP BY u ORDER BY COUNT(t) DESC")
    List<User> findMostSuitableUsersByTags(@Param("tags") List<String> tags);
}
