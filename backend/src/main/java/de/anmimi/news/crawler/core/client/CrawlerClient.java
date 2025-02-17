package de.anmimi.news.crawler.core.client;

import org.jsoup.select.Elements;

public interface CrawlerClient {
    Elements load(String url, String selector);
}
