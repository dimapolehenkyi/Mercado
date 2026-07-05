package com.example.mercado.unit.courses.module.controller.adminController;

import com.example.mercado.common.exception.GlobalExceptionHandler;
import com.example.mercado.configs.MessageConfig;
import com.example.mercado.courses.module.controller.adminController.ModuleAdminController;
import com.example.mercado.courses.module.service.adminService.ModuleAdminService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ModuleAdminController.class)
@ActiveProfiles("test")
@Import(
        {
                GlobalExceptionHandler.class,
                MessageConfig.class
        }
)
@DisplayName("Module-Admin Controller Test")
public class ModuleAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ModuleAdminService service;

}
