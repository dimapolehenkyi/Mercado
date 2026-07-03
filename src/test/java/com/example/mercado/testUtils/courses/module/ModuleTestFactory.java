package com.example.mercado.testUtils.courses.module;

import com.example.mercado.courses.module.dto.CreateModuleRequest;
import com.example.mercado.courses.module.dto.UpdateModuleRequest;
import com.example.mercado.courses.module.entity.Module;

import java.util.function.Consumer;

/**
 * Helper-class for testing package of Module
 */
public class ModuleTestFactory {

    /**
     * Function for fast creating Module-entity
     * where you can customize parameter's
     * @param consumer
     * @return Module
     */
    public static Module createModule(
            Consumer<Module.ModuleBuilder> consumer
    ) {
        Module.ModuleBuilder builder = Module.builder()
                .name("Test module")
                .courseId(1L)
                .description("Test description");
        consumer.accept(builder);
        return builder.build();
    }

    /**
     * Function for fast creating "CreateModuleRequest"
     * where you can customize parameters
     * @param customizer
     * @return CreateModuleRequest
     */
    public static CreateModuleRequest createModuleRequest(
            Consumer<Args> customizer
    ) {
        Args args = new Args();
        customizer.accept(args);
        return new CreateModuleRequest(
                args.name,
                args.description
        );
    }

    /**
     * Function for fast creating "UpdateModuleRequest"
     * where you can customize parameters
     * @param customizer
     * @return UpdateModuleRequest
     */
    public static UpdateModuleRequest updateModuleRequest(
            Consumer<Args> customizer
    ) {
        Args args = new Args();
        args.name = "New Module";
        args.description = "New description";

        customizer.accept(args);
        return new UpdateModuleRequest(
                args.name,
                args.description
        );
    }

    public static class Args {
        public String name = "Test Module";
        public String description = "Test description";
    }

}
