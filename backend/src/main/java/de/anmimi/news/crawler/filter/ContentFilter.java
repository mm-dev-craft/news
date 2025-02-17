package de.anmimi.news.crawler.filter;

import de.anmimi.news.crawler.core.TitleAndLink;

import java.util.List;

public interface ContentFilter {
    FilterdContent filterRelevantContent(List<TitleAndLink> content);
}
