package com.example.mercado.courses.module.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.courses.module.entity.Module;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import com.example.mercado.courses.testutils.module.ModuleTestFactory;
import lombok.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("ModuleRepository Test")
public class ModuleRepositoryTest {

    @Autowired
    private ModuleRepository repository;

    @Test
    @DisplayName("Func existsByNameAndCourseId should return true if module exist")
    void existsByNameAndCourseId_shouldReturnTrue_ifModuleExist() {
        Module module = createModule(
                1L, "Java Core 1", ModuleStatus.PUBLISHED, ModuleType.PAID
        );

        repository.save(module);

        boolean exists = repository.existsByNameAndCourseId(module.getName(), module.getCourseId());

        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("Func existsByNameAndCourseId should return false if module doesn't exist")
    void existsByNameAndCourseId_shouldReturnFalse_ifModuleDoesntExist() {
        boolean exists = repository.existsByNameAndCourseId("Not found", 2L);
        Assertions.assertFalse(exists);
    }

    @Test
    @DisplayName("Func findAllByCourseIdAndDeletedFalseOrderByPositionAsc should return page of modules")
    void findAllByCourseIdAndDeletedFalseOrderByPositionAsc_shouldReturnPage_ofModules() {
        Long courseId = 2L;

        Module m1 = createModule(
                courseId, "Java Core 1", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m1.setPosition(1);

        Module m2 = createModule(
                courseId, "Java Core 2", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m2.setPosition(3);

        Module m3 = createModule(
                courseId, "Java Core 3", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m3.setPosition(2);

        Module m4 = createModule(
                3L, "Python Core 4", ModuleStatus.PUBLISHED, ModuleType.FREE
        );
        m4.setPosition(1);

        Module m5 = createModule(
                courseId, "Java Core 5", ModuleStatus.ARCHIVED, ModuleType.PAID
        );
        m5.setPosition(5);
        m5.setDeleted(true);

        repository.saveAll(List.of(m1, m2, m3, m4, m5));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Module> result = repository.findAllByCourseIdAndDeletedFalseOrderByPositionAsc(courseId, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.getTotalElements());

        List<Module> modules = result.getContent();

        Assertions.assertNotNull(modules);
        Assertions.assertEquals(3, modules.size());

        Assertions.assertEquals(1, modules.get(0).getPosition());
        Assertions.assertEquals(2, modules.get(1).getPosition());
        Assertions.assertEquals(3, modules.get(2).getPosition());

        Assertions.assertTrue(modules.stream().allMatch(m -> m.getCourseId().equals(courseId)));

        Assertions.assertTrue(modules.stream().noneMatch(Module::isDeleted));
    }

    @Test
    @DisplayName("Func findAllByCourseIdAndDeletedFalseOrderByPositionAsc should return list of modules")
    void findAllByCourseIdAndDeletedFalseOrderByPositionAsc_shouldReturnList_ofModules() {
        Long courseId = 2L;

        Module m1 = createModule(
                courseId, "Java Core 1", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m1.setPosition(1);

        Module m2 = createModule(
                courseId, "Java Core 2", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m2.setPosition(3);

        Module m3 = createModule(
                courseId, "Java Core 3", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m3.setPosition(2);

        Module m4 = createModule(
                3L, "Python Core 4", ModuleStatus.PUBLISHED, ModuleType.FREE
        );
        m4.setPosition(1);

        Module m5 = createModule(
                courseId, "Java Core 5", ModuleStatus.ARCHIVED, ModuleType.PAID
        );
        m5.setPosition(5);
        m5.setDeleted(true);

        repository.saveAll(List.of(m1, m2, m3, m4, m5));

        List<Module> modules = repository.findAllByCourseIdAndDeletedFalseOrderByPositionAsc(courseId);

        Assertions.assertNotNull(modules);
        Assertions.assertEquals(3, modules.size());

        Assertions.assertNotNull(modules);
        Assertions.assertEquals(3, modules.size());

        Assertions.assertEquals(1, modules.get(0).getPosition());
        Assertions.assertEquals(2, modules.get(1).getPosition());
        Assertions.assertEquals(3, modules.get(2).getPosition());

        Assertions.assertTrue(modules.stream().allMatch(m -> m.getCourseId().equals(courseId)));

        Assertions.assertTrue(modules.stream().noneMatch(Module::isDeleted));
    }

    @Test
    @DisplayName("Func countByCourseIdAndDeletedFalse should return long")
    void countByCourseIdAndDeletedFalse_shouldReturnLong() {
        Long courseId = 2L;

        Module m1 = createModule(
                courseId, "Java Core 1", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m1.setPosition(1);

        Module m2 = createModule(
                courseId, "Java Core 2", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m2.setPosition(3);

        Module m3 = createModule(
                courseId, "Java Core 3", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m3.setPosition(2);

        Module m4 = createModule(
                3L, "Python Core 4", ModuleStatus.PUBLISHED, ModuleType.FREE
        );
        m4.setPosition(1);

        Module m5 = createModule(
                courseId, "Java Core 5", ModuleStatus.ARCHIVED, ModuleType.PAID
        );
        m5.setPosition(5);
        m5.setDeleted(true);

        repository.saveAll(List.of(m1, m2, m3, m4, m5));

        Long count =  repository.countByCourseIdAndDeletedFalse(courseId);

        Assertions.assertNotNull(count);
        Assertions.assertEquals(3, count.longValue());
    }

    @Test
    @DisplayName("Func findByCourseIdAndPosition should return optional of module")
    void findByCourseIdAndPosition_shouldReturnOptional_ofModule() {
        Long courseId = 2L;
        Module m1 = createModule(
                courseId, "Java Core 1", ModuleStatus.PUBLISHED, ModuleType.PAID
        );
        m1.setPosition(2);

        repository.save(m1);

        Optional<Module> found = repository.findByCourseIdAndPosition(courseId, 2);

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(2, found.get().getPosition());
    }

    @Test
    @DisplayName("Func findByIdAndCourseId should return optional of module")
    void findByIdAndCourseId_shouldReturnOptional_ofModule() {
        Long courseId = 2L;
        Module m1 = createModule(
                courseId, "Java Core 1", ModuleStatus.PUBLISHED, ModuleType.PAID
        );

        Module saved = repository.save(m1);

        Optional<Module> found = repository.findByIdAndCourseId(saved.getId(), courseId);

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(saved.getId(), found.get().getId());
        Assertions.assertEquals(courseId, found.get().getCourseId());
    }



    private @NonNull Module createModule(
            @NonNull Long courseId,
            @NonNull String name,
            @NonNull ModuleStatus status,
            @NonNull ModuleType type
    ) {
        return ModuleTestFactory.createModule(courseId, name, status, type);
    }

}
