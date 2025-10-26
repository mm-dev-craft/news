package de.anmimi.news.config;

import de.anmimi.news.crawler.filter.AIContextFilter;
import de.anmimi.news.crawler.filter.ContentFilter;
import de.anmimi.news.crawler.filter.MatchingWordFilter;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FilterConfig {

    @Bean
    public ContentFilter aiFilter(ChatClient gpt4o, HeadlinesConfig headlinesConfig) {
        return new AIContextFilter(gpt4o, headlinesConfig);
    }

    @Bean
    @Profile("no-ai")
    public ContentFilter wordFilter(HeadlinesConfig headlinesConfig) {
        return new MatchingWordFilter(headlinesConfig);
    }
}
