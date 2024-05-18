package ru.sixbeans.meetingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.sixbeans.meetingengine.entity.PersonalInfo;

import java.util.Optional;

public interface PersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {
    @Query(value = "SELECT pi.* FROM personal_info pi JOIN user_meetings um ON pi.user_id = um.meeting_partner_id AND um.user_id = :userId", nativeQuery = true)
    Optional<PersonalInfo> getPartnerPersonalInfoFromUserMeetings(Long userId);
}
