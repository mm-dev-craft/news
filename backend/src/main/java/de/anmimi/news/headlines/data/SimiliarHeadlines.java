package de.anmimi.news.headlines.data;

import java.util.Set;

public record SimiliarHeadlines(String title, Set<Headline> similiarHeadlines) {
    public Headline baseHeadline() {
        return similiarHeadlines.stream().filter(headline -> headline.getTitle()
                .equalsIgnoreCase(title))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
