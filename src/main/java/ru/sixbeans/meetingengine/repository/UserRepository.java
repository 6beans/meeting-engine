package ru.sixbeans.meetingengine.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sixbeans.meetingengine.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsBySignedSub(String signedSub);

    Optional<User> findByEmail(String email);

    @Query(value = "SELECT u.*, COUNT(ut.tags_id) AS common_tags " +
            "FROM users_tags ut " +
            "JOIN users u ON ut.users_id = u.id " +
            "WHERE ut.tags_id IN (SELECT tags_id FROM users_tags WHERE users_id = :userId) " +
            "AND ut.users_id != :userId " +
            "GROUP BY u.id " +
            "ORDER BY common_tags DESC;", nativeQuery = true)
    List<User> getRecommendedUsers(@Param("userId") Long userId);

}
