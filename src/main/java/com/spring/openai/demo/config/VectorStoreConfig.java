package com.spring.openai.demo.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

@Configuration
public class VectorStoreConfig {

    @Value("classpath:/Budget_Speech.txt")
    private Resource budget;

    /**
     * An embedding model is a machine learning tool that converts complex,
     * unstructured data—such as text, images, or audio—into dense,
     * numerical vectors (lists of numbers) that capture semantic meaning.
     *
     * These vectors allow computers to understand relationships,
     * placing similar items closer together in a multi-dimensional space,
     * which is essential for AI applications like search, recommendation systems, and RAG
     *
     * How Embedding Models WorkInput to Vector:
     * They transform raw data (like a sentence or image) into a vector of numbers,
     * often ranging from 384 to over 1024 dimensions.
     *
     * Semantic Understanding:
     * Unlike keyword matching, embedding models understand that "king" and "queen" are related,
     * or that two different product descriptions mean the same thing.
     *
     * Vector Space:
     * The numerical vectors are plotted in a high-dimensional space.
     * The distance between these vectors indicates their semantic similiarity.
     *
     * @param embeddingModel
     *
     * @return vectorStore
     */
    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore  vectorStore =  SimpleVectorStore
                                                .builder(embeddingModel)
                                                .build();

        File vectorStoreFile = new File ("D://vector-store-output.json");

        if (vectorStoreFile.exists()) {
            System.out.println("Loaded Vector Store File !");
            vectorStore.load(vectorStoreFile);

        } else {
            System.out.println("Create Vector Store File");
            TextReader textReader = new TextReader(budget);

            textReader.getCustomMetadata()
                    .put("filename", "Budget_Speech.txt");

            List<Document> documents = textReader.get();

            TextSplitter textSplitter = new TokenTextSplitter();

            List<Document> splitDocuments = textSplitter.apply(documents);

            /* This section below need OpenAi API Key !!!
               vectorStore.add(splitDocuments);
               vectorStore.save(vectorStoreFile);
            */
        }

        return  vectorStore;
    }

}
