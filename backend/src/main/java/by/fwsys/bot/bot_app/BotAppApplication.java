package by.fwsys.bot.bot_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BotAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(BotAppApplication.class, args);
	}
}
