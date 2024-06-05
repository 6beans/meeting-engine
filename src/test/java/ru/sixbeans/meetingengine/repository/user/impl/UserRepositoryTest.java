package ru.sixbeans.meetingengine.repository.user.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.User;
import ru.sixbeans.meetingengine.repository.TagRepository;
import ru.sixbeans.meetingengine.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "classpath:/initialize-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class UserRepositoryTest {

    Pageable pageable = PageRequest.of(0, 10);

    @Autowired
    private UserRepository userRepository;

    @Test
    void getRecommendedUsers() {
        Page<User> result = userRepository.getRecommendedUsers(1L, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).extracting("userName")
                .containsExactly("@username2");
    }

    @Test
    void filterUsersByTags() {
        Set<Integer> tagSet = new HashSet<>();
        tagSet.add(1);
        Page<User> result = userRepository.filterUsersByTags(tagSet, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("userName")
                .contains("@username2", "@username1");
    }

}
