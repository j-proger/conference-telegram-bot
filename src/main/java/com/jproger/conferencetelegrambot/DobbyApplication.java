package com.jproger.conferencetelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DobbyApplication {
	public static void main(String[] args) {
		SpringApplication.run(DobbyApplication.class, args);
	}

}

//TODO: Доделать обработку action на создание вопроса
//TODO: Удалить middle name из сущности user
//TODO: Прикрутить embedded message queue