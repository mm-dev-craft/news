package de.anmimi.news.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.SimpleApiKey;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {


    @Bean
    public ChatModel chatModel(@Value("${spring.ai.openai.chat.api-key}") char[] apiKey) {
        return OpenAiChatModel.builder()
                .openAiApi(OpenAiApi.builder()
                        .apiKey(new SimpleApiKey(new String(apiKey)))
                        .build())
                .build();
    }

    @Bean
    public ChatClient gpt4o(ChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.1)
                        .build())
                .build();
    }

}
