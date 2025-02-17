package de.anmimi.news.crawler.filter;

import java.util.List;

public record FilterdContent(List<TitleAndLinkWithKeyword> content) {
    public static FilterdContent empty() {
        return new FilterdContent(List.of());
    }
}
