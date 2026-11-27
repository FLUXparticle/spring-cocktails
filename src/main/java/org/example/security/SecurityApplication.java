package org.example.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

import java.util.Map;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SecurityApplication {
    public static void main(String[] args) {
        var application = new SpringApplication(SecurityApplication.class);
        application.setDefaultProperties(Map.of("spring.config.additional-location", "classpath:/security/"));
        application.run(args);
    }
}
