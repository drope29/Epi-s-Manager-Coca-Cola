package com.epis;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class Application {

	static {
		Dotenv.load()
				.entries()
				.forEach(e ->
						System.setProperty(e.getKey(), e.getValue())
				);
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
