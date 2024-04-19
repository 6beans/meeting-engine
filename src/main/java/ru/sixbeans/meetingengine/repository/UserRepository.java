package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsBySub(String sub);
}
