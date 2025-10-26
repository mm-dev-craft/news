package de.anmimi.news.crawler.core.implementation;

import de.anmimi.news.crawler.core.AbstractCrawler;
import de.anmimi.news.crawler.core.LinkAndDescription;
import de.anmimi.news.crawler.core.client.CrawlerClient;
import de.anmimi.news.crawler.core.TitleAndLink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class NYTCrawler extends AbstractCrawler {

    private static final String NEW_YORK_TIMES = "https://www.nytimes.com/";

    public NYTCrawler(CrawlerClient client) {
        super(client);
    }

    @Override
    protected Elements executeCrawling() {
        Elements elements = client.load(NEW_YORK_TIMES, "section.story-wrapper");
        log.info("Crawled {} elements from the NYT", elements.size());
        return elements;
    }

    @Override
    protected TitleAndLink extractHeadLinesAndLinksFromDom(Element element) {
        String title = element.select("p.summary-class").text();
        String link = element.getElementsByTag("a").attr("href");
        return new TitleAndLink(title, link);
    }

}
