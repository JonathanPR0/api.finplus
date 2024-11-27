package com.polarplus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.polarplus.controllers.AuthController;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ApiApplication {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();

		// Definir todas as variáveis do .env como propriedades do sistema
		dotenv.entries().forEach(entry -> {
			String key = entry.getKey();
			String value = entry.getValue();

			// Verifica se a variável já foi definida antes de definir no sistema
			if (System.getProperty(key) == null) {
				System.setProperty(key, value);
			}
		});

		SpringApplication.run(ApiApplication.class, args);
		logger.warn("API iniciada na porta " + System.getProperty("API_PORT") + "...");

	}

}
