package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}