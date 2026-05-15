package com.spring.openai.demo.controller;

import com.spring.openai.demo.model.GeminiModel;
import com.spring.openai.demo.model.ModelListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/api/gemini")
public class GeminiModelController {
    private static final Logger log = LoggerFactory.getLogger(GeminiModelController.class);

    /**
     * #https://aistudio.google.com/app/api-keys?_gl=1*kdjzi*_ga*MzI3NDQzMDIuMTc3Nzc3MTU1NA..*_ga_P1DBVKWT6V*czE3Nzc3NzE1NTQkbzEkZzAkdDE3Nzc3NzE1ODQkajMwJGwwJGgyMDk1MTI3NDMx&project=gen-lang-client-0903506764
     */
    @Value("${spring.ai.openai.api-key}")
    private String GEMINI_API_KEY;

    private RestClient restClient = null;

    public GeminiModelController(RestClient.Builder builder) {
        this.restClient = builder
                            .baseUrl("https://generativelanguage.googleapis.com")
                            .build();
    }

    @GetMapping("/models")
    public List<GeminiModel> models() {
        ResponseEntity<ModelListResponse> response = restClient.get().uri("/v1beta/openai/models")
                .header("Authorization", "Bearer " + GEMINI_API_KEY)
                .retrieve()
                .toEntity(ModelListResponse.class);

        return response.getBody().data();
    }

}
