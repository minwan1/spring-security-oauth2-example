package com.example.oauth.config;

import com.example.oauth.user.User;
import com.example.oauth.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository) {
        return args -> {
            userRepository.save(new User("테스트1", "test1", "test111"));
            userRepository.save(new User("테스트2", "test2", "test222"));
            userRepository.save(new User("테스트3", "test3", "test333"));
        };
    }
}
