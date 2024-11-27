package com.polarplus.configuration;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

// * CLASSE APENAS ILUSTRATIVA (JÁ USADA NO APIAPLICATION.JAVA)

@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnvVariables() {
        // Carregar o arquivo .env
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
    }
}
