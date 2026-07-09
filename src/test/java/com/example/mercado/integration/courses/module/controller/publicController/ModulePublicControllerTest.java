package com.example.mercado.integration.courses.module.controller.publicController;

import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.testUtils.base.AbstractRepositoryTest;
import com.example.mercado.testUtils.courses.module.ModuleTestFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Module Public Controller Test")
public class ModulePublicControllerTest extends AbstractRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModuleRepository repository;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    // =========================
    // getModuleById
    // =========================
    @Nested
    class GetModuleById {

        @Test
        void getModuleById_shouldReturn200AndModuleResponse_whenValidRequestAndModuleExists() throws Exception {
            Long courseId = 1L;

            Module module = repository.save(
                    ModuleTestFactory.createModule(m -> {
                        m.setName("Module");
                        m.setCourseId(courseId);
                        m.setPosition(1);
                    })
            );

            mockMvc.perform(get("/api/courses/{courseId}/modules/{moduleId}", courseId, module.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Module"))
                    .andExpect(jsonPath("$.courseId").value(courseId))
                    .andExpect(jsonPath("$.position").value(1));
        }

        @Test
        void getModuleById_shouldReturn404_whenModuleNotFound() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            mockMvc.perform(get("/api/courses/{courseId}/modules/{moduleId}", courseId, moduleId))
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void getModuleById_shouldReturn400_whenIdInRequestIsInvalid(String url) throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isBadRequest());
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/courses/-1/modules/1",
                    "/api/courses/a/modules/1",
                    "/api/courses/A/modules/1",
                    "/api/courses/!/modules/1",
                    "/api/courses/1/modules/-1",
                    "/api/courses/1/modules/a",
                    "/api/courses/1/modules/A",
                    "/api/courses/1/modules/!",
                    "/api/courses/-1/modules/-1"
            );
        }

    }

    // =========================
    // getAllModules
    // =========================
    @Nested
    class GetAllModules {

        @Test
        void getAllModules_shouldReturn200AndPages_whenValidRequestAndModuleExists() throws Exception {
            Long courseId = 1L;

            for (int i = 1; i <= 3; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            mockMvc.perform(get("/api/courses/{courseId}/modules", courseId)
                            .param("page", "0")
                            .param("size", "5")
                            .param("sort", "position,asc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.size()").value(3))

                    .andExpect(jsonPath("$.content[0].name").value("Module 1"))
                    .andExpect(jsonPath("$.content[0].position").value(0))

                    .andExpect(jsonPath("$.content[1].name").value("Module 2"))
                    .andExpect(jsonPath("$.content[1].position").value(1))

                    .andExpect(jsonPath("$.content[2].name").value("Module 3"))
                    .andExpect(jsonPath("$.content[2].position").value(2));
        }

        @Test
        void getAllModules_shouldReturn200AndEmptyPage_whenValidRequestAndModulesDoesNotExists() throws Exception {
            Long courseId = 1L;

            mockMvc.perform(get("/api/courses/{courseId}/modules", courseId)
                            .param("page", "0")
                            .param("size", "5")
                            .param("sort", "position,asc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content.size()").value(0));
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void getAllModules_shouldReturn400_whenIdInRequestIsInvalid(String url) throws Exception {
            mockMvc.perform(get(url)
                            .param("page", "0")
                            .param("size", "5")
                            .param("sort", "position,asc"))
                    .andExpect(status().isBadRequest());
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/courses/-1/modules",
                    "/api/courses/a/modules",
                    "/api/courses/A/modules",
                    "/api/courses/!/modules"
            );
        }

    }

    // =========================
    // countAllModules
    // =========================
    @Nested
    class countAllModules {

        @Test
        void countAllModules_shouldReturn200AndNumberOfModules_whenValidRequestAndModuleExists() throws Exception {
            Long courseId = 1L;

            for (int i = 1; i <= 3; i++) {
                int number = i;
                repository.save(
                        ModuleTestFactory.createModule(m -> {
                            m.setName("Module " + number);
                            m.setCourseId(courseId);
                            m.setPosition(number - 1);
                        })
                );
            }

            mockMvc.perform(get("/api/courses/{courseId}/modules/count", courseId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("3"));
        }

        @Test
        void countAllModules_shouldReturn200AndZero_whenModulesDoesNotExists() throws Exception {
            Long courseId = 1L;

            mockMvc.perform(get("/api/courses/{courseId}/modules/count", courseId))
                    .andExpect(status().isOk())
                    .andExpect(content().string("0"));
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void countAllModules_shouldReturn400_whenIdInRequestIsInvalid(String url) throws Exception {
            mockMvc.perform(get(url))
                    .andExpect(status().isBadRequest());
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/courses/-1/modules/count",
                    "/api/courses/a/modules/count",
                    "/api/courses/A/modules/count",
                    "/api/courses/!/modules/count"
            );
        }

    }

}
