package com.spring.openai.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AIApplication {

	public static void main(String[] args) {
		SpringApplication.run(AIApplication.class, args);
	}

//    @Bean
//    CommandLineRunner commandLineRunner(ChatClient.Builder builder) {
//        return args -> {
//            var client = builder.build();
//            String response =client.prompt("Tell me an interesting fact about google")
//                    .call()
//                    .content();
//            System.out.println(response);
//        };
//    }
}
