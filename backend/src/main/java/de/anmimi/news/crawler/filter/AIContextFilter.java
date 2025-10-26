package de.anmimi.news.crawler.filter;

import de.anmimi.news.config.HeadlinesConfig;
import de.anmimi.news.crawler.core.TitleAndLink;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class that uses Spring AI's ChatClient to filter a list of news entries.
 * The LLM now returns, for each matching entry, the title, link, and all matching keywords.
 */
@Slf4j
public class AIContextFilter implements ContentFilter {

    private final ChatClient chat;
    private final Set<String> relevantKeywords;

    private static final String SYSTEM_TEMPLATE = """
            You are an expert content filtering assistant.
            Your task is to analyze a list of news entries and identify those that are contextually relevant.
            A news entry is considered relevant if it is related to one or more passed keywords.
            The keywords are provided as a comma-separated list.
            Please return your answer strictly as a JSON array of objects.
            Each object must have the following fields: "title", "link", and "matchingKeywords".
            The "title" and "link" must exactly match one of the original entries, and "matchingKeywords" should be an array of all keywords that triggered its selection.
            Do not include any additional text or formatting.
            """;

    private static final String USER_TEMPLATE = """
            You are given the following list of news entries separated by the '|' character:
            {headlines}
            
            Each entry is in the format: "title :: link".
            Your task is to identify those entries that are contextually relevant to the following keywords: {keywords}.
            """;

    public AIContextFilter(ChatClient gpt4o, HeadlinesConfig headlinesConfig) {
        this.chat = gpt4o;
        this.relevantKeywords = new HashSet<>(headlinesConfig.matchingWords());
    }

    @Override
    public FilterdContent filterRelevantContent(List<TitleAndLink> content) {
        List<TitleAndLink> validContent = content.stream()
                .filter(tl -> tl.title() != null && !tl.title().isEmpty())
                .toList();
        if (validContent.isEmpty()) {
            return FilterdContent.empty();
        }

        String joinedEntries = validContent.stream()
                .map(tl -> tl.title() + " :: " + tl.link())
                .collect(Collectors.joining(" | "));

        List<TitleAndLinkWithKeyword> relevantItems;
        try {
            relevantItems = chat
                    .prompt()
                    .system(systemSpec -> systemSpec.text(SYSTEM_TEMPLATE))
                    .user(userSpec -> userSpec
                            .text(USER_TEMPLATE)
                            .param("headlines", joinedEntries)
                            .param("keywords", String.join(", ", relevantKeywords))
                    )
                    .call()
                    .entity(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            log.error("Error calling LLM or parsing AI response.", e);
            return FilterdContent.empty();
        }

        if (relevantItems == null || relevantItems.isEmpty()) {
            log.info("No entries matched the AI filter criteria.");
            return FilterdContent.empty();
        }

        log.info("AI found {} matching entries out of {} total.", relevantItems.size(), validContent.size());
        return new FilterdContent(relevantItems);
    }
}
