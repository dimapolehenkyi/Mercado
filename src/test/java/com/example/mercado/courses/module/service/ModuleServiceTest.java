package com.example.mercado.courses.module.service;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.ModuleResponse;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import com.example.mercado.courses.module.exception.ModuleNotFoundException;
import com.example.mercado.courses.module.mapper.ModuleMapper;
import com.example.mercado.courses.module.repository.ModuleRepository;
import com.example.mercado.courses.testutils.module.ModuleTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModuleServiceImpl Test")
public class ModuleServiceTest {

    @Mock
    private ModuleRepository repository;

    @Mock
    private ModuleMapper mapper;

    @InjectMocks
    private ModuleServiceImpl service;


    private ModuleResponse moduleResponse;
    private Module module;
    private CreateModuleRequest createModuleRequest;
    private UpdateModuleRequest updateModuleRequest;

    @BeforeEach
    void setUp() {
        module = ModuleTestFactory.createModule(
                1L, "Java Core", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        moduleResponse = ModuleTestFactory.createResponse();
        createModuleRequest = ModuleTestFactory.createModuleRequest();
        updateModuleRequest = ModuleTestFactory.updateModuleRequest();
    }



    @Test
    @DisplayName("Func createModule should return response")
    void createModule_shouldReturnResponse() {
        Long courseId = 1L;

        Mockito.when(repository.existsByNameAndCourseId(createModuleRequest.name(), courseId))
                .thenReturn(false);
        Mockito.when(mapper.toResponse(module))
                .thenReturn(moduleResponse);
        Mockito.when(mapper.toEntity(createModuleRequest, courseId))
                .thenReturn(module);
        Mockito.when(repository.save(Mockito.any(Module.class)))
                .thenAnswer(i -> i.getArgument(0));

        ModuleResponse response = service.createModule(courseId, createModuleRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(moduleResponse, response);

        Mockito.verify(mapper, Mockito.times(1))
                .toEntity(createModuleRequest, courseId);
        Mockito.verify(mapper, Mockito.times(1))
                .toResponse(module);
        Mockito.verify(repository, Mockito.times(1))
                .save(Mockito.any(Module.class));
    }

    @Test
    @DisplayName("Func updateModule should return response")
    void updateModule_shouldReturnResponse() {
        Long courseId = 1L;
        Long moduleId = 2L;

        ModuleResponse response = new ModuleResponse(
                1L,
                1L,
                "Updated module",
                "Updated description",
                ModuleStatus.PUBLISHED,
                ModuleType.PAID,
                null,
                null
        );

        Mockito.when(repository.findByIdAndCourseId(moduleId, courseId))
                .thenReturn(Optional.of(module));
        Mockito.when(mapper.toResponse(module))
                .thenReturn(response);

        ModuleResponse result = service.updateModule(courseId, moduleId, updateModuleRequest);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.name(), response.name());
        Assertions.assertEquals(result.description(), response.description());
        Assertions.assertEquals(result.status(), response.status());
        Assertions.assertEquals(result.moduleType(), response.moduleType());

        Mockito.verify(repository, Mockito.times(1)).findByIdAndCourseId(moduleId, courseId);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(module);
    }

    @Test
    @DisplayName("Func updateModule should throw exception if module not found")
    void updateModule_shouldThrowException_ifModuleNotFound() {
        Long courseId = 1L;
        Long moduleId = 2L;

        Mockito.when(repository.findByIdAndCourseId(moduleId, courseId)).thenThrow(
                new ModuleNotFoundException(moduleId)
        );

        Assertions.assertThrows(
                ModuleNotFoundException.class,
                () -> service.updateModule(courseId, moduleId, updateModuleRequest)
        );

        Mockito.verify(repository, Mockito.times(1)).findByIdAndCourseId(moduleId, courseId);
        Mockito.verify(mapper, Mockito.never()).toResponse(module);
    }

    @Test
    @DisplayName("Func getModuleById should return response")
    void getModuleById_shouldReturnResponse() {
        Long courseId = 1L;
        Long moduleId = 2L;

        ModuleResponse response = moduleResponse;

        Mockito.when(repository.findByIdAndCourseId(moduleId, courseId))
                .thenReturn(Optional.of(module));
        Mockito.when(mapper.toResponse(module))
                .thenReturn(response);

        ModuleResponse result = service.getModuleById(courseId, moduleId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(moduleResponse, result);

        Mockito.verify(repository, Mockito.times(1)).findByIdAndCourseId(moduleId, courseId);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(module);
    }

    @Test
    @DisplayName("Func getModuleById should return null if module not found")
    void getModuleById_shouldReturnNull_ifModuleNotFound() {
        Long courseId = 1L;
        Long moduleId = 2L;

        Mockito.when(repository.findByIdAndCourseId(moduleId, courseId)).thenThrow(
                new ModuleNotFoundException(moduleId)
        );

        Assertions.assertThrows(
                ModuleNotFoundException.class,
                () -> service.updateModule(courseId, moduleId, updateModuleRequest)
        );

        Mockito.verify(repository, Mockito.times(1)).findByIdAndCourseId(moduleId, courseId);
        Mockito.verify(mapper, Mockito.never()).toResponse(module);
    }

    @Test
    @DisplayName("Func deleteModule should set deleted on module")
    void deleteModule_shouldSetDeleted_onModule() {
        Long courseId = 1L;
        Long moduleId = 2L;

        Mockito.when(repository.findByIdAndCourseId(moduleId, courseId))
                .thenReturn(Optional.of(module));

        service.deleteModule(courseId, moduleId);

        Assertions.assertTrue(module.isDeleted());

        Mockito.verify(repository, Mockito.times(1)).findByIdAndCourseId(moduleId, courseId);
    }

    @Test
    @DisplayName("Func publishModule should return response and change status on PUBLISHED")
    void publishModule_shouldReturnResponse_andChangeStatus_onPublished() {
        Long courseId = 1L;
        Long moduleId = 2L;

        ModuleResponse response = moduleResponse;
        Module expected =  module;
        expected.setStatus(ModuleStatus.ARCHIVED);

        Mockito.when(repository.findByIdAndCourseId(moduleId, courseId))
                .thenReturn(Optional.of(expected));
        Mockito.when(mapper.toResponse(expected))
                .thenReturn(response);

        ModuleResponse result = service.publishModule(courseId, moduleId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(ModuleStatus.PUBLISHED, result.status());

        Mockito.verify(repository, Mockito.times(1)).findByIdAndCourseId(moduleId, courseId);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(expected);
    }

    @Test
    @DisplayName("Func archiveModule should return response and change status on ARCHIVED")
    void archiveModule_shouldReturnResponse_andChangeStatus_onArchived() {
        Long courseId = 1L;
        Long moduleId = 2L;

        module.setStatus(ModuleStatus.PUBLISHED);

        Mockito.when(repository.findByIdAndCourseId(moduleId, courseId))
                .thenReturn(Optional.of(module));
        Mockito.when(mapper.toResponse(Mockito.any(Module.class)))
                .thenAnswer(invocation -> {
                    Module m = invocation.getArgument(0);
                    return new ModuleResponse(
                            m.getId(),
                            m.getCourseId(),
                            m.getName(),
                            m.getDescription(),
                            m.getStatus(),
                            m.getModuleType(),
                            null,
                            null
                    );
                });

        ModuleResponse result = service.archiveModule(courseId, moduleId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(ModuleStatus.ARCHIVED, module.getStatus());
        Assertions.assertEquals(ModuleStatus.ARCHIVED, result.status());

        Mockito.verify(repository, Mockito.times(1)).findByIdAndCourseId(moduleId, courseId);
        Mockito.verify(mapper, Mockito.times(1)).toResponse(module);
    }

    @Test
    @DisplayName("Func moveModuleUp should move module up on one position")
    void moveModuleUp_shouldMoveModuleUp_onOnePosition() {}

    @Test
    @DisplayName("Func moveModuleDown should move module down on one position")
    void moveModuleDown_shouldMoveModuleDown_onOnePosition() {}

    @Test
    @DisplayName("Func existsById should return true if module exist")
    void existsById_shouldReturnTrue_ifModuleExist() {}

    @Test
    @DisplayName("Func existsByNameInCourse should return true if name in course exist")
    void existsByNameInCourse_shouldReturnTrue_ifNameInCourseExist() {}

    @Test
    @DisplayName("Func countModulesByCourse should return long")
    void countModulesByCourse_shouldReturnLong() {}

    @Test
    @DisplayName("Func deleteModulesByCourse should set parameter on true")
    void deleteModulesByCourse_shouldSetParameter_onTrue() {}

    @Test
    @DisplayName("Func updateStatusAllModules should update all module's status")
    void updateStatusAllModules_shouldUpdateAllModulesStatus() {}

    @Test
    @DisplayName("Func getCourseModules should return page of modules")
    void getCourseModules_shouldReturnPage_ofModules() {}

    @Test
    @DisplayName("Func getAllCourseModules should return list of all modules")
    void getAllCourseModules_shouldReturnList_ofAllModules() {}

}
