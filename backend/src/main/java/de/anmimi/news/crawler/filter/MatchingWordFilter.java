package de.anmimi.news.crawler.filter;

import de.anmimi.news.config.HeadlinesConfig;
import de.anmimi.news.crawler.core.TitleAndLink;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MatchingWordFilter implements ContentFilter {

    private final Set<String> matchingWords;

    public MatchingWordFilter(HeadlinesConfig headlinesConfig) {
        this.matchingWords = new LinkedHashSet<>(headlinesConfig.matchingWords());
    }

    @Override
    public FilterdContent filterRelevantContent(List<TitleAndLink> content) {
        List<TitleAndLinkWithKeyword> filtered = content.stream()
                .map(this::mapToTitleAndLinkWithKeyword)
                .filter(Objects::nonNull)
                .toList();
        return new FilterdContent(filtered);
    }

    private TitleAndLinkWithKeyword mapToTitleAndLinkWithKeyword(TitleAndLink titleAndLink) {
        log.trace("Processing headline: {}", titleAndLink.title());
        Set<String> matchingKeywords = extractMatchingKeywords(titleAndLink.title());
        if (matchingKeywords.isEmpty()) {
            return null;
        }
        return new TitleAndLinkWithKeyword(titleAndLink.title(), titleAndLink.link(), matchingKeywords);
    }

    private Set<String> extractMatchingKeywords(String title) {
        return Arrays.stream(title.split("\\s"))
                .map(String::toLowerCase)
                .filter(matchingWords::contains)
                .collect(Collectors.toSet());
    }
}
