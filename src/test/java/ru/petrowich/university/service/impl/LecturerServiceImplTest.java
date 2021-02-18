package ru.petrowich.university.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.petrowich.university.repository.LecturerRepository;
import ru.petrowich.university.model.Course;
import ru.petrowich.university.model.Lecturer;
import ru.petrowich.university.model.Lesson;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

class LecturerServiceImplTest {
    private static final Integer PERSON_ID_50005 = 50005;
    private static final Integer PERSON_ID_50006 = 50006;
    private static final String PERSON_EMAIL_50005 = "otryad.kovboev@university.edu";
    private static final String PERSON_EMAIL_50006 = "ushat.pomoev@university.edu";
    private static final Integer COURSE_ID_51 = 51;
    private static final Integer COURSE_ID_52 = 52;
    private static final String COURSE_NAME_51 = "math";
    private static final String COURSE_NAME_52 = "biology";
    private static final Long LESSON_ID_5000001 = 5000001L;
    private static final Long LESSON_ID_5000002 = 5000002L;
    private static final Long LESSON_ID_5000003 = 5000003L;

    private final Lesson fistLesson = new Lesson().setId(LESSON_ID_5000001);
    private final Lesson secondLesson = new Lesson().setId(LESSON_ID_5000002);
    private final Lesson thirdLesson = new Lesson().setId(LESSON_ID_5000003);

    private final Lecturer firstLecturer = new Lecturer().setId(PERSON_ID_50005).setEmail(PERSON_EMAIL_50005).setActive(true);
    private final Lecturer secondLecturer = new Lecturer().setId(PERSON_ID_50006).setEmail(PERSON_EMAIL_50006).setActive(false);

    private final Course firstCourse = new Course().setId(COURSE_ID_51).setName(COURSE_NAME_51).setAuthor(firstLecturer).setActive(true);
    private final Course secondCourse = new Course().setId(COURSE_ID_52).setName(COURSE_NAME_52).setAuthor(firstLecturer).setActive(true);

    private AutoCloseable autoCloseable;

    {
        List<Course> firstLecturerCourses = new ArrayList<>();
        firstLecturerCourses.add(firstCourse);
        firstLecturerCourses.add(secondCourse);
        firstLecturer.setCourses(firstLecturerCourses);

        List<Lesson> firstLecturerLessons = new ArrayList<>();
        firstLecturerLessons.add(fistLesson);
        firstLecturerLessons.add(secondLesson);
        firstLecturerLessons.add(thirdLesson);
        firstLecturer.setLessons(firstLecturerLessons);
    }

    @Mock
    private LecturerRepository mockLecturerRepository;

    @InjectMocks
    private LecturerServiceImpl lecturerServiceImpl;

    @BeforeEach
    private void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void releaseMocks() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testGetByIdShouldReturnLecturerWhenLecturerIdPassed() {
        Optional<Lecturer> optionalFirstLecturer = Optional.of(firstLecturer);
        when(mockLecturerRepository.findById(PERSON_ID_50005)).thenReturn(optionalFirstLecturer);

        Lecturer actual = lecturerServiceImpl.getById(PERSON_ID_50005);

        verify(mockLecturerRepository, times(1)).findById(PERSON_ID_50005);

        assertThat(actual).usingRecursiveComparison().isEqualTo(firstLecturer);
    }

    @Test
    void testGetByIdShouldReturnNullWhenWhenNonexistentIdPassed() {
        when(mockLecturerRepository.findById(-1)).thenReturn(Optional.empty());
        Lecturer actual = lecturerServiceImpl.getById(-1);

        verify(mockLecturerRepository, times(1)).findById(-1);
        assertNull(actual, "null should be returned");
    }

    @Test
    void testGetByIdShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lecturerServiceImpl.getById(null), "GetById(null) should throw InvalidDataAccessApiUsageException");
        verify(mockLecturerRepository, times(0)).save(null);
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedLecturer() {
        lecturerServiceImpl.add(firstLecturer);
        verify(mockLecturerRepository, times(1)).save(firstLecturer);
    }

    @Test
    void testAddShouldInvokeRepositorySaveWithPassedNull() {
        lecturerServiceImpl.add(null);
        verify(mockLecturerRepository, times(1)).save(null);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedLecturer() {
        lecturerServiceImpl.update(firstLecturer);
        verify(mockLecturerRepository, times(1)).save(firstLecturer);
    }

    @Test
    void testUpdateShouldInvokeRepositorySaveWithPassedNull() {
        lecturerServiceImpl.update(null);
        verify(mockLecturerRepository, times(1)).save(null);
    }

    @Test
    void testDeleteShouldInvokeRepositorySaveWithPassedLecturer() {
        Lecturer actual = new Lecturer().setId(PERSON_ID_50005).setActive(true);

        Optional<Lecturer> optionalFirstLecturer = Optional.of(actual);
        when(mockLecturerRepository.findById(PERSON_ID_50005)).thenReturn(optionalFirstLecturer);

        lecturerServiceImpl.delete(firstLecturer);

        verify(mockLecturerRepository, times(1)).findById(PERSON_ID_50005);
        assertFalse(actual.isActive(),"lecturer should turn inactive");
        verify(mockLecturerRepository, times(1)).save(firstLecturer);
    }

    @Test
    void testDeleteShouldThrowNullPointerExceptionWhenNullPassed() {
        assertThrows(NullPointerException.class, () -> lecturerServiceImpl.delete(null),"delete(null) should throw NullPointerException");
        verify(mockLecturerRepository, times(0)).save(null);
    }

    @Test
    void testGetAllShouldReturnLecturerList() {
        List<Lecturer> expected = new ArrayList<>();
        expected.add(firstLecturer);
        expected.add(secondLecturer);

        when(mockLecturerRepository.findAll()).thenReturn(expected);

        List<Lecturer> actual = lecturerServiceImpl.getAll();

        verify(mockLecturerRepository, times(1)).findAll();

        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
