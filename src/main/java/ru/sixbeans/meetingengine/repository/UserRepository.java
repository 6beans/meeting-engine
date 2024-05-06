package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    boolean existsByProviderAndSubject(String provider, String subject);
}
