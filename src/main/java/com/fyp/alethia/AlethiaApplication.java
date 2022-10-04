package com.fyp.alethia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { 	DataSourceAutoConfiguration.class })
public class AlethiaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AlethiaApplication.class, args);
	}
}
