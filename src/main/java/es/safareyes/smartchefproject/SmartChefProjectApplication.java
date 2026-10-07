package es.safareyes.smartchefproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartChefProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartChefProjectApplication.class, args);
    }
}