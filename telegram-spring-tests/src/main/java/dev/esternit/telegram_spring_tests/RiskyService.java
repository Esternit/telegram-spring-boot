package dev.esternit.telegram_spring_tests;

import dev.esternit.telegram_spring_boot_starter.entities.SendMessageParams;
import dev.esternit.telegram_spring_boot_starter.entities.User;
import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnFailure;
import dev.esternit.telegram_spring_boot_starter.interfaces.NotifyOnSuccess;
import dev.esternit.telegram_spring_boot_starter.services.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
public class RiskyService {

    @Autowired
    TelegramService telegramService;


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

    public void sendSomething(){
        SendMessageParams params = new SendMessageParams();
        params.setChatId("735028324");
        params.setText("Hello, this is a plain text message.");

        boolean sent = telegramService.sendMessage(params);
        if (sent) {
            System.out.println("✅ Message sent successfully!");
        } else {
            System.out.println("❌ Failed to send message.");
        }
    }

    public void getMe(){
        User user = telegramService.getMe();

        System.out.println(user.getId());
    }
}
