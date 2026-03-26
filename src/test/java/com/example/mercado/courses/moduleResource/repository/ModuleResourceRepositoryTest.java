package com.example.mercado.courses.moduleResource.repository;

import com.example.mercado.courses.moduleResource.entity.ModuleResource;
import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("ModuleResourceRepository Tests")
public class ModuleResourceRepositoryTest {

    @Autowired
    private ModuleResourceRepository repository;

    @Test
    @DisplayName("Func findAllByModuleIdOrderByPositionAsc should return list of ModuleResources")
    void findAllByModuleIdOrderByPositionAsc_shouldReturnList_ofModuleResources() {
        Long moduleId = 1L;

        ModuleResource resource1 = ModuleResource.builder()
                .moduleId(moduleId)
                .name("module1")
                .url("url1")
                .thumbnailUrl("thumbnail1")
                .type(ModuleResourceType.MP4)
                .position(1)
                .build();
        ModuleResource resource2 = ModuleResource.builder()
                .moduleId(moduleId)
                .name("module2")
                .url("url2")
                .thumbnailUrl("thumbnail2")
                .type(ModuleResourceType.MP4)
                .position(2)
                .build();

        repository.save(resource1);
        repository.save(resource2);

        List<ModuleResource> resources = repository.findAllByModuleIdOrderByPositionAsc(moduleId);

        Assertions.assertNotNull(resources);
        Assertions.assertEquals(2, resources.size());
        Assertions.assertEquals(1, resources.getFirst().getPosition());
        Assertions.assertEquals(2, resources.get(1).getPosition());
    }

    @Test
    @DisplayName("Func findByModuleIdAndId should return ModuleResource")
    void findByModuleIdAndId_shouldReturnModuleResource() {
        Long moduleId = 1L;

        ModuleResource saved = repository.save(ModuleResource.builder()
                .moduleId(moduleId)
                .name("module1")
                .url("url1")
                .thumbnailUrl("thumbnail1")
                .type(ModuleResourceType.MP4)
                .position(1)
                .build());

        Optional<ModuleResource> found = repository.findByModuleIdAndId(moduleId, saved.getId());

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals(saved, found.get());
    }

    @Test
    @DisplayName("Func existsByModuleIdAndNameAndType should return true")
    void existsByModuleIdAndNameAndType_shouldReturnTrue() {
        Long moduleId = 1L;

        ModuleResource saved = repository.save(ModuleResource.builder()
                .moduleId(moduleId)
                .name("module1")
                .url("url1")
                .thumbnailUrl("thumbnail1")
                .type(ModuleResourceType.MP4)
                .position(1)
                .build());

        boolean exist = repository.existsByModuleIdAndNameAndType(
                saved.getModuleId(), saved.getName(), saved.getType()
        );

        Assertions.assertTrue(exist);
    }

    @Test
    @DisplayName("Func countByModuleId should return Long")
    void countByModuleId_shouldReturnLong() {
        Long moduleId = 1L;

        ModuleResource resource1 = ModuleResource.builder()
                .moduleId(moduleId)
                .name("module1")
                .url("url1")
                .thumbnailUrl("thumbnail1")
                .type(ModuleResourceType.MP4)
                .position(1)
                .build();
        ModuleResource resource2 = ModuleResource.builder()
                .moduleId(moduleId)
                .name("module2")
                .url("url2")
                .thumbnailUrl("thumbnail2")
                .type(ModuleResourceType.MP4)
                .position(2)
                .build();

        repository.save(resource1);
        repository.save(resource2);

        int count = repository.countByModuleId(moduleId);

        Assertions.assertEquals(2, count);
    }

}
