package com.jproger.conferencetelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@SpringBootApplication
public class DobbyApplication {
	public static void main(String[] args) {
		SpringApplication.run(DobbyApplication.class, args);
	}

}

//TODO: Remove middle name from user entity
//TODO: Put away text messages for users to message source
//TODO: Autotests
//TODO: Add check topic status for selection