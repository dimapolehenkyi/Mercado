package com.example.mercado.integration.courses.course.service.publicService;

import com.example.mercado.courses.course.repository.CourseRepository;
import com.example.mercado.courses.course.service.publicService.CoursePublicService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("CoursePublicServiceImplTest Integration Test")
@ActiveProfiles("test")
public class CoursePublicServiceImplTest {

    @Autowired
    private CourseRepository repository;

    @Autowired
    private CoursePublicService service;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }


    @Test
    @DisplayName("getCoursesByStatus should return paged courses when /COURSE-STATUS/ exists")
    void getCoursesByStatus_shouldReturnPagedCourses_whenStatusExists() {

    }

    @Test
    @DisplayName("getCoursesByStatus should return empty page when no one course with this /COURSE-STATUS/ exists")
    void getCoursesByStatus_shouldReturnEmptyPage_whenNoCoursesWithStatus() {

    }

    @Test
    @DisplayName("getMyCourse should return user's courses when user has courses")
    void getMyCourse_shouldReturnUserCourses_whenUserHasCourses() {

    }

    @Test
    @DisplayName("getMyCourse should return empty page when user hasn't courses")
    void getMyCourse_shouldReturnEmptyPage_whenUserHasNoCourses() {

    }

    @Test
    @DisplayName("getPopularCourses should return courses sorted by /STUDENT-COUNT/ desc")
    void getPopularCourses_shouldReturnCoursesSortedByStudentCountDesc() {

    }

    @Test
    @DisplayName("getPopularCourses should return empty page when no one courses exist")
    void getPopularCourses_shouldReturnEmptyPage_whenNoCoursesExist() {

    }

    @Test
    @DisplayName("getActiveCourseById should return course by ID when course is /ACTIVE/")
    void getActiveCourseById_shouldReturnCourse_whenCourseIsActive() {

    }

    @Test
    @DisplayName("getActiveCourseById should throw exception when course /NOT-FOUND/")
    void getActiveCourseById_shouldThrowException_whenCourseNotFound() {

    }

    @Test
    @DisplayName("getActiveCourseById should throw exception when course is /NOT-ACTIVE/")
    void getActiveCourseById_shouldThrowException_whenCourseIsNotActive() {

    }

    @Test
    @DisplayName("getAllCourses should return paged courses when courses exist")
    void getAllCourses_shouldReturnPagedCourses_whenCoursesExist() {

    }

    @Test
    @DisplayName("getAllCourses should return empty page when no one courses exist")
    void getAllCourses_shouldReturnEmptyPage_whenNoCoursesExist() {

    }

    @Test
    @DisplayName("getCoursesByTeacherId should return courses when teacher has courses")
    void getCoursesByTeacherId_shouldReturnCourses_whenTeacherHasCourses() {

    }

    @Test
    @DisplayName("getCoursesByTeacherId should return empty page when teacher hasn't courses")
    void getCoursesByTeacherId_shouldReturnEmptyPage_whenTeacherHasNoCourses() {

    }

    @Test
    @DisplayName("searchCourse should return filtered courses when valid filter")
    void searchCourse_shouldReturnFilteredCourses_whenValidFilter() {

    }

    @Test
    @DisplayName("searchCourse should return empty page when no one courses match filter")
    void searchCourse_shouldReturnEmptyPage_whenNoCoursesMatchFilter() {

    }

    @Test
    @DisplayName("searchCourse should sort by /PRICE-ASC/ when sort type /PRICE-ASC/")
    void searchCourse_shouldSortByPriceAsc_whenSortTypePriceAsc() {

    }

    @Test
    @DisplayName("searchCourse should sort by /PRICE-DESC/ when sort type /PRICE-DESC/")
    void searchCourse_shouldSortByPriceDesc_whenSortTypePriceDesc() {

    }

    @Test
    @DisplayName("searchCourse should sort by /NEWEST/ when sort type /NEWEST/")
    void searchCourse_shouldSortByNewest_whenSortTypeNewest() {

    }

    @Test
    @DisplayName("searchCourse should sort by /RATING-ASC/ when sort type /RATING-ASC/")
    void searchCourse_shouldSortByRatingAsc_whenSortTypeRatingAsc() {

    }

    @Test
    @DisplayName("searchCourse should sort by /RATING-DESC/ when sort type /RATING-DESC/")
    void searchCourse_shouldSortByRatingDesc_whenSortTypeRatingDesc() {

    }

    @Test
    @DisplayName("countAllCourses should return correct count course's when courses exist")
    void countAllCourses_shouldReturnCorrectCount_whenCoursesExist() {

    }

    @Test
    @DisplayName("countAllCourses should return zero when no one courses exist")
    void countAllCourses_shouldReturnZero_whenNoCoursesExist() {

    }

    @Test
    @DisplayName("countTeacherCoursesById should return correct count when teacher has courses")
    void countTeacherCoursesById_shouldReturnCorrectCount_whenTeacherHasCourses() {

    }

    @Test
    @DisplayName("countTeacherCoursesById should return zero when teacher hasn't courses")
    void countTeacherCoursesById_shouldReturnZero_whenTeacherHasNoCourses() {

    }

}
