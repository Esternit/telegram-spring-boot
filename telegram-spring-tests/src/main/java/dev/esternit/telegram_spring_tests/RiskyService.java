package dev.esternit.telegram_spring_tests;

import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnFailure;
import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnSuccess;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class RiskyService {


    @NotifyOnSuccess(
            value = "Backup completed successfully",
            includeResult = true,
            severity = NotifyOnSuccess.Severity.NORMAL,
            chatId = "735028324"
    )
    public void riskyMethodSuccess() {
        String s = null;
    }

    @NotifyOnFailure(
            value = "Database backup failed",
            includeStackTrace = true,
            notifyOn = {RuntimeException.class},
            ignore = {FileNotFoundException.class},
            severity = NotifyOnFailure.Severity.CRITICAL,
            chatId = "735028324"
    )
    public void riskyMethodError() {
        throw new RuntimeException("TestError");
    }
}
