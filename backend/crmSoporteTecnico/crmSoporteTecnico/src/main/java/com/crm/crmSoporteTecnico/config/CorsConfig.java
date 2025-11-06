package com.crm.crmSoporteTecnico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Esta configuración es para evitar que Spring Boot bloquee la conexión desde el Frontend por motivos de seguridad (Cors).
 */
@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todos los endpoints.
                        .allowedOrigins("http://localhost:4200") // Aquí se añade la URL del Frontend.
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true); // Necesario para que las Cookies HttpOnly puedan viajar.
            }
        };
    }

}
