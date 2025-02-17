package de.anmimi.news.crawler.core;

import java.util.concurrent.CompletableFuture;

public interface Crawler {
    CompletableFuture<HeadlineSourceAndContent> crawleForHeadlines();
}
