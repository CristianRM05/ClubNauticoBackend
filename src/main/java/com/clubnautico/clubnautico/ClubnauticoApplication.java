package com.clubnautico.clubnautico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.clubnautico.clubnautico")
@EntityScan("com.clubnautico.clubnautico")

public class ClubnauticoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClubnauticoApplication.class, args);
    }

}
