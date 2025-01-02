package com.polarplus.infra.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS para todas as rotas
                .allowedOrigins("*") // Permite todas as origens
                .allowedMethods("GET", "POST", "DELETE", "PUT") // Permite os métodos
                .allowedHeaders("*"); // Permite todos os cabeçalhos
    }
}
