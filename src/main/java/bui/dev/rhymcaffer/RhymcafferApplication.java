package bui.dev.rhymcaffer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RhymcafferApplication {

    public static void main(String[] args) {
        SpringApplication.run(RhymcafferApplication.class, args);
    }

}
