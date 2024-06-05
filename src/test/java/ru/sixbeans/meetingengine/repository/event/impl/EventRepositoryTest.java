package ru.sixbeans.meetingengine.repository.event.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.sixbeans.meetingengine.entity.Event;
import ru.sixbeans.meetingengine.repository.EventRepository;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "classpath:/initialize-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class EventRepositoryTest {

    Pageable pageable = PageRequest.of(0, 10);

    @Autowired
    private EventRepository eventRepository;

    @Test
    void getRecommendedEvents() {
        Page<Event> result = eventRepository.getRecommendedEvents(2L, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).extracting("title")
                .contains("Basketball");
    }

    @Test
    void filterEventsByTags() {
        Set<Integer> tagSet = new HashSet<>();
        tagSet.add(2);

        Page<Event> result = eventRepository.filterEventsByTags(tagSet, pageable);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).extracting("title")
                .contains("Basketball", "Voleyball");
    }

}
