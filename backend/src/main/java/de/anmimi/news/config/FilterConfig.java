package de.anmimi.news.config;

import de.anmimi.news.crawler.filter.AIContextFilter;
import de.anmimi.news.crawler.filter.ContentFilter;
import de.anmimi.news.crawler.filter.MatchingWordFilter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FilterConfig {

    @Bean
    public ContentFilter aiFilter(OpenAiChatModel openAiChatModel,
                                       HeadlinesConfig headlinesConfig) {
        return new AIContextFilter(openAiChatModel, headlinesConfig);
    }

    @Bean
    @Profile("no-ai")
    public ContentFilter wordFilter(HeadlinesConfig headlinesConfig) {
        return new MatchingWordFilter(headlinesConfig);
    }
}
