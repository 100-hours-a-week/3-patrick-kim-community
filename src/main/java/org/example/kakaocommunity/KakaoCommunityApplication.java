package org.example.kakaocommunity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KakaoCommunityApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakaoCommunityApplication.class, args);
    }

}
