package dev.esternit.telegram_spring_tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class TelegramSpringTestsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(TelegramSpringTestsApplication.class, args);

		RiskyService riskyService = ctx.getBean(RiskyService.class);
//		riskyService.riskyMethodError();
//		riskyService.riskyMethodError();

		riskyService.sendSomething();

		ctx.close();
	}

}
