package dev.esternit.telegram_spring_boot_starter.config;

import dev.esternit.telegram_spring_boot_starter.util.NotificationAspect;
import dev.esternit.telegram_spring_boot_starter.util.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

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
    public NotificationAspect notificationAspect(TelegramService telegramService) {
        return new NotificationAspect(telegramService);
    }
}