package com.ins.insstatistique;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class InsstatistiqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(InsstatistiqueApplication.class, args);
	}

}
