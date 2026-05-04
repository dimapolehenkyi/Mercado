package com.example.mercado.integration.courses.course.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.courses.course.entity.Course;
import com.example.mercado.courses.course.enums.CourseStatus;
import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.testUtils.courses.course.CourseTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository repository;

    @Test
    @DisplayName("Func findByName should return course when exists")
    void findByName_shouldReturnCourse_whenExists() {

        Course course = CourseTestFactory.createCourse(
                a -> a.name("Java Course")
        );

        repository.save(course);

        Optional<Course> result = repository.findByName("Java Course");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Java Course", result.get().getName());
    }

    @Test
    @DisplayName("Func findByName should return empty when not exists")
    void findByName_shouldReturnEmpty_whenNotExists() {

        Optional<Course> result = repository.findByName("NOT_EXIST");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Func findByName should be case sensitive by default")
    void findByName_shouldBeCaseSensitive() {

        Course course = CourseTestFactory.createCourse(
                a -> a.name("Java Course")
        );

        repository.save(course);

        Optional<Course> result = repository.findByName("java course");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Func should not allow duplicate names")
    void shouldNotAllowDuplicateNames() {

        Course course1 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java Course");
                    a.description("Desc1");
                }
        );

        Course course2 = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java Course");
                    a.description("Desc2");
                }
        );

        repository.save(course1);

        Assertions.assertThrows(Exception.class, () -> {
            repository.saveAndFlush(course2);
        });
    }

    @Test
    @DisplayName("Func existsByName should return true when course exists")
    void existsByName_shouldReturnTrue_whenExists() {

        Course course = CourseTestFactory.createCourse(
                a -> a.name("Java Course")
        );

        repository.save(course);

        boolean exists = repository.existsByName("Java Course");

        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Func existsByName should return false when course does not exist")
    void existsByName_shouldReturnFalse_whenNotExists() {

        boolean exists = repository.existsByName("NOT_EXIST");

        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("Func existsByName should be case sensitive by default")
    void existsByName_shouldBeCaseSensitive() {

        Course course = CourseTestFactory.createCourse(
                a -> a.name("Java Course")
        );

        repository.save(course);

        boolean exists = repository.existsByName("java course");

        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("Func existsByIdAndStatus should return true when match exists")
    void existsByIdAndStatus_shouldReturnTrue_whenMatchExists() {

        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java Course");
                    a.status(CourseStatus.PUBLISHED);
                }
        );

        Course saved = repository.save(course);

        boolean result = repository.existsByIdAndStatus(
                saved.getId(),
                CourseStatus.PUBLISHED
        );

        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("Func existsByIdAndStatus should return false when status does not match")
    void existsByIdAndStatus_shouldReturnFalse_whenStatusMismatch() {

        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java Course");
                    a.status(CourseStatus.ARCHIVED);
                }
        );

        Course saved = repository.save(course);

        boolean result = repository.existsByIdAndStatus(
                saved.getId(),
                CourseStatus.PUBLISHED
        );

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Func existsByIdAndStatus should return false when id does not exist")
    void existsByIdAndStatus_shouldReturnFalse_whenIdNotExists() {

        boolean result = repository.existsByIdAndStatus(
                999999L,
                CourseStatus.PUBLISHED
        );

        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("Func existsByIdAndStatus should correctly handle enum mapping")
    void existsByIdAndStatus_shouldHandleEnumCorrectly() {

        Course course = CourseTestFactory.createCourse(
                a -> {
                    a.name("Java Course");
                    a.status(CourseStatus.ARCHIVED);
                }
        );

        Course saved = repository.save(course);

        Assertions.assertTrue(
                repository.existsByIdAndStatus(saved.getId(), CourseStatus.ARCHIVED)
        );

        Assertions.assertFalse(
                repository.existsByIdAndStatus(saved.getId(), CourseStatus.PUBLISHED)
        );
    }

    @Test
    @DisplayName("Func countByTeacherId should return correct number of courses")
    void countByTeacherId_shouldReturnCorrectCount() {

        Long teacherId = 1L;

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Course 1");
                            a.teacherId(teacherId);
                            a.status(CourseStatus.PUBLISHED);
                        })
        );

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Course 2");
                            a.teacherId(teacherId);
                            a.status(CourseStatus.PUBLISHED);
                        })
        );

        long count = repository.countByTeacherId(teacherId);

        Assertions.assertEquals(2, count);
    }

    @Test
    @DisplayName("Func countByTeacherId should return 0 when no courses exist")
    void countByTeacherId_shouldReturnZero_whenNoCourses() {

        long count = repository.countByTeacherId(999L);

        Assertions.assertEquals(0, count);
    }

    @Test
    @DisplayName("Func countByTeacherId should not count other teachers courses")
    void countByTeacherId_shouldNotCountOtherTeachers() {

        Long teacher1 = 1L;
        Long teacher2 = 2L;

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Course 1");
                            a.teacherId(teacher1);
                            a.status(CourseStatus.PUBLISHED);
                        })
        );

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Course 2");
                            a.teacherId(teacher2);
                            a.status(CourseStatus.PUBLISHED);
                        })
        );

        long count = repository.countByTeacherId(teacher1);

        Assertions.assertEquals(1, count);
    }

    @Test
    @DisplayName("Func findAllByUserId should return paginated results")
    void findAllByUserId_shouldReturnPage() {

        Long userId = 1L;

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            repository.save(
                    CourseTestFactory.createCourse(
                            a -> {
                                a.name("Course " + finalI);
                                a.userId(userId);
                                a.status(CourseStatus.PUBLISHED);
                            })
            );
        }

        Pageable pageable = PageRequest.of(0, 2);

        Page<Course> result = repository.findAllByUserId(userId, pageable);

        Assertions.assertEquals(2, result.getContent().size());
        Assertions.assertEquals(5, result.getTotalElements());
        Assertions.assertEquals(3, result.getTotalPages());
    }

    @Test
    @DisplayName("Func findAllByUserId should return correct second page")
    void findAllByUserId_shouldReturnSecondPage() {

        Long userId = 1L;

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            repository.save(
                    CourseTestFactory.createCourse(
                            a -> {
                                a.name("Course " + finalI);
                                a.userId(userId);
                                a.status(CourseStatus.PUBLISHED);
                            })
            );
        }

        Pageable pageable = PageRequest.of(1, 2);

        Page<Course> result = repository.findAllByUserId(userId, pageable);

        Assertions.assertEquals(2, result.getContent().size());
    }

    @Test
    @DisplayName("Func findAllByUserId should not return other users data")
    void findAllByUserId_shouldFilterCorrectly() {

        Long user1 = 1L;
        Long user2 = 2L;

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Course 1");
                            a.userId(user1);
                            a.status(CourseStatus.PUBLISHED);
                        })
        );

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Course 2");
                            a.userId(user2);
                            a.status(CourseStatus.PUBLISHED);
                        })
        );

        Page<Course> result = repository.findAllByUserId(user1, PageRequest.of(0, 10));

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals("Course 1", result.getContent().getFirst().getName());
    }

    @Test
    @DisplayName("Func findAllByUserId should return empty page when no data")
    void findAllByUserId_shouldReturnEmptyPage() {

        Page<Course> result = repository.findAllByUserId(
                999L,
                PageRequest.of(0, 10)
        );

        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    @DisplayName("Func findByStatus should return paginated courses with given status")
    void findByStatus_shouldReturnPage() {

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            repository.save(
                    CourseTestFactory.createCourse(
                            a -> {
                                a.name("Archived " + finalI);
                                a.status(CourseStatus.ARCHIVED);
                            })
            );
        }

        for (int i = 0; i < 3; i++) {
            int finalI = i;
            repository.save(
                    CourseTestFactory.createCourse(
                            a -> {
                                a.name("Published " + finalI);
                                a.status(CourseStatus.PUBLISHED);
                            })
            );
        }

        Page<Course> result = repository.findByStatus(
                CourseStatus.PUBLISHED,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(3, result.getTotalElements());
        Assertions.assertEquals(3, result.getContent().size());

        Assertions.assertTrue(
                result.getContent().stream()
                        .allMatch(c -> c.getStatus() == CourseStatus.PUBLISHED)
        );
    }

    @Test
    @DisplayName("Func findByStatus should paginate correctly")
    void findByStatus_shouldPaginateCorrectly() {

        for (int i = 0; i < 10; i++) {
            int finalI = i;
            repository.save(
                    CourseTestFactory.createCourse(
                            a -> {
                                a.name("Published " + finalI);
                                a.status(CourseStatus.PUBLISHED);
                            })
            );
        }

        Page<Course> page1 = repository.findByStatus(
                CourseStatus.PUBLISHED,
                PageRequest.of(0, 5)
        );

        Page<Course> page2 = repository.findByStatus(
                CourseStatus.PUBLISHED,
                PageRequest.of(1, 5)
        );

        Assertions.assertEquals(5, page1.getContent().size());
        Assertions.assertEquals(5, page2.getContent().size());
        Assertions.assertEquals(10, page1.getTotalElements());
    }

    @Test
    @DisplayName("Func findByStatus should return empty page when no matches")
    void findByStatus_shouldReturnEmpty_whenNoMatches() {

        Page<Course> result = repository.findByStatus(
                CourseStatus.PUBLISHED,
                PageRequest.of(0, 10)
        );

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Func findByStatus should not mix different statuses")
    void findByStatus_shouldFilterCorrectly() {

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Drafted course");
                            a.status(CourseStatus.DRAFT);
                        }
                )
        );
        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Published course");
                            a.status(CourseStatus.PUBLISHED);
                        }
                )
        );

        Page<Course> result = repository.findByStatus(
                CourseStatus.PUBLISHED,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(CourseStatus.PUBLISHED, result.getContent().get(0).getStatus());
    }


    @Test
    @DisplayName("Func findByTeacherId should return paginated courses")
    void findAllByTeacherId_shouldReturnPage() {

        Long teacherId = 1L;

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            repository.save(
                    CourseTestFactory.createCourse(
                            a -> {
                                a.name("Course " + finalI);
                                a.teacherId(teacherId);
                            })
            );
        }

        Page<Course> result = repository.findAllByTeacherId(
                teacherId,
                PageRequest.of(0, 3)
        );

        Assertions.assertEquals(3, result.getContent().size());
        Assertions.assertEquals(5, result.getTotalElements());
        Assertions.assertEquals(2, result.getTotalPages());
    }

    @Test
    @DisplayName("Func findByTeacherId should correctly paginate results")
    void findAllByTeacherId_shouldPaginateCorrectly() {

        Long teacherId = 1L;

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            repository.save(
                    CourseTestFactory.createCourse(
                            a -> {
                                a.name("Course " + finalI);
                                a.teacherId(teacherId);
                            })
            );
        }

        Page<Course> page1 = repository.findAllByTeacherId(teacherId, PageRequest.of(0, 5));
        Page<Course> page2 = repository.findAllByTeacherId(teacherId, PageRequest.of(1, 5));

        Assertions.assertEquals(5, page1.getContent().size());
        Assertions.assertEquals(2, page2.getContent().size());
    }

    @Test
    @DisplayName("Func findByTeacherId should not return other teachers courses")
    void findAllByTeacherId_shouldFilterCorrectly() {

        Long teacher1 = 1L;
        Long teacher2 = 2L;

        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Teacher 1 course");
                            a.teacherId(teacher1);
                        }
                )
        );
        repository.save(
                CourseTestFactory.createCourse(
                        a -> {
                            a.name("Teacher 2 course");
                            a.teacherId(teacher2);
                        }
                )
        );

        Page<Course> result = repository.findAllByTeacherId(
                teacher1,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getContent().size());
        Assertions.assertEquals(teacher1, result.getContent().getFirst().getTeacherId());
    }

    @Test
    @DisplayName("Func findByTeacherId should return empty page when no data")
    void findAllByTeacherId_shouldReturnEmpty() {

        Page<Course> result = repository.findAllByTeacherId(
                999L,
                PageRequest.of(0, 10)
        );

        Assertions.assertTrue(result.isEmpty());
    }
}
