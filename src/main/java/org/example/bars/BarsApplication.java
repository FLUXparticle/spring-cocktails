package org.example.bars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BarsApplication {
    public static void main(String[] args) {
        SpringApplication.run(BarsApplication.class, args);
    }
}
