package com.spring.openai.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChatGPTController {
    private final ChatClient chatClient;

    // Spring autoconfigures this builder for you
    public ChatGPTController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Usage: http://localhost:8080/api/chat-gpt/find-person?sports=tennis
     * @param sports
     * @return
     */
    @GetMapping("/api/chat-gpt/find-person")
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
     * Usage: http://localhost:8080/api/chat-gpt/prompt?message=Tell an interesting fact about google
     * @param message
     * @return
     */
    @GetMapping("/api/chat-gpt/prompt")
    public String prompt(@RequestParam String message) {
        PromptTemplate template = new PromptTemplate(message);
        Prompt prompt = template.create();

        return chatClient.prompt(prompt).call().content();
    }

    /**
     * Usage: http://localhost:8080/api/chat-gpt/ask?sports=tennis
     * @param sports
     * @return
     */
    @GetMapping("/api/chat-gpt/ask")
    public String ask(@RequestParam String sports) {
        var systemMessage = new SystemMessage("""
                Your primary function is to share information about sports.
                If someone ask about anything else, you can say that you only share about sport.
                """);

        var userMessage = new UserMessage(
            String.format("""
                     List of  5 most popular person in {sports} along
                     with their career achievements. Show the details in the proper readable format.
                     """, sports));

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        return chatClient.prompt(prompt).call().content();
    }


}
