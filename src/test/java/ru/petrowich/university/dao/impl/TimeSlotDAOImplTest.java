package ru.petrowich.university.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.petrowich.university.AppConfigurationTest;
import ru.petrowich.university.dao.DaoNotFoundException;
import ru.petrowich.university.dao.LessonDAO;
import ru.petrowich.university.dao.TimeSlotDAO;
import ru.petrowich.university.model.TimeSlot;
import ru.petrowich.university.model.Lesson;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatObject;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(classes = {AppConfigurationTest.class})
class TimeSlotDAOImplTest {
    private static final Integer NONEXISTENT_TIME_SLOT_ID = 0;
    private static final String NEW_TIME_SLOT_NAME = "ninth lesson";
    private static final LocalTime NEW_TIME_SLOT_START_TIME = LocalTime.of(21, 40);
    private static final LocalTime NEW_TIME_SLOT_END_TIME = LocalTime.of(23, 20);
    private static final Integer EXISTENT_TIME_SLOT_ID_1 = 1;
    private static final Integer EXISTENT_TIME_SLOT_ID_2 = 2;
    private static final Integer EXISTENT_TIME_SLOT_ID_3 = 3;
    private static final Integer EXISTENT_TIME_SLOT_ID_4 = 4;
    private static final Integer EXISTENT_TIME_SLOT_ID_5 = 5;
    private static final Integer EXISTENT_TIME_SLOT_ID_6 = 6;
    private static final Integer EXISTENT_TIME_SLOT_ID_7 = 7;
    private static final Integer EXISTENT_TIME_SLOT_ID_8 = 8;
    private static final String EXISTENT_TIME_SLOT_NAME_1 = "first lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_2 = "second lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_3 = "third lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_4 = "fourth lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_5 = "fifth lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_6 = "sixth lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_7 = "seventh lesson";
    private static final String EXISTENT_TIME_SLOT_NAME_8 = "eighth lesson";
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_1 = LocalTime.of(8, 0);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_2 = LocalTime.of(9, 40);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_3 = LocalTime.of(11, 20);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_4 = LocalTime.of(13, 20);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_5 = LocalTime.of(15, 0);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_6 = LocalTime.of(16, 40);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_7 = LocalTime.of(18, 20);
    private static final LocalTime EXISTENT_TIME_SLOT_START_TIME_8 = LocalTime.of(20, 0);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_1 = LocalTime.of(9, 30);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_2 = LocalTime.of(11, 10);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_3 = LocalTime.of(12, 50);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_4 = LocalTime.of(14, 50);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_5 = LocalTime.of(16, 30);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_6 = LocalTime.of(18, 10);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_7 = LocalTime.of(19, 50);
    private static final LocalTime EXISTENT_TIME_SLOT_END_TIME_8 = LocalTime.of(21, 30);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TimeSlotDAO timeSlotDAOImpl;

    @Autowired
    private LessonDAO lessonDAOImpl;

