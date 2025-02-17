package de.anmimi.news.crawler.core.client;

import de.anmimi.news.crawler.core.client.loader.Loader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@RequiredArgsConstructor
@Slf4j
public class JSoupCrawlerClient implements CrawlerClient {

    private final Loader loader;

    public Elements load(String url, String selector) {
        log.info("crawling: {} with selector: {}", url, selector);
        Document document = loader.load(url);
        return document.select(selector);
    }

}
