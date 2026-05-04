package com.spring.openai.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatGPTController {
    private final ChatClient chatClient;

    // Spring autoconfigures this builder for you
    public ChatGPTController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Usage: http://localhost:8080/find-person?sports=tennis
     * @param sports
     * @return
     */
    @GetMapping("/find-person")
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

    /**
     * Usage: http://localhost:8080/prompt?message=Tell an interesting fact about google
     * @param message
     * @return
     */
    @GetMapping("/prompt")
    public String prompt(@RequestParam String message) {
        PromptTemplate template = new PromptTemplate(message);
        Prompt prompt = template.create();

        return chatClient.prompt(prompt).call().content();
    }

}
