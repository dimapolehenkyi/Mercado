package com.example.mercado.testUtils.base;

import com.example.mercado.configs.TestContainerConfig;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@Import(TestContainerConfig.class)
@ActiveProfiles("test")
public abstract class AbstractRepositoryTest {
}
