package com.SpringBoot.TestingHW;

import org.springframework.boot.SpringApplication;

public class TestTestingHwApplication {

	public static void main(String[] args) {
		SpringApplication.from(TestingHwApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
