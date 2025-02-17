package de.anmimi.news.crawler.filter;

import java.util.Set;

public record TitleAndLinkWithKeyword(String title, String link, Set<String> matchingKeywords) {
}

