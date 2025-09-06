package dev.esternit.telegram_spring_boot_starter.config;

import dev.esternit.telegram_spring_boot_starter.services.AsyncTelegramNotifier;
import dev.esternit.telegram_spring_boot_starter.util.NotificationAspect;
import dev.esternit.telegram_spring_boot_starter.services.TelegramService;
import dev.esternit.telegram_spring_boot_starter.util.NotificationThrottler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ConditionalOnClass(TelegramService.class)
@ConditionalOnProperty(name = "telegram.bot.token")
@EnableConfigurationProperties(TelegramProperties.class)
@EnableAspectJAutoProxy
public class TelegramAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TelegramAutoConfiguration.class);

    public TelegramAutoConfiguration() {
        log.info("TelegramAutoConfiguration is enabled");
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "telegram.bot.token")
    public TelegramService telegramService(TelegramProperties properties, RestTemplateBuilder builder) {
        return new TelegramService(properties, builder);
    }

    @Bean
    @ConditionalOnMissingBean
    public NotificationThrottler notificationThrottler() {
        return new NotificationThrottler();
    }

    @Bean("telegramNotificationExecutor")
    @ConditionalOnMissingBean(name = "telegramNotificationExecutor")
    public Executor telegramNotificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("telegram-notifier-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnMissingBean
    public AsyncTelegramNotifier asyncTelegramNotifier(TelegramService telegramService) {
        return new AsyncTelegramNotifier(telegramService);
    }

    @Bean
    @ConditionalOnMissingBean
    public NotificationAspect notificationAspect(NotificationThrottler throttler, AsyncTelegramNotifier asyncNotifier) {
        return new NotificationAspect(throttler, asyncNotifier);
    }
}