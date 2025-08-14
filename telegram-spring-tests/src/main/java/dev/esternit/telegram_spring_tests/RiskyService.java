package dev.esternit.telegram_spring_tests;

import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnFailure;
import org.springframework.stereotype.Service;

@Service
public class RiskyService {


    @NotifyOnFailure(
            message = "Method failed",
            includeStackTrace = true
    )
    public void riskyMethod() {
        throw new RuntimeException("TestError");
    }
}
