package de.anmimi.news.crawler.core;

import java.util.List;

public record HeadlineSourceAndContent(String source, List<TitleAndLink> content) {
}
