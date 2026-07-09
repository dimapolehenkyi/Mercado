package com.example.mercado.integration.courses.module.controller.adminController;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ReorderModuleRequest;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
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
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Module Admin Controller Test")
public class ModuleAdminControllerTest extends AbstractRepositoryTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModuleRepository repository;

    @AfterEach
    void clean() {
        repository.deleteAllInBatch();
    }

    // =========================
    // createModule
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class CreateModule {

        @Test
        void createModule_shouldReturn201AndResponse_whenValidRequest() throws Exception {
            Long courseId = 1L;

            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "Module";
                m.description = "Module description";
            });

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                        .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name").value("Module"))
                    .andExpect(jsonPath("$.description").value("Module description"))
                    .andExpect(jsonPath("$.courseId").value(courseId))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.position").value(0));
        }

        @Test
        void createModule_shouldReturn403_whenRequestIsForbidden() throws Exception {
            Long courseId = 1L;

            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "Module";
                m.description = "Module description";
            });

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void createModule_shouldReturn409_whenModuleWithSameNameAlreadyExistsInCourse() throws Exception {
            Long courseId = 1L;

            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module");
                m.setDescription("Module description");
                m.setCourseId(courseId);
            }));

            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "Module";
                m.description = "Module description";
            });

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void createModule_shouldReturn400_whenIdInRequestIsInvalid(String url) throws Exception {
            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = "Module";
                m.description = "Module description";
            });

            mockMvc.perform(post(url)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules",
                    "/api/admin/courses/a/modules",
                    "/api/admin/courses/A/modules",
                    "/api/admin/courses/!/modules",
                    "/api/admin/courses/ /modules"
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {
                " ", "    ", "!", "@",
                "#", "$", "%", "^", "&"
        })
        void createModule_shouldReturn400_whenRequestBodyIsInvalid(String name) throws Exception {
            Long courseId = 1L;

            CreateModuleRequest request = ModuleTestFactory.createModuleRequest(m -> {
                m.name = name;
                m.description = "Module description";
            });

            mockMvc.perform(post("/api/admin/courses/{courseId}/modules", courseId)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

    }

    // =========================
    // updateModule
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class UpdateModule {

        @Test
        void updateModule_shouldReturn200AndResponse_whenValidRequest() throws Exception {
            Long courseId = 1L;

            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module");
                m.setDescription("Module description");
                m.setPosition(1);
                m.setCourseId(courseId);
            }));

            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "New module";
                m.description = "New module description";
            });

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, module.getId())
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("New module"))
                    .andExpect(jsonPath("$.description").value("New module description"))
                    .andExpect(jsonPath("$.courseId").value(courseId))
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.position").value(1));
        }

        @Test
        void updateModule_shouldReturn403_whenRequestIsForbidden() throws Exception {
            Long courseId = 1L;

            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module");
                m.setDescription("Module description");
                m.setCourseId(courseId);
            }));

            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "New module";
                m.description = "New module description";
            });

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, module.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @Test
        void updateModule_shouldReturn404_whenModuleNotFound() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "New module";
                m.description = "New module description";
            });

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updateModule_shouldReturn409_whenModuleWithSameNameAlreadyExistsInCourse() throws Exception {
            Long courseId = 1L;

            Module moduleOne = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module One");
                m.setDescription("Module One description");
                m.setPosition(0);
                m.setCourseId(courseId);
            }));
            Module moduleTwo = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module Two");
                m.setDescription("Module Two description");
                m.setPosition(1);
                m.setCourseId(courseId);
            }));

            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "Module Two";
                m.description = "Module Two description";
            });

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleOne.getId())
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void updateModule_shouldReturn400_whenIdInRequestIsInvalid(String url) throws Exception {
            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = "New module";
                m.description = "New module description";
            });

            mockMvc.perform(patch(url)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules/1",
                    "/api/admin/courses/a/modules/1",
                    "/api/admin/courses/A/modules/1",
                    "/api/admin/courses/!/modules/1",
                    "/api/admin/courses/ /modules/1",
                    "/api/admin/courses/1/modules/-1",
                    "/api/admin/courses/1/modules/a",
                    "/api/admin/courses/1/modules/A",
                    "/api/admin/courses/1/modules/!",
                    "/api/admin/courses/1/modules/ ",
                    "/api/admin/courses/-1/modules/-1"
            );
        }

        @ParameterizedTest
        @ValueSource(strings = {
                " ", "    ", "!", "@",
                "#", "$", "%", "^", "&"
        })
        void updateModule_shouldReturn400_whenRequestBodyIsInvalid(String name) throws Exception {
            Long courseId = 1L;

            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module");
                m.setDescription("Module description");
                m.setCourseId(courseId);
            }));

            UpdateModuleRequest request = ModuleTestFactory.updateModuleRequest(m -> {
                m.name = name;
                m.description = "New module description";
            });

            mockMvc.perform(patch("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, module.getId())
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

    }

    // =========================
    // deleteModule
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class DeleteModule {

        @Test
        void deleteModule_shouldReturn204_whenValidRequest() throws Exception {
            Long courseId = 1L;

            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module");
                m.setDescription("Module description");
                m.setPosition(1);
                m.setCourseId(courseId);
            }));

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, module.getId())
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                    .andExpect(status().isNoContent());

            assertTrue(repository.findByIdAndCourseId(module.getId(), courseId).orElseThrow().isDeleted());
        }

        @Test
        void deleteModule_shouldReturn404_whenModuleNotFound() throws Exception {
            Long courseId = 1L;
            Long moduleId = 2L;

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, moduleId)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                    .andExpect(status().isNotFound());
        }

        @Test
        void deleteModule_shouldReturn403_whenRequestIsForbidden() throws Exception {
            Long courseId = 1L;

            Module module = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module");
                m.setDescription("Module description");
                m.setPosition(1);
                m.setCourseId(courseId);
            }));

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules/{moduleId}", courseId, module.getId()))
                    .andExpect(status().isForbidden());

            assertFalse(repository.findByIdAndCourseId(module.getId(), courseId).orElseThrow().isDeleted());
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void deleteModule_shouldReturn400_whenIdInRequestIsInvalid(String url) throws Exception {
            mockMvc.perform(delete(url)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                    .andExpect(status().isBadRequest());
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules/1",
                    "/api/admin/courses/a/modules/1",
                    "/api/admin/courses/A/modules/1",
                    "/api/admin/courses/!/modules/1",
                    "/api/admin/courses/1/modules/-1",
                    "/api/admin/courses/1/modules/a",
                    "/api/admin/courses/1/modules/A",
                    "/api/admin/courses/1/modules/!",
                    "/api/admin/courses/-1/modules/-1"
            );
        }

    }

    // =========================
    // deleteAllModules
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class DeleteAllModules {

        @Test
        void deleteAllModules_shouldReturn204_whenValidRequest() throws Exception {
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

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules", courseId)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN"))))
                    .andExpect(status().isNoContent());

            Pageable pageable = PageRequest.of(0, 10);
            Page<Module> result = repository.findAllByCourseIdAndDeletedFalse(courseId, pageable);

            assertTrue(result.isEmpty());
        }

        @Test
        void deleteAllModules_shouldReturn403_whenRequestIsForbidden() throws Exception {
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

            mockMvc.perform(delete("/api/admin/courses/{courseId}/modules", courseId))
                    .andExpect(status().isForbidden());
        }

    }

    // =========================
    // updatePosition
    // =========================
    @Nested
    @WithMockUser(authorities = "ADMIN")
    class UpdatePosition {

        @Test
        void updatePosition_shouldReturn200AndResponse_whenValidRequest() throws Exception {
            Long courseId = 1L;

            Module moduleOne = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module 1");
                m.setPosition(0);
                m.setCourseId(courseId);
            }));
            Module moduleTwo = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module 2");
                m.setPosition(1);
                m.setCourseId(courseId);
            }));
            Module moduleThree = repository.save(ModuleTestFactory.createModule(m -> {
                m.setName("Module 3");
                m.setPosition(2);
                m.setCourseId(courseId);
            }));

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            mockMvc.perform(put("/api/admin/courses/{courseId}/modules/{moduleId}/position", courseId, moduleOne.getId())
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Module 1"))
                    .andExpect(jsonPath("$.courseId").value(courseId))
                    .andExpect(jsonPath("$.id").value(moduleOne.getId()));

            assertEquals(2, repository.findByIdAndCourseId(moduleOne.getId(), courseId).orElseThrow().getPosition());
        }

        @Test
        void updatePosition_shouldReturn404_whenModuleNotFound() throws Exception {
            Long courseId = 1L;

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            mockMvc.perform(put("/api/admin/courses/{courseId}/modules/{moduleId}/position", courseId, 2L)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound());
        }

        @Test
        void updatePosition_shouldReturn403_whenRequestIsForbidden() throws Exception {
            Long courseId = 1L;

            ReorderModuleRequest request = new ReorderModuleRequest(2);

            mockMvc.perform(put("/api/admin/courses/{courseId}/modules/{moduleId}/position", courseId, 2L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());
        }

        @ParameterizedTest
        @NullSource
        @ValueSource(ints = {
                -1
        })
        void updatePosition_shouldReturn400_whenPositionInRequestIsInvalid(Integer position)throws Exception  {
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

            ReorderModuleRequest request = new ReorderModuleRequest(position);

            mockMvc.perform(put("/api/admin/courses/{courseId}/modules/{moduleId}/position", courseId, 2L)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @ParameterizedTest
        @MethodSource("invalidIdInRequest")
        void updatePosition_shouldReturn400_whenIdInRequestIsInvalid(String url) throws Exception {
            ReorderModuleRequest request = new ReorderModuleRequest(2);

            mockMvc.perform(put(url)
                            .with(user("admin").authorities(new SimpleGrantedAuthority("ADMIN")))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        static Stream<String> invalidIdInRequest() {
            return Stream.of(
                    "/api/admin/courses/-1/modules/1/position",
                    "/api/admin/courses/a/modules/1/position",
                    "/api/admin/courses/A/modules/1/position",
                    "/api/admin/courses/!/modules/1/position",
                    "/api/admin/courses/1/modules/-1/position",
                    "/api/admin/courses/1/modules/a/position",
                    "/api/admin/courses/1/modules/A/position",
                    "/api/admin/courses/1/modules/!/position",
                    "/api/admin/courses/-1/modules/-1/position"
            );
        }

    }

}
