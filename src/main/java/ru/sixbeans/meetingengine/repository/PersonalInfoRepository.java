package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sixbeans.meetingengine.entity.PersonalInfo;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {

}