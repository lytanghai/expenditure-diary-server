package com.expenditure_diary.expenditure_diary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ExpenditureDiaryApplication {


	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Phnom_Penh"));
	}

	public static void main(String[] args) {
		SpringApplication.run(ExpenditureDiaryApplication.class, args);
	}

}
