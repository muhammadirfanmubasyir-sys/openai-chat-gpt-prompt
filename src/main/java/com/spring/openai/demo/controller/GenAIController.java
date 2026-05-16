package com.spring.openai.demo.controller;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.audio.tts.TextToSpeechPrompt;
import org.springframework.ai.audio.tts.TextToSpeechResponse;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gen")
public class GenAIController {

    @Autowired
    private OpenAiImageModel openAiImageModel;

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    @Autowired
    private OpenAiAudioSpeechModel openAiAudioSpeechModel;


    @GetMapping("/text-to-image/{prompt}")
    public String generateImageFromText(@PathVariable("prompt") String prompt) {

        ImageResponse response = openAiImageModel.call(
                                        new ImagePrompt(prompt,
                                                OpenAiImageOptions.builder()
                                                        .height(1024)
                                                        .width(1024)
                                                        .N(1)
                                                        .build())
        );

        return response.getResult().getOutput().getUrl();
    }

    @GetMapping("/image-to-text")
    public String generateTextFromImage() {
        String prompt = "Explain what do you see in this image";
        String imageAbsolutePath = "C:\\Users\\LENOVO L13 YOGA\\Pictures\\BUDHA.jpg";
        String response =
                ChatClient.create(chatModel).prompt()
                        .user(promptUserSpec -> promptUserSpec.text(prompt)
                                .media(MimeTypeUtils.IMAGE_JPEG,
                                        new FileSystemResource(imageAbsolutePath))
                        ).call()
                        .content();

        return response;
    }

    @GetMapping("/audio-to-text")
    public String generateTextFromAudio() {
        String absoluteFilePath = "D:\\harvard.wav";

        OpenAiAudioTranscriptionOptions options
                = OpenAiAudioTranscriptionOptions.builder()
                .language("en")
                .temperature(0f) // lower number =>  lower noise
                .responseFormat(OpenAiAudioTranscriptionOptions.DEFAULT_RESPONSE_FORMAT)
                .build();

        AudioTranscriptionPrompt prompt =
                new AudioTranscriptionPrompt(new FileSystemResource(absoluteFilePath));

        AudioTranscriptionResponse response =
                openAiAudioTranscriptionModel.call(prompt);

        return response.getResult().getOutput();
    }

    @GetMapping("/text-to-audio/{prompt}")
    public ResponseEntity<ByteArrayResource> generateAudioFromText(@PathVariable("prompt") String prompt) {
        OpenAiAudioSpeechOptions options =
                OpenAiAudioSpeechOptions.builder()
                        .model("tts-1")
                        .speed(1.0d)
                        .voice(OpenAiAudioSpeechOptions.Voice.SHIMMER)
                        .build();

        TextToSpeechPrompt speechPrompt = new TextToSpeechPrompt(prompt, options);
        TextToSpeechResponse textToSpeechResponse =  openAiAudioSpeechModel.call(speechPrompt);

        byte[] responseBytes = textToSpeechResponse.getResult().getOutput();

        ByteArrayResource byteArrayResource = new ByteArrayResource(responseBytes);

        return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(byteArrayResource.contentLength())
                    .header(  HttpHeaders.CONTENT_DISPOSITION,
                              ContentDisposition.attachment()
                                      .filename("D:\\output.mp3")
                                      .build().toString()
                            )
                    .body(byteArrayResource);

    }
}
