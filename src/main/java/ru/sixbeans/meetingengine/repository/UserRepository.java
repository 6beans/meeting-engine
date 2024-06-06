package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByIdAndSubscribers_Id(String userId, String subscriberId);

    boolean existsByIdAndSubscriptions_Id(String userId, String subscriberId);
}
