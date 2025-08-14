package dev.esternit.telegram_spring_tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class RiskyServiceIntegrationTest {

    @Autowired
    private RiskyService riskyService;

    @Test
    void riskyMethod_ShouldLogErrorAndThrow() {
        assertThatThrownBy(() -> riskyService.riskyMethod())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("TestError");
    }
}
