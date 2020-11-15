package com.webperside.namazvaxtlaribot;

import com.webperside.namazvaxtlaribot.external.APIService;
import com.webperside.namazvaxtlaribot.telegram.TelegramListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@RequiredArgsConstructor
@EnableScheduling
public class NamazVaxtlariBotApplication implements CommandLineRunner {

	private final TelegramListener telegramListener;
	private final APIService apiService;

	public static void main(String[] args) {
		SpringApplication.run(NamazVaxtlariBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		apiService.get();
		telegramListener.listener();
	}
}
