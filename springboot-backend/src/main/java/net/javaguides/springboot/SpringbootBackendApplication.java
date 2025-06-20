package net.javaguides.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "net.javaguides.springboot")
@EnableJpaRepositories(basePackages = "net.javaguides.springboot.repository")
public class SpringbootBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootBackendApplication.class, args);
    }
}
