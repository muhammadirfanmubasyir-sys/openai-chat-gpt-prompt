package com.spring.openai.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AIController {
    private final ChatClient chatClient;

    // Spring autoconfigures this builder for you
    public AIController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/")
    public String findPopularSportsPerson(@RequestParam String sports) {
        String message = """
                List of  5 most popular person in {sports} along
                with their career achievements.
                Show the details in the proper readable format.
                """;
        PromptTemplate template = new PromptTemplate(message);
        Prompt prompt = template.create(Map.of("sports", sports));

        return chatClient.prompt(prompt).call().content();
    }

}
