package ru.sixbeans.meetingengine.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sixbeans.meetingengine.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    boolean existsByIssuerAndSubject(String provider, String subject);

    Optional<User> findByIssuerAndSubject(String issuer, String subject);

    boolean existsByIdAndSubscribers_Id(Long userId, Long subscriberId);

    boolean existsByIdAndSubscriptions_Id(Long userId, Long subscriberId);

    @Query(value = """
            SELECT users.*
            FROM tag_users
            JOIN users ON tag_users.users_id = users.id
            WHERE tag_users.tags_id IN (SELECT tags_id FROM tag_users WHERE users_id = :userId)
            AND tag_users.users_id != :userId
            GROUP BY users.id
            ORDER BY COUNT(tag_users.tags_id)
            DESC
            """,
            countQuery = """
            SELECT COUNT(DISTINCT users.id)
            FROM tag_users
            JOIN users ON tag_users.users_id = users.id
            WHERE tag_users.tags_id IN (SELECT tags_id FROM tag_users WHERE users_id = :userId)
            AND tag_users.users_id != :userId
            """, nativeQuery = true)
    Page<User> getRecommendedUsers(@Param("userId") Long userId, Pageable pageable);

    @Query(value = """
            SELECT users.*
            FROM users
            JOIN tag_users ON users.id = tag_users.users_id
            WHERE tag_users.tags_id IN :tags
            GROUP BY users.id
            ORDER BY COUNT(tag_users.tags_id)
            DESC""",
            countQuery = """
            SELECT COUNT(DISTINCT users.id).*
            FROM users
            JOIN tag_users ON users.id = tag_users.users_id
            WHERE tag_users.tags_id IN :tags
            """, nativeQuery = true)
    Page<User> filterUsersByTags(@Param("tags") Set<Integer> tags, Pageable pageable);
}