    @Autowired
    private String populateDbSql;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute(populateDbSql);
    }

    @Test
    void testGetByIdShouldReturnExistentLesson() {
        TimeSlot expected = new TimeSlot()
                .setId(EXISTENT_TIME_SLOT_ID_1)
                .setName(EXISTENT_TIME_SLOT_NAME_1)
                .setStartTime(EXISTENT_TIME_SLOT_START_TIME_1)
                .setEndTime(EXISTENT_TIME_SLOT_END_TIME_1);

        TimeSlot actual = timeSlotDAOImpl.getById(EXISTENT_TIME_SLOT_ID_1);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testGetByIdShouldShouldThrowDaoNotFoundExceptionWhenNonexistentIdPassed() {
        assertThrows(DaoNotFoundException.class, () -> timeSlotDAOImpl.getById(NONEXISTENT_TIME_SLOT_ID), "DaoNotFoundException throw is expected");
    }

    @Test
    void testGetByIdShouldShouldThrowDaoNotFoundExceptionWhenNullPassed() {
        assertThrows(DaoNotFoundException.class, () -> timeSlotDAOImpl.getById(null), "DaoNotFoundException throw is expected");
    }

    @Test
    void testAddShouldAddNewTimeSlot() {
        TimeSlot expected = new TimeSlot()
                .setName(NEW_TIME_SLOT_NAME)
                .setStartTime(NEW_TIME_SLOT_START_TIME)
                .setEndTime(NEW_TIME_SLOT_END_TIME);

        timeSlotDAOImpl.add(expected);
        assertNotNull(expected.getId(), "add() should set new id to the timeslot, new id cannot be null");

        TimeSlot actual = timeSlotDAOImpl.getById(expected.getId());
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testAddShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> timeSlotDAOImpl.add(null), "add(null) should throw NullPointerException");
    }

    @Test
    void testUpdateShouldUpdateExistentTimeSlot() {
        TimeSlot actual = timeSlotDAOImpl.getById(EXISTENT_TIME_SLOT_ID_1);

        actual.setName(NEW_TIME_SLOT_NAME)
                .setStartTime(NEW_TIME_SLOT_START_TIME)
                .setEndTime(NEW_TIME_SLOT_END_TIME);

        timeSlotDAOImpl.update(actual);

        TimeSlot expected = timeSlotDAOImpl.getById(EXISTENT_TIME_SLOT_ID_1);
        assertThatObject(actual).isEqualToComparingFieldByField(expected);
    }

    @Test
    void testUpdateShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> timeSlotDAOImpl.update(null), "update(null) should throw NullPointerException");
    }

    @Test
    void testDeleteShouldDeleteTimeSlotWhenExistentTimeSlotPassed() {
        TimeSlot timeSlot = timeSlotDAOImpl.getById(EXISTENT_TIME_SLOT_ID_1);
        List<Lesson> allLessonsBefore = lessonDAOImpl.getAll();
        timeSlotDAOImpl.delete(timeSlot);

        assertThrows(DaoNotFoundException.class, () -> timeSlotDAOImpl.getById(EXISTENT_TIME_SLOT_ID_1), "DaoNotFoundException throw is expected");


        List<Lesson> allLessonsAfter = lessonDAOImpl.getAll();

        Set<Lesson> allLessonsBeforeSet = new HashSet<>(allLessonsBefore);
        Set<Lesson> allLessonsAfterSet = new HashSet<>(allLessonsAfter);
        assertThat(allLessonsBeforeSet).usingElementComparatorIgnoringFields().isNotEqualTo(allLessonsAfterSet);

        List<Lesson> deletedTimeSlotLessons = allLessonsAfter.stream()
                .filter(lesson -> lesson.getTimeSlot().equals(timeSlot))
                .collect(Collectors.toList());
        assertEquals(0, deletedTimeSlotLessons.size(), "empty lessons list with deleted is expected");
    }

    @Test
    void testGetAllShouldReturnAllTimeSlotList() {
        List<TimeSlot> expected = new ArrayList<>();
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_1).setName(EXISTENT_TIME_SLOT_NAME_1).setStartTime(EXISTENT_TIME_SLOT_START_TIME_1).setEndTime(EXISTENT_TIME_SLOT_END_TIME_1));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_2).setName(EXISTENT_TIME_SLOT_NAME_2).setStartTime(EXISTENT_TIME_SLOT_START_TIME_2).setEndTime(EXISTENT_TIME_SLOT_END_TIME_2));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_3).setName(EXISTENT_TIME_SLOT_NAME_3).setStartTime(EXISTENT_TIME_SLOT_START_TIME_3).setEndTime(EXISTENT_TIME_SLOT_END_TIME_3));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_4).setName(EXISTENT_TIME_SLOT_NAME_4).setStartTime(EXISTENT_TIME_SLOT_START_TIME_4).setEndTime(EXISTENT_TIME_SLOT_END_TIME_4));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_5).setName(EXISTENT_TIME_SLOT_NAME_5).setStartTime(EXISTENT_TIME_SLOT_START_TIME_5).setEndTime(EXISTENT_TIME_SLOT_END_TIME_5));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_6).setName(EXISTENT_TIME_SLOT_NAME_6).setStartTime(EXISTENT_TIME_SLOT_START_TIME_6).setEndTime(EXISTENT_TIME_SLOT_END_TIME_6));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_7).setName(EXISTENT_TIME_SLOT_NAME_7).setStartTime(EXISTENT_TIME_SLOT_START_TIME_7).setEndTime(EXISTENT_TIME_SLOT_END_TIME_7));
        expected.add(new TimeSlot().setId(EXISTENT_TIME_SLOT_ID_8).setName(EXISTENT_TIME_SLOT_NAME_8).setStartTime(EXISTENT_TIME_SLOT_START_TIME_8).setEndTime(EXISTENT_TIME_SLOT_END_TIME_8));

        List<TimeSlot> actual = timeSlotDAOImpl.getAll();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);

        Set<TimeSlot> expectedSet = new HashSet<>(expected);
        Set<TimeSlot> actualSet = new HashSet<>(actual);
        assertThat(actualSet).usingElementComparatorIgnoringFields().isEqualTo(expectedSet);
    }
}
