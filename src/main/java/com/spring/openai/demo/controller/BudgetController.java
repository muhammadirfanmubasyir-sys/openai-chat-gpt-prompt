package com.spring.openai.demo.controller;

import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import static org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor.Builder;

@RestController
public class BudgetController {

    private ChatClient chatClient;

    /**
     * A vector embedding is a way of representing data (like words, sentences, or images)
     * as a list of numbers (a vector).
     * These numbers place the data into a multi-dimensional space where items with
     * similar meanings are positioned close together.
     *
     * WordVector [X, Y]Meaning
     * "Puppy"   [-0.2, -0.9]   Slightly sad, highly animal-related
     * "Kitten"  [-0.1, -0.8]   Slightly sad, highly animal-related
     * "Baby"    [0.8, 0.6]     Highly happy, highly human-related
     *
     * By plotting these vectors, the computer understands that
     * "puppy" is much closer in meaning to "kitten" than to "baby"
     * based entirely on their numerical distance.
     */
    private VectorStore vectorStore;

    public BudgetController(ChatClient.Builder builder,
                            VectorStore vectorStore) {

        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
    }

    @GetMapping("/budget")
    public String budgetQuestionAndAnswer(@RequestParam(value = "message",
                                                        defaultValue = "What is the Highlight of the Budget 2024-25")
                                          String message) {

         Builder advisorBuilder = QuestionAnswerAdvisor.builder(vectorStore);

        return chatClient
                .prompt()
                .advisors(advisorBuilder.build())
                .user(message)
                .call()
                .content();
    }
}
