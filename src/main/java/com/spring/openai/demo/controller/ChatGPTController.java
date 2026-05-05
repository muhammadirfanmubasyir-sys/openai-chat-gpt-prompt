package com.spring.openai.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChatGPTController {
    private final ChatClient chatClient;

    @Value("classpath:/prompts/message.st")
    private Resource myPromptResource;

    // Spring autoconfigures this builder for you
    public ChatGPTController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * Usage: http://localhost:8080/api/chat-gpt/find-person?sports=tennis
     * @param sports
     * @return  String response from Chat GPT
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
     * @return  String response from Chat GPT
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
     * @return String response from Chat GPT
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

    /**
     * Usage: http://localhost:8080/api/chat-gpt/find-sports?player-name=Mohammed Ali
     * @param 'player-name'
     *
     * @return Player
     */
    @GetMapping("/api/chat-gpt/find-sports")
    public Player findPopularSportByPerson(@RequestParam(value = "player-name") String playerName) {
        BeanOutputConverter<Player> converter =
                new BeanOutputConverter(Player.class);


        PromptTemplate template = new PromptTemplate(myPromptResource);
        Prompt prompt = template.create(Map.of("playerName", playerName,
                                                "format", converter.getFormat()));

        return converter.convert(chatClient.prompt(prompt).call().content());
    }
}
