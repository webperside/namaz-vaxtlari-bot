package com.webperside.namazvaxtlaribot;

import com.webperside.namazvaxtlaribot.external.APIService;
import com.webperside.namazvaxtlaribot.telegram.TelegramListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.DigestUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;

import static com.webperside.namazvaxtlaribot.config.Config.dates;

@SpringBootApplication
@RequiredArgsConstructor
public class NamazVaxtlariBotApplication implements CommandLineRunner {

	private final TelegramListener telegramListener;
	private final APIService apiService;

	public static void main(String[] args) {
		SpringApplication.run(NamazVaxtlariBotApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		apiService.get();
		System.out.println(dates);
		telegramListener.listener();
	}
}
