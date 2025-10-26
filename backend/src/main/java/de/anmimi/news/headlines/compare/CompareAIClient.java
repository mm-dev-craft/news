package de.anmimi.news.headlines.compare;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Component
@Slf4j
@RequiredArgsConstructor
public class CompareAIClient {

    private final ChatClient gpt4o;

    @Async
    public <T> CompletableFuture<T> sendChatAsync(Prompt prompt, ParameterizedTypeReference<T> responseType) {
        return CompletableFuture.completedFuture(gpt4o.prompt(prompt)
                .call()
                .entity(responseType));
    }
}
