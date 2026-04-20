package com.example.mercado.courses.course.courseLearningPoint.controller.integration;

import com.example.mercado.courses.course.courseLearningPoint.service.interfaces.CourseAdminLearningPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class IntegrationCoursePublicLearningPointControllerTest {

    @Autowired
    private CourseAdminLearningPointService service;

}
