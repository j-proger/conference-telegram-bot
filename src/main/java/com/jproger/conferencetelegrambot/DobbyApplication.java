package com.jproger.conferencetelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DobbyApplication {
	public static void main(String[] args) {
		SpringApplication.run(DobbyApplication.class, args);
	}

}

//TODO: Удалить middle name из сущности user
//TODO: Вынести общий функционал в базовый класс у консамеров
//TODO: Доделать обработку action на создание вопроса