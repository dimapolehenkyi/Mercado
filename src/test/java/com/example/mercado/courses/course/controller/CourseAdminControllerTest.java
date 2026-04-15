package com.example.mercado.courses.course.controller;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.course.service.interfaces.CourseCommandService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(CourseAdminController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("CourseAdminController Test")
public class CourseAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CourseCommandService service;

}
